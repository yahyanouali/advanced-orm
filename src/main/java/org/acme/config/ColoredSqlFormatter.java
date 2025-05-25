package org.acme.config;

import com.p6spy.engine.spy.appender.MessageFormattingStrategy;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColoredSqlFormatter implements MessageFormattingStrategy {

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_PURPLE = "\u001B[35m";

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        if (prepared == null || prepared.isBlank()) return "";

        String formattedTime = LocalDateTime.now().format(FORMATTER);
        prepared = colorizeSQL(prepared);

        String header = String.format("%s%s%s | %sConnection: %d%s | %sExecution Time: %s ms%s | %s%s%s",
                ANSI_CYAN, formattedTime, ANSI_RESET,
                ANSI_GREEN, connectionId, ANSI_RESET,
                ANSI_YELLOW, elapsed, ANSI_RESET,
                ANSI_PURPLE, category, ANSI_RESET);

        String[] lines = prepared.split("\n");
        int maxLength = Math.max(80, Arrays.stream(lines)
                .map(line -> line.replaceAll("\u001B\\[[;\\d]*m", "").length())
                .max(Integer::compareTo).orElse(0));

        String border = "*".repeat(maxLength + 4);
        StringBuilder output = new StringBuilder("\n").append(border).append("\n");

        appendLineWithPadding(output, header, maxLength);
        output.append(border).append("\n");

        for (String line : lines) {
            appendLineWithPadding(output, line, maxLength);
        }

        output.append(border);
        return output.toString();
    }

    private void appendLineWithPadding(StringBuilder sb, String content, int maxLength) {
        int visibleLength = content.replaceAll("\u001B\\[[;\\d]*m", "").length();
        sb.append("* ").append(content)
                .append(" ".repeat(maxLength - visibleLength + 1)).append("*\n");
    }

    private String colorizeSQL(String sql) {
        sql = replaceAliasesWithTableNames(sql);
        sql = formatSQLMultiline(sql);

        sql = colorizeKeyword(sql, getSQLKeywords(), ANSI_BLUE);
        sql = colorizeOperator(sql, ANSI_PURPLE);
        sql = colorizePattern(sql, "\\b(\\d+(\\.\\d+)?)\\b", ANSI_RED);           // numbers
        sql = colorizePattern(sql, "'([^']*)'", ANSI_GREEN + "'$1'" + ANSI_RESET); // strings
        sql = colorizePattern(sql, "(\\(|\\)|,)", ANSI_YELLOW + "$1" + ANSI_RESET); // parens & commas
        sql = colorizePattern(sql, "(?i)\\bAS\\s+(\\w+)\\b", ANSI_BLUE + "AS" + ANSI_RESET + " " + ANSI_CYAN + "$1" + ANSI_RESET);
        sql = colorizeIdentifiers(sql); // fallback for column/table names

        return sql;
    }

    private String colorizeKeyword(String sql, String[] keywords, String color) {
        String keywordRegex = "\\b(" + String.join("|", keywords) + ")\\b";
        return sql.replaceAll("(?i)" + keywordRegex, color + "$1" + ANSI_RESET);
    }

    private String colorizeOperator(String sql, String color) {
        return sql.replaceAll("(=|!=|<>|>|<|>=|<=|\\+|-|\\*|/|%|\\^)", color + "$1" + ANSI_RESET);
    }

    private String colorizePattern(String sql, String regex, String replacement) {
        return sql.replaceAll(regex, replacement);
    }

    private String colorizeIdentifiers(String sql) {
        // Color any word that isn't already surrounded by ANSI codes (naive but effective)
        return sql.replaceAll("(?<!\\u001B\\[)(?<!\\u001B\\[[;\\d])(?<!\\u001B\\[[;\\d][;\\d])(\\b[a-zA-Z_][a-zA-Z0-9_]*\\b)(?!\\u001B\\[[;\\d]*m)", ANSI_CYAN + "$1" + ANSI_RESET);
    }

    private String[] getSQLKeywords() {
        return new String[] {
                "SELECT", "FROM", "WHERE", "JOIN", "LEFT", "RIGHT", "INNER", "OUTER", "FULL", "CROSS",
                "ON", "AND", "OR", "NOT", "GROUP BY", "ORDER BY", "HAVING", "LIMIT", "OFFSET",
                "INSERT", "UPDATE", "DELETE", "SET", "VALUES", "CREATE", "ALTER", "DROP", "TABLE",
                "INDEX", "VIEW", "PROCEDURE", "FUNCTION", "TRIGGER", "CASE", "WHEN", "THEN", "ELSE",
                "END", "AS", "DISTINCT", "UNION", "ALL", "IS", "NULL", "TRUE", "FALSE", "IN",
                "BETWEEN", "LIKE", "EXISTS", "COUNT", "SUM", "AVG", "MIN", "MAX", "COALESCE",
                "NULLIF", "BEGIN", "COMMIT", "ROLLBACK", "WITH"
        };
    }

    private String replaceAliasesWithTableNames(String sql) {
        Map<String, String> aliasToTable = new HashMap<>();
        Matcher matcher = Pattern.compile("(?i)(FROM|JOIN)\\s+([a-zA-Z_][a-zA-Z0-9_]*)\\s+([a-zA-Z][a-zA-Z0-9_]*)")
                .matcher(sql);

        while (matcher.find()) {
            aliasToTable.put(matcher.group(3), matcher.group(2).toLowerCase());
        }

        for (Map.Entry<String, String> entry : aliasToTable.entrySet()) {
            String alias = entry.getKey();
            String table = entry.getValue();

            sql = sql.replaceAll("(?i)(FROM|JOIN)\\s+([a-zA-Z_][a-zA-Z0-9_]*)\\s+" + alias + "\\b", "$1 $2 " + table);
            sql = sql.replaceAll("\\b" + alias + "\\.", table + ".");
        }

        return sql;
    }

    private String formatSQLMultiline(String sql) {
        sql = sql.replaceAll("\\s+", " ").trim();

        String[] keywords = {
                "SELECT", "FROM", "WHERE", "GROUP BY", "ORDER BY", "HAVING", "LIMIT", "OFFSET",
                "JOIN", "LEFT JOIN", "RIGHT JOIN", "INNER JOIN", "OUTER JOIN", "FULL JOIN", "CROSS JOIN",
                "UNION", "UNION ALL", "INTERSECT"
        };

        for (String kw : keywords) {
            sql = sql.replaceAll("(?i)\\b" + Pattern.quote(kw) + "\\b", "\n" + kw.toUpperCase());
        }

        return sql;
    }
}

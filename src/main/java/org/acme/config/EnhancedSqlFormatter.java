package org.acme.config;

import com.p6spy.engine.spy.appender.MessageFormattingStrategy;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EnhancedSqlFormatter implements MessageFormattingStrategy {


    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        if (sql == null || sql.isBlank()) return "";

        String formattedTime = LocalDateTime.now().format(FORMATTER);
        sql = formatSQL(sql);

        String header = String.format("-- %s | Connection: %d | Execution Time: %s ms | %s",
                formattedTime, connectionId, elapsed, category);

        String[] lines = sql.split("\n");

        StringBuilder output = new StringBuilder();

        output.append(header);

        for (int i = 0, linesLength = lines.length; i < linesLength; i++) {
            String line = lines[i];
            if (i == linesLength - 1) {
                output.append(line).append(";");
            } else {
                output.append(line).append("\n");
            }
        }

        return output.toString();
    }

    private String formatSQL(String sql) {
        sql = replaceAliasesWithTableNames(sql);
        sql = formatSQLMultiline(sql);
        return sql;
    }

    private String replaceAliasesWithTableNames(String sql) {
        Map<String, String> aliasToTable = new HashMap<>();
        Matcher matcher = Pattern
                .compile("(?i)(FROM|JOIN)\\s+([a-zA-Z_][a-zA-Z0-9_]*)\\s+([a-zA-Z][a-zA-Z0-9_]*)")
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
                "SELECT", "FROM", "WHERE", "GROUP BY", "ORDER BY", "HAVING", "LIMIT", "OFFSET", "INSERT", "UPDATE",
                "JOIN", "LEFT JOIN", "RIGHT JOIN", "INNER JOIN", "OUTER JOIN", "FULL JOIN", "CROSS JOIN",
                "UNION", "UNION ALL", "INTERSECT"
        };

        for (String kw : keywords) {
            sql = sql.replaceAll("(?i)\\b" + Pattern.quote(kw) + "\\b", "\n" + kw.toUpperCase());
        }

        return sql;
    }
}

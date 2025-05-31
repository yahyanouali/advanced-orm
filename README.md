# advanced-orm

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Using P6Spy for SQL Monitoring in Development

This project uses P6Spy to monitor and log SQL queries during development. P6Spy is a library that intercepts database statements and logs them along with execution time and other useful information.

### What is P6Spy?

P6Spy is a framework that enables database statement monitoring. It intercepts all JDBC transactions and logs them to a file or console. Key benefits include:

- Detailed SQL logging with execution times
- No code changes required in your application logic
- Ability to log only in development environments
- Customizable logging format
- Ability to identify slow queries

### Adding P6Spy to Your Project

1. Add the P6Spy dependency to your `pom.xml`:

```xml
<properties>
    <p6spy.version>3.9.1</p6spy.version>
</properties>

<dependencies>
    <dependency>
        <groupId>p6spy</groupId>
        <artifactId>p6spy</artifactId>
        <version>${p6spy.version}</version>
    </dependency>
</dependencies>
```

2. Create a `spy.properties` file in your `src/main/resources` directory:

```properties
# Basic configuration
realdriver=com.mysql.cj.jdbc.Driver
appender=com.p6spy.engine.spy.appender.FileLogger
logfile=target/spy.log.sql

# Log format
logMessageFormat=com.p6spy.engine.spy.appender.CustomLineFormat
customLogMessageFormat=-- %(currentTime) | took %(executionTime) | category: %(category) | connectionId: %(connectionId) | \n%(sqlSingleLine);

# Date format
dateformat=yyyy-MM-dd HH:mm:ss

# Filter configuration
filter=false
# include=
# exclude=
```

### Configuring P6Spy for Development Mode Only

To ensure P6Spy is only used in development mode and doesn't impact production:

1. In your main `application.properties`, keep your standard database configuration:

```properties
quarkus.datasource.db-kind=mysql
quarkus.datasource.username=root
quarkus.datasource.password=example
quarkus.datasource.jdbc.url=jdbc:mysql://localhost:3306/yourdb
```

2. Create an `application-dev.properties` file with P6Spy configuration:

```properties
quarkus.datasource.jdbc.driver=com.p6spy.engine.spy.P6SpyDriver
quarkus.datasource.jdbc.url=jdbc:p6spy:mysql://localhost:3306/yourdb
```

This setup ensures that P6Spy is only used when the dev profile is active, which happens automatically when running with `./mvnw quarkus:dev`.

### Example P6Spy Output

When your application executes SQL queries, P6Spy logs them to the configured location (in this case, `target/spy.log.sql`). Here's an example of the output:

```sql
-- 2025-05-25 18:30:52 | took 4 | category: statement | connectionId: 0 |
SELECT films.id,
       films.annee,
       films.genre,
       films.id_realisateur,
       films.resume,
       films.titre
FROM Film films;

-- 2025-05-25 18:30:52 | took 2 | category: statement | connectionId: 0 |
SELECT artisteEntity.id,
       artisteEntity.annee_naissance,
       artisteEntity.nom,
       artisteEntity.prenom
FROM Artiste artisteEntity
WHERE artisteEntity.id IN (1, 123, 122, 135, 138, 142, 168, 170, 172, 79, 81, 83, 219, 91, 101, 111);
```

Each log entry includes:
- Timestamp: When the query was executed
- Execution time: How long the query took (in milliseconds)
- Category: Type of statement (e.g., statement, batch, commit)
- Connection ID: Database connection identifier
- SQL: The actual SQL statement in a formatted way

### Advanced Configuration Options

P6Spy offers many configuration options:

- **Slow query detection**: Set `executionThreshold` to log only queries that exceed a certain execution time
- **Custom logging formats**: Customize the log format to include specific information
- **Filtering**: Include or exclude specific tables or queries from logging
- **Different appenders**: Log to console, file, or custom destinations

For more details, see the [P6Spy documentation](https://p6spy.readthedocs.io/en/latest/configandusage.html).

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/advanced-orm-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

## Related Guides

- REST ([guide](https://quarkus.io/guides/rest)): A Jakarta REST implementation utilizing build time processing and Vert.x. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it.
- Hibernate ORM ([guide](https://quarkus.io/guides/hibernate-orm)): Define your persistent model with Hibernate ORM and Jakarta Persistence
- REST Jackson ([guide](https://quarkus.io/guides/rest#json-serialisation)): Jackson serialization support for Quarkus REST. This extension is not compatible with the quarkus-resteasy extension, or any of the extensions that depend on it
- JDBC Driver - MySQL ([guide](https://quarkus.io/guides/datasource)): Connect to the MySQL database via JDBC

## Provided Code

### Hibernate ORM

Create your first JPA entity

[Related guide section...](https://quarkus.io/guides/hibernate-orm)



### REST

Easily start your REST Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)

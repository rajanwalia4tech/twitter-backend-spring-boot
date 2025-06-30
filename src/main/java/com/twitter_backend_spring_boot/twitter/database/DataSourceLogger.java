package com.twitter_backend_spring_boot.twitter.database;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import java.sql.Connection;

@Component
public class DataSourceLogger {

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void logDataSourceDetails() {
        try (Connection conn = dataSource.getConnection()) {
            System.out.println("Database JDBC URL: " + conn.getMetaData().getURL());
            System.out.println("Database driver: " + conn.getMetaData().getDriverName());
            System.out.println("Database version: " + conn.getMetaData().getDatabaseProductVersion());
            System.out.println("Autocommit mode: " + conn.getAutoCommit());
            System.out.println("Isolation level: " + conn.getTransactionIsolation());
            // For pool size, cast if you have HikariDataSource:
            if (dataSource instanceof com.zaxxer.hikari.HikariDataSource) {
                com.zaxxer.hikari.HikariDataSource hikariDS = (com.zaxxer.hikari.HikariDataSource) dataSource;
                System.out.println("Minimum pool size: " + hikariDS.getMinimumIdle());
                System.out.println("Maximum pool size: " + hikariDS.getMaximumPoolSize());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

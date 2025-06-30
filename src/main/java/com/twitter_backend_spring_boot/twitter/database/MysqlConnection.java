package com.twitter_backend_spring_boot.twitter.database;
import com.twitter_backend_spring_boot.twitter.logger.InjectLogger;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import java.sql.Connection;
import org.slf4j.Logger;


@Component
public class MysqlConnection {

    @InjectLogger
    private Logger log;

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void checkConnection() {
        try (Connection conn = dataSource.getConnection()) {
            log.info("✅ Connected to MySQL DB: " + conn.getMetaData().getURL());
        } catch (Exception e) {
            log.error("❌ Failed to connect to database: " + e.getMessage());
        }
    }
}

package com.twitter_backend_spring_boot.twitter.config;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import java.io.File;

@Component
public class LoggingConfiguration {

    @PostConstruct
    public void init() {
        File logDir = new File("/var/logs/twitter");
        if (!logDir.exists()) {
            boolean created = logDir.mkdirs();
            System.out.println("Created log directory: " + created);
        }
    }
}

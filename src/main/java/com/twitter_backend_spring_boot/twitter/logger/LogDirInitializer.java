package com.twitter_backend_spring_boot.twitter.logger;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.io.File;

@Component
public class LogDirInitializer {

    @InjectLogger
    private Logger log;
    private static final String LOG_DIR = "logs/app";

    @PostConstruct
    public void init() {
        File dir = new File(LOG_DIR);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            log.info("Log directory created: {}", dir.getAbsolutePath());
        } else {
            log.info("Log directory already exists: {}", LOG_DIR);
        }
    }
}

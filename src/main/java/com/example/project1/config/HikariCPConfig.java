package com.example.project1.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HikariCPConfig {
    private static final Logger logger = LoggerFactory.getLogger(HikariCPConfig.class);

    private static final long connectionTimeout = 30000;

    private static final boolean readOnly = false;

    private static final long idleTimeout = 6000;

    private static final long maxLifetime = 60000;

    private static final int maxPoolSize = 30;

    private static final int minIdle = 1; //

    public static HikariDataSource buildHikariDataSource(String driver, String url, String username, String password) {
        HikariConfig config = new HikariConfig();
        config.setPoolName(String.format("Hikari pool name: %s", url));
        config.setDriverClassName(driver);
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);

        config.setConnectionTimeout(connectionTimeout);
        config.setReadOnly(readOnly);
        config.setIdleTimeout(idleTimeout);
        config.setMaxLifetime(maxLifetime);
        config.setMaximumPoolSize(maxPoolSize);
        config.setMinimumIdle(minIdle);

        config.setConnectionTestQuery("SELECT 1");

        logger.info("HikariCP Configuration: driver={}, url={}, maxPoolSize={}, minIdle={}",
                driver, url, maxPoolSize, minIdle);



        return new HikariDataSource(config);
    }
}

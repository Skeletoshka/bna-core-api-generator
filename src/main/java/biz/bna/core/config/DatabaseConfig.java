package biz.bna.core.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Autowired
    private final ConnectionSettings connectionSettings;

    @Autowired
    public DatabaseConfig(ConnectionSettings connectionSettings) {
        this.connectionSettings = connectionSettings;
    }

    @Bean
    public DataSource dataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(connectionSettings.getDriver());
        hikariConfig.setJdbcUrl(connectionSettings.getUrl());
        hikariConfig.setUsername(connectionSettings.getUsername());
        hikariConfig.setPassword(connectionSettings.getPassword());
        hikariConfig.setMaximumPoolSize(connectionSettings.getJdbcMaxPoolSize());
        hikariConfig.setPoolName("main");
        return new HikariDataSource(hikariConfig);
    }
}
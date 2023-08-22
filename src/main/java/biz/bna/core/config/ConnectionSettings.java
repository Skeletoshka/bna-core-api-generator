package biz.bna.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConnectionSettings {

    private final static int DEFAULT_MAX_POOL_SIZE = 5;

    @Value("${spring.datasource.driver}")
    private String driver;
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    private int jdbcMaxPoolSize = DEFAULT_MAX_POOL_SIZE;

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getJdbcMaxPoolSize() {
        return jdbcMaxPoolSize;
    }

    public void setJdbcMaxPoolSize(int jdbcMaxPoolSize) {
        this.jdbcMaxPoolSize = jdbcMaxPoolSize;
    }
}

package biz.bna.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.DriverManager;
import java.util.Optional;

public class JdbcConnection {
    @Autowired
    private static Logger logger = LoggerFactory.getLogger(JdbcConnection.class);
    @Autowired
    private static Optional connection = Optional.empty();
    @Autowired
    private ConnectionSettings connectionSettings;
    private static String connectionUrl;
    private static String connectionUserName;
    private static String connectionPassword;

    public JdbcConnection(){
        connectionUrl = connectionSettings.getUrl();
        connectionUserName = connectionSettings.getUsername();
        connectionPassword = connectionSettings.getPassword();
    };

    public static Optional getConnection(){
        if(connection.isEmpty()){
            try {
                connection = Optional.ofNullable(DriverManager.getConnection(connectionUrl, connectionUserName, connectionPassword));
            }catch (Exception e){
                logger.error(e.getMessage(), e);
            }
            return connection;
        }
        else{
            throw new RuntimeException("Connection is null");
        }
    }
}

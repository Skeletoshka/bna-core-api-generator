package biz.bna.core.config;

import biz.bna.core.ConsoleApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**Класс, запускающийся с запуском приложения
* */
@Component
public class InitApp implements ApplicationRunner {
    Logger logger = LoggerFactory.getLogger(InitApp.class);

    @Autowired
    ApplicationContext applicationContext;

    @Value("${spring.datasource.url}")
    private String url;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("Init app ... run");
        logger.info("connecting to " + url);
        ConsoleApplication.setApplicationContext(applicationContext);
        logger.info("Init app ... complete");
    }
}

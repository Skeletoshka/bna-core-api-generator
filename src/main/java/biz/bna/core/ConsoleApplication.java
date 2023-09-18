package biz.bna.core;

import biz.bna.core.generator.*;
import biz.bna.core.utils.OrmUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import java.util.Map;

@SpringBootApplication
public class ConsoleApplication implements CommandLineRunner {

    public static Map<String, String> tableSubstitutions;
    public static Map<String, String> columnSubstitutions;
    @Value("${app.package.name}")
    public String packageName;
    @Value("${app.entities}")
    public String entities;

    private static final Logger logger = LoggerFactory.getLogger(ConsoleApplication.class);

    private static ApplicationContext applicationContext;

    public static void main(String[] args) {
        logger.info("STARTING THE APPLICATION");
        SpringApplication.run(ConsoleApplication.class, args).close();
        logger.info("APPLICATION FINISHED");
    }
    @Override
    public void run(String... args) {
        OrmUtils.loadSubstitutions();
        //Генерация моделей
        ModelGenerator modelGenerator = new ModelGenerator(packageName);
        modelGenerator.forEntities(entities);
        modelGenerator.run();

        //Генерация DTO
        DTOGenerator dtoGenerator = new DTOGenerator(packageName);
        dtoGenerator.forEntities(entities);
        dtoGenerator.run();

        //Генерация View
        ViewGenerator viewGenerator = new ViewGenerator(packageName);
        viewGenerator.forEntities(entities);
        viewGenerator.run();

        //Генерация Validator
        ValidatorGenerator validatorGenerator = new ValidatorGenerator(packageName);
        validatorGenerator.forEntities(entities);
        validatorGenerator.run();

        //Генерация Repository
        RepositoryGenerator repositoryGenerator = new RepositoryGenerator(packageName);
        repositoryGenerator.forEntities(entities);
        repositoryGenerator.run();
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static void setApplicationContext(ApplicationContext applicationContext) {
        ConsoleApplication.applicationContext = applicationContext;
    }
}

package {packageName};

import biz.spring.core.model.{modelName};
import biz.spring.core.utils.DatabaseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class {modelName}Repository implements TableRepository<{modelName}>{

    private static Logger logger = LoggerFactory.getLogger({modelName}Repository.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    //TODO указать цифры файла sql
    @Override
    public void create(){
        Resource resource = new ClassPathResource("sql/000000-{tableName}.sql");
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator(resource);
        databasePopulator.setSqlScriptEncoding("UTF-8");
        databasePopulator.execute(jdbcTemplate.getDataSource());
        logger.info("{tableName} created");
    }

    @Override
    public void drop(){
        String[] tables = {"{tableName}"};
        drop(tables);
    }

    //TODO для тестов необходимо тут добавить сущности
    @Override
    public void load(){
        {modelName}[] entities = {
        };
        insert(Arrays.asList(entities));
        DatabaseUtils.setSequenceValue("{tableName}_id_gen", entities.length+1);
    }
}

package biz.bna.core.utils;

import biz.bna.core.ConsoleApplication;
import biz.bna.core.config.DatabaseConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;

public class OrmUtils {

    private static Logger logger = LoggerFactory.getLogger(OrmUtils.class);

    public static NamedParameterJdbcTemplate getJDBC(){
        ApplicationContext context = ConsoleApplication.getApplicationContext();
        DataSource dataSource = context.getBean(DatabaseConfig.class).dataSource();
        return new NamedParameterJdbcTemplate(dataSource);
    }

    public static JdbcTemplate getJDBCTemplate(){
        ApplicationContext context = ConsoleApplication.getApplicationContext();
        DataSource dataSource = context.getBean(DatabaseConfig.class).dataSource();
        return new NamedParameterJdbcTemplate(dataSource).getJdbcTemplate();
    }

    public static String getSchemaName() throws SQLException {
        ApplicationContext context = ConsoleApplication.getApplicationContext();
        DataSource dataSource = context.getBean(DatabaseConfig.class).dataSource();
        return dataSource.getConnection().getSchema();
    }

    public static void loggerSql(String sql){
        logger.info(sql);
    }

    public static String loadResource(Resource resource){
        try(InputStream is = resource.getInputStream()){
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static void loadSubstitutions(){
        ConsoleApplication.columnSubstitutions = new HashMap<>();
        ConsoleApplication.tableSubstitutions = new HashMap<>();
        Properties substitution = new Properties();

        try {
            substitution.load(Files.newInputStream(Path.of("substitution.properties")));
            String tablePrefix = "table.";
            String columnPrefix = "field.";
            substitution.forEach((k, v) -> {
                if(((String)k).startsWith(tablePrefix)){
                    ConsoleApplication.tableSubstitutions.put(((String) k).substring(tablePrefix.length()), (String) v);
                }
                if(((String)k).startsWith(columnPrefix)){
                    ConsoleApplication.columnSubstitutions.put(((String) k).substring(columnPrefix.length()), (String) v);
                }
            });
        } catch (Exception e){
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static String buildConstructorPlaceholder(String table){
        return String.format("\n\tpublic %s(){\n\n\t}\n\n", getClassName(table));
    }

    public static String convertPgTypeToJavaType(String pgDataType){
        switch (pgDataType){
            case "integer":
            case "int4": return "Integer";
            case "date":
            case "datetime": return "Date";
            case "varchar":
            case "character varying":return "String";
            case "float":
            case "double":
            case "numeric": return "Double";
            default: return "???";
        }
    }

    public static String getAttributeName(String columnName){
        String attributeName = ConsoleApplication.columnSubstitutions.get(columnName.replace("_", ""));
        if(attributeName != null){
            return attributeName;
        }else{
            Integer index = columnName.indexOf('_');
            columnName = columnName.replaceFirst("_", "");
            String str = ("" + columnName.charAt(index)).toUpperCase(Locale.ROOT);
            return columnName.substring(0, index) + str + columnName.substring(index + 1);
        }
    }

    public static String getClassName(String cls){
        return ConsoleApplication.tableSubstitutions.get(cls);
    }
}

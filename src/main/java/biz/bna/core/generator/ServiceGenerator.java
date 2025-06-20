package biz.bna.core.generator;

import biz.bna.core.utils.FileWriter;
import biz.bna.core.utils.OrmUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class ServiceGenerator implements Runnable{
    private final String packageName;

    private String entities;

    String template;

    public ServiceGenerator(String packageName){
        this.packageName = packageName;
        template = OrmUtils.getResourceFileAsString(File.separator + "template" + File.separator + "Service.java");
    }

    public void forEntities(String entities){
        this.entities = entities;
    }

    @Override
    public void run() {
        try {
            FileWriter.clearCatalog(FileWriter.SERVICE_PATH);
        }catch (IOException e){
            throw new RuntimeException(e.getMessage(), e);
        }
        Arrays.stream(entities.split(",")).forEach(entity -> {
            String className = OrmUtils.getClassName(entity);
            while(template.contains("{modelName}")){
                template = template.replace("{modelName}", className);
            }
            while(template.contains("{tableName}")){
                template = template.replace("{tableName}", entity);
            }
            String attributeName = className.substring(0, 1).toLowerCase().concat(className.substring(1));
            while(template.contains("{modelParamName}")){
                template = template.replace("{modelParamName}", attributeName);
            }
            template = template.replace("{packageName}", packageName.concat(".validator"));
            try {
                FileWriter.writeFile(FileWriter.SERVICE_PATH, OrmUtils.getClassName(entity).concat("Service.java"), template);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}

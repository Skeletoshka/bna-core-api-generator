package biz.bna.core.generator;

import biz.bna.core.utils.FileWriter;
import biz.bna.core.utils.OrmUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class RepositoryGenerator implements Runnable{

    private final String packageName;

    private String entities;

    String template;

    public RepositoryGenerator(String packageName){
        this.packageName = packageName;
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        template = OrmUtils.getResourceFileAsString(File.separator + "template" + File.separator + "Repository.java");
    }

    public void forEntities(String entities){
        this.entities = entities;
    }

    @Override
    public void run() {
        try {
            FileWriter.clearCatalog(FileWriter.REPOSITORY_PATH);
        }catch (IOException e){
            throw new RuntimeException(e.getMessage(), e);
        }
        Arrays.stream(entities.split(",")).forEach(entity -> {
            while(template.contains("{modelName}")){
                template = template.replace("{modelName}", OrmUtils.getClassName(entity));
            }
            while(template.contains("{tableName}")){
                template = template.replace("{tableName}", entity);
            }
            template = template.replace("{packageName}", packageName.concat(".repository"));
            try {
                FileWriter.writeFile(FileWriter.REPOSITORY_PATH, OrmUtils.getClassName(entity).concat("Repository.java"), template);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}

package biz.bna.core.generator;

import biz.bna.core.utils.FileWriter;
import biz.bna.core.utils.OrmUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class ControllerGenerator implements Runnable{

    private final String packageName;

    private String entities;

    String template;

    public ControllerGenerator(String packageName){
        this.packageName = packageName;
        template = OrmUtils.getResourceFileAsString(File.separator + "template" + File.separator + "Controller.java");
    }

    public void forEntities(String entities){
        this.entities = entities;
    }

    @Override
    public void run() {
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
            template = template.replace("{packageName}", packageName.concat(".controller"));
            try {
                FileWriter.writeFile(FileWriter.CONTROLLER_PATH, OrmUtils.getClassName(entity).concat("Controller.java"), template);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}

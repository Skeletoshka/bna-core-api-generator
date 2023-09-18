package biz.bna.core.generator;

import biz.bna.core.utils.FileWriter;
import biz.bna.core.utils.OrmUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class ValidatorGenerator implements Runnable{

    private final String packageName;

    private String entities;

    String template;

    public ValidatorGenerator(String packageName){
        this.packageName = packageName;
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        template = OrmUtils.getResourceFileAsString(File.separator + "template" + File.separator + "Validator.java");
    }

    public void forEntities(String entities){
        this.entities = entities;
    }

    @Override
    public void run() {
        Arrays.stream(entities.split(",")).forEach(entity -> {
            while(template.contains("{modelName}")){
                template = template.replace("{modelName}", OrmUtils.getClassName(entity));
            }
            template = template.replace("{packageName}", packageName.concat(".validator"));
            try {
                FileWriter.writeFile(FileWriter.VALIDATOR_PATH, OrmUtils.getClassName(entity).concat("Validator.java"), template);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}

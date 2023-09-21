package biz.bna.core.utils;

import org.apache.tomcat.util.http.fileupload.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;

public class FileWriter {

    public static final String MODEL_PATH = "javaClasses\\model";
    public static final String VIEW_PATH = "javaClasses\\view";
    public static final String DTO_PATH = "javaClasses\\dto";
    public static final String CONTROLLER_PATH = "javaClasses\\controller";
    public static final String SERVICE_PATH = "javaClasses\\service";
    public static final String VALIDATOR_PATH = "javaClasses\\validator";
    public static final String REPOSITORY_PATH = "javaClasses\\repository";
    public static final String CONTROLLER_TEST_PATH = "javaClasses\\tests";
    public static void writeFile(String path,String fileName, String content) throws IOException {
        Files.createDirectories(Paths.get(path));
        Files.write(Paths.get(path.concat("\\").concat(fileName)), Collections.singleton(content), StandardCharsets.UTF_8);
    }

    public static void clearCatalog(String path) throws IOException {
        FileUtils.cleanDirectory(new File(path));
    }

}

package biz.bna.core.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;

public class FileWriter {

    public static void writeFile(String path,String fileName, String content) throws IOException {
        Files.createDirectories(Paths.get(path));
        Files.write(Paths.get(path.concat("\\").concat(fileName)), Collections.singleton(content), StandardCharsets.UTF_8);
    }

}

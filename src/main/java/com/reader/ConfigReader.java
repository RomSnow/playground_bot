package com.reader;

import com.logger.PGBotLogger;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.Level;

public class ConfigReader {
    public static String getDataFromConfFile(String name) {
        var result = "";
        var currentDir = System.getProperty("user.dir");
        var currentPath = Paths.get(currentDir,"config", name);
        var file = new File(currentPath.toString());
        try {
            result = FileUtils.readFileToString(file, "UTF-8");
        } catch (IOException e) {
            PGBotLogger.getLogger().log(Level.INFO, e.getMessage());
            e.printStackTrace();
        }

        return result;
    }
}

package com.logger;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PGBotLogger {
    private static Logger logger = null;

    public PGBotLogger(String name) {
        logger = Logger.getLogger(name);
    }

    public static Logger getLogger() {
        return logger;
    }

    public void preSetup(Level level) {
        try {
            var sysOut = new ConsoleHandler();
            sysOut.setLevel(level);

            var currentDir = System.getProperty("user.dir");
            var currentPath = Paths.get(currentDir,"logs");
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy-");
            LocalDateTime now = LocalDateTime.now();
            var pattern = String.format("%s/%s%%u%%g.log", currentPath, dtf.format(now));
            var fh = new FileHandler(pattern, 10000, 10000);
            fh.setLevel(level);

            logger.addHandler(sysOut);
            logger.addHandler(fh);
            logger.setLevel(level);
            logger.setUseParentHandlers(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

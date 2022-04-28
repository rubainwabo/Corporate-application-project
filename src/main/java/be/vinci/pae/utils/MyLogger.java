package be.vinci.pae.utils;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MyLogger {

  private static Logger logger;

  public static void init() {
    logger = Logger.getLogger("Log");
    FileHandler fileHandler;
    try {
      fileHandler = new FileHandler("log/log.txt");
      logger.addHandler(fileHandler);
      SimpleFormatter simpleFormatter = new SimpleFormatter();
      fileHandler.setFormatter(simpleFormatter);
    } catch (IOException | SecurityException e) {
      logger.info("Exception: " + e.getMessage());
    }
  }

  public static void writeError(Level level, Throwable exception) {
    logger.log(level, exception.getMessage(), exception);
  }

  public static void writeMessage(Level level, String message) {
    logger.log(level, message);
  }
}

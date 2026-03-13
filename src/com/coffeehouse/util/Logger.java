package com.coffeehouse.util;

import java.io.InputStream;
import java.util.Properties;

public final class Logger {
  private static final String CONFIG_FILE = "logging.properties";
  private static LogLevel logLevel = LogLevel.INFO;

  private Logger() {}

  static {
    try (InputStream input = Logger.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
      if (input != null) {
        Properties props = new Properties();
        props.load(input);
        String raw = props.getProperty("log.level", "INFO");
        logLevel = LogLevel.from(raw);
      }
    } catch (Exception e) {
      System.err.println("[WARN] Could not load logging.properties, using INFO level.");
    }
  }

  public static void info(String message) {
    if (shouldLog(LogLevel.INFO)) {
      System.out.println("[INFO] " + message);
    }
  }

  public static void debug(String message) {
    if (shouldLog(LogLevel.DEBUG)) {
      System.out.println("[DEBUG] " + message);
    }
  }

  public static void error(String message) {
    if (shouldLog(LogLevel.ERROR)) {
      System.err.println("[ERROR] " + message);
    }
  }

  public static void error(String message, Throwable throwable) {
    if (shouldLog(LogLevel.ERROR)) {
      System.err.println("[ERROR] " + message);
      throwable.printStackTrace(System.err);
    }
  }

  private static boolean shouldLog(LogLevel level) {
    return level.priority() >= logLevel.priority();
  }

  private enum LogLevel {
    DEBUG(0),
    INFO(5),
    ERROR(10);

    private final int priority;

    LogLevel(int priority) {
      this.priority = priority;
    }

    int priority() {
      return priority;
    }

    static LogLevel from(String raw) {
      if (raw == null) return INFO;
      
      String normalized = raw.trim().toUpperCase();
      for (LogLevel level : values()) {
        if (level.name().equals(normalized)) return level;
      }

      return INFO;
    }
  }
}

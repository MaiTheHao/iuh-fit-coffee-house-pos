package com.coffeehouse.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class EnvConfig {

	private static final String CONFIG_FILE = "application.properties";
	private static final Properties PROPERTIES = new Properties();

	private EnvConfig() {}

	static {
		try (InputStream input = EnvConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
			if (input == null) {
				Logger.error("Error: " + CONFIG_FILE + " not found in classpath.");
				throw new RuntimeException("Config file not found: " + CONFIG_FILE);
			}
			PROPERTIES.load(input);
			Logger.info("Configuration loaded from classpath.");
		} catch (IOException e) {
			Logger.error("Failed to read " + CONFIG_FILE, e);
			throw new RuntimeException("Cannot read " + CONFIG_FILE, e);
		}
	}

	public static String get(String key) {
		return PROPERTIES.getProperty(key);
	}

	public static String getOrDefault(String key, String defaultValue) {
		return PROPERTIES.getProperty(key, defaultValue);
	}

	public static String getRequired(String key) {
		String value = PROPERTIES.getProperty(key);
		if (value == null || value.isBlank()) {
			throw new IllegalStateException("Missing required config: " + key);
		}
		return value;
	}
}
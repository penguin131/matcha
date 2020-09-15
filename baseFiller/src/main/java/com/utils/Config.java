package com.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
	private static final Properties config = new Properties();
	private static final Properties localConfig = new Properties();
	static {
		try {
			File configFile = new File(System.getProperty("user.dir") + "/src/main/resources/application.properties");
			InputStream input1 = new FileInputStream(configFile);
			config.load(input1);
			File testConfigFile = new File(System.getProperty("user.dir") + "/src/main/resources/local/application.properties");
			InputStream input3 = new FileInputStream(testConfigFile);
			localConfig.load(input3);
		} catch (IOException ex) {
			ex.printStackTrace();
			System.exit(1);
		}
	}

	public static Properties getConfig() {
		if (getMode().equals(Mode.PROD)) {
			return config;
		} else {
			return localConfig;
		}
	}

	public static String getCoordinatesFile() {
		return System.getProperty("user.dir") + "/src/main/resources/" + config.getProperty("file");
	}

	public static Mode getMode() {
		if ("true".equals(config.getProperty("mode.prod"))) {
			return Mode.PROD;
		}
		return Mode.LOCAL;
	}

	public static String getUrl() {
		if (getMode().equals(Mode.PROD)) {
			return config.getProperty("url");
		} else {
			return localConfig.getProperty("url");
		}
	}

	public static File getLoginFile() {
		return new File(System.getProperty("user.dir") + "/src/main/resources/logins.txt");
	}

	public static File getEmailFile() {
		return new File(System.getProperty("user.dir") + "/src/main/resources/email.txt");
	}
}

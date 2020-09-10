package com;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class TestConfig {

	private static final Properties testConfig = new Properties();
	static {
		try {
			File testConfigFile = new File("target/test-classes/application.properties");
			InputStream input3 = new FileInputStream(testConfigFile);
			testConfig.load(input3);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static Properties getTestConfig() {
		return testConfig;
	}

	public static String getUrl() {
		return testConfig.getProperty("url");
	}
}

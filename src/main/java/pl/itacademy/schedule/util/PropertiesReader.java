package pl.itacademy.schedule.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {

	private static final String PROPERTIES_FILE_NAME = "application.properties";
	private static final String SEARCH_LOCATION_A = "./config/" + PROPERTIES_FILE_NAME;
	private static final String SEARCH_LOCATION_B = "./" + PROPERTIES_FILE_NAME;

	private Properties properties;
	private static final PropertiesReader INSTANCE = new PropertiesReader();

	private PropertiesReader() {
		readApplicationProperties();
	}

	public static PropertiesReader getInstance() {
		return INSTANCE;
	}

	public void readApplicationProperties() {
		try {
			readPropertiesFile(SEARCH_LOCATION_A);
		} catch (IOException e) {
			try {
				readPropertiesFile(SEARCH_LOCATION_B);
			} catch (IOException e2) {
				try {
					readDefaultsFromJAR();
				} catch (IOException ignored) {
				}
			}
		}
	}

	private void readPropertiesFile(String propertiesFileLocation) throws IOException {
		InputStream input = new FileInputStream(propertiesFileLocation);
		properties = new Properties();
		properties.load(input);
		input.close();
	}

	private void readDefaultsFromJAR() throws IOException {
		InputStream input = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME);
		properties = new Properties();
		if (input != null) {
			properties.load(input);
			input.close();
		}
	}

	public String readProperty(String key) {
		return properties.getProperty(key);
	}
}

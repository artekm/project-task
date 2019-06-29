package pl.itacademy.schedule.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {

	private static final String PROPERTIES_FILE_NAME = "application.properties";
	private static final String SEARCH_LOCATION_A = "./config/";
	private static final String SEARCH_LOCATION_B = "./";
	private static final String DEFAULT_EXCEL_NAME = "default";
	private static final String DEFAULT_EXCEL_EXTENSION = "xls";

	private Properties properties;
	private static final PropertiesReader INSTANCE = new PropertiesReader();

	private PropertiesReader() {
		readApplicationProperties(PROPERTIES_FILE_NAME);
	}

	public static PropertiesReader getInstance() {
		return INSTANCE;
	}

	public void readApplicationProperties(String applicationFileName) {
		try {
			readPropertiesFile(SEARCH_LOCATION_A + applicationFileName);
		} catch (IOException e) {
			try {
				readPropertiesFile(SEARCH_LOCATION_B + applicationFileName);
			} catch (IOException e2) {
				try {
					readDefaultsFromJAR();
				} catch (IOException e3) {
					properties.setProperty("excel.defaultName", DEFAULT_EXCEL_NAME);
					properties.setProperty("excel.defaultExtension", DEFAULT_EXCEL_EXTENSION);
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
		properties.load(input);
		input.close();
	}

	public String readProperty(String key) {
		return properties.getProperty(key);
	}
}

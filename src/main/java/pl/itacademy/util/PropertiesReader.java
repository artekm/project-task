package pl.itacademy.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {

    private static final String APPLICATION_PROPERTIES = "application.properties";
    private static final String PRIMARY_LOCATION = "./config/" + APPLICATION_PROPERTIES;
    private static final String SECONDARY_LOCATION = "./" + APPLICATION_PROPERTIES;

    private static final PropertiesReader INSTANCE = new PropertiesReader();

    private final Properties properties = new Properties();

    private PropertiesReader() {
        try (InputStream inputStream = createInputStream()) {
            properties.load(inputStream);
        } catch (IOException ignored) {
        }
    }

    public static PropertiesReader getINSTANCE() {
        return INSTANCE;
    }

    public String readProperty(String key) {
        return properties.getProperty(key);
    }

    private InputStream createInputStream() {
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(PRIMARY_LOCATION);
        } catch (IOException e) {
            try {
                inputStream = new FileInputStream(SECONDARY_LOCATION);
            } catch (IOException e1) {
                inputStream = this.getClass().getClassLoader().getResourceAsStream(APPLICATION_PROPERTIES);
            }
        }
        return inputStream;
    }

}

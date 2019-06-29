package pl.itacademy.util;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Properties;

public class PropertiesReader {

    private static final String APPLICATION_PROPERTIES = "application.properties";

    private static Properties properties = new Properties();

    private FileSystem fileSystem;

    public PropertiesReader() throws URISyntaxException, IOException {
        initReader();
    }

    public void initReader() throws IOException, URISyntaxException {
        URL resource = this.getClass().getClassLoader().getResource(APPLICATION_PROPERTIES);
        URI uri = resource.toURI();
        String[] array = uri.toString().split("!");
        fileSystem = FileSystems.newFileSystem(URI.create(array[0]), new HashMap<>());
        Path path = fileSystem.getPath(array[1]);
        properties.load(Files.newInputStream(path));
    }

    public String readProperty(String key) {
        return properties.getProperty(key);
    }

    public void closeReader() throws IOException {
        fileSystem.close();
    }
}

package pl.itacademy.schedule.util;

import static org.junit.Assert.assertEquals;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PopertiesReaderTest {
	private static final String PROPERTIES_FILE_NAME = "application.properties";
	private static final String SEARCH_LOCATION_A = "./config/";
	private static final String SEARCH_LOCATION_B = "./";

	private PropertiesReader reader;

	@Before
	public void setUp() throws Exception {
		storeApplicationPropertyFile(SEARCH_LOCATION_A + PROPERTIES_FILE_NAME);
		storeApplicationPropertyFile(SEARCH_LOCATION_B + PROPERTIES_FILE_NAME);
		reader = PropertiesReader.getInstance();
	}

	@Test
	public void PropertyBox_readsDefaults_fromLocationA() throws Exception {
		reader.readApplicationProperties(PROPERTIES_FILE_NAME);
		assertEquals("schedule", reader.readProperty("excel.defaultName"));
		assertEquals("H:mm", reader.readProperty("timeFormat"));
		assertEquals("d.MM.uuuu", reader.readProperty("dateFormat"));
	}

	@Test
	public void PropertyBox_readsDefaults_fromLocationB() throws Exception {
		deleteFile(SEARCH_LOCATION_A + PROPERTIES_FILE_NAME);
		reader.readApplicationProperties(PROPERTIES_FILE_NAME);
		assertEquals("schedule", reader.readProperty("excel.defaultName"));
		assertEquals("H:mm", reader.readProperty("timeFormat"));
		assertEquals("d.MM.uuuu", reader.readProperty("dateFormat"));
	}

	@Test
	public void PropertyBox_readsDefaultExcelFile_fromJAR() throws Exception {
		deleteFile(SEARCH_LOCATION_A + PROPERTIES_FILE_NAME);
		deleteFile(SEARCH_LOCATION_B + PROPERTIES_FILE_NAME);
		reader.readApplicationProperties(PROPERTIES_FILE_NAME);
		assertEquals("scheduleJAR", reader.readProperty("excel.defaultName"));
		assertEquals("H:mm", reader.readProperty("timeFormat"));
		assertEquals("d.MM.uuuu", reader.readProperty("dateFormat"));
	}

	@After
	public void tearDown() throws Exception {
		storeApplicationPropertyFile(SEARCH_LOCATION_A + PROPERTIES_FILE_NAME);
		storeApplicationPropertyFile(SEARCH_LOCATION_B + PROPERTIES_FILE_NAME);
	}

	private void storeApplicationPropertyFile(String location) throws Exception {
		List<String> fileContent = Arrays.asList("excel.defaultName=schedule",
				"timeFormat=H:mm", "dateFormat=d.MM.uuuu");
		Path path = Paths.get(location);
		Files.write(path, fileContent);
	}

	private void deleteFile(String location) throws Exception {
		Path path = Paths.get(location);
		Files.delete(path);
	}
}

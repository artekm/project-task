package pl.itacademy.schedule.util;

import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PropertiesReaderTest {
	private static final String SEARCH_LOCATION_A = "./config/application.properties";
	private static final String SEARCH_LOCATION_B = "./application.properties";

	private PropertiesReader reader;

	@Before
	public void setUp() throws Exception {
		storeApplicationPropertyFile(SEARCH_LOCATION_A);
		storeApplicationPropertyFile(SEARCH_LOCATION_B);
		reader = PropertiesReader.getInstance();
	}

	@Test
	public void PropertyBox_readsDefaults_fromLocationA() throws Exception {
		reader.readApplicationProperties();
		assertEquals("schedule", reader.readProperty("excel.defaultName"));
		assertEquals("H:mm", reader.readProperty("timeFormat"));
		assertEquals("d.MM.uuuu", reader.readProperty("dateFormat"));
	}

	@Test
	public void PropertyBox_readsDefaults_fromLocationB() throws Exception {
		deleteFile(SEARCH_LOCATION_A);
		reader.readApplicationProperties();
		assertEquals("schedule", reader.readProperty("excel.defaultName"));
		assertEquals("H:mm", reader.readProperty("timeFormat"));
		assertEquals("d.MM.uuuu", reader.readProperty("dateFormat"));
	}

	@Test
	public void PropertyBox_readsDefaultExcelFile_fromJAR() throws Exception {
		deleteFile(SEARCH_LOCATION_A);
		deleteFile(SEARCH_LOCATION_B);
		reader.readApplicationProperties();
		assertEquals("scheduleJAR", reader.readProperty("excel.defaultName"));
		assertEquals("H:mm", reader.readProperty("timeFormat"));
		assertEquals("d.MM.uuuu", reader.readProperty("dateFormat"));
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

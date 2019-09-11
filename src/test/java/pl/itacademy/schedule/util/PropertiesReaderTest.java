package pl.itacademy.schedule.util;

import static org.junit.Assert.assertNotNull;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

public class PropertiesReaderTest {
	private static final String SEARCH_LOCATION_A = "./config/application.properties";
	private static final String SEARCH_LOCATION_B = "./application.properties";

	private PropertiesReader reader;

	@Before
	public void setUp() throws Exception {
		reader = PropertiesReader.getInstance();
	}

	@Test
	public void PropertyBox_readsDefaults_fromLocationA() throws Exception {
		reader.readApplicationProperties();
		assertNotNull(reader.readProperty("excel.defaultName"));
		assertNotNull(reader.readProperty("timeFormat"));
		assertNotNull(reader.readProperty("dateFormat"));
	}

	@Test
	public void PropertyBox_readsDefaults_fromLocationB() throws Exception {
		hideFile(SEARCH_LOCATION_A);
		reader.readApplicationProperties();
		assertNotNull(reader.readProperty("excel.defaultName"));
		assertNotNull(reader.readProperty("timeFormat"));
		assertNotNull(reader.readProperty("dateFormat"));
		showFile(SEARCH_LOCATION_A);
	}

	@Test
	public void PropertyBox_readsDefaultExcelFile_fromJAR() throws Exception {
		hideFile(SEARCH_LOCATION_A);
		hideFile(SEARCH_LOCATION_B);
		reader.readApplicationProperties();
		assertNotNull(reader.readProperty("excel.defaultName"));
		assertNotNull(reader.readProperty("timeFormat"));
		assertNotNull(reader.readProperty("dateFormat"));
		showFile(SEARCH_LOCATION_A);
		showFile(SEARCH_LOCATION_B);
	}

	private void hideFile(String location) throws Exception {
		Path pathOld = Paths.get(location).normalize().toAbsolutePath();
		Path pathNew = Paths.get(location + ".hide").normalize().toAbsolutePath();
		Files.move(pathOld, pathNew);
	}

	private void showFile(String location) throws Exception {
		Path pathOld = Paths.get(location + ".hide").normalize().toAbsolutePath();
		Path pathNew = Paths.get(location).normalize().toAbsolutePath();
		Files.move(pathOld, pathNew);
	}
}

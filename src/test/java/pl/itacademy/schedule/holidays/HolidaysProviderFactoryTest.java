package pl.itacademy.schedule.holidays;

import static org.junit.Assert.assertEquals;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import pl.itacademy.schedule.util.PropertiesReader;

public class HolidaysProviderFactoryTest {

	@Test
	public void getProvider_returnsProviderStatedInPropertiesFile() {
		PropertiesReader properties = PropertiesReader.getInstance();
		String providerNameFromProperties = properties.readProperty("holidaysProvider");
		HolidaysProvider providerFromFactory = HolidaysProviderFactory.getProvider();
		assertEquals(providerNameFromProperties, providerFromFactory.getClass().getSimpleName());
	}

	@Test
	public void getProvider_returnsHolidaysNone_whenPropertiesFileMissing() throws Exception {
		hideFile("./config/application.properties");
		hideFile("./application.properties");
		PropertiesReader reader = PropertiesReader.getInstance();
		reader.readApplicationProperties();
		HolidaysProvider providerFromFactory = HolidaysProviderFactory.getProvider();
		assertEquals("HolidaysNone", providerFromFactory.getClass().getSimpleName());
		showFile("./config/application.properties");
		showFile("./application.properties");
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

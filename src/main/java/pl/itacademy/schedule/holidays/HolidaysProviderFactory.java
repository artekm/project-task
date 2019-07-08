package pl.itacademy.schedule.holidays;

import pl.itacademy.schedule.util.PropertiesReader;

public class HolidaysProviderFactory {

	public static HolidaysProvider getProvider() {
		PropertiesReader properties = PropertiesReader.getInstance();
		String provider = properties.readProperty("holidaysProvider");

		String pack = HolidaysProviderFactory.class.getPackageName();
		String className = pack + "." + provider;
		HolidaysProvider webClient;
		try {
			webClient = (HolidaysProvider) Class.forName(className).getConstructor().newInstance();
		} catch (Exception e) {
			System.out.println("Class " + className + " not found");
			webClient = new HolidaysNone();
		}
		System.out.println("Using " + webClient.getClass().getName());
		return webClient;
	}
}

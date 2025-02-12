package pl.itacademy.schedule.holidays;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import javax.net.ssl.HttpsURLConnection;
import com.jayway.jsonpath.JsonPath;

import pl.itacademy.schedule.util.PropertiesReader;

public class HolidaysFromEnrico implements HolidaysProvider {

	@Override
	public Collection<LocalDate> getHolidays(LocalDate from, LocalDate to) {
		try {
			String holidaysString = askEnricoForHolidays(from, to);
			List<LocalDate> holidays = parseHolidays(holidaysString);
			return holidays;
		} catch (Exception e4) {
			System.out.println("Unable to obtain holidays from the web");
			return Collections.emptyList();
		}
	}

	private String askEnricoForHolidays(LocalDate startDate, LocalDate endDate) throws IOException {
		System.setProperty("com.sun.security.enableAIAcaIssuers", "true");

		String queryString = prepareQuery(startDate, endDate);
		URL myURL = new URL(queryString);

		HttpsURLConnection con = (HttpsURLConnection) myURL.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String received = in.readLine();
		in.close();

		return received;
	}

	private String prepareQuery(LocalDate startDate, LocalDate endDate) {
		PropertiesReader properties = PropertiesReader.getInstance();
		String url = properties.readProperty("enrico.url");
		String action = properties.readProperty("enrico.action");
		String country = properties.readProperty("enrico.country");
		String type = properties.readProperty("enrico.type");
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(properties.readProperty("enrico.dateFormat"));

		String queryString = String.format("%s?action=%s&country=%s&type=%s&fromDate=%s&toDate=%s",
				url, action, country, type, startDate.format(dateFormatter), endDate.format(dateFormatter));
		
		return queryString;
	}
	
	private List<LocalDate> parseHolidays(String input) {

		PropertiesReader properties = PropertiesReader.getInstance();
		String jsonPath = properties.readProperty("enrico.jsonPath");

		List<Map<String, Integer>> holidays = JsonPath.parse(input).read(jsonPath);

		List<LocalDate> holidayDates = holidays.stream()
				.map(holiday -> LocalDate.of(holiday.get("year"), holiday.get("month"), holiday.get("day")))
				.collect(Collectors.toList());

		return holidayDates;
	}
}

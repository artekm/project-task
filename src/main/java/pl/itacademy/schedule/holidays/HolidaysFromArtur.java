package pl.itacademy.schedule.holidays;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.jayway.jsonpath.JsonPath;

import pl.itacademy.schedule.util.PropertiesReader;

public class HolidaysFromArtur implements HolidaysProvider{
	
	@Override
	public Collection<LocalDate> getHolidays(LocalDate from, LocalDate to) {
		try {
			String holidaysString = askArturForHolidays(from, to);
			List<LocalDate> holidays = parseHolidays(holidaysString);
			return holidays;
		} catch (Exception e) {
			System.out.println("Unable to obtain holidays from the web");
			return Collections.emptyList();
		}
	}

	private String askArturForHolidays(LocalDate startDate, LocalDate endDate) throws IOException {
		System.setProperty("com.sun.security.enableAIAcaIssuers", "true");

		String queryString = prepareQuery(startDate, endDate);
		URL myURL = new URL(queryString);

		HttpURLConnection con = (HttpURLConnection) myURL.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String received = in.readLine();
		in.close();

		return received;
	}

	private String prepareQuery(LocalDate startDate, LocalDate endDate) {
		PropertiesReader properties = PropertiesReader.getInstance();
		String url = properties.readProperty("artur.url");
		String action = properties.readProperty("artur.action");
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(properties.readProperty("artur.dateFormat"));

		String queryString = String.format("%s/%s?begin=%s&end=%s",
				url, action, startDate.format(dateFormatter), endDate.format(dateFormatter));
		return queryString;
	}
	
	private List<LocalDate> parseHolidays(String input) {

		PropertiesReader properties = PropertiesReader.getInstance();
		String jsonPath = properties.readProperty("artur.jsonPath");

		List<String> holidays = JsonPath.parse(input).read(jsonPath);

		List<LocalDate> holidayDates = holidays.stream()
				.map(holiday -> LocalDate.parse(holiday))
				.collect(Collectors.toList());

		return holidayDates;
	}

}

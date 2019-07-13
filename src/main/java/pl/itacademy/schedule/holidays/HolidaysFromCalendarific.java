package pl.itacademy.schedule.holidays;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import com.jayway.jsonpath.JsonPath;

import pl.itacademy.schedule.util.PropertiesReader;

public class HolidaysFromCalendarific implements HolidaysProvider {

	@Override
	public Collection<LocalDate> getHolidays(LocalDate fromDate, LocalDate toDate) {
		int yearStart = fromDate.getYear();
		int yearEnd = toDate.getYear();
		int year = yearStart;
		try {
			Collection<LocalDate> publicHolidays = getHolidaysForYear(year);
			while (year < yearEnd) {
				Thread.sleep(1500);
				year++;
				publicHolidays.addAll(getHolidaysForYear(year));
			}
			return publicHolidays;
		} catch (Exception e4) {
			System.out.println("Unable to obtain holidays from the web");
			return Collections.emptyList();
		}
	}

	public List<LocalDate> getHolidaysForYear(int year) {

		PropertiesReader reader = PropertiesReader.getInstance();
		String url = reader.readProperty("calendarific.url");
		String country = reader.readProperty("calendarific.country");
		String type = reader.readProperty("calendarific.type");
		String key = reader.readProperty("calendarific.api.key");
		String jsonPath = reader.readProperty("calendarific.jsonPath");

		WebTarget webTarget = ClientBuilder.newClient()
				.target(url)
				.queryParam("country", country)
				.queryParam("type", type)
				.queryParam("api_key", key)
				.queryParam("year", year);

		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

		String json = invocationBuilder.get(String.class);

		List<String> holidays = JsonPath.parse(json).read(jsonPath);

		return holidays.stream()
				.map(holiday -> LocalDate.parse(holiday, DateTimeFormatter.ISO_DATE))
				.collect(Collectors.toList());
	}
}

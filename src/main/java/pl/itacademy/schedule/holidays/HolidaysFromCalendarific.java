package pl.itacademy.schedule.holidays;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import com.jayway.jsonpath.JsonPath;

import pl.itacademy.schedule.util.PropertiesReader;

public class HolidaysFromCalendarific implements HolidaysProvider {

	@Override
	public Collection<LocalDate> getHolidays(LocalDate from, LocalDate to) {
		int yearStart = from.getYear();
		int yearEnd = to.getYear();
		int year = yearStart;
		Collection<LocalDate> publicHolidays = getHolidaysForYear(year);
		while (year < yearEnd) {
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			year++;
			publicHolidays.addAll(getHolidaysForYear(year));
		}
		return publicHolidays;
	}

	public List<LocalDate> getHolidaysForYear(int year) {

		Client client = ClientBuilder.newClient();
		String url = PropertiesReader.getInstance().readProperty("calendarific.url");
		String country = PropertiesReader.getInstance().readProperty("calendarific.country");
		String type = PropertiesReader.getInstance().readProperty("calendarific.type");
		String key = PropertiesReader.getInstance().readProperty("calendarific.api.key");
		String jsonPath = PropertiesReader.getInstance().readProperty("calendarific.jsonPath");
		WebTarget webTarget = client.target(url)
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

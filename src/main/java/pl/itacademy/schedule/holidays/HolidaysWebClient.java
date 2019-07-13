package pl.itacademy.schedule.holidays;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import com.jayway.jsonpath.JsonPath;
import pl.itacademy.schedule.util.PropertiesReader;

public class HolidaysWebClient {

	public List<LocalDate> getHolidays(int year) {

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

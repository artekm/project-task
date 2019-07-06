package pl.itacademy.schedule.holidays;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import pl.itacademy.schedule.util.PropertiesReader;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

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

        DocumentContext context = JsonPath.parse(json);
        JSONArray holidays = context.read(jsonPath);

        return holidays.stream()
                .map(holiday -> LocalDate.parse((String) holiday, DateTimeFormatter.ISO_DATE))
                .collect(Collectors.toList());
    }
}

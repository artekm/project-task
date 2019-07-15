package pl.itacademy.web;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import pl.itacademy.util.PropertiesReader;

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

    public static void main(String[] args) throws InterruptedException {
        HolidaysWebClient client = new HolidaysWebClient();
        List<LocalDate> holidays19 = client.getHolidays(2019);
        Thread.sleep(1100);
        List<LocalDate> holidays20 = client.getHolidays(2020);

        System.out.println(holidays19);
        System.out.println(holidays20);
    }

    public List<LocalDate> getHolidays(int year) {
        try {
            Thread.sleep(1100);
        } catch (InterruptedException ignore) {
        }

        PropertiesReader propertiesReader = PropertiesReader.getInstance();
        String url = propertiesReader.readProperty("calendarific.url");
        String path = propertiesReader.readProperty("calendarific.path");
        String apiKey = propertiesReader.readProperty("calendarific.key");
        String jsonPath = propertiesReader.readProperty("calendarific.jsonPath");
        String countryCode = propertiesReader.readProperty("calendarific.countryCode");
        String holidayType = propertiesReader.readProperty("calendarific.holidayType");

        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(url)
                .path(path)
                .queryParam("api_key", apiKey)
                .queryParam("year", year)
                .queryParam("type", holidayType)
                .queryParam("country", countryCode);
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

        String json = invocationBuilder.get(String.class);

        DocumentContext jsonContext = JsonPath.parse(json);
        JSONArray holidays = jsonContext.read(jsonPath);

        return holidays.stream()
                .map(value -> LocalDate.parse((String) value, DateTimeFormatter.ISO_DATE))
                .collect(Collectors.toList());
    }
}

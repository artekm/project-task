package pl.itacademy.schedule;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ObjectMapperTest {

    @Test
    public void objectMapperTest() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Person janKowalski = new Person("Jan", "Kowalski", new Address("Krakow"), LocalDate.of(2019, 1, 1));
        String json = objectMapper.writeValueAsString(janKowalski);

        System.out.println(json);

        Person personFromJson = objectMapper.readValue(json, Person.class);
        System.out.println(personFromJson);
    }

    private static class Person {
        String firstName;
        String lastName;
        Address address;
        @JsonSerialize(using = LocalDateSerializer.class)
        @JsonDeserialize(using = LocalDateDeserializer.class)
        LocalDate birthDay;

        public Person(String firstName, String lastName, Address address, LocalDate birthDay) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.address = address;
            this.birthDay = birthDay;
        }

        public Person() {
        }

        public LocalDate getBirthDay() {
            return birthDay;
        }

        public void setBirthDay(LocalDate birthDay) {
            this.birthDay = birthDay;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    ", address=" + address +
                    ", birthDay=" + birthDay +
                    '}';
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public Address getAddress() {
            return address;
        }

        public void setAddress(Address address) {
            this.address = address;
        }
    }

    private static class Address {
        String city;

        public Address() {
        }

        public Address(String city) {
            this.city = city;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        @Override
        public String toString() {
            return "Address{" +
                    "city='" + city + '\'' +
                    '}';
        }
    }

    private static class LocalDateSerializer extends StdSerializer<LocalDate> {

        protected LocalDateSerializer() {
            super(LocalDate.class);
        }

        @Override
        public void serialize(LocalDate localDate, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeString(localDate.format(DateTimeFormatter.ISO_DATE));
        }
    }

    private static class LocalDateDeserializer extends StdDeserializer<LocalDate> {
        public LocalDateDeserializer() {
            super(LocalDate.class);
        }

        @Override
        public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            return LocalDate.parse(jsonParser.readValueAs(String.class), DateTimeFormatter.ISO_DATE);
        }
    }

}
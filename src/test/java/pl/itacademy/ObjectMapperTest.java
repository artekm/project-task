package pl.itacademy;

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
import java.util.Objects;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class ObjectMapperTest {

    @Test
    public void objectMapperDemo() throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();

        Person janKowalski = new Person("Jan", "Kowalski", new Address("Krakow"), LocalDate.now());
        String json = objectMapper.writeValueAsString(janKowalski);

        System.out.println(json);

        Person person = objectMapper.readValue(json, Person.class);
        System.out.println(person);

        assertThat(janKowalski, equalTo(person));
    }

    private static class Person {
        private String firstName;
        private String lastName;
        private Address address;

        @JsonSerialize(using = LocalDateSerializer.class)
        @JsonDeserialize(using = LocalDateDeserializer.class)
        private LocalDate birthDay;

        public Person() {
        }

        public Person(String firstName, String lastName, Address address, LocalDate birthDay) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.address = address;
            this.birthDay = birthDay;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Person person = (Person) o;
            return Objects.equals(firstName, person.firstName) &&
                    Objects.equals(lastName, person.lastName) &&
                    Objects.equals(address, person.address) &&
                    Objects.equals(birthDay, person.birthDay);
        }

        @Override
        public int hashCode() {

            return Objects.hash(firstName, lastName, address, birthDay);
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
        private String city;

        public Address() {
        }

        public Address(String city) {
            this.city = city;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Address address = (Address) o;
            return Objects.equals(city, address.city);
        }

        @Override
        public int hashCode() {

            return Objects.hash(city);
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
        public void serialize(LocalDate value, JsonGenerator generator, SerializerProvider provider) throws IOException {
            generator.writeString(value.format(DateTimeFormatter.ISO_DATE));
        }
    }

    private static class LocalDateDeserializer extends StdDeserializer<LocalDate> {

        protected LocalDateDeserializer() {
            super(LocalDate.class);
        }

        @Override
        public LocalDate deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
            return LocalDate.parse(parser.getText(), DateTimeFormatter.ISO_DATE);
        }
    }
}

package pl.itacademy.schedule.parameters;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import pl.itacademy.schedule.exception.IncorrectParametersException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.TUESDAY;

public class ParametersValidatorTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private ParametersValidator validator;

    @Before
    public void setUp() {
        validator = new ParametersValidator();
    }

    @Test
    public void validate_doesNotContainsRequiredParameters_throwsIncorrectParametersException() throws IncorrectParametersException {
        EnteredParameters parameters = new EnteredParameters();

        expectedException.expect(IncorrectParametersException.class);
        expectedException.expectMessage("Missing required parameter");

        validator.validate(parameters);
    }

    @Test
    public void validate_containsAllProperParameters_throwsNoExceptions() throws IncorrectParametersException {
        EnteredParameters parameters = new EnteredParameters();
        parameters.setBeginTime(LocalTime.of(10, 0));
        parameters.setEndTime(LocalTime.of(11, 0));
        parameters.setStartDate(LocalDate.of(2010, 10, 10));
        parameters.setHoursNumber(30);
        parameters.setLessonDays(Arrays.asList(MONDAY, TUESDAY));

        validator.validate(parameters);
    }

    @Test
    public void validate_negativeHoursNumber_throwsIncorrectParametersExeption() throws IncorrectParametersException {
        EnteredParameters parameters = new EnteredParameters();
        parameters.setBeginTime(LocalTime.of(10, 0));
        parameters.setEndTime(LocalTime.of(11, 0));
        parameters.setStartDate(LocalDate.of(2010, 10, 10));
        parameters.setHoursNumber(-30);
        parameters.setLessonDays(Arrays.asList(MONDAY, TUESDAY));

        expectedException.expect(IncorrectParametersException.class);
        expectedException.expectMessage("Incorrect hours number -- can't be negative");

        validator.validate(parameters);
    }

    @Test
    public void validate_startTimeAfterEndTime_throwsIncorrectParametersExeption() throws IncorrectParametersException {
        EnteredParameters parameters = new EnteredParameters();
        parameters.setBeginTime(LocalTime.of(12, 0));
        parameters.setEndTime(LocalTime.of(11, 0));
        parameters.setStartDate(LocalDate.of(2010, 10, 10));
        parameters.setHoursNumber(30);
        parameters.setLessonDays(Arrays.asList(MONDAY, TUESDAY));

        expectedException.expect(IncorrectParametersException.class);
        expectedException.expectMessage("Start time can't be after end time");

        validator.validate(parameters);
    }
}
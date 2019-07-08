package pl.itacademy.validation;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import pl.itacademy.exceptions.InvalidParameterException;
import pl.itacademy.parameters.InputParameters;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;

public class ParametersValidatorTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private ParametersValidator validator;

    @Before
    public void setUp() {
        validator = new ParametersValidator();
    }

    @Test
    public void validate_nullLessonDays_throwsInvalidParametersException() throws InvalidParameterException {
        expectedException.expect(InvalidParameterException.class);
        expectedException.expectMessage("Lesson days are empty");

        InputParameters inputParameters = new InputParameters();

        validator.validate(inputParameters);
    }

    @Test
    public void validate_emptyLessonDays_throwsInvalidParametersException() throws InvalidParameterException {
        expectedException.expect(InvalidParameterException.class);
        expectedException.expectMessage("Lesson days are empty");

        InputParameters inputParameters = new InputParameters();
        inputParameters.setLessonDays(Collections.emptyList());

        validator.validate(inputParameters);
    }

    @Test
    public void validate_emptyHoursNumber_throwsInvalidParametersException() throws InvalidParameterException {
        expectedException.expect(InvalidParameterException.class);
        expectedException.expectMessage("Lesson hours are empty");

        InputParameters inputParameters = new InputParameters();
        inputParameters.setLessonDays(Collections.singletonList(DayOfWeek.MONDAY));

        validator.validate(inputParameters);
    }

    @Test
    public void validate_nullBeginTime_throwsInvalidParametersException() throws InvalidParameterException {
        expectedException.expect(InvalidParameterException.class);
        expectedException.expectMessage("Begin time is null or empty");

        InputParameters inputParameters = new InputParameters();
        inputParameters.setLessonDays(Collections.singletonList(DayOfWeek.MONDAY));
        inputParameters.setHoursNumber(2);

        validator.validate(inputParameters);
    }

    @Test
    public void validate_nullEndTime_throwsInvalidParametersException() throws InvalidParameterException {
        expectedException.expect(InvalidParameterException.class);
        expectedException.expectMessage("End time is null or empty");

        InputParameters inputParameters = new InputParameters();
        inputParameters.setLessonDays(Collections.singletonList(DayOfWeek.MONDAY));
        inputParameters.setHoursNumber(2);
        inputParameters.setBeginTime(LocalTime.now());

        validator.validate(inputParameters);
    }

    @Test
    public void validate_nullStartDate_throwsInvalidParametersException() throws InvalidParameterException {
        expectedException.expect(InvalidParameterException.class);
        expectedException.expectMessage("Start date is null or empty");

        InputParameters inputParameters = new InputParameters();
        inputParameters.setLessonDays(Collections.singletonList(DayOfWeek.MONDAY));
        inputParameters.setHoursNumber(2);
        inputParameters.setBeginTime(LocalTime.of(17, 0));
        inputParameters.setEndTime(LocalTime.of(18, 30));

        validator.validate(inputParameters);
    }

    @Test
    public void validate_negativeCountOfHours_throwsInvalidParametersException() throws InvalidParameterException {
        expectedException.expect(InvalidParameterException.class);
        expectedException.expectMessage("Hours should be a positive number");

        InputParameters inputParameters = new InputParameters();
        inputParameters.setLessonDays(Collections.singletonList(DayOfWeek.MONDAY));
        inputParameters.setStartDate(LocalDate.now());
        inputParameters.setHoursNumber(-2);
        inputParameters.setBeginTime(LocalTime.of(17, 0));
        inputParameters.setEndTime(LocalTime.of(18, 30));

        validator.validate(inputParameters);
    }

    @Test
    public void validate_hoursNumberIsZero_throwsInvalidParametersException() throws InvalidParameterException {
        expectedException.expect(InvalidParameterException.class);
        expectedException.expectMessage("Hours should be a positive number");

        InputParameters inputParameters = new InputParameters();
        inputParameters.setLessonDays(Collections.singletonList(DayOfWeek.MONDAY));
        inputParameters.setStartDate(LocalDate.now());
        inputParameters.setHoursNumber(0);
        inputParameters.setBeginTime(LocalTime.of(17, 0));
        inputParameters.setEndTime(LocalTime.of(18, 30));

        validator.validate(inputParameters);
    }

    @Test
    public void validate_beginTimeAfterEndTime_throwsInvalidParametersException() throws InvalidParameterException {
        expectedException.expect(InvalidParameterException.class);
        expectedException.expectMessage("Begin time is after end time");

        InputParameters inputParameters = new InputParameters();
        inputParameters.setLessonDays(Collections.singletonList(DayOfWeek.MONDAY));
        inputParameters.setStartDate(LocalDate.now());
        inputParameters.setHoursNumber(4);
        inputParameters.setBeginTime(LocalTime.of(19, 0));
        inputParameters.setEndTime(LocalTime.of(18, 30));

        validator.validate(inputParameters);
    }

    @Test
    public void validate_properParameters_throwsNoException() throws InvalidParameterException {
        InputParameters inputParameters = new InputParameters();
        inputParameters.setLessonDays(Collections.singletonList(DayOfWeek.MONDAY));
        inputParameters.setStartDate(LocalDate.now());
        inputParameters.setHoursNumber(4);
        inputParameters.setBeginTime(LocalTime.of(17, 0));
        inputParameters.setEndTime(LocalTime.of(18, 30));

        validator.validate(inputParameters);
    }
}
package pl.itacademy.validation;

import pl.itacademy.exceptions.InvalidParameterException;
import pl.itacademy.parameters.InputParameters;

import java.time.DayOfWeek;
import java.util.Collection;

import static java.util.Objects.isNull;

public class ParametersValidator {

    public void validate(InputParameters inputParameters) throws InvalidParameterException {
        Collection<DayOfWeek> lessonDays = inputParameters.getLessonDays();
        if (isNull(lessonDays) || inputParameters.getLessonDays().isEmpty()) {
            throw new InvalidParameterException("Lesson days are empty");
        }

        if (isNull(inputParameters.getHoursNumber())) {
            throw new InvalidParameterException("Lesson hours are empty");
        }

        if (isNull(inputParameters.getBeginTime())) {
            throw new InvalidParameterException("Begin time is null or empty");
        }

        if (isNull(inputParameters.getEndTime())) {
            throw new InvalidParameterException("End time is null or empty");
        }

        if (isNull(inputParameters.getStartDate())) {
            throw new InvalidParameterException("Start date is null or empty");
        }

        if (inputParameters.getHoursNumber() <= 0) {
            throw new InvalidParameterException("Hours should be a positive number");
        }

        if (inputParameters.getBeginTime().isAfter(inputParameters.getEndTime())) {
            throw new InvalidParameterException("Begin time is after end time");
        }
    }
}

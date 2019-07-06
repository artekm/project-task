package pl.itacademy.schedule.parameters;

import org.apache.commons.collections4.CollectionUtils;
import pl.itacademy.schedule.exception.IncorrectParametersException;

import java.time.LocalTime;

import static java.util.Objects.isNull;

public class ParametersValidator {
    public void validate(EnteredParameters parameters) throws IncorrectParametersException {

        Integer numberOfHours = parameters.getHoursNumber();
        LocalTime beginTime = parameters.getBeginTime();
        LocalTime endTime = parameters.getEndTime();
        if (CollectionUtils.isEmpty(parameters.getLessonDays())
                || isNull(beginTime)
                || isNull(endTime)
                || isNull(parameters.getStartDate())
                || isNull(numberOfHours)) {
            throw new IncorrectParametersException("Missing required parameter");
        }
        if (numberOfHours <= 0) {
            throw new IncorrectParametersException("Incorrect hours number -- can't be negative or zero");
        }
        if (beginTime.isAfter(endTime)) {
            throw new IncorrectParametersException("Start time can't be after end time");
        }
    }

}

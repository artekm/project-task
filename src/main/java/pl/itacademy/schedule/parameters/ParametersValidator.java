package pl.itacademy.schedule.parameters;

import pl.itacademy.schedule.exception.IncorrectParametersException;

import java.time.LocalTime;

public class ParametersValidator {
    public void validate(EnteredParameters parameters) throws IncorrectParametersException {

        Integer numberOfHours = parameters.getHoursNumber();
        LocalTime beginTime = parameters.getBeginTime();
        LocalTime endTime = parameters.getEndTime();
        if (parameters.getLessonDays()== null || parameters.getLessonDays().isEmpty()
        || beginTime ==null
        || endTime ==null
        || parameters.getStartDate()==null
        || numberOfHours ==null){
            throw new IncorrectParametersException("Missing required parameter");

        }
        if (numberOfHours <=0){
            throw new   IncorrectParametersException("Incorrect hours number -- can't be negative or zero");
        }
        if (beginTime.isAfter(endTime)){
            throw new   IncorrectParametersException("Start time can't be after end time");
        }
    }

}

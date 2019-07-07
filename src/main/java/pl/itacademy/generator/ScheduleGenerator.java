package pl.itacademy.generator;

import pl.itacademy.parameters.InputParameters;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;

public class ScheduleGenerator {

    public Schedule generate(InputParameters parameters) {
        long requiredTime = parameters.getHoursNumber() * 60;
        long lessonDuration = Duration.between(parameters.getBeginTime(), parameters.getEndTime()).toMinutes();
        Collection<Lesson> lessons = new ArrayList<>();
        while (requiredTime > 0) {
            LocalDate nextDay = getNextDay(parameters.getStartDate(), parameters.getLessonDays());
            lessons.add(new Lesson(nextDay, parameters.getBeginTime(), parameters.getEndTime()));

            requiredTime -= lessonDuration;
        }
        return new Schedule(lessons, true);
    }


    private LocalDate getNextDay(LocalDate startDate, Collection<DayOfWeek> classesDays) {

        while (isNotRequiredDayOfWeek(classesDays, startDate)) {

            startDate = startDate.plusDays(1);
        }
        return startDate;
    }

    private boolean isNotRequiredDayOfWeek(Collection<DayOfWeek> classesDays, LocalDate currentDate) {

        return !isRequiredDayOfWeek(classesDays, currentDate);
    }

    private boolean isRequiredDayOfWeek(Collection<DayOfWeek> classesDays, LocalDate currentDate) {
        return !classesDays.contains(currentDate.getDayOfWeek());
    }

}

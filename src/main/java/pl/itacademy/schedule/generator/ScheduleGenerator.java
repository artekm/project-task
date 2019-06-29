package pl.itacademy.schedule.generator;

import pl.itacademy.schedule.parameters.EnteredParameters;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

public class ScheduleGenerator {

    public Schedule generate(EnteredParameters parameters) {

        Collection<Lesson> lessons = new ArrayList<>();

        long dailyMinutes = Duration.between(parameters.getBeginTime(), parameters.getEndTime()).toMinutes();
        long totalMinutes = parameters.getHoursNumber() * 60;
        LocalDate startDate = findNextDate(parameters.getStartDate(), parameters.getLessonDays());
        while (totalMinutes > 0) {
            lessons.add(new Lesson(startDate, parameters.getBeginTime(), parameters.getEndTime()));
            totalMinutes -= dailyMinutes;
            startDate = findNextDate(startDate.plusDays(1), parameters.getLessonDays());
        }

        return new Schedule(lessons, true);
    }

    private LocalDate findNextDate(LocalDate startDate, Collection<DayOfWeek> daysOfWeek) {
        while (notLessonsDayOfWeek(daysOfWeek, startDate)) {
            startDate = startDate.plusDays(1);
        }
        return startDate;
    }

    private boolean notLessonsDayOfWeek(Collection<DayOfWeek> daysOfWeek, LocalDate date) {
        return !daysOfWeek.contains(date.getDayOfWeek());
    }

}

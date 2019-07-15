package pl.itacademy.generator;

import pl.itacademy.parameters.InputParameters;
import pl.itacademy.web.HolidaysWebClient;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ScheduleGenerator {

    private HolidaysWebClient webClient;
    private List<LocalDate> holidays;

    public ScheduleGenerator(HolidaysWebClient webClient) {
        this.webClient = webClient;
    }

    public Schedule generate(InputParameters parameters) {
        holidays = webClient.getHolidays(parameters.getStartDate().getYear());

        long requiredTime = parameters.getHoursNumber() * 60;
        long lessonDuration = Duration.between(parameters.getBeginTime(), parameters.getEndTime()).toMinutes();
        Collection<Lesson> lessons = new ArrayList<>();

        Schedule schedule = new Schedule(lessons, parameters.getHoursNumber(), true);

        Collection<DayOfWeek> lessonDaysOfWeek = parameters.getLessonDays();
        LocalDate nextDay = getNextDay(parameters.getStartDate(), lessonDaysOfWeek);
        while (requiredTime > 0) {
            if (requiredTime < lessonDuration) {
                lessons.add(new Lesson(nextDay, parameters.getBeginTime(), parameters.getEndTime().minusMinutes(lessonDuration - requiredTime)));
                schedule.setLessonsFitToSchedule(false);
            } else {
                lessons.add(new Lesson(nextDay, parameters.getBeginTime(), parameters.getEndTime()));
            }
            requiredTime -= lessonDuration;
            nextDay = getNextDay(nextDay.plusDays(1), lessonDaysOfWeek);
        }
        return schedule;
    }

    private LocalDate getNextDay(LocalDate startDate, Collection<DayOfWeek> classesDays) {
        int currentYear = startDate.getYear();
        while (isNotRequiredDayOfWeek(classesDays, startDate) || isHoliday(startDate)) {
            startDate = startDate.plusDays(1);
            if (startDate.getYear() != currentYear) {
                currentYear++;
                updateHolidays(currentYear);
            }
        }
        return startDate;
    }

    private void updateHolidays(int year) {
        holidays = webClient.getHolidays(year);
    }

    private boolean isHoliday(LocalDate date) {
        return holidays.contains(date);
    }

    private boolean isNotRequiredDayOfWeek(Collection<DayOfWeek> classesDays, LocalDate currentDate) {
        return !isRequiredDayOfWeek(classesDays, currentDate);
    }

    private boolean isRequiredDayOfWeek(Collection<DayOfWeek> classesDays, LocalDate currentDate) {
        return classesDays.contains(currentDate.getDayOfWeek());
    }

}

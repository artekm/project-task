package pl.itacademy.schedule.generator;

import pl.itacademy.schedule.parameters.EnteredParameters;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;

public class ScheduleGenerator {
	private Collection<DayOfWeek> lessonDays;

	public Schedule generate(EnteredParameters parameters) {

		Collection<Lesson> lessons = new ArrayList<>();
		Schedule schedule = new Schedule();

		LocalTime beginTime = parameters.getBeginTime();
		LocalTime endTime = parameters.getEndTime();
		lessonDays = parameters.getLessonDays();

		long dailyMinutes = Duration.between(beginTime, endTime).toMinutes();
		long totalMinutes = parameters.getHoursNumber() * 60;
		LocalDate startDate = findNextDate(parameters.getStartDate());
		while (totalMinutes >= dailyMinutes) {
			lessons.add(new Lesson(startDate, beginTime, endTime));
			totalMinutes -= dailyMinutes;
			startDate = findNextDate(startDate.plusDays(1));
		}
		if (totalMinutes > 0) {
			lessons.add(new Lesson(startDate, beginTime, beginTime.plusMinutes(totalMinutes)));
			schedule.setLastDayShorter(true);
		}
		schedule.setLessons(lessons);
		schedule.setNumberOfHours(parameters.getHoursNumber());
		schedule.setNumberOfDays(lessons.size());
		return schedule;
	}

	private LocalDate findNextDate(LocalDate startDate) {
		while (notLessonsDayOfWeek(startDate)) {
			startDate = startDate.plusDays(1);
		}
		return startDate;
	}

	private boolean notLessonsDayOfWeek(LocalDate date) {
		return !lessonDays.contains(date.getDayOfWeek());
	}
}

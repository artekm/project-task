package pl.itacademy.schedule.generator;

import pl.itacademy.schedule.holidays.HolidaysWebClient;
import pl.itacademy.schedule.parameters.EnteredParameters;

import java.time.*;
import java.util.*;

public class ScheduleGenerator {

	private HolidaysWebClient webClient;

	public ScheduleGenerator(HolidaysWebClient webClient) {
		this.webClient = webClient;
	}

	public Schedule generate(EnteredParameters parameters) {

		Collection<Lesson> lessons = new ArrayList<>();
		Schedule schedule = new Schedule();
		LocalTime beginTime = parameters.getBeginTime();
		LocalTime endTime = parameters.getEndTime();
		Collection<DayOfWeek> lessonDays = parameters.getLessonDays();
		long dailyMinutes = Duration.between(beginTime, endTime).toMinutes();
		long totalMinutes = parameters.getHoursNumber() * 60;

		Collection<LocalDate> publicHolidays = webClient.getHolidaysFromEnrico(parameters.getStartDate(),
				parameters.getStartDate().plusYears(1));

		LocalDate startDate = findNextDate(parameters.getStartDate(), lessonDays, publicHolidays);
		while (totalMinutes >= dailyMinutes) {
			lessons.add(new Lesson(startDate, beginTime, endTime));
			totalMinutes -= dailyMinutes;
			startDate = findNextDate(startDate.plusDays(1), lessonDays, publicHolidays);
		}
		if (totalMinutes > 0) {
			lessons.add(new Lesson(startDate, beginTime, beginTime.plusMinutes(totalMinutes)));
			schedule.setLastDayShorter(true);
		}
		schedule.setLessons(lessons);
		schedule.setNumberOfHours(parameters.getHoursNumber());

		return schedule;
	}

	private LocalDate findNextDate(LocalDate startDate, Collection<DayOfWeek> lessonDays,
			Collection<LocalDate> publicHolidays) {
		while (notLessonsDayOfWeek(startDate, lessonDays) || isPublicHoliday(startDate, publicHolidays)) {
			startDate = startDate.plusDays(1);
		}
		return startDate;
	}

	private boolean notLessonsDayOfWeek(LocalDate date, Collection<DayOfWeek> lessonDays) {
		return !lessonDays.contains(date.getDayOfWeek());
	}

	private boolean isPublicHoliday(LocalDate date, Collection<LocalDate> publicHolidays) {
		return publicHolidays.contains(date);
	}
}

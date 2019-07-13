package pl.itacademy.schedule.generator;

import java.time.*;
import java.util.*;

import pl.itacademy.schedule.holidays.HolidaysProvider;
import pl.itacademy.schedule.parameters.EnteredParameters;

public class ScheduleGenerator {

	private HolidaysProvider webClient;

	public ScheduleGenerator(HolidaysProvider webClient) {
		this.webClient = webClient;
	}

	public Schedule generate(EnteredParameters parameters) {

		Collection<Lesson> lessons = new ArrayList<>();
		Schedule schedule = new Schedule();
		LocalTime beginTime = parameters.getBeginTime();
		LocalTime endTime = parameters.getEndTime();
		Collection<DayOfWeek> lessonDays = parameters.getLessonDays();
		int dailyMinutes = (int) Duration.between(beginTime, endTime).toMinutes();
		int totalMinutes = parameters.getHoursNumber() * 60;

		int rawMonthEstimation = (totalMinutes / dailyMinutes) / (lessonDays.size() * 4) + 1; // 4 weeks/month, plus 1

		Collection<LocalDate> publicHolidays = webClient.getHolidays(parameters.getStartDate(),
				parameters.getStartDate().plusMonths(rawMonthEstimation));

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

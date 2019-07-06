package pl.itacademy.schedule.generator;

import pl.itacademy.schedule.holidays.HolidaysWebClient;
import pl.itacademy.schedule.parameters.EnteredParameters;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;

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
		LocalDate startDate = findNextDate(parameters.getStartDate(), lessonDays);
		while (totalMinutes >= dailyMinutes) {
			lessons.add(new Lesson(startDate, beginTime, endTime));
			totalMinutes -= dailyMinutes;
			startDate = findNextDate(startDate.plusDays(1), lessonDays);
		}
		if (totalMinutes > 0) {
			lessons.add(new Lesson(startDate, beginTime, beginTime.plusMinutes(totalMinutes)));
			schedule.setLastDayShorter(true);
		}
		schedule.setLessons(lessons);
		schedule.setNumberOfHours(parameters.getHoursNumber());

		return schedule;
	}

	private LocalDate findNextDate(LocalDate startDate, Collection<DayOfWeek> lessonDays) {
		while (notLessonsDayOfWeek(startDate, lessonDays)) {
			startDate = startDate.plusDays(1);
		}
		return startDate;
	}

	private boolean notLessonsDayOfWeek(LocalDate date, Collection<DayOfWeek> lessonDays) {
		return !lessonDays.contains(date.getDayOfWeek());
	}
}

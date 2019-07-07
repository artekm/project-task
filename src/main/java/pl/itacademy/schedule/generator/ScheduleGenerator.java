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

//		Use below code to get holidays from calendarific
//		Collection<LocalDate> publicHolidays = webClient.getHolidays(parameters.getStartDate().getYear());
//		try {
//			Thread.sleep(1500);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		Collection<LocalDate> publicHolidays2 = webClient.getHolidays(parameters.getStartDate().plusYears(1).getYear());
//		publicHolidays.addAll(publicHolidays2);

//		Use below code to get holidays from Enrico
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

	// This is unused so far, but maybe sometime ...
	public boolean isHolidayByRule(LocalDate date) {
		int day = date.getDayOfMonth();
		int mon = date.getMonthValue();
		if ((day == 1) && (mon == 1))
			return true; // Nowy Rok
		if ((day == 6) && (mon == 1))
			return true; // 3 Królów
		if ((day == 1) && (mon == 5))
			return true; // Święto Pracy
		if ((day == 3) && (mon == 5))
			return true; // Dzień Konstytucji
		if ((day == 15) && (mon == 8))
			return true; // Wniebowzięcie NMP
		if ((day == 1) && (mon == 11))
			return true; // Wszystkich Świętych
		if ((day == 11) && (mon == 11))
			return true; // Dzień Niepodległości
		if ((day == 25) && (mon == 12))
			return true; // Boże Narodzenie 1
		if ((day == 26) && (mon == 12))
			return true; // Boże Narodzenie 2

		int a = date.getYear() % 19;
		int b = date.getYear() % 4;
		int c = date.getYear() % 7;
		int d = (a * 19 + 24) % 30;
		int e = (2 * b + 4 * c + 6 * d + 5) % 7;
		if (d == 29 && e == 6)
			d -= 7;
		if (d == 28 && e == 6 && a > 10)
			d -= 7;
		LocalDate easter = LocalDate.of(date.getYear(), 3, 22).plusDays(d + e);

		if (easter.isEqual(date))
			return true; // Wielkanoc
		if (easter.plusDays(1).isEqual(date))
			return true; // Wielkanoc poniedziałek
		if (easter.plusDays(49).isEqual(date))
			return true; // Zielone Świątki
		if (easter.plusDays(60).isEqual(date))
			return true; // Boże Ciało
		return false;
	}
}

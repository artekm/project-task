package pl.itacademy.schedule.holidays;

import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedList;

public class HolidaysByRule implements HolidaysProvider {

	@Override
	public Collection<LocalDate> getHolidays(LocalDate from, LocalDate to) {
		Collection<LocalDate> holidays = new LinkedList<>();
		LocalDate date = from;
		while (!date.isAfter(to)) {
			if (isHolidayByRule(date)) {
				holidays.add(date);
			}
			date = date.plusDays(1);
		}
		return holidays;
	}

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

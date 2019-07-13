package pl.itacademy.schedule.holidays;

import java.time.LocalDate;
import java.util.Collection;

public interface HolidaysProvider {

	Collection<LocalDate> getHolidays(LocalDate from, LocalDate to);
}

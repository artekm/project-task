package pl.itacademy.schedule.holidays;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

public class HolidaysNone implements HolidaysProvider{

	@Override
	public Collection<LocalDate> getHolidays(LocalDate from, LocalDate to) {
		return Collections.emptyList();
	}
}

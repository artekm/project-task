package pl.itacademy.schedule.holidays;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Test;

public class HolidaysNoneTest {

	@Test
	public void getHolidays_returnsEmptyList() {
		HolidaysProvider provider = new HolidaysNone();
		assertTrue(provider.getHolidays(LocalDate.now(), LocalDate.now().plusYears(1)).isEmpty());
	}
}

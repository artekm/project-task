package pl.itacademy.schedule.holidays;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Test;

public class HolidaysFromEnricoTest {

	@Test
	public void getHolidays_returnsList() {
		HolidaysProvider provider = new HolidaysFromEnrico();
		assertFalse(provider.getHolidays(LocalDate.now(), LocalDate.now().plusYears(1)).isEmpty());
	}

}

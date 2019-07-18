package pl.itacademy.schedule.parameters;

import static java.time.DayOfWeek.*;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.time.DayOfWeek;
import java.util.*;

public class DaysParserTest {

	@Test
	public void getListByAcronym_returnsListOfOneEnum_forGivenOneDay() throws Exception {
		Set<DayOfWeek> daysSet2 = EnumSet.of(SATURDAY);
		assertEquals(daysSet2, DaysParser.getDaysSetByAcronyms("sat"));
	}

	@Test
	public void getListByAcronym_returnsListOfEnums_forGivenDays() throws Exception {
		Set<DayOfWeek> daysSet1 = EnumSet.of(TUESDAY, THURSDAY);
		assertEquals(daysSet1, DaysParser.getDaysSetByAcronyms("tue_thu"));
	}

	@Test
	public void getListByAcronym_returnsListOfEnums_forAllDays() throws Exception {
		Set<DayOfWeek> daysSet = EnumSet.allOf(DayOfWeek.class);
		assertEquals(daysSet, DaysParser.getDaysSetByAcronyms("Mon_Tue_Wed_Thu_Fri_Sat_Sun"));
	}

	@Test
	public void getListByAcronym_returnsListOfEnums_forStrangeAllDays() throws Exception {
		Set<DayOfWeek> daysSet = EnumSet.allOf(DayOfWeek.class);
		assertEquals(daysSet, DaysParser.getDaysSetByAcronyms("tu_friday+satur,sun&mo_we&thu"));
	}

	@Test
	public void getListByAcronym_returnsListOfEnums_forExcessiveSeparators() throws Exception {
		Set<DayOfWeek> daysSet = EnumSet.allOf(DayOfWeek.class);
		assertEquals(daysSet, DaysParser.getDaysSetByAcronyms("__Mon__Tue_&Wed&&Thu_,Fri____Sat_Sun&&"));
	}

	@Test
	public void getListByAcronym_returnsList_forRange() {
		Set<DayOfWeek> daysSet1 = EnumSet.of(TUESDAY, WEDNESDAY, THURSDAY);
		assertEquals(daysSet1, DaysParser.getDaysSetByAcronyms("tue-thu"));
	}

	@Test
	public void getListByAcronym_returnsList_forRanges() {
		Set<DayOfWeek> daysSet1 = EnumSet.of(MONDAY, TUESDAY, THURSDAY, FRIDAY);
		assertEquals(daysSet1, DaysParser.getDaysSetByAcronyms("mon-tue&thu-fri"));
	}

	@Test
	public void getListByAcronym_returnsList_forRangesAndAcronyms() {
		Set<DayOfWeek> daysSet1 = EnumSet.of(FRIDAY, SATURDAY, SUNDAY);
		assertEquals(daysSet1, DaysParser.getDaysSetByAcronyms("fri&sat-sun"));
	}

	@Test
	public void getListByAcronym_returnsList_forRangesWithRepeatedDash() {
		Set<DayOfWeek> daysSet1 = EnumSet.of(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY);
		assertEquals(daysSet1, DaysParser.getDaysSetByAcronyms("mon--fri"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void getListByAcronym_throwsIllegalArgumentException_forNullString() throws Exception {
		DaysParser.getDaysSetByAcronyms(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getListByAcronym_throwsIllegalArgumentException_forEmptyString() throws Exception {
		DaysParser.getDaysSetByAcronyms("");
	}

	@Test(expected = IllegalArgumentException.class)
	public void getListByAcronym_throwsIllegalArgumentException_forIncorrectAcronym() throws Exception {
		DaysParser.getDaysSetByAcronyms("san_sat");
	}

	@Test(expected = IllegalArgumentException.class)
	public void getListByAcronym_throwsIllegalArgumentException_forTooShortAcronym() throws Exception {
		DaysParser.getDaysSetByAcronyms("t");
	}

	@Test(expected = IllegalArgumentException.class)
	public void getListByAcronym_throwsIllegalArgumentException_forRepeatedDay() throws Exception {
		DaysParser.getDaysSetByAcronyms("mo_tu_we_tu_fr_sa_su");
	}

	@Test(expected = IllegalArgumentException.class)
	public void getListByAcronym_throwsIllegalArgumentException_forIncorrectRange() throws Exception {
		DaysParser.getDaysSetByAcronyms("mo-tue-wed");
	}

	@Test(expected = IllegalArgumentException.class)
	public void getListByAcronym_throwsIllegalArgumentException_forIncorrectRange2() throws Exception {
		DaysParser.getDaysSetByAcronyms("mo-,sat");
	}

	@Test(expected = IllegalArgumentException.class)
	public void getListByAcronym_throwsIllegalArgumentException_forDayRepeatedInRange() throws Exception {
		DaysParser.getDaysSetByAcronyms("mo-mon");
	}

	@Test(expected = IllegalArgumentException.class)
	public void getListByAcronym_throwsIllegalArgumentException_forDayRepeatedAfter() throws Exception {
		DaysParser.getDaysSetByAcronyms("mo-fri&wed");
	}

	@Test(expected = IllegalArgumentException.class)
	public void getListByAcronym_throwsIllegalArgumentException_forDayRepeatedBefore() throws Exception {
		DaysParser.getDaysSetByAcronyms("wed&mon-fri");
	}

	@Test(expected = IllegalArgumentException.class)
	public void getListByAcronym_throwsIllegalArgumentException_forOverlapingRanges() throws Exception {
		DaysParser.getDaysSetByAcronyms("wed-fri&mon-wed");
	}

	@Test(expected = IllegalArgumentException.class)
	public void getListByAcronym_throwsIllegalArgumentException_forRangeStartAfterEnd() throws Exception {
		DaysParser.getDaysSetByAcronyms("fri-mon");
	}

	@Test(expected = IllegalArgumentException.class)
	public void getListByAcronym_throwsIllegalArgumentException_forInvalidDayInRange() throws Exception {
		DaysParser.getDaysSetByAcronyms("fri-man");
	}
}

package pl.itacademy.schedule.parameters;

import java.time.DayOfWeek;
import java.util.EnumSet;
import java.util.Set;

/**
 * <p>
 * Simple class to parse given string for any set of a DayOfWeek
 * 
 * @author Artur :-)
 */
public class DaysParser {

	private static final Set<DayOfWeek> dayEnums = EnumSet.allOf(DayOfWeek.class);

	private static Set<DayOfWeek> resultEnumSet;

	public static void main(String[] args) throws IllegalArgumentException {
		if (args.length > 0)
			System.out.println(getDaysSetByAcronyms(args[0]));
	}

	private DaysParser() {
	}

	/**
	 * <p>
	 * Parses given days' names acronyms.
	 * <p>
	 * An acronym of length of 2 or more characters, any case, and any order can be
	 * used.
	 * <p>
	 * It is allowed to use ranges, for example {@code mon-fri}.
	 * <p>
	 * Delimiters may be any set of {@code [underscore,plus,ampersand,comma]}.
	 * 
	 * @param daysLine String input, for example {@code "mon-wed&saturday-sunday" }
	 *                 or {@code "Tue_Thu"}.
	 * @return EnumSet of <a href=
	 *         "https://docs.oracle.com/javase/10/docs/api/java/time/DayOfWeek.html">DayOfWeek</a>.
	 * @throws IllegalArgumentException in the following cases:
	 *                                  <ul>
	 *                                  <li>daysLine in empty or null</li>
	 *                                  <li>daysLine contains unknown or misspelled
	 *                                  acronym</li>
	 *                                  <li>daysLine contains duplicated (repeated)
	 *                                  day</li>
	 *                                  <li>daysLine contains too short acronym</li>
	 *                                  <li>daysLine contains misspelled range</li>
	 *                                  <li>daysLine contains overlapping
	 *                                  ranges</li>
	 *                                  </ul>
	 */
	public static Set<DayOfWeek> getDaysSetByAcronyms(String daysLine) throws IllegalArgumentException {
		if (daysLine == null || daysLine.length() == 0)
			throw new IllegalArgumentException("Days line is null or empty");

		String[] daysAcronymsToCheck = daysLine.split("[_+,&]+");
		resultEnumSet = EnumSet.noneOf(DayOfWeek.class);

		for (String dayAcronym : daysAcronymsToCheck) {
			if (!dayAcronym.isEmpty()) {
				if (dayAcronym.contains("-"))
					addRangeDay(dayAcronym);
				else
					addSingleDay(dayAcronym);
			}
		}
		return resultEnumSet;
	}

	private static void addSingleDay(String dayAcronym) throws IllegalArgumentException {
		DayOfWeek dayEnumFound = findEnumForAcronym(dayAcronym);
		if (resultEnumSet.contains(dayEnumFound))
			throw new IllegalArgumentException("Repeated day: " + dayAcronym);
		resultEnumSet.add(dayEnumFound);
	}

	private static void addRangeDay(String rangeAcronym) throws IllegalArgumentException {
		String[] rangeBounds = rangeAcronym.split("-+");
		if (rangeBounds.length != 2)
			throw new IllegalArgumentException("Incorrect range: " + rangeAcronym);
		int rangeBegin = findEnumForAcronym(rangeBounds[0]).getValue();
		int rangeEnd = findEnumForAcronym(rangeBounds[1]).getValue();
		if (rangeBegin >= rangeEnd)
			throw new IllegalArgumentException("Range begin is after the end: " + rangeAcronym);
		for (int i = rangeBegin; i <= rangeEnd; i++)
			addSingleDay(DayOfWeek.of(i).name());
	}

	private static DayOfWeek findEnumForAcronym(String dayAcronym) throws IllegalArgumentException {
		if (dayAcronym.length() < 2)
			throw new IllegalArgumentException("Too short day acronym: " + dayAcronym);
		for (DayOfWeek dayEnum : dayEnums) {
			if (dayEnum.name().startsWith(dayAcronym.toUpperCase())) {
				return dayEnum;
			}
		}
		throw new IllegalArgumentException("Unknown day acronym: " + dayAcronym);
	}
}

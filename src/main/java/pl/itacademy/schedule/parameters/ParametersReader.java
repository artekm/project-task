package pl.itacademy.schedule.parameters;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import pl.itacademy.schedule.exception.IncorrectParametersException;
import pl.itacademy.schedule.util.PropertiesReader;

public class ParametersReader {

	private static final Options OPTIONS = new Options();

	static {
		OPTIONS.addOption("n", true, "Number of hours");
		OPTIONS.addOption("s", true, "Start date");
		OPTIONS.addOption("b", true, "Begin time");
		OPTIONS.addOption("e", true, "End time");
		OPTIONS.addOption("d", true, "Lesson days' names separated by underscore");
		OPTIONS.addOption("f", true, "Generated file name");
		OPTIONS.addOption("h", "Show help");
	}

	public EnteredParameters parseArguments(String[] args) throws IncorrectParametersException, ParseException {
		if (Objects.isNull(args)) {
			throw new IncorrectParametersException("Arguments are null");
		}
		if (args.length == 0) {
			throw new IncorrectParametersException("Arguments are empty");
		}

		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = parser.parse(OPTIONS, args);
		
		PropertiesReader propertiesReader = PropertiesReader.getInstance();
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(propertiesReader.readProperty("dateFormat"));
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(propertiesReader.readProperty("timeFormat"));
		
		EnteredParameters enteredParameters = new EnteredParameters();
		if (cmd.hasOption("n")) {
			Integer hours = Integer.valueOf(cmd.getOptionValue("n"));
			enteredParameters.setHoursNumber(hours);
		}
		if (cmd.hasOption("s")) {
			LocalDate startDate = LocalDate.parse(cmd.getOptionValue("s"), dateFormatter);
			enteredParameters.setStartDate(startDate);
		}
		if (cmd.hasOption("b")) {
			LocalTime beginTime = LocalTime.parse(cmd.getOptionValue("b"), timeFormatter);
			enteredParameters.setBeginTime(beginTime);
		}
		if (cmd.hasOption("e")) {
			LocalTime endTime = LocalTime.parse(cmd.getOptionValue("e"), timeFormatter);
			enteredParameters.setEndTime(endTime);
		}

		if (cmd.hasOption("f")) {
			enteredParameters.setFileName(cmd.getOptionValue("f"));
		}

		if (cmd.hasOption("d")) {
			String[] days = cmd.getOptionValue("d").toUpperCase().split("_");

			Collection<DayOfWeek> daysOfWeek = Arrays.stream(days)
					.map(DayOfWeek::valueOf)
					.collect(Collectors.toList());

			enteredParameters.setLessonDays(daysOfWeek);
		}

		enteredParameters.setShowHelp(cmd.hasOption("h"));

		return enteredParameters;
	}

	public static class UsagePrinter {
		public void printHelp() {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("java -jar Scheduler.jar -d <daysOfWeek> -b <beginTime> -e <endTime> -s <startDate> -n <numberOfHours> [-f <fileName>] [-h]", OPTIONS);
		}
	}
}

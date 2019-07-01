package pl.itacademy.parameters;

import org.apache.commons.cli.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;

public class InputParametersReader {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.uuuu");

    public InputParameters readParameters(String[] args) throws ParseException {
        InputParameters inputParameters = new InputParameters();
        Options options = new Options();
        options.addOption("d", true, "Lessons days of week");
        options.addOption("n", true, "Number Hours");
        options.addOption("b", true, "Begin Time");
        options.addOption("e", true, "End Time");
        options.addOption("s", true, "Start Date");
        options.addOption("f", true, "File Name");
        options.addOption("h", "Show Help");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        if (cmd.hasOption("d")) {
            Collection<DayOfWeek> daysOfWeek = new ArrayList<>();
            String[] inputDays = cmd.getOptionValue("d").split("_");
            for (String day : inputDays) {
                daysOfWeek.add(DayOfWeek.valueOf(day.toUpperCase()));
            }
            inputParameters.setLessonDays(daysOfWeek);
        }
        if (cmd.hasOption("n")) {
            String numbersOfHours = cmd.getOptionValue("n");
            inputParameters.setHoursNumber(Integer.valueOf(numbersOfHours));
        }
        if (cmd.hasOption("b")) {
            String beginTime = cmd.getOptionValue("b");
            inputParameters.setBeginTime(LocalTime.parse(beginTime));
        }
        if (cmd.hasOption("e")) {
            String endTime = cmd.getOptionValue("e");
            inputParameters.setEndTime(LocalTime.parse(endTime));
        }
        if (cmd.hasOption("s")) {
            String startDate = cmd.getOptionValue("s");
            inputParameters.setStartDate(LocalDate.parse(startDate, DATE_FORMAT));
        }
        if (cmd.hasOption("f")) {
            String fileName = cmd.getOptionValue("f");
            inputParameters.setFileName(fileName);
        }
        inputParameters.setShowHelp(cmd.hasOption("h"));

        return inputParameters;
    }
}

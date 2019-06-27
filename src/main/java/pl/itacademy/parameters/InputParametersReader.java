package pl.itacademy.parameters;

import org.apache.commons.cli.*;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collection;

public class InputParametersReader {

    public InputParameters readParameters(String[] args) throws ParseException {
        InputParameters inputParameters = new InputParameters();
        Options options = new Options();
        options.addOption("d", true, "Lessons days of week");
        options.addOption("n", true, "Number Hours");
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

        return inputParameters;
    }
}

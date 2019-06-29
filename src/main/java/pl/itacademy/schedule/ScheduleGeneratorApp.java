package pl.itacademy.schedule;

import org.apache.commons.cli.ParseException;
import pl.itacademy.schedule.exception.IncorrectParametersException;
import pl.itacademy.schedule.parameters.EnteredParameters;
import pl.itacademy.schedule.parameters.ParametersReader;

public class ScheduleGeneratorApp {

    public static void main(String[] args) {

        ParametersReader.UsagePrinter usagePrinter = new ParametersReader.UsagePrinter();
        ParametersReader parametersReader = new ParametersReader();
        EnteredParameters enteredParameters = null;
        try {
            enteredParameters = parametersReader.parseArguments(args);
            if (enteredParameters.isShowHelp()) {
                usagePrinter.printHelp();
                return;
            }
        } catch (IncorrectParametersException | ParseException  e) {
            System.out.println(e.getMessage());
            usagePrinter.printHelp();
        } catch (NumberFormatException e) {
            System.out.println("Impossible to read number " + e.getMessage());
        }

    }
}

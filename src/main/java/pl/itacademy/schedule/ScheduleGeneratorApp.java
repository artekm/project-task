package pl.itacademy.schedule;

import org.apache.commons.cli.ParseException;
import org.apache.poi.ss.usermodel.Workbook;
import pl.itacademy.schedule.exception.IncorrectParametersException;
import pl.itacademy.schedule.generator.ExcelCreator;
import pl.itacademy.schedule.generator.Schedule;
import pl.itacademy.schedule.generator.ScheduleGenerator;
import pl.itacademy.schedule.holidays.HolidaysWebClient;
import pl.itacademy.schedule.parameters.EnteredParameters;
import pl.itacademy.schedule.parameters.ParametersReader;
import pl.itacademy.schedule.parameters.ParametersValidator;
import pl.itacademy.schedule.util.PropertiesReader;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static java.util.Objects.isNull;

public class ScheduleGeneratorApp {

    public static void main(String[] args) {

        ParametersReader.UsagePrinter usagePrinter = new ParametersReader.UsagePrinter();
        ParametersReader parametersReader = new ParametersReader();
        EnteredParameters enteredParameters;
        try {
            enteredParameters = parametersReader.parseArguments(args);
            if (enteredParameters.isShowHelp()) {
                usagePrinter.printHelp();
                return;
            }
            ParametersValidator validator = new ParametersValidator();
            validator.validate(enteredParameters);
        } catch (IncorrectParametersException | ParseException e) {
            System.out.println("Wrong parameters! " + e.getMessage());
            usagePrinter.printHelp();
            return;
        } catch (NumberFormatException e) {
            System.out.println("Impossible to read number " + e.getMessage());
            return;
        }

        HolidaysWebClient webClient = new HolidaysWebClient();
        ScheduleGenerator scheduleGenerator = new ScheduleGenerator(webClient);
        Schedule schedule = scheduleGenerator.generate(enteredParameters);

        String fileName = enteredParameters.getFileName();
        if (isNull(fileName)) {
            fileName = PropertiesReader.getInstance().readProperty("excel.defaultName");
        } else if (!fileName.endsWith(".xlsx")) {
            fileName = fileName + ".xlsx";
        }

        ExcelCreator excelCreator = new ExcelCreator();
        try (Workbook workbook = excelCreator.createWorkbook(schedule);
             OutputStream outputStream = new FileOutputStream(fileName)) {
            workbook.write(outputStream);
        } catch (IOException e) {
            System.out.println("Impossible to write workbook as a file: " + fileName);
        }
    }

}

package pl.itacademy.schedule;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeParseException;

import org.apache.commons.cli.ParseException;
import org.apache.poi.ss.usermodel.Workbook;

import pl.itacademy.schedule.exception.IncorrectParametersException;
import pl.itacademy.schedule.generator.ExcelCreator;
import pl.itacademy.schedule.generator.Schedule;
import pl.itacademy.schedule.generator.ScheduleGenerator;
import pl.itacademy.schedule.holidays.HolidaysProvider;
import pl.itacademy.schedule.holidays.HolidaysProviderFactory;
import pl.itacademy.schedule.parameters.EnteredParameters;
import pl.itacademy.schedule.parameters.ParametersReader;
import pl.itacademy.schedule.parameters.ParametersValidator;
import pl.itacademy.schedule.util.PropertiesReader;

public class ScheduleGeneratorApp {
	public static final String WARNING_MESSAGE = "Warning: last lesson is shorter than the others\n";

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
		} catch (IncorrectParametersException | ParseException | DateTimeParseException e) {
			System.out.println(e.getMessage());
			usagePrinter.printHelp();
			return;
		} catch (IllegalArgumentException e) {
			System.out.println("Impossible to read number " + e.getMessage());
			usagePrinter.printHelp();
			return;
		}
		HolidaysProvider webClient = HolidaysProviderFactory.getProvider();
		
		ScheduleGenerator scheduleGenerator = new ScheduleGenerator(webClient);
		Schedule schedule = scheduleGenerator.generate(enteredParameters);

		String fileName;
		if (enteredParameters.getFileName() == null) {
			PropertiesReader propertiesReader = PropertiesReader.getInstance();
			fileName = propertiesReader.readProperty("excel.defaultName");
		} else {
			fileName = enteredParameters.getFileName();
		}
		if (!fileName.endsWith(".xlsx")) {
			fileName = fileName + ".xlsx";
		}

		ExcelCreator excelCreator = new ExcelCreator();

		Workbook workbook = excelCreator.createWorkbook(schedule);

		try {
			OutputStream stream = new FileOutputStream(fileName);
			workbook.write(stream);
			workbook.close();
			System.out.println("Successfully saved the schedule to file " + fileName);
		} catch (IOException e) {
			System.out.println("Impossible to write schedule workbook.");
			return;
		}
		try {
			if (schedule.isLastDayShorter()) {
				System.out.println(WARNING_MESSAGE);
				String shortName = fileName.substring(0, fileName.lastIndexOf("."));
				Files.write(Paths.get(shortName + "-warning.txt"), WARNING_MESSAGE.getBytes());
			}
		} catch (IOException ignore) {
		}
	}
}

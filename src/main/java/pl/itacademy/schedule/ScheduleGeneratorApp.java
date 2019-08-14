package pl.itacademy.schedule;

import org.apache.commons.cli.ParseException;
import org.apache.poi.ss.usermodel.Workbook;
import pl.itacademy.schedule.exception.IncorrectParametersException;
import pl.itacademy.schedule.generator.ExcelCreator;
import pl.itacademy.schedule.generator.Schedule;
import pl.itacademy.schedule.generator.ScheduleGenerator;
import pl.itacademy.schedule.gui.GUI;
import pl.itacademy.schedule.holidays.HolidaysProvider;
import pl.itacademy.schedule.holidays.HolidaysProviderFactory;
import pl.itacademy.schedule.parameters.EnteredParameters;
import pl.itacademy.schedule.parameters.ParametersReader;
import pl.itacademy.schedule.parameters.ParametersValidator;
import pl.itacademy.schedule.util.PropertiesReader;

import java.awt.EventQueue;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeParseException;

public class ScheduleGeneratorApp {
	public static final String WARNING_MESSAGE = "Warning: last lesson is shorter than the others\n";

	public static void main(String[] args) {

		if (args.length==0) {
			EventQueue.invokeLater(() -> new GUI().runGUI());
			return;
		}
		ParametersReader parametersReader = new ParametersReader();
		ParametersReader.UsagePrinter usagePrinter = parametersReader.new UsagePrinter();
		EnteredParameters enteredParameters;
		try {
			enteredParameters = parametersReader.parseArguments(args);
			if (enteredParameters.isShowHelp()) {
				usagePrinter.printHelp();
				return;
			}
			ParametersValidator validator = new ParametersValidator();
			validator.validate(enteredParameters);
		} catch (NumberFormatException e) {
			System.out.println("Impossible to read number " + e.getMessage());
			usagePrinter.printHelp();
			return;
		} catch (IncorrectParametersException | ParseException | DateTimeParseException | IllegalArgumentException e) {
			System.out.println(e.getMessage());
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



		try(OutputStream stream = new FileOutputStream(fileName);
			Workbook workbook = excelCreator.createWorkbook(schedule)) {
			workbook.write(stream);
			System.out.println("Successfully saved the schedule to file " + fileName);
		} catch (IOException e) {
			System.out.println("Impossible to write schedule workbook.");
			return;
		}

		if(schedule.isLastDayShorter()){
			System.out.println(WARNING_MESSAGE);
			try {
				String shortName = fileName.substring(0, fileName.lastIndexOf("."));
				Files.write(Paths.get(shortName + "-warning.txt"), WARNING_MESSAGE.getBytes());
			} catch (IOException ignored) {}
		}
	}
}

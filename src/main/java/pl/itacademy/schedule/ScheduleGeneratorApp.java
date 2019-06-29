package pl.itacademy.schedule;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.format.DateTimeFormatter;

import org.apache.commons.cli.ParseException;
import org.apache.poi.ss.usermodel.Workbook;

import pl.itacademy.schedule.exception.IncorrectParametersException;
import pl.itacademy.schedule.generator.ExcelCreator;
import pl.itacademy.schedule.generator.Schedule;
import pl.itacademy.schedule.generator.ScheduleGenerator;
import pl.itacademy.schedule.parameters.EnteredParameters;
import pl.itacademy.schedule.parameters.ParametersReader;
import pl.itacademy.schedule.util.PropertiesReader;

public class ScheduleGeneratorApp {

	public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("d.MM.uuuu");
	public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("H:mm");

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
			ScheduleGenerator scheduleGenerator = new ScheduleGenerator();
			Schedule schedule = scheduleGenerator.generate(enteredParameters);

			String fileName;
			if (enteredParameters.getFileName() == null) {
				PropertiesReader propertiesReader = PropertiesReader.getInstance();
				fileName = propertiesReader.readProperty("excel.defaultName") + "."
							+ propertiesReader.readProperty("excel.defaultExtension");
			} else {
				fileName = enteredParameters.getFileName();
			}

			ExcelCreator excelCreator = new ExcelCreator();
			
			Workbook workbook;
			if (fileName.toLowerCase().endsWith("xlsx")) {
				workbook = excelCreator.createXSSFWorkbook(schedule);
			} else {
				workbook = excelCreator.createHSSFWorkbook(schedule);
			}	
			
			//temporary solution start
			OutputStream stream = new FileOutputStream(fileName);
			workbook.write(stream);
			workbook.close();
			//temporary solution stop
			
		} catch (IncorrectParametersException | ParseException | IOException e) {
			System.out.println(e.getMessage());
			usagePrinter.printHelp();
		} catch (NumberFormatException e) {
			System.out.println("Impossible to read number " + e.getMessage());
		}
	}

}

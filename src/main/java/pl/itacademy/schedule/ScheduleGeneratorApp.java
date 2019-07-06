package pl.itacademy.schedule;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.cli.ParseException;
import org.apache.poi.ss.usermodel.Workbook;

import pl.itacademy.schedule.exception.IncorrectParametersException;
import pl.itacademy.schedule.generator.ExcelCreator;
import pl.itacademy.schedule.generator.Schedule;
import pl.itacademy.schedule.generator.ScheduleGenerator;
import pl.itacademy.schedule.parameters.EnteredParameters;
import pl.itacademy.schedule.parameters.ParametersReader;
import pl.itacademy.schedule.parameters.ParametersValidator;
import pl.itacademy.schedule.util.PropertiesReader;

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
			ParametersValidator validator = new ParametersValidator();
			validator.validate(enteredParameters);
			
			ScheduleGenerator scheduleGenerator = new ScheduleGenerator();
			Schedule schedule = scheduleGenerator.generate(enteredParameters);

			String fileName;
			if (enteredParameters.getFileName() == null) {
				PropertiesReader propertiesReader = PropertiesReader.getInstance();
				fileName = propertiesReader.readProperty("excel.defaultName") + ".xlsx";
			} else {
				fileName = enteredParameters.getFileName();
				if (!fileName.endsWith(".xlsx"))
					fileName = fileName + ".xlsx";
			}

			ExcelCreator excelCreator = new ExcelCreator();

			Workbook workbook = excelCreator.createWorkbook(schedule);

			// temporary solution start
			OutputStream stream = new FileOutputStream(fileName);
			workbook.write(stream);
			workbook.close();
			// temporary solution stop

		} catch (IncorrectParametersException | ParseException | IOException e) {
			System.out.println(e.getMessage());
			usagePrinter.printHelp();
		} catch (NumberFormatException e) {
			System.out.println("Impossible to read number " + e.getMessage());
		}
	}

}

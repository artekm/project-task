package pl.itacademy.schedule;

import java.awt.EventQueue;
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
import pl.itacademy.schedule.gui.GUI;
import pl.itacademy.schedule.holidays.HolidaysProvider;
import pl.itacademy.schedule.holidays.HolidaysProviderFactory;
import pl.itacademy.schedule.parameters.EnteredParameters;
import pl.itacademy.schedule.parameters.ParametersReader;
import pl.itacademy.schedule.parameters.ParametersValidator;
import pl.itacademy.schedule.util.PropertiesReader;

public class ScheduleGeneratorApp {
	public static final String WARNING_MESSAGE = "Warning: last lesson is shorter than the others\n";

	private EnteredParameters parameters;
	private Schedule schedule;
	private Workbook workbook;

	public EnteredParameters getParameters() {
		return parameters;
	}

	public void setParameters(EnteredParameters parameters) {
		this.parameters = parameters;
	}

	public Schedule getSchedule() {
		return schedule;
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}

	public void run(String[] args) {

		if (args.length == 0) {
			EventQueue.invokeLater(() -> new GUI(this).runGUI());
			return;
		}

		if (readParameters(args)) {
			generateSchedule(true);
			generateExcel();
			saveExcelToFile(null,true);
		}
	}

	public static void main(String[] args) {
		ScheduleGeneratorApp app = new ScheduleGeneratorApp();
		app.run(args);
	}

	public boolean readParameters(String[] args) {
		ParametersReader parametersReader = new ParametersReader();
		ParametersReader.UsagePrinter usagePrinter = parametersReader.new UsagePrinter();
		try {
			parameters = parametersReader.parseArguments(args);
			if (parameters.isShowHelp()) {
				usagePrinter.printHelp();
				return false;
			}
			ParametersValidator validator = new ParametersValidator();
			validator.validate(parameters);
		} catch (NumberFormatException e) {
			System.out.println("Impossible to read number " + e.getMessage());
			usagePrinter.printHelp();
			return false;
		} catch (IncorrectParametersException | ParseException | DateTimeParseException | IllegalArgumentException e) {
			System.out.println(e.getMessage());
			usagePrinter.printHelp();
			return false;
		}
		return true;
	}

	public boolean generateSchedule(boolean verbose) {
		HolidaysProvider webClient = HolidaysProviderFactory.getProvider(verbose);

		ScheduleGenerator scheduleGenerator = new ScheduleGenerator(webClient);
		schedule = scheduleGenerator.generate(parameters);
		return true;
	}

	public boolean generateExcel() {
		ExcelCreator excelCreator = new ExcelCreator();
		workbook = excelCreator.createWorkbook(schedule);
		return true;
	}

	public boolean saveExcelToFile(String file,boolean verbose) {
		String fileName;
		if (file != null) {
			fileName = file;
		} else {
			if (parameters.getFileName() == null) {
				PropertiesReader propertiesReader = PropertiesReader.getInstance();
				fileName = propertiesReader.readProperty("excel.defaultName");
			} else {
				fileName = parameters.getFileName();
			}
			if (!fileName.endsWith(".xlsx")) {
				fileName = fileName + ".xlsx";
			}
		}
		try (OutputStream stream = new FileOutputStream(fileName)) {
			workbook.write(stream);
			if (verbose)
				System.out.println("Successfully saved the schedule to file " + fileName);
		} catch (IOException e) {
			if (verbose)
				System.out.println("Impossible to write schedule workbook.");
			return false;
		}

		if (schedule.isLastDayShorter()) {
			if (verbose)
				System.out.println(WARNING_MESSAGE);
			try {
				String shortName = fileName.substring(0, fileName.lastIndexOf("."));
				Files.write(Paths.get(shortName + "-warning.txt"), WARNING_MESSAGE.getBytes());
			} catch (IOException ignored) {
			}
		}
		return true;
	}

}

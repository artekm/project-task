package pl.itacademy;

import org.apache.commons.cli.ParseException;
import org.apache.poi.ss.usermodel.Workbook;
import pl.itacademy.excel.ExcelGenerator;
import pl.itacademy.exceptions.InvalidParameterException;
import pl.itacademy.generator.Schedule;
import pl.itacademy.generator.ScheduleGenerator;
import pl.itacademy.parameters.InputParameters;
import pl.itacademy.parameters.InputParametersReader;
import pl.itacademy.validation.ParametersValidator;
import pl.itacademy.web.HolidaysWebClient;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class ScheduleGeneratorApp {

    public static void main(String[] args) throws IOException {
        InputParametersReader inputParametersReader = new InputParametersReader();
        InputParameters inputParameters;
        try {
            inputParameters = inputParametersReader.readParameters(args);
        } catch (ParseException e) {
            System.out.println("You entered wrong parameters!\nWrite [-h] to show help.");
            return;
        } catch (IllegalArgumentException e) {
            System.out.println("You entered wrong day name!\nWrite [-h] to show help.");
            return;
        }

        if (inputParameters.isShowHelp()) {
            inputParametersReader.showHelp();
            return;
        }

        ParametersValidator parametersValidator = new ParametersValidator();
        try {
            parametersValidator.validate(inputParameters);
        } catch (InvalidParameterException e) {
            System.out.println("Error:\n" + e.getMessage());
            return;
        }

        HolidaysWebClient holidaysWebClient = new HolidaysWebClient();
        ScheduleGenerator scheduleGenerator = new ScheduleGenerator(holidaysWebClient);
        Schedule schedule = scheduleGenerator.generate(inputParameters);

        ExcelGenerator excelGenerator = new ExcelGenerator();
        Workbook workbook = excelGenerator.createScheduleWorkbook(schedule);

        OutputStream outputStream = Files.newOutputStream(Paths.get("C:\\1\\" + inputParameters.getFileName() + ".xlsx"));
        workbook.write(outputStream);
        outputStream.close();
        workbook.close();

        if (!schedule.isLessonsFitToSchedule()) {
            Path path = Paths.get("C:\\1\\warning.txt");
            List<String> warning = Arrays.asList("WARNING!", "Lessons hours doesn't fit lesson begin time and end time.", "Last lesson duration was reduced.");
            Files.write(path, warning, StandardCharsets.UTF_8);
            System.out.println("Warning file was created at -> " + path.toString());
        }

        System.out.println("Creating a file was successful.");
    }
}

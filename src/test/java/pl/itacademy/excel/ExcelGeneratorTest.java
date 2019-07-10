package pl.itacademy.excel;

import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Ignore;
import org.junit.Test;
import pl.itacademy.generator.Lesson;
import pl.itacademy.generator.Schedule;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

public class ExcelGeneratorTest {

    @Test
    @Ignore
    public void createExcelFile() throws IOException {
        ExcelGenerator generator = new ExcelGenerator();

        Lesson firstLesson = new Lesson(LocalDate.of(2019, 7, 8),
                LocalTime.of(17, 30), LocalTime.of(18, 0));
        Lesson secondLesson = new Lesson(LocalDate.of(2019, 8, 8),
                LocalTime.of(18, 30), LocalTime.of(19, 0));
        List<Lesson> lessons = Arrays.asList(firstLesson, secondLesson);

        Schedule schedule = new Schedule(lessons, true);

        Workbook workbook = generator.createScheduleWorkbook(schedule);

        OutputStream outputStream = Files.newOutputStream(Paths.get("C:\\1\\file.xlsx"));
        workbook.write(outputStream);
        outputStream.close();
        workbook.close();
    }

}
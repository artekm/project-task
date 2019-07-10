package pl.itacademy.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import pl.itacademy.generator.Lesson;
import pl.itacademy.generator.Schedule;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;

public class ExcelGenerator {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public Workbook createScheduleWorkbook(Schedule schedule) {
        Workbook workbook = new XSSFWorkbook();

        short dateFormat = workbook.createDataFormat().getFormat("dd.MM.yyyy");
        CellStyle dateStyle = workbook.createCellStyle();
        dateStyle.setDataFormat(dateFormat);
        short timeFormat = workbook.createDataFormat().getFormat("[h]:mm");
        CellStyle timeStyle = workbook.createCellStyle();
        timeStyle.setDataFormat(timeFormat);

        Sheet sheet = workbook.createSheet();

        Collection<Lesson> lessons = schedule.getLessons();
        int currentRowNumber = 0;
        for (Lesson lesson : lessons) {
            Row row = sheet.createRow(currentRowNumber);

            Cell dateCell = row.createCell(0);
            Date date = Date.from(lesson.getDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
            dateCell.setCellValue(date);
            dateCell.setCellStyle(dateStyle);

            Cell beginTime = row.createCell(3);
            String time = lesson.getBeginTime().format(FORMATTER);
            beginTime.setCellValue(DateUtil.convertTime(time));
            beginTime.setCellStyle(timeStyle);

            Cell endTime = row.createCell(4);
            time = lesson.getEndTime().format(FORMATTER);
            endTime.setCellValue(DateUtil.convertTime(time));
            endTime.setCellStyle(timeStyle);

            Cell formulaCell = row.createCell(5);
            formulaCell.setCellFormula(createFormula(currentRowNumber));

            currentRowNumber++;
        }

        Row hoursDoneRow = sheet.getRow(0);
        if (hoursDoneRow == null) {
            hoursDoneRow = sheet.createRow(0);
        }
        Cell hoursDoneTextCell = hoursDoneRow.createCell(7);
        hoursDoneTextCell.setCellValue("hours done");
        Cell hoursDoneValueCell = hoursDoneRow.createCell(8);
        hoursDoneValueCell.setCellFormula("SUM(F1:F57)");

        return workbook;
    }

    private String createFormula(int lineNumber) {
        lineNumber++;
        return "IF(B" + lineNumber +
                "=\"done\",HOUR(E" + lineNumber +
                "-D" + lineNumber +
                ") + minute(E" + lineNumber +
                "-D" + lineNumber +
                ")/60,\"\")";
    }

}

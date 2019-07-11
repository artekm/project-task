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

        Font arialFont = workbook.createFont();
        arialFont.setFontName("Arial");

        CellStyle arialStyle = workbook.createCellStyle();
        arialStyle.setFont(arialFont);

        short dateFormat = workbook.createDataFormat().getFormat("dd.MM.yyyy");
        CellStyle dateStyle = workbook.createCellStyle();
        dateStyle.setDataFormat(dateFormat);
        dateStyle.setFont(arialFont);

        short timeFormat = workbook.createDataFormat().getFormat("[h]:mm");
        CellStyle timeStyle = workbook.createCellStyle();
        timeStyle.setDataFormat(timeFormat);
        timeStyle.setFont(arialFont);

        CellStyle statusStyle = workbook.createCellStyle();
        Font statusFont = workbook.createFont();
        statusFont.setBold(true);
        statusFont.setFontName("Arial");
        statusStyle.setFont(statusFont);

        Sheet sheet = workbook.createSheet();

        Collection<Lesson> lessons = schedule.getLessons();
        int currentRowNumber = 0;
        for (Lesson lesson : lessons) {
            Row row = sheet.createRow(currentRowNumber);

            Cell dateCell = row.createCell(0);
            Date date = Date.from(lesson.getDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
            dateCell.setCellValue(date);
            dateCell.setCellStyle(dateStyle);

            Cell doneCell = row.createCell(1);
            doneCell.setCellStyle(arialStyle);

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
            formulaCell.setCellStyle(arialStyle);

            currentRowNumber++;
        }

        Row hoursDoneRow = sheet.getRow(0);
        if (hoursDoneRow == null) {
            hoursDoneRow = sheet.createRow(0);
        }

        Cell hoursDoneTextCell = hoursDoneRow.createCell(7);
        hoursDoneTextCell.setCellValue("hours done");
        hoursDoneTextCell.setCellStyle(arialStyle);
        Cell hoursDoneValueCell = hoursDoneRow.createCell(8);
        hoursDoneValueCell.setCellFormula("SUM(F1:F57)");
        hoursDoneValueCell.setCellStyle(arialStyle);


        Row hoursPlannedRow;
        if ((hoursPlannedRow = sheet.getRow(1)) == null) {
            hoursPlannedRow = sheet.createRow(1);
        }

        Cell hoursPlannedTextCell = hoursPlannedRow.createCell(7);
        hoursPlannedTextCell.setCellValue("hours planned");
        hoursPlannedTextCell.setCellStyle(arialStyle);
        Cell hoursPlannedValueCell = hoursPlannedRow.createCell(8);
        hoursPlannedValueCell.setCellValue(schedule.getHoursPlanned());
        hoursPlannedValueCell.setCellStyle(arialStyle);


        Row lessonsDoneRow;
        if ((lessonsDoneRow = sheet.getRow(3)) == null) {
            lessonsDoneRow = sheet.createRow(3);
        }

        Cell lessonsDoneTextCell = lessonsDoneRow.createCell(7);
        lessonsDoneTextCell.setCellValue("lessons done");
        lessonsDoneTextCell.setCellStyle(arialStyle);
        Cell lessonsDoneValueCell = lessonsDoneRow.createCell(8);
        lessonsDoneValueCell.setCellFormula("COUNTIF(B1:B57,\"done\")");
        lessonsDoneValueCell.setCellStyle(arialStyle);


        Row lessonsPlannedRow;
        if ((lessonsPlannedRow = sheet.getRow(4)) == null) {
            lessonsPlannedRow = sheet.createRow(4);
        }

        Cell lessonsPlannedTextCell = lessonsPlannedRow.createCell(7);
        lessonsPlannedTextCell.setCellValue("lessons planned");
        lessonsPlannedTextCell.setCellStyle(arialStyle);
        Cell lessonsPlannedValueCell1 = lessonsPlannedRow.createCell(8);
        lessonsPlannedValueCell1.setCellValue(lessons.size());
        lessonsPlannedValueCell1.setCellStyle(arialStyle);


        Row statusRow = lessons.size() < 5 ? sheet.createRow(5) : sheet.createRow(lessons.size());

        Cell statusTextCell = statusRow.createCell(7);
        statusTextCell.setCellValue("STATUS:");
        statusTextCell.setCellStyle(statusStyle);
        Cell statusValueCell = statusRow.createCell(8);
        statusValueCell.setCellFormula("IF(I1=I2,\"COMPLETED\",\"IN PROGRESS\")");
        statusValueCell.setCellStyle(statusStyle);

        for (int col=0; col < 8; col++) {
            sheet.autoSizeColumn(col);
        }

        sheet.setColumnWidth(5, 1000);
        sheet.setColumnWidth(8, 3700);

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

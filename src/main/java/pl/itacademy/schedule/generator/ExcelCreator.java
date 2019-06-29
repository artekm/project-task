package pl.itacademy.schedule.generator;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import static pl.itacademy.schedule.ScheduleGeneratorApp.DATE_FORMATTER;
import static pl.itacademy.schedule.ScheduleGeneratorApp.TIME_FORMATTER;

public class ExcelCreator {

	private Workbook workbook;
	private CellStyle cellStyleRight;
	private CellStyle cellStyleLeft;
	private CellStyle cellStyleCenter;

	public Workbook createHSSFWorkbook(Schedule schedule) {
		workbook = new HSSFWorkbook();
		fillWorkbook(schedule);
		return workbook;
	}

	public Workbook createXSSFWorkbook(Schedule schedule) {
		workbook = new XSSFWorkbook();
		fillWorkbook(schedule);
		return workbook;
	}

	public void fillWorkbook(Schedule schedule) {

		createCellStyles();
		Sheet sheet = workbook.createSheet("Schedule");

		int rowNum = 0;
		for (Lesson day : schedule.getLessons()) {

			fillCell(sheet, rowNum, 0, day.getDate().format(DATE_FORMATTER), cellStyleRight);
			fillCell(sheet, rowNum, 3, day.getBeginTime().format(TIME_FORMATTER), cellStyleRight);
			fillCell(sheet, rowNum, 4, day.getEndTime().format(TIME_FORMATTER), cellStyleRight);

			rowNum++;
		}

		fillCell(sheet, 0, 7, "hours done", cellStyleRight);
		fillCell(sheet, 0, 8, 0, cellStyleLeft);

		fillCell(sheet, 1, 7, "hours planned", cellStyleRight);
		fillCell(sheet, 1, 8, schedule.getNumberOfHours(), cellStyleLeft);

		fillCell(sheet, 3, 7, "lessons done", cellStyleRight);
		fillCell(sheet, 3, 8, 0, cellStyleLeft);

		fillCell(sheet, 4, 7, "lessons planned", cellStyleRight);
		fillCell(sheet, 4, 8, schedule.getNumberOfDays(), cellStyleLeft);

		sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(3);
		sheet.autoSizeColumn(4);
		sheet.autoSizeColumn(7);
		sheet.autoSizeColumn(8);
	}

	private void fillCell(Sheet sheet, int row, int column, String value, CellStyle style) {
		Cell cell = selectCell(sheet, row, column);
		cell.setCellValue(value);
		cell.setCellStyle(style);
	}

	private void fillCell(Sheet sheet, int row, int column, int value, CellStyle style) {
		Cell cell = selectCell(sheet, row, column);
		cell.setCellValue(value);
		cell.setCellStyle(style);
	}

	private Cell selectCell(Sheet sheet, int rowNumber, int columnNumber) {
		Row row = createRow(sheet, rowNumber);
		Cell cell = createCell(row, columnNumber);
		return cell;
	}

	private Row createRow(Sheet sheet, int rowNumber) {
		Row row = sheet.getRow(rowNumber);
		if (row == null)
			row = sheet.createRow(rowNumber);
		return row;
	}

	private Cell createCell(Row row, int columnNumber) {
		Cell cell = row.getCell(columnNumber);
		if (cell == null)
			cell = row.createCell(columnNumber);
		return cell;
	}

	private void createCellStyles() {
		cellStyleRight = workbook.createCellStyle();
		cellStyleRight.setAlignment(HorizontalAlignment.RIGHT);
		cellStyleLeft = workbook.createCellStyle();
		cellStyleLeft.setAlignment(HorizontalAlignment.LEFT);
		cellStyleCenter = workbook.createCellStyle();
		cellStyleCenter.setAlignment(HorizontalAlignment.CENTER);
	}
}

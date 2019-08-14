package pl.itacademy.schedule.gui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import org.apache.poi.ss.usermodel.Workbook;

import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;
import pl.itacademy.schedule.generator.ExcelCreator;
import pl.itacademy.schedule.generator.Lesson;
import pl.itacademy.schedule.generator.Schedule;
import pl.itacademy.schedule.generator.ScheduleGenerator;
import pl.itacademy.schedule.holidays.HolidaysProvider;
import pl.itacademy.schedule.holidays.HolidaysProviderFactory;
import pl.itacademy.schedule.parameters.EnteredParameters;
import pl.itacademy.schedule.util.PropertiesReader;

public class GUI {

	private SimpleDateFormat dateFormat;
	private DateTimeFormatter timeFormatter;
	private DateTimeFormatter dateFormatter;

	private static final String[] HOURS_IN = { "06:00", "06:30", "07:00", "07:30", "08:00", "08:30", "09:00",
			"09:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30",
			"15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00",
			"20:30", "21:00", "21:30", "22:00" };
	
	private static final String[] COLUMNS = { "Lesson", "Date", "Day of week", "Begin time", "End time" };
	private static final String WARNING_MESSAGE = "Warning: last lesson is shorter than the others\n";

	private String[] hours;
	private Schedule schedule;

	public GUI() {
		PropertiesReader props = PropertiesReader.getInstance();
		dateFormat = new SimpleDateFormat(props.readProperty("dateFormat"));
		dateFormatter = DateTimeFormatter.ofPattern(props.readProperty("dateFormat"));
		timeFormatter = DateTimeFormatter.ofPattern(props.readProperty("timeFormat"));
		hours = Arrays.stream(HOURS_IN).map(s -> LocalTime.parse(s).format(timeFormatter)).collect(Collectors.toList())
				.toArray(new String[0]);
	}

	public void runGUI() {
		Locale.setDefault(Locale.ENGLISH);
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);

		JFrame frame = new JFrame();
		frame.setTitle("Scheduler");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationByPlatform(true);
		frame.setResizable(true);
		frame.setLayout(new GridBagLayout());
		frame.setPreferredSize(new Dimension(900, 500));

		JCheckBox cbMon = new JCheckBox("Monday");
		JCheckBox cbTue = new JCheckBox("Tuesday");
		JCheckBox cbWed = new JCheckBox("Wednesday");
		JCheckBox cbThu = new JCheckBox("Thursday");
		JCheckBox cbFri = new JCheckBox("Friday");
		JCheckBox cbSat = new JCheckBox("Saturday");
		JCheckBox cbSun = new JCheckBox("Sunday");

		JPanel panelBox = new JPanel(new GridLayout(7, 1));
		panelBox.add(cbMon);
		panelBox.add(cbTue);
		panelBox.add(cbWed);
		panelBox.add(cbThu);
		panelBox.add(cbFri);
		panelBox.add(cbSat);
		panelBox.add(cbSun);
		panelBox.setBorder(BorderFactory.createTitledBorder("Lesson days"));

		JButton btnGenerate = new JButton("Generate schedule");
		btnGenerate.setMnemonic(KeyEvent.VK_G);
		btnGenerate.setEnabled(false);

		JButton btnClear = new JButton("Clear data");
		btnClear.setMnemonic(KeyEvent.VK_C);

		JButton btnSave = new JButton("Save as Excel");
		btnSave.setMnemonic(KeyEvent.VK_S);
		btnSave.setEnabled(false);

		JButton btnEnd = new JButton("End");
		btnEnd.setMnemonic(KeyEvent.VK_E);

		UtilDateModel model = new UtilDateModel();
		JDatePanelImpl datePanel = new JDatePanelImpl(model);
		JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new AbstractFormatter() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object stringToValue(String text) throws ParseException {
				return dateFormat.parseObject(text);
			}

			@Override
			public String valueToString(Object value) throws ParseException {
				return (value != null) ? dateFormat.format(((Calendar) value).getTime()) : "";
			}
		});
		datePicker.setBorder(BorderFactory.createTitledBorder("Start date"));
		datePicker.setPreferredSize(new Dimension(100, 50));

		JComboBox<String> cbxBeginHour = new JComboBox<>(hours);
		cbxBeginHour.setSelectedItem(null);
		cbxBeginHour.setBorder(BorderFactory.createTitledBorder("Begin hour"));

		JComboBox<String> cbxEndHour = new JComboBox<>(hours);
		cbxEndHour.setSelectedItem(null);
		cbxEndHour.setBorder(BorderFactory.createTitledBorder("End hour"));

		JSpinner spiNumberHours = new JSpinner(new SpinnerNumberModel(40, 1, 999, 1));
		spiNumberHours.setBorder(BorderFactory.createTitledBorder("Number of hours"));

		JTable tabSchedule = new JTable(new DefaultTableModel(COLUMNS, 0)) {
			private static final long serialVersionUID = 1L;
			DefaultTableCellRenderer renderRight = new DefaultTableCellRenderer();
			{
				renderRight.setHorizontalAlignment(SwingConstants.RIGHT);
			}

			@Override
			public TableCellRenderer getCellRenderer(int arg0, int arg1) {
				return renderRight;
			}
		};
		tabSchedule.setFont(new Font(Font.MONOSPACED, Font.BOLD, 16));
		tabSchedule.setEnabled(false);

		JScrollPane scrollPane = new JScrollPane(tabSchedule);
		scrollPane.setBorder(BorderFactory.createTitledBorder("Schedule"));
		scrollPane.setEnabled(false);

		frame.add(panelBox, new GBC(0, 0, 1, 4).setFill(GBC.HORIZONTAL).setWeight(0, 0));
		frame.add(datePicker, new GBC(0, 4, 1, 1).setFill(GBC.HORIZONTAL).setWeight(0, 100));
		frame.add(cbxBeginHour, new GBC(0, 5, 1, 1).setFill(GBC.HORIZONTAL).setWeight(0, 100));
		frame.add(cbxEndHour, new GBC(0, 6, 1, 1).setFill(GBC.HORIZONTAL).setWeight(0, 100));
		frame.add(spiNumberHours, new GBC(0, 7, 1, 1).setFill(GBC.HORIZONTAL).setWeight(0, 100));
		frame.add(btnClear, new GBC(0, 8, 1, 1).setFill(GBC.HORIZONTAL).setInsets(2).setWeight(0, 100));
		frame.add(btnGenerate, new GBC(0, 9, 1, 1).setFill(GBC.HORIZONTAL).setInsets(2).setWeight(0, 100));
		frame.add(btnSave, new GBC(0, 10, 1, 1).setFill(GBC.HORIZONTAL).setInsets(2).setWeight(0, 100));
		frame.add(btnEnd, new GBC(0, 11, 1, 1).setFill(GBC.HORIZONTAL).setInsets(2).setWeight(0, 100));
		frame.add(scrollPane, new GBC(1, 0, 1, 12).setWeight(100, 0).setFill(GBC.BOTH));

		Consumer<ActionEvent> inputCompleteVerifier = (event) -> {
			if ((!cbMon.isSelected() && !cbTue.isSelected() && !cbWed.isSelected() && !cbThu.isSelected() &&
					!cbFri.isSelected() && !cbSat.isSelected() && !cbSun.isSelected())
					|| (datePicker.getModel().getValue() == null)
					|| (cbxBeginHour.getSelectedItem() == null)
					|| (cbxEndHour.getSelectedItem() == null))
				btnGenerate.setEnabled(false);
			else
				btnGenerate.setEnabled(true);
			if (tabSchedule.getRowCount() > 0)
				btnSave.setEnabled(true);
			else
				btnSave.setEnabled(false);
		};

		cbMon.addActionListener(event -> inputCompleteVerifier.accept(event));
		cbTue.addActionListener(event -> inputCompleteVerifier.accept(event));
		cbWed.addActionListener(event -> inputCompleteVerifier.accept(event));
		cbThu.addActionListener(event -> inputCompleteVerifier.accept(event));
		cbFri.addActionListener(event -> inputCompleteVerifier.accept(event));
		cbSat.addActionListener(event -> inputCompleteVerifier.accept(event));
		cbSun.addActionListener(event -> inputCompleteVerifier.accept(event));

		datePicker.addActionListener(event -> inputCompleteVerifier.accept(event));
		cbxBeginHour.addActionListener(event -> inputCompleteVerifier.accept(event));
		cbxEndHour.addActionListener(event -> inputCompleteVerifier.accept(event));

		btnGenerate.addActionListener(event -> {
			EnteredParameters parameters = new EnteredParameters();

			EnumSet<DayOfWeek> lessonDays = EnumSet.noneOf(DayOfWeek.class);
			if (cbMon.isSelected())
				lessonDays.add(DayOfWeek.MONDAY);
			if (cbTue.isSelected())
				lessonDays.add(DayOfWeek.TUESDAY);
			if (cbWed.isSelected())
				lessonDays.add(DayOfWeek.WEDNESDAY);
			if (cbThu.isSelected())
				lessonDays.add(DayOfWeek.THURSDAY);
			if (cbFri.isSelected())
				lessonDays.add(DayOfWeek.FRIDAY);
			if (cbSat.isSelected())
				lessonDays.add(DayOfWeek.SATURDAY);
			if (cbSun.isSelected())
				lessonDays.add(DayOfWeek.SUNDAY);
			parameters.setLessonDays(lessonDays);

			UtilDateModel dateModel = (UtilDateModel) datePicker.getModel();
			parameters.setStartDate(LocalDate.of(dateModel.getYear(), dateModel.getMonth() + 1, dateModel.getDay()));

			parameters.setBeginTime(LocalTime.parse(cbxBeginHour.getSelectedItem().toString(), timeFormatter));
			parameters.setEndTime(LocalTime.parse(cbxEndHour.getSelectedItem().toString(), timeFormatter));
			parameters.setHoursNumber((Integer) spiNumberHours.getValue());

			if (parameters.getBeginTime().isAfter(parameters.getEndTime())) {
				JOptionPane.showMessageDialog(frame, "End time is set before begin time!", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			HolidaysProvider webClient = HolidaysProviderFactory.getProvider();

			ScheduleGenerator scheduleGenerator = new ScheduleGenerator(webClient);
			schedule = scheduleGenerator.generate(parameters);
			Object[][] data = new Object[schedule.getLessons().size()][5];

			int row = 0;
			for (Lesson lesson : schedule.getLessons()) {
				data[row][0] = row + 1;
				data[row][1] = lesson.getDate().format(dateFormatter);
				data[row][2] = lesson.getDate().getDayOfWeek().toString().toLowerCase();
				data[row][3] = lesson.getBeginTime().format(timeFormatter);
				data[row][4] = lesson.getEndTime().format(timeFormatter);
				row++;
			}
			((DefaultTableModel) tabSchedule.getModel()).setDataVector(data, COLUMNS);
			scrollPane.setEnabled(true);
			tabSchedule.setEnabled(true);
			btnSave.setEnabled(true);
		});

		btnSave.addActionListener(event -> {
			JFileChooser chooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
					"Excel workbooks", "xlsx");
			chooser.setFileFilter(filter);
			chooser.setCurrentDirectory(new File("."));
			chooser.setDialogTitle("Save as excel file");
			int result = chooser.showSaveDialog(null);
			if (result != JFileChooser.APPROVE_OPTION)
				return;
			String newName = chooser.getSelectedFile().toString();

			ExcelCreator excelCreator = new ExcelCreator();

			try (OutputStream stream = new FileOutputStream(newName);
					Workbook workbook = excelCreator.createWorkbook(schedule)) {
				workbook.write(stream);
			} catch (IOException e) {
				System.out.println("Impossible to write schedule workbook.");
				return;
			}

			if (schedule.isLastDayShorter()) {
				try {
					String shortName = newName.substring(0, newName.lastIndexOf("."));
					Files.write(Paths.get(shortName + "-warning.txt"), WARNING_MESSAGE.getBytes());
				} catch (IOException ignored) {
				}
			}
		});

		btnEnd.addActionListener(event -> SwingUtilities.getWindowAncestor(btnEnd).dispose());

		btnClear.addActionListener(event -> {
			cbMon.setSelected(false);
			cbTue.setSelected(false);
			cbWed.setSelected(false);
			cbThu.setSelected(false);
			cbFri.setSelected(false);
			cbSat.setSelected(false);
			cbSun.setSelected(false);
			LocalDate now = LocalDate.now();
			datePicker.getModel().setDate(now.getYear(), now.getMonthValue() - 1, now.getDayOfMonth());
			datePicker.getJFormattedTextField().setText("");
			cbxBeginHour.setSelectedItem(null);
			cbxEndHour.setSelectedItem(null);
			spiNumberHours.setValue(40);
		});

		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> new GUI().runGUI());
	}

}

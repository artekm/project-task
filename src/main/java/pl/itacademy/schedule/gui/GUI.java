package pl.itacademy.schedule.gui;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;
import pl.itacademy.schedule.ScheduleGeneratorApp;
import pl.itacademy.schedule.generator.Lesson;
import pl.itacademy.schedule.generator.Schedule;
import pl.itacademy.schedule.parameters.EnteredParameters;
import pl.itacademy.schedule.util.PropertiesReader;

public class GUI {

	private DateTimeFormatter timeFormatter;
	private DateTimeFormatter dateFormatter;

	private static final String[] HOURS_IN = { "06:00", "06:30", "07:00", "07:30", "08:00", "08:30", "09:00",
			"09:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30",
			"15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00",
			"20:30", "21:00", "21:30", "22:00" };
	private String[] hours;

	private static final String[] COLUMNS = { "Lesson", "Date", "Day of week", "Begin time", "End time" };

	public static final Color ODD_ROW_COLOR = Color.decode("#F0F0F0");
	public static final Color EVEN_ROW_COLOR = Color.WHITE;

	private ScheduleGeneratorApp app;

	public GUI(ScheduleGeneratorApp app) {
		this.app = app;
		PropertiesReader props = PropertiesReader.getInstance();
		dateFormatter = DateTimeFormatter.ofPattern(props.readProperty("dateFormat"));
		timeFormatter = DateTimeFormatter.ofPattern(props.readProperty("timeFormat"));
		hours = Arrays.stream(HOURS_IN).map(s -> LocalTime.parse(s).format(timeFormatter)).collect(Collectors.toList())
				.toArray(new String[0]);
	}

	@SuppressWarnings("serial")
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

		JButton btnClear = new JButton("Clear data");
		btnClear.setMnemonic(KeyEvent.VK_C);

		JButton btnGenerate = new JButton("Generate schedule");
		btnGenerate.setMnemonic(KeyEvent.VK_G);
		btnGenerate.setEnabled(false);

		JButton btnSave = new JButton("Save as Excel");
		btnSave.setMnemonic(KeyEvent.VK_S);
		btnSave.setEnabled(false);

		JButton btnEnd = new JButton("End");
		btnEnd.setMnemonic(KeyEvent.VK_E);

		JDatePickerImpl datePicker = new JDatePickerImpl(new JDatePanelImpl(new UtilDateModel()),
				new AbstractFormatter() {

					@Override
					public Object stringToValue(String text) throws ParseException {
						return dateFormatter.parse(text);
					}

					@Override
					public String valueToString(Object value) throws ParseException {
						if (value == null)
							return "";
						LocalDate date = ((Calendar) value).getTime().toInstant().atZone(ZoneId.systemDefault())
								.toLocalDate();
						return dateFormatter.format(date);
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
			DefaultTableCellRenderer renderRight = new DefaultTableCellRenderer();

			@Override
			public TableCellRenderer getCellRenderer(int arg0, int arg1) {
				renderRight.setHorizontalAlignment(SwingConstants.RIGHT);
				if (arg0 % 2 == 0)
					renderRight.setBackground(EVEN_ROW_COLOR);
				else
					renderRight.setBackground(ODD_ROW_COLOR);
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

		JOptionPane pane = new JOptionPane(new JLabel("Generating schedule  - please wait"),
				JOptionPane.INFORMATION_MESSAGE);
		JDialog dlgScheduling = pane.createDialog(frame, "Scheduler");
		dlgScheduling.setModalityType(Dialog.ModalityType.MODELESS);

		Consumer<ActionEvent> inputVerifier = (event) -> {
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

		cbMon.addActionListener(event -> inputVerifier.accept(event));
		cbTue.addActionListener(event -> inputVerifier.accept(event));
		cbWed.addActionListener(event -> inputVerifier.accept(event));
		cbThu.addActionListener(event -> inputVerifier.accept(event));
		cbFri.addActionListener(event -> inputVerifier.accept(event));
		cbSat.addActionListener(event -> inputVerifier.accept(event));
		cbSun.addActionListener(event -> inputVerifier.accept(event));

		datePicker.addActionListener(event -> inputVerifier.accept(event));
		cbxBeginHour.addActionListener(event -> inputVerifier.accept(event));
		cbxEndHour.addActionListener(event -> inputVerifier.accept(event));

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

			SwingWorker<Void, Void> otherT = new SwingWorker<Void, Void>() {

				@Override
				protected Void doInBackground() throws Exception {
					dlgScheduling.setVisible(true);
					app.setParameters(parameters);
					app.generateSchedule(false);
					app.generateExcel();
					Schedule schedule = app.getSchedule();
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
					dlgScheduling.setVisible(false);
					return null;
				}
			};
			otherT.execute();

			scrollPane.setEnabled(true);
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

			app.saveExcelToFile(newName,false);
		});

		btnEnd.addActionListener(event -> SwingUtilities.getWindowAncestor(btnEnd).dispose());

		frame.pack();
		frame.setVisible(true);
	}
}

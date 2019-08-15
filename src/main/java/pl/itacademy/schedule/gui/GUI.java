package pl.itacademy.schedule.gui;

import java.awt.Color;
import java.awt.Component;
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
import java.util.stream.Stream;

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

	private String[] hours;

	private static final String[] COLUMNS = { "Date", "Day of week", "Begin time", "End time" };

	public static final Color ODD_ROW_COLOR = Color.decode("#F0F0F0");
	public static final Color EVEN_ROW_COLOR = Color.WHITE;

	private ScheduleGeneratorApp app;

	public GUI(ScheduleGeneratorApp app) {
		this.app = app;
		PropertiesReader props = PropertiesReader.getInstance();
		dateFormatter = DateTimeFormatter.ofPattern(props.readProperty("dateFormat"));
		timeFormatter = DateTimeFormatter.ofPattern(props.readProperty("timeFormat"));
		hours = Stream.iterate(LocalTime.of(06, 00), lt -> !lt.isAfter(LocalTime.of(22, 00)), lt -> lt.plusMinutes(30))
				.map(lt -> lt.format(timeFormatter))
				.toArray(String[]::new);
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

		JPanel panelBox = new JPanel(new GridLayout(7, 1));
		panelBox.setBorder(BorderFactory.createTitledBorder("Lesson days"));
		for (DayOfWeek dow : DayOfWeek.values())
			panelBox.add(new JCheckBox(dow.name().toLowerCase()));

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
						LocalDate date = ((Calendar) value).getTime()
								.toInstant().atZone(ZoneId.systemDefault())
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
				renderRight.setBackground((arg0 % 2 == 0) ? EVEN_ROW_COLOR : ODD_ROW_COLOR);
				return renderRight;
			}
		};
		tabSchedule.setFont(new Font(Font.MONOSPACED, Font.BOLD, 16));
		tabSchedule.setEnabled(false);

		JScrollPane scrollPane = new JScrollPane(tabSchedule);
		scrollPane.setBorder(BorderFactory.createTitledBorder("Schedule"));
		scrollPane.setEnabled(false);
		JTable rowTable = new RowNumberTable(tabSchedule);
		scrollPane.setRowHeaderView(rowTable);
		scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, rowTable.getTableHeader());

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
			boolean anyChecked = Arrays.stream(panelBox.getComponents())
					.map(c -> ((JCheckBox) c).isSelected())
					.reduce(false, Boolean::logicalOr);
			btnGenerate.setEnabled(anyChecked
					&& (datePicker.getModel().getValue() != null)
					&& (cbxBeginHour.getSelectedItem() != null)
					&& (cbxEndHour.getSelectedItem() != null));
			btnSave.setEnabled(tabSchedule.getRowCount() > 0);
		};

		for (Component cb : panelBox.getComponents())
			((JCheckBox) cb).addActionListener(event -> inputVerifier.accept(event));

		datePicker.addActionListener(event -> inputVerifier.accept(event));
		cbxBeginHour.addActionListener(event -> inputVerifier.accept(event));
		cbxEndHour.addActionListener(event -> inputVerifier.accept(event));

		btnClear.addActionListener(event -> {
			for (Component cb : panelBox.getComponents())
				((JCheckBox) cb).setSelected(false);
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
			for (Component cb : panelBox.getComponents())
				if (((JCheckBox) cb).isSelected())
					lessonDays.add(DayOfWeek.valueOf(((JCheckBox) cb).getActionCommand().toUpperCase()));
			parameters.setLessonDays(lessonDays);

			UtilDateModel dateModel = (UtilDateModel) datePicker.getModel();
			parameters.setStartDate(LocalDate.of(dateModel.getYear(), dateModel.getMonth() + 1, dateModel.getDay()));

			parameters.setBeginTime(LocalTime.parse(cbxBeginHour.getSelectedItem().toString(), timeFormatter));
			parameters.setEndTime(LocalTime.parse(cbxEndHour.getSelectedItem().toString(), timeFormatter));
			parameters.setHoursNumber((Integer) spiNumberHours.getValue());

			if (!parameters.getBeginTime().isBefore(parameters.getEndTime())) {
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

					Object[][] data = new Object[schedule.getLessons().size()][COLUMNS.length];
					int row = 0;
					for (Lesson lesson : schedule.getLessons()) {
						data[row][0] = lesson.getDate().format(dateFormatter);
						data[row][1] = lesson.getDate().getDayOfWeek().toString().toLowerCase();
						data[row][2] = lesson.getBeginTime().format(timeFormatter);
						data[row][3] = lesson.getEndTime().format(timeFormatter);
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

			app.saveExcelToFile(newName, false);
		});

		btnEnd.addActionListener(event -> SwingUtilities.getWindowAncestor(btnEnd).dispose());

		frame.pack();
		frame.setVisible(true);
	}
}

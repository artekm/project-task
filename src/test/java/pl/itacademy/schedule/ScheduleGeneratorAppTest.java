package pl.itacademy.schedule;

import org.junit.Test;

public class ScheduleGeneratorAppTest {

	@Test
	public void main_finishesOK_whenCorrectParametersEntered() {
		String commandLine = "-d monday_tuesday -b 9:00 -e 12:00 -s 1.01.2019 -n 48 -f excelTestFile.xlsx";
		String[] args = commandLine.split(" ");
		ScheduleGeneratorApp.main(args);
	}

	@Test
	public void main_finishesOK_whenCorrectParametersEnteredWithFileNameReadFromProperties() {
		String commandLine = "-d monday_tuesday -b 9:00 -e 12:00 -s 1.01.2019 -n 47";
		String[] args = commandLine.split(" ");
		ScheduleGeneratorApp.main(args);
	}

	@Test
	public void main_ShowsUsage_forHelp() {
		String commandLine = "-h";
		String[] args = commandLine.split(" ");
		ScheduleGeneratorApp.main(args);
	}

	@Test
	public void main_ShowsUsage_forIncorrectInput() {
		String commandLine = "-d monday_tuesday -b 9:00 -aaa 12:00 -s 1.01.2019 -n 47";
		String[] args = commandLine.split(" ");
		ScheduleGeneratorApp.main(args);
	}

	@Test
	public void main_ShowsUsage_forIllegalArgument() {
		String commandLine = "-d monday_tuesday -b 9:00 -e 12:00 -s 1.01.2019 -n 4a7";
		String[] args = commandLine.split(" ");
		ScheduleGeneratorApp.main(args);
	}
}

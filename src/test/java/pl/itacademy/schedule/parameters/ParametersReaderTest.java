package pl.itacademy.schedule.parameters;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import java.time.*;
import java.time.format.DateTimeParseException;

import org.apache.commons.cli.ParseException;
import org.junit.*;
import org.junit.rules.ExpectedException;

import pl.itacademy.schedule.exception.IncorrectParametersException;

public class ParametersReaderTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private ParametersReader parametersReader;

    @Before
    public void setUp() {
        parametersReader = new ParametersReader();
    }

    @Test
    public void parseArguments_nullArguments_throwsIncorrectParametersException() throws IncorrectParametersException, ParseException {
        expectedException.expect(IncorrectParametersException.class);
        expectedException.expectMessage("Arguments are null");

        parametersReader.parseArguments(null);
    }

    @Test
    public void parseArguments_emptyArguments_throwsIncorrectParametersException() throws IncorrectParametersException, ParseException {
        expectedException.expect(IncorrectParametersException.class);
        expectedException.expectMessage("Arguments are empty");

        parametersReader.parseArguments(new String[0]);
    }

    @Test
    public void parseArguments_containsHoursParameter_returnsEnteredParametersWithHours() throws IncorrectParametersException, ParseException {
        String[] args = new String[]{"-n", "20"};
        EnteredParameters result = parametersReader.parseArguments(args);

        assertThat(result.getHoursNumber(), equalTo(20));
    }

    @Test
    public void parseArguments_containsStartDateParameter_returnsEnteredParametersWithDate() throws IncorrectParametersException, ParseException {
        String[] args = new String[]{"-s", "22.06.2019"};
        LocalDate expectedDate = LocalDate.of(2019, 6, 22);
        EnteredParameters result = parametersReader.parseArguments(args);

        assertThat(result.getStartDate(), equalTo(expectedDate));
    }

    @Test
    public void parseArguments_containsBeginTimeParameter_returnsEnteredParametersWithBeginTime() throws IncorrectParametersException, ParseException {
        String[] args = new String[]{"-b", "10:00"};
        LocalTime expectedTime = LocalTime.of(10, 0, 0);
        EnteredParameters result = parametersReader.parseArguments(args);

        assertThat(result.getBeginTime(), equalTo(expectedTime));
    }

    @Test
    public void parseArguments_containsEndTimeParameter_returnsEnteredParametersWithEndTime() throws IncorrectParametersException, ParseException {
        String[] args = new String[]{"-e", "12:00"};
        LocalTime expectedTime = LocalTime.of(12, 0, 0);
        EnteredParameters result = parametersReader.parseArguments(args);

        assertThat(result.getEndTime(), equalTo(expectedTime));
    }

    @Test
    public void parseArguments_containsOneDayParameter_returnsEnteredParametersWithDay() throws IncorrectParametersException, ParseException {
        String[] args = new String[]{"-d", "monday"};
        EnteredParameters result = parametersReader.parseArguments(args);

        assertThat(result.getLessonDays(), containsInAnyOrder(DayOfWeek.MONDAY));
    }

    @Test
    public void parseArguments_containsDaysParameter_returnsEnteredParametersWithDays() throws IncorrectParametersException, ParseException {
        String[] args = new String[]{"-d", "monday_tuesday_friday"};
        EnteredParameters result = parametersReader.parseArguments(args);

        assertThat(result.getLessonDays(), containsInAnyOrder(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.FRIDAY));
    }

    @Test
    public void parseArguments_containsHelpParameter_returnsEnteredParametersWithShowHelp() throws IncorrectParametersException, ParseException {
        String[] args = new String[]{"-h"};
        EnteredParameters result = parametersReader.parseArguments(args);

        assertThat(result.isShowHelp(), equalTo(true));
    }

    @Test
    public void parseArguments_containsFileNameParameter_returnsEnteredParametersWithFileName() throws IncorrectParametersException, ParseException {
        String fileName = "pliczek.xls";
        String[] args = new String[]{"-f", fileName};
        EnteredParameters result = parametersReader.parseArguments(args);

        assertThat(result.getFileName(), equalTo(fileName));
    }
    
	@Test(expected = ParseException.class)
	public void parseArguments_throwsException_forUnknownParameter() throws Exception {
		String commandLine = "-d monday_tuesday -b 9:00 -aaa 12:00 -s 1.01.2019 -n 47";
		String[] args = commandLine.split(" ");
        parametersReader.parseArguments(args);
	}

	@Test(expected = IllegalArgumentException.class)
	public void parseArguments_throwsException_forFaultyNumber() throws Exception {
		String commandLine = "-d monday_tuesday -b 9:00 -e 12:00 -s 1.01.2019 -n 4a7";
		String[] args = commandLine.split(" ");
        parametersReader.parseArguments(args);
	}

	@Test(expected = DateTimeParseException.class)
	public void parseArguments_throwsException_forMisformatedDate() throws Exception {
		String commandLine = "-d monday_tuesday -b 9:00 -e 12:00 -s 2019-01-09 -n 47";
		String[] args = commandLine.split(" ");
        parametersReader.parseArguments(args);
	}

	@Test(expected = DateTimeParseException.class)
	public void parseArguments_throwsException_forMisformatedTime() throws Exception {
		String commandLine = "-d monday_tuesday -b 9.00 -e 12:00 -s 1.01.2019 -n 47";
		String[] args = commandLine.split(" ");
        parametersReader.parseArguments(args);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void parseArguments_throwsException_forIncorrectDays() throws Exception {
		String commandLine = "-d monday_tusday -b 9:00 -e 12:00 -s 1.01.2019 -n 47";
		String[] args = commandLine.split(" ");
        parametersReader.parseArguments(args);
	}
	
}

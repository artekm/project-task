package pl.itacademy.parameters;

import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import pl.itacademy.exception.IncorrectParametersException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class ParametersReaderTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private ParametersReader parametersReader;

    @Before
    public void setUp() {
        parametersReader = new ParametersReader();
        //args=new String[]{"-d","mon_tue","-b","10:00","-e","12:00","-n","20","-s","10.05.2019"};

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
}
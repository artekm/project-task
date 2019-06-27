package pl.itacademy.parameters;

import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;

import java.time.DayOfWeek;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class InputParametersReaderTest {

    private InputParametersReader inputParametersReader;

    @Before
    public void setUp() {
        inputParametersReader = new InputParametersReader();
    }

    @Test
    public void readParameter_inputOneDay_rerunsCollectionWithOneDayOfWeek() throws ParseException {
        String[] args = new String[]{"-d", "monday"};

        InputParameters inputParameters = inputParametersReader.readParameters(args);

        assertThat(inputParameters.getLessonDays(), containsInAnyOrder(DayOfWeek.MONDAY));
    }

    @Test
    public void readParameters_inputSeveralDays_rerunsCollectionOfDaysOfWeek() throws ParseException {
        String[] args = new String[]{"-d", "monday_Friday"};

        InputParameters inputParameters = inputParametersReader.readParameters(args);

        assertThat(inputParameters.getLessonDays(), containsInAnyOrder(DayOfWeek.MONDAY, DayOfWeek.FRIDAY));
    }

    @Test
    public void readParameters_readHoursNumber_returnsNumber() throws ParseException {
        String[] args = new String[]{"-n", "5"};

        InputParameters inputParameters = inputParametersReader.readParameters(args);

        assertThat(inputParameters.getHoursNumber(), equalTo(5));
    }
}
package pl.itacademy.parameters;

import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

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

    @Test
    public void readParameters_readBeginTime_returnsBeginTime() throws ParseException {
        String[] args = new String[]{"-b", "17:00"};

        InputParameters inputParameters = inputParametersReader.readParameters(args);

        assertThat(inputParameters.getBeginTime(), equalTo(LocalTime.of(17, 0)));
    }

    @Test
    public void readParameters_readEndTime_returnsEndTime() throws ParseException {
        String[] args = new String[]{"-e", "18:30"};

        InputParameters inputParameters = inputParametersReader.readParameters(args);

        assertThat(inputParameters.getEndTime(), equalTo(LocalTime.of(18, 30)));
    }

    @Test
    public void readParameters_readStartDate_returnsStartDate() throws ParseException {
        String[] args = new String[]{"-s", "01.07.2019"};

        InputParameters inputParameters = inputParametersReader.readParameters(args);

        assertThat(inputParameters.getStartDate(), equalTo(LocalDate.of(2019, 7, 1)));
    }

    @Test
    public void readParameters_readFileName_returnsFileName() throws ParseException {
        String fileName = "java";
        String[] args = new String[]{"-f", fileName};

        InputParameters inputParameters = inputParametersReader.readParameters(args);

        assertThat(inputParameters.getFileName(), equalTo(fileName));
    }

    @Test
    public void readParameters_readHelp_returnsTrue() throws ParseException {
        String[] args = new String[]{"-h"};

        InputParameters inputParameters = inputParametersReader.readParameters(args);

        assertThat(inputParameters.isShowHelp(), equalTo(true));
    }
}
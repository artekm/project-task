package pl.itacademy.generator;

import org.junit.Before;
import org.junit.Test;
import pl.itacademy.parameters.InputParameters;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class ScheduleGeneratorTest {

    private ScheduleGenerator scheduleGenerator;

    @Before
    public void setUp() {
        scheduleGenerator = new ScheduleGenerator();
    }

    @Test
    public void generate_getInputParameters_returnsSchedule() {
        InputParameters inputParameters = new InputParameters();
        inputParameters.setStartDate(LocalDate.of(2019, 7, 1));
        inputParameters.setBeginTime(LocalTime.of(17, 0));
        inputParameters.setEndTime(LocalTime.of(18, 30));
        inputParameters.setLessonDays(Arrays.asList(DayOfWeek.MONDAY, DayOfWeek.THURSDAY));
        inputParameters.setHoursNumber(6);

        Lesson first = new Lesson(LocalDate.of(2019, 7, 1), LocalTime.of(17, 0), LocalTime.of(18, 30));
        Lesson second = new Lesson(LocalDate.of(2019, 7, 4), LocalTime.of(17, 0), LocalTime.of(18, 30));
        Lesson third = new Lesson(LocalDate.of(2019, 7, 8), LocalTime.of(17, 0), LocalTime.of(18, 30));
        Lesson fourth = new Lesson(LocalDate.of(2019, 7, 11), LocalTime.of(17, 0), LocalTime.of(18, 30));

        Schedule schedule = scheduleGenerator.generate(inputParameters);

        assertThat(schedule.getLessons(), containsInAnyOrder(first, second, third, fourth));
        assertThat(schedule.isLessonsFitToSchedule(), equalTo(true));
    }
}
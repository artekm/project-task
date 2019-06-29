package pl.itacademy.schedule.generator;

import org.junit.Before;
import org.junit.Test;
import pl.itacademy.schedule.parameters.EnteredParameters;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.TUESDAY;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

public class ScheduleGeneratorTest {

    private ScheduleGenerator generator;

    @Before
    public void setUp() {
        generator = new ScheduleGenerator();
    }

    @Test
    public void generate_generatesScheduleFromParameters() {
        EnteredParameters parameters = new EnteredParameters();
        parameters.setBeginTime(LocalTime.of(9, 0));
        parameters.setEndTime(LocalTime.of(12, 0));
        parameters.setStartDate(LocalDate.of(2019, 7, 1));
        parameters.setHoursNumber(6);
        parameters.setLessonDays(Arrays.asList(SATURDAY, TUESDAY));

        Lesson firstLesson = new Lesson(LocalDate.of(2019, 7, 2), LocalTime.of(9, 0), LocalTime.of(12, 0));
        Lesson secondLesson = new Lesson(LocalDate.of(2019, 7, 6), LocalTime.of(9, 0), LocalTime.of(12, 0));

        Schedule schedule = generator.generate(parameters);
        assertThat(schedule.getLessons(), containsInAnyOrder(firstLesson, secondLesson));
    }

}
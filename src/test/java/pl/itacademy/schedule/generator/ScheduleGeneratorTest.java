package pl.itacademy.schedule.generator;

import org.junit.Before;
import org.junit.Test;
import pl.itacademy.schedule.holidays.HolidaysWebClient;
import pl.itacademy.schedule.parameters.EnteredParameters;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.TUESDAY;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class ScheduleGeneratorTest {

    private ScheduleGenerator generator;

    @Before
    public void setUp() {
        HolidaysWebClient webClient = new HolidaysWebClientMock();
        generator = new ScheduleGenerator(webClient);
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
        assertThat(schedule.getNumberOfHours(), equalTo(6));
        assertThat(schedule.isLastDayShorter(), equalTo(false));
    }

    @Test
    public void generate_requiredHoursCantBeFitIntoDailySchedule_createsLastDayShorter_addsFlagForShorterLastDay() {
        EnteredParameters parameters = new EnteredParameters();
        parameters.setBeginTime(LocalTime.of(9, 0));
        parameters.setEndTime(LocalTime.of(12, 0));
        parameters.setStartDate(LocalDate.of(2019, 7, 1));
        parameters.setHoursNumber(7);
        parameters.setLessonDays(Arrays.asList(SATURDAY, TUESDAY));

        Lesson firstLesson = new Lesson(LocalDate.of(2019, 7, 2), LocalTime.of(9, 0), LocalTime.of(12, 0));
        Lesson secondLesson = new Lesson(LocalDate.of(2019, 7, 6), LocalTime.of(9, 0), LocalTime.of(12, 0));
        Lesson thirdLesson = new Lesson(LocalDate.of(2019, 7, 9), LocalTime.of(9, 0), LocalTime.of(10, 0));

        Schedule schedule = generator.generate(parameters);
        assertThat(schedule.getLessons(), containsInAnyOrder(firstLesson, secondLesson, thirdLesson));
        assertThat(schedule.getNumberOfHours(), equalTo(7));
        assertTrue(schedule.isLastDayShorter());
    }

    private final static class HolidaysWebClientMock extends HolidaysWebClient {
        @Override
        public List<LocalDate> getHolidays(int year) {
            return Arrays.asList(LocalDate.of(2019, 1, 1),
                    LocalDate.of(2019, 1, 6));
        }
    }

}
package pl.itacademy.parameters;

import java.time.DayOfWeek;
import java.util.Collection;

public class InputParameters {
    private Collection<DayOfWeek> lessonDays;

    private int hoursNumber;

    public int getHoursNumber() {
        return hoursNumber;
    }

    public void setHoursNumber(int hoursNumber) {
        this.hoursNumber = hoursNumber;
    }

    public Collection<DayOfWeek> getLessonDays() {
        return lessonDays;
    }

    public void setLessonDays(Collection<DayOfWeek> lessonDays) {
        this.lessonDays = lessonDays;
    }
}

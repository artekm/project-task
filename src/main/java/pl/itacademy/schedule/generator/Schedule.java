package pl.itacademy.schedule.generator;

import java.util.Collection;

public class Schedule {

    private Collection<Lesson> lessons;

    private boolean successfulSchedule;

    public Schedule(Collection<Lesson> lessons, boolean successfulSchedule) {
        this.lessons = lessons;
        this.successfulSchedule = successfulSchedule;
    }

    public Collection<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(Collection<Lesson> lessons) {
        this.lessons = lessons;
    }

    public boolean isSuccessfulSchedule() {
        return successfulSchedule;
    }

    public void setSuccessfulSchedule(boolean successfulSchedule) {
        this.successfulSchedule = successfulSchedule;
    }
}

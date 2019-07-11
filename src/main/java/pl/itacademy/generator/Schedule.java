package pl.itacademy.generator;

import java.util.Collection;

public class Schedule {

    private Collection<Lesson> lessons;
    private int hoursPlanned;

    private boolean lessonsFitToSchedule;

    public Schedule(Collection<Lesson> lessons, int hoursPlanned, boolean lessonsFitToSchedule) {
        this.lessons = lessons;
        this.hoursPlanned = hoursPlanned;
        this.lessonsFitToSchedule = lessonsFitToSchedule;
    }

    public Collection<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(Collection<Lesson> lessons) {
        this.lessons = lessons;
    }

    public boolean isLessonsFitToSchedule() {
        return lessonsFitToSchedule;
    }

    public void setLessonsFitToSchedule(boolean lessonsFitToSchedule) {
        this.lessonsFitToSchedule = lessonsFitToSchedule;
    }

    public int getHoursPlanned() {
        return hoursPlanned;
    }

    public void setHoursPlanned(int hoursPlanned) {
        this.hoursPlanned = hoursPlanned;
    }
}

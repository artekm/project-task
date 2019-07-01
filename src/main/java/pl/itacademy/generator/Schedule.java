package pl.itacademy.generator;

import java.util.Collection;

public class Schedule {

    private Collection<Lesson> lessons;

    private boolean lessonsFitToSchedule;

    public Schedule(Collection<Lesson> lessons, boolean lessonsFitToSchedule) {
        this.lessons = lessons;
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
}

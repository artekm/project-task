package pl.itacademy.schedule.generator;

import java.util.Collection;

public class Schedule {

    private Collection<Lesson> lessons;

    private boolean lastDayShorter;

    private int numberOfHours;

    public Schedule() {
    }

    public Collection<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(Collection<Lesson> lessons) {
        this.lessons = lessons;
    }

	public boolean isLastDayShorter() {
		return lastDayShorter;
	}

	public void setLastDayShorter(boolean lastDayShorter) {
		this.lastDayShorter = lastDayShorter;
	}

	public int getNumberOfHours() {
		return numberOfHours;
	}

	public void setNumberOfHours(int numberOfHours) {
		this.numberOfHours = numberOfHours;
	}
    
}

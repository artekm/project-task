package pl.itacademy.schedule.generator;

import java.util.Collection;

public class Schedule {

    private Collection<Lesson> lessons;

    private boolean lastDayShorter;
    private int numberOfDays;
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

	public int getNumberOfDays() {
		return numberOfDays;
	}

	public void setNumberOfDays(int numberOfDays) {
		this.numberOfDays = numberOfDays;
	}

	public int getNumberOfHours() {
		return numberOfHours;
	}

	public void setNumberOfHours(int numberOfHours) {
		this.numberOfHours = numberOfHours;
	}
    
}

package pl.itacademy.parameters;

import pl.itacademy.util.PropertiesReader;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;

public class InputParameters {
    private Collection<DayOfWeek> lessonDays;

    private Integer hoursNumber;

    private LocalTime beginTime;

    private LocalTime endTime;

    private LocalDate startDate;

    private boolean showHelp;

    private String fileName;

    public InputParameters() {
        fileName = PropertiesReader.getInstance().readProperty("excel.defaultName");
    }

    public boolean isShowHelp() {
        return showHelp;
    }

    public void setShowHelp(boolean showHelp) {
        this.showHelp = showHelp;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public LocalTime getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(LocalTime beginTime) {
        this.beginTime = beginTime;
    }

    public Integer getHoursNumber() {
        return hoursNumber;
    }

    public void setHoursNumber(Integer hoursNumber) {
        this.hoursNumber = hoursNumber;
    }

    public Collection<DayOfWeek> getLessonDays() {
        return lessonDays;
    }

    public void setLessonDays(Collection<DayOfWeek> lessonDays) {
        this.lessonDays = lessonDays;
    }
}

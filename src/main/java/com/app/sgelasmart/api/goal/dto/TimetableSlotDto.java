package com.app.sgelasmart.api.goal.dto;

public class TimetableSlotDto {

    private String title;
    private String dayOfWeek;
    private String date;
    private String startTime;
    private String endTime;

    public TimetableSlotDto() {}

    public TimetableSlotDto(String title, String dayOfWeek, String startTime, String endTime) {
        this.title = title;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public TimetableSlotDto(String title, String dayOfWeek, String date, String startTime, String endTime) {
        this.title = title;
        this.dayOfWeek = dayOfWeek;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}

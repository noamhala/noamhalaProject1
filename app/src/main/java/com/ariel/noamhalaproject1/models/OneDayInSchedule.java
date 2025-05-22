package com.ariel.noamhalaproject1.models;

import java.io.Serializable;

public class OneDayInSchedule implements Serializable {

    // Fields for the day and hour of the schedule
    protected String date;
    protected String hour;

    // Constructor with parameters to initialize day and hour
    public OneDayInSchedule(String date, String hour) {
        this.date = date;
        this.hour = hour;
    }

    // Default constructor (empty)
    public OneDayInSchedule() {
        // No initialization, fields will be set through setters
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    // Getter for 'hour'
    public String getHour() {
        return hour;
    }

    // Setter for 'hour'
    public void setHour(String hour) {
        this.hour = hour;
    }

    @Override
    public String toString() {
        return "OneDayInSchedule{" +
                "date='" + date + '\'' +
                ", hour='" + hour + '\'' +
                '}';
    }
}


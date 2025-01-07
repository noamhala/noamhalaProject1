package com.ariel.noamhalaproject1.models;

import java.io.Serializable;

public class OneDayInSchedule implements Serializable {

    // Fields for the day and hour of the schedule
    private String day;
    private String hour;

    // Constructor with parameters to initialize day and hour
    public OneDayInSchedule(String day, String hour) {
        this.day = day;
        this.hour = hour;
    }

    // Default constructor (empty)
    public OneDayInSchedule() {
        // No initialization, fields will be set through setters
    }

    // Getter for 'day'
    public String getDay() {
        return day;
    }

    // Setter for 'day'
    public void setDay(String day) {
        this.day = day;
    }

    // Getter for 'hour'
    public String getHour() {
        return hour;
    }

    // Setter for 'hour'
    public void setHour(String hour) {
        this.hour = hour;
    }

    // Overriding toString to return a string representation of the object
    @Override
    public String toString() {
        return "OneDayInSchedule{" +
                "day='" + day + '\'' +
                ", hour='" + hour + '\'' +
                '}';
    }
}


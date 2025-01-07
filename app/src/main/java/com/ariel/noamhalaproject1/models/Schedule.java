package com.ariel.noamhalaproject1.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Schedule implements Serializable {

    private ArrayList<OneDayInSchedule> mySchedule = new ArrayList<>();  // Use OneDayInSchedule instead of OneDay

    // Default constructor
    public Schedule() {
        // mySchedule is already initialized above
    }

    // Constructor that initializes the list with the provided ArrayList
    public Schedule(ArrayList<OneDayInSchedule> mySchedule) {
        this.mySchedule = mySchedule;
    }

    // Getter for mySchedule
    public ArrayList<OneDayInSchedule> getMySchedule() {
        return mySchedule;
    }

    // Setter for mySchedule
    public void setMySchedule(ArrayList<OneDayInSchedule> mySchedule) {
        this.mySchedule = mySchedule;
    }

    // Method to add a OneDayInSchedule object to the schedule
    public void addToMySchedule(OneDayInSchedule oneDayInSchedule) {
        this.mySchedule.add(oneDayInSchedule);
    }

    // Overridden toString method to return the schedule as a string
    @Override
    public String toString() {
        return "Schedule{" +
                "mySchedule=" + mySchedule +
                '}';
    }
}

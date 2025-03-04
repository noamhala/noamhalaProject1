package com.ariel.noamhalaproject1.models;

import java.io.Serializable;

public class Workout implements Serializable {

    protected String id;           // Unique identifier for the workout
    protected User trainee;        // The trainee participating in the workout
    protected Coach coach;         // The coach leading the workout
    protected String date;         // Date of the workout (e.g., "2025-02-27")
    protected String hour;         // Time of the workout (e.g., "14:00")
    protected String goals;        // The goals for the workout (e.g., "Build muscle", "Improve endurance")
    protected String location;     // The location of the workout (e.g., "Gym", "Outdoor")

    // Constructor to initialize a Workout object
    public Workout(String id, User trainee, Coach coach, String date, String hour, String goals, String location) {
        this.id = id;
        this.trainee = trainee;
        this.coach = coach;
        this.date = date;
        this.hour = hour;
        this.goals = goals;
        this.location = location;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getTrainee() {
        return trainee;
    }

    public void setTrainee(User trainee) {
        this.trainee = trainee;
    }

    public Coach getCoach() {
        return coach;
    }

    public void setCoach(Coach coach) {
        this.coach = coach;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getGoals() {
        return goals;
    }

    public void setGoals(String goals) {
        this.goals = goals;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Workout{" +
                "id='" + id + '\'' +
                ", trainee=" + trainee +
                ", coach=" + coach +
                ", date='" + date + '\'' +
                ", hour='" + hour + '\'' +
                ", goals='" + goals + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}

    package com.ariel.noamhalaproject1.models;

    import androidx.annotation.NonNull;

    import java.io.Serializable;

    public class Workout implements Serializable {

        protected String id;           // Unique identifier for the workout
        protected String traineeId;        // The trainee participating in the workout
        protected String coachId;         // The coach leading the workout
        protected String date;         // Date of the workout (e.g., "2025-02-27")
        protected String hour;         // Time of the workout (e.g., "14:00")
        protected String goals;        // The goals for the workout (e.g., "Build muscle", "Improve endurance")
        protected String location;     // The location of the workout (e.g., "Gym", "Outdoor")
        protected Boolean isAccepted; // The status of the workout (null for pending, true for accepted, false for reject)

        public Workout() {
        }

        public Workout(String id, String traineeId, String coachId, String date, String hour, String goals, String location, Boolean isAccepted) {
            this.id = id;
            this.traineeId = traineeId;
            this.coachId = coachId;
            this.date = date;
            this.hour = hour;
            this.goals = goals;
            this.location = location;
            this.isAccepted = isAccepted;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTraineeId() {
            return traineeId;
        }

        public void setTraineeId(String traineeId) {
            this.traineeId = traineeId;
        }

        public String getCoachId() {
            return coachId;
        }

        public void setCoachId(String coachId) {
            this.coachId = coachId;
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

        public Boolean getAccepted() {
            return isAccepted;
        }

        public void setAccepted(Boolean accepted) {
            isAccepted = accepted;
        }

        @NonNull
        @Override
        public String toString() {
            return "Workout{" +
                    "id='" + id + '\'' +
                    ", traineeId='" + traineeId + '\'' +
                    ", coachId='" + coachId + '\'' +
                    ", date='" + date + '\'' +
                    ", hour='" + hour + '\'' +
                    ", goals='" + goals + '\'' +
                    ", location='" + location + '\'' +
                    ", isAccepted=" + isAccepted +
                    '}';
        }
    }

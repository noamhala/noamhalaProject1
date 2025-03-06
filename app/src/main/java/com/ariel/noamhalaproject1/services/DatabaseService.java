package com.ariel.noamhalaproject1.services;

import android.util.Log;

import androidx.annotation.Nullable;

import com.ariel.noamhalaproject1.models.Coach;
import com.ariel.noamhalaproject1.models.Trainee;
import com.ariel.noamhalaproject1.models.User;
import com.ariel.noamhalaproject1.models.Workout;  // Make sure to import the Workout model
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DatabaseService {

    // Tag for logging
    private static final String TAG = "DatabaseService";

    public void getTrainees(@NotNull final DatabaseCallback<List<Trainee>> callback) {
        readData("trainees/").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting data", task.getException());
                callback.onFailed(task.getException());
                return;
            }

            List<Trainee> trainees = new ArrayList<>();
            task.getResult().getChildren().forEach(dataSnapshot -> {
                Trainee trainee = dataSnapshot.getValue(Trainee.class);  // Deserialize the trainee
                Log.d(TAG, "Got trainee: " + trainee);
                trainees.add(trainee);  // Add to the list
            });

            callback.onCompleted(trainees);  // Return the list of trainees
        });
    }

    // Callback interface for database operations
    public interface DatabaseCallback<T> {

        void onCompleted(T object);
        void onFailed(Exception e);
    }

    // Singleton instance of this service
    private static DatabaseService instance;

    // Reference to the Firebase Realtime Database
    private final DatabaseReference databaseReference;

    // Private constructor to initialize the database reference
    private DatabaseService() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    // Get instance of the DatabaseService (singleton)
    public static DatabaseService getInstance() {
        if (instance == null) {
            instance = new DatabaseService();
        }
        return instance;
    }

    // Private method to write data to Firebase at a specific path
    private void writeData(@NotNull final String path, @NotNull final Object data, final @Nullable DatabaseCallback<Void> callback) {
        databaseReference.child(path).setValue(data).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (callback != null) {
                    callback.onCompleted(task.getResult());
                }
            } else {
                if (callback != null) {
                    callback.onFailed(task.getException());
                }
            }
        });
    }

    // Private method to read data from Firebase at a specific path
    private DatabaseReference readData(@NotNull final String path) {
        return databaseReference.child(path);
    }

    // Private method to get data from Firebase at a specific path
    private <T> void getData(@NotNull final String path, @NotNull final Class<T> clazz, @NotNull final DatabaseCallback<T> callback) {
        readData(path).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting data", task.getException());
                callback.onFailed(task.getException());
                return;
            }
            T data = task.getResult().getValue(clazz);
            callback.onCompleted(data);
        });
    }

    // Private method to generate a new ID for a new object in the database
    private String generateNewId(@NotNull final String path) {
        return databaseReference.child(path).push().getKey();
    }

    // New method to generate a unique workout ID
    public String generateWorkoutId() {
        return generateNewId("workouts");
    }

    // Public method to create a new user in the database
    public void createNewUser(@NotNull final User user, @Nullable final DatabaseCallback<Void> callback) {
        writeData("users/" + user.getId(), user, callback);
    }

    // Public method to get a user from the database by ID
    public void getUser(String uid, @NotNull final DatabaseCallback<User> callback) {
        getData("users/", User.class, callback);
    }

    // Public method to get a coach from the database by ID
    public void getCoach(String uid, @NotNull final DatabaseCallback<Coach> callback) {
        getData("coaches/"+uid, Coach.class, callback);
    }

    // Public method to get a trainee from the database by ID
    public void getTrainee(String uid, @NotNull final DatabaseCallback<Trainee> callback) {
        getData("trainees/" + uid, Trainee.class, callback);
    }

    // Public method to get all coaches
    public void getCoaches(@NotNull final DatabaseCallback<List<Coach>> callback) {
        readData("coaches/").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting data", task.getException());
                callback.onFailed(task.getException());
                return;
            }
            List<Coach> coaches = new ArrayList<>();
            task.getResult().getChildren().forEach(dataSnapshot -> {
                Coach coach = dataSnapshot.getValue(Coach.class);
                Log.d(TAG, "Got coaches: " + coach);
                coaches.add(coach);
            });

            callback.onCompleted(coaches);
        });
    }

    // Public method to create a new trainee in the database
    public void createNewTrainee(@NotNull final Trainee trainee, @Nullable final DatabaseCallback<Void> callback) {
        writeData("trainees/" + trainee.getId(), trainee, callback);  // Save to "trainees" path, not "users"
    }

    // Public method to create a new coach in the database
    public void createNewCoach(@NotNull final Coach coach, @Nullable final DatabaseCallback<Void> callback) {
        writeData("coaches/" + coach.getId(), coach, callback);
    }

    // New public method to fetch workouts for a specific coach
    public void getCoachWorkouts(Coach coachId, @NotNull final DatabaseCallback<List<Workout>> callback) {
        readData("workouts/coach/" + coachId).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting data", task.getException());
                callback.onFailed(task.getException());
                return;
            }
            List<Workout> workouts = new ArrayList<>();
            task.getResult().getChildren().forEach(dataSnapshot -> {
                Workout workout = dataSnapshot.getValue(Workout.class);  // Deserialize the workout
                Log.d(TAG, "Got workout: " + workout);
                workouts.add(workout);  // Add to the list
            });

            callback.onCompleted(workouts);  // Return the list of workouts
        });
    }

    // New public method to submit a workout request
    public void submitWorkoutRequestForCoach(Workout workout, @Nullable final DatabaseCallback<Void> callback) {
        writeData("coaches/"+workout.getCoachId()+  "/workouts/" + workout.getId(), workout, callback);
    }

    // New public method to submit a workout request
    public void submitWorkoutRequestForTrainee(Workout workout, @Nullable final DatabaseCallback<Void> callback) {
        writeData("trainees/"+workout.getTraineeId()+  "/workouts/" + workout.getId(), workout, callback);
    }
}

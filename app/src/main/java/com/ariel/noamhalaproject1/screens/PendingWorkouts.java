package com.ariel.noamhalaproject1.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ariel.noamhalaproject1.R;
import com.ariel.noamhalaproject1.adapters.WorkoutAdapter;
import com.ariel.noamhalaproject1.models.Workout;
import com.ariel.noamhalaproject1.services.AuthenticationService;
import com.ariel.noamhalaproject1.services.DatabaseService;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class PendingWorkouts extends AppCompatActivity {
    private static final String TAG = "PendingWorkouts";

    private ListView lvWorkoutRequests;
    private WorkoutAdapter workoutAdapter;
    private ArrayList<Workout> workoutRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_workouts);

        lvWorkoutRequests = findViewById(R.id.lvWorkoutRequests);
        workoutRequests = new ArrayList<>();

        // Get the current coach's ID (You can use your own method to get the logged-in coach's ID)

        // Fetch the workout requests for the coach
        fetchPendingWorkouts();
    }

    private void fetchPendingWorkouts() {
        String coachId = AuthenticationService.getInstance().getCurrentUserId();
        // Fetch workout requests from the database that are pending for the coach
        DatabaseService.getInstance().getCoachWorkouts(coachId, new DatabaseService.DatabaseCallback<List<Workout>>() {
            @Override
            public void onCompleted(List<Workout> workouts) {
                Log.i(TAG, workouts.toString());
                Log.i(TAG, "size = "+workouts.size());
                workouts.removeIf(workout -> workout.getAccepted() != null);
                workoutRequests.clear(); // Clear previous data
                workoutRequests.addAll(workouts);
                workoutAdapter = new WorkoutAdapter(PendingWorkouts.this, workoutRequests, new WorkoutAdapter.OnItemWorkout() {
                    @Override
                    public boolean isShowAccept() {
                        return true;
                    }

                    @Override
                    public boolean isShowReject() {
                        return true;
                    }
                    @Override
                    public boolean isShowAdd() {
                        return false; // or true, depending on what you want
                    }

                    @Override
                    public void onAccept(Workout workout) {
                        approveWorkout(workout);
                    }

                    @Override
                    public void onReject(Workout workout) {
                        rejectWorkout(workout);
                    }

                    @Override
                    public void onAdd(Workout workout) {
                        // Leave empty or handle "Add" logic if needed
                    }

                    @Override
                    public void onDetails(Workout workout) {
                        Intent intent = new Intent(PendingWorkouts.this, DetailsWorkout.class);
                        intent.putExtra("workout", workout);
                        startActivity(intent);
                    }
                });
                lvWorkoutRequests.setAdapter(workoutAdapter);

            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(PendingWorkouts.this, "Error fetching workout requests.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // Method to approve the workout
    private void approveWorkout(Workout workout) {
        workout.setAccepted(true);

        // Accept the workout and update status in the database
        DatabaseService.getInstance().updateWorkout(workout, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void object) {
                Toast.makeText(PendingWorkouts.this, "Workout Approved!", Toast.LENGTH_SHORT).show();
                // Refresh the list of workouts after approval
                fetchPendingWorkouts(); // Replace with the actual coach ID logic
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(PendingWorkouts.this, "Failed to approve workout.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    // Method to reject the workout
    private void rejectWorkout(Workout workout) {
        workout.setAccepted(false);

        // You can create a similar method like acceptWorkout to update rejection status.
        // Here's an example of how you might implement rejecting a workout:
        DatabaseService.getInstance().updateWorkout(workout, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void object) {
                Toast.makeText(PendingWorkouts.this, "Workout Rejected!", Toast.LENGTH_SHORT).show();
                // Refresh the list of workouts after approval
                fetchPendingWorkouts(); // Replace with the actual coach ID logic
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(PendingWorkouts.this, "Failed to reject workout.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}


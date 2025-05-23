package com.ariel.noamhalaproject1.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ariel.noamhalaproject1.R;
import com.ariel.noamhalaproject1.adapters.WorkoutAdapter;
import com.ariel.noamhalaproject1.models.Trainee;
import com.ariel.noamhalaproject1.models.Workout;
import com.ariel.noamhalaproject1.services.AuthenticationService;
import com.ariel.noamhalaproject1.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;

public class GetTraineeSchedule extends AppCompatActivity {

    ListView lvTraineeSchedule;
    ArrayList<Workout> workouts = new ArrayList<>();
    WorkoutAdapter adpTraineeSchedule;

    private DatabaseService databaseService;
    private AuthenticationService authenticationService;
    String uid;
    Trainee trainee = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_get_trainee_schedule);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        databaseService = DatabaseService.getInstance();
        authenticationService = AuthenticationService.getInstance();
        uid = authenticationService.getCurrentUserId();

        lvTraineeSchedule = findViewById(R.id.lvTraineeSchedule);  // Ensure this matches the ListView ID in XML
        adpTraineeSchedule = new WorkoutAdapter(this, workouts, new WorkoutAdapter.OnItemWorkout() {
            @Override
            public boolean isShowAccept() {
                return false;
            }

            @Override
            public boolean isShowReject() {
                return false;
            }

            @Override
            public void onAccept(Workout workout) {}

            @Override
            public void onReject(Workout workout) {}

            @Override
            public void onDetails(Workout workout) {
                Intent intent = new Intent(GetTraineeSchedule.this, DetailsWorkout.class);
                intent.putExtra("workout", workout);
                startActivity(intent);
            }
        });  // Pass the list of workouts to the adapter
        lvTraineeSchedule.setAdapter(adpTraineeSchedule);

        // Fetch the workouts for the trainee
        databaseService.getTraineeWorkouts(uid, new DatabaseService.DatabaseCallback<List<Workout>>() {
            @Override
            public void onCompleted(List<Workout> ws) {
                Log.d("GetTraineeSchedule", "Retrieved workouts: " + ws.size());
                ws.removeIf(workout -> workout.getAccepted() == null);
                workouts.addAll(ws);  // Add the retrieved workouts to the list
                adpTraineeSchedule.notifyDataSetChanged();  // Notify the adapter to update the ListView

                Log.e("workoutList", workouts.toString());
            }

            @Override
            public void onFailed(Exception e) {
                Log.e("GetTraineeScheduleError", "Error fetching workouts", e);
                // Show error message to the user
                Toast.makeText(GetTraineeSchedule.this, "Failed to fetch workouts", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

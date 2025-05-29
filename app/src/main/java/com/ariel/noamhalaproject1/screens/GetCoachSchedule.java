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
import com.ariel.noamhalaproject1.models.Coach;
import com.ariel.noamhalaproject1.models.Workout;
import com.ariel.noamhalaproject1.services.AuthenticationService;
import com.ariel.noamhalaproject1.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class GetCoachSchedule extends AppCompatActivity {

    ListView lvCoachSchedule;
    ArrayList<Workout> workouts = new ArrayList<>();
    WorkoutAdapter workoutAdapter;

    private DatabaseService databaseService;
    private AuthenticationService authenticationService;
    String uid;
    Coach coach = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_get_coach_schedule);  // Make sure this layout is the one we finalized

        // Handle system bar insets for edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize services
        databaseService = DatabaseService.getInstance();
        authenticationService = AuthenticationService.getInstance();
        Intent intent = getIntent();
        uid = intent.hasExtra("coachId") ? intent.getStringExtra("coachId") : authenticationService.getCurrentUserId();
        // Setup ListView
        lvCoachSchedule = findViewById(R.id.lvCoachSchedule);
        workoutAdapter = new WorkoutAdapter(this, workouts, new WorkoutAdapter.OnItemWorkout() {
            @Override
            public boolean isShowAccept() {
                return false;
            }

            @Override
            public boolean isShowReject() {
                return false;
            }
            @Override

            public boolean isShowAdd() {
                return false;
            }


            @Override
            public void onAccept(Workout workout) {}

            @Override
            public void onReject(Workout workout) {}

            public void onAdd(Workout workout) {}

            @Override
            public void onDetails(Workout workout) {
                Intent intent = new Intent(GetCoachSchedule.this, DetailsWorkout.class);
                intent.putExtra("workout", workout);
                startActivity(intent);
            }
        });
        lvCoachSchedule.setAdapter(workoutAdapter);

        // Fetch workouts for the coach
        databaseService.getCoachWorkouts(uid, new DatabaseService.DatabaseCallback<List<Workout>>() {
            @Override
            public void onCompleted(List<Workout> ws) {
                Log.d("GetCoachSchedule", "Retrieved workouts: " + ws.size());
                ws.removeIf(workout -> workout.getAccepted() == null || !workout.getAccepted());

                // Add workouts to the list and update the adapter
                workouts.addAll(ws);
                workoutAdapter.notifyDataSetChanged();

                Log.e("workoutList", workouts.toString());
            }

            @Override
            public void onFailed(Exception e) {
                Log.e("GetCoachScheduleError", "Error fetching workouts", e);
                Toast.makeText(GetCoachSchedule.this, "Failed to fetch workouts", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

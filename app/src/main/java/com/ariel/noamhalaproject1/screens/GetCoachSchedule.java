package com.ariel.noamhalaproject1.screens;

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

public class GetCoachSchedule extends AppCompatActivity {

    ListView lvSearch1;
    ArrayList<Workout> workouts = new ArrayList<>();
    WorkoutAdapter adpSearch1;

    private DatabaseService databaseService;
    private AuthenticationService authenticationService;
    String uid;
    Coach coach = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_get_coach_schedule);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        databaseService = DatabaseService.getInstance();
        authenticationService = AuthenticationService.getInstance();
        uid = authenticationService.getCurrentUserId();

        lvSearch1 = findViewById(R.id.lvCoachSchedule);
        adpSearch1 = new WorkoutAdapter(this, workouts);  // Updated adapter initialization
        lvSearch1.setAdapter(adpSearch1);

        databaseService.getWorkoutsForCoach(uid,  new DatabaseService.DatabaseCallback<List<Workout>>() {
            @Override
            public void onCompleted(List<Workout> object) {
                Log.d("GetCoachSchedule", "Retrieved workouts: " + object.size());
                        workouts.addAll(object);
                        adpSearch1.notifyDataSetChanged();

                Log.e("workoutList", workouts.toString());
            }

            @Override
            public void onFailed(Exception e) {
                Log.e("GetCoachScheduleError", "Error fetching workouts", e);
                // Show error message to the user

                        Toast.makeText(GetCoachSchedule.this, "Failed to fetch workouts", Toast.LENGTH_SHORT).show();

            }
        });
    }
}

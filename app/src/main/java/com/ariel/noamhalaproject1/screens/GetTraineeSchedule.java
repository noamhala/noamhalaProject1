package com.ariel.noamhalaproject1.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.ariel.noamhalaproject1.models.Trainee;
import com.ariel.noamhalaproject1.models.Workout;
import com.ariel.noamhalaproject1.services.AuthenticationService;
import com.ariel.noamhalaproject1.services.DatabaseService;
import com.google.firebase.auth.FirebaseAuth;

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
        Intent intent = getIntent();
        uid = intent.hasExtra("traineeId") ? intent.getStringExtra("traineeId") : authenticationService.getCurrentUserId();

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
            public boolean isShowAdd() {
                return true;
            }

            @Override
            public void onAccept(Workout workout) {}

            @Override
            public void onReject(Workout workout) {}

            @Override
            public void onAdd(Workout workout) {
                AddWorkout(workout);
            }

            @Override
            public void onDetails(Workout workout) {
                Intent intent = new Intent(GetTraineeSchedule.this, DetailsWorkout.class);
                intent.putExtra("workout", workout);
                startActivity(intent);
            }
        });
        lvTraineeSchedule.setAdapter(adpTraineeSchedule);

        // Fetch the workouts for the trainee
        databaseService.getTraineeWorkouts(uid, new DatabaseService.DatabaseCallback<List<Workout>>() {
            @Override
            public void onCompleted(List<Workout> ws) {
                Log.d("GetTraineeSchedule", "Retrieved workouts: " + ws.size());
                ws.removeIf(workout -> workout.getAccepted() == null || !workout.getAccepted());
                workouts.addAll(ws);
                if (workouts.isEmpty()) {
                    Toast.makeText(GetTraineeSchedule.this, "No approved workouts found", Toast.LENGTH_SHORT).show();
                }
                adpTraineeSchedule.notifyDataSetChanged();
                Log.e("workoutList", workouts.toString());
            }

            @Override
            public void onFailed(Exception e) {
                Log.e("GetTraineeScheduleError", "Error fetching workouts", e);
                Toast.makeText(GetTraineeSchedule.this, "Failed to fetch workouts", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_about) {
            Intent go = new Intent(getApplicationContext(), About.class);
            startActivity(go);
            return true;
        } else if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut(); // Log out the user

            Intent goLogin = new Intent(getApplicationContext(), Login.class);
            goLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear back stack
            startActivity(goLogin);
            finish(); // Finish current activity
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void AddWorkout(Workout workout) {

            Intent intent = new Intent(GetTraineeSchedule.this, CoachRequest.class);
            intent.putExtra("coachId", workout.getCoachId());
            startActivity(intent);

    }
}

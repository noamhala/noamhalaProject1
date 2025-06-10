package com.ariel.noamhalaproject1.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ariel.noamhalaproject1.R;
import com.ariel.noamhalaproject1.models.Coach;
import com.ariel.noamhalaproject1.models.Trainee;
import com.ariel.noamhalaproject1.models.Workout;
import com.ariel.noamhalaproject1.services.DatabaseService;
import com.google.firebase.auth.FirebaseAuth;

public class DetailsWorkout extends AppCompatActivity {

    TextView tvCoachName, tvTraineeName, tvDate, tvTime, tvLocation, tvgoals, tvstatus;
    Workout workout;

    DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_details_workout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.databaseService = DatabaseService.getInstance();
        this.workout = getIntent().getSerializableExtra("workout", Workout.class);

        initView();
        getNames();

    }

    private void initView() {
        tvCoachName = findViewById(R.id.tv_trainee_name);
        tvTraineeName = findViewById(R.id.tv_phone_trainee);
        tvDate = findViewById(R.id.tv_email_trainee);
        tvTime = findViewById(R.id.tv_city_trainee);
        tvLocation = findViewById(R.id.tv_age_trainee);
        tvgoals = findViewById(R.id.tv_height_trainee);
        tvstatus = findViewById(R.id.tv_weight_trainee);

        tvDate.setText(this.workout.getDate());
        tvTime.setText(this.workout.getHour());
        tvLocation.setText(this.workout.getLocation());
        tvgoals.setText(this.workout.getGoals());
        String accepted = "Pending";
        Boolean isAccepted = this.workout.getAccepted();
        if (isAccepted == null) {
            accepted = "Pending";
        }
        else if (isAccepted) {
            accepted = "Accepted";
        } else {
            accepted = "Rejected";
        }
        tvstatus.setText(accepted);

    }

    private void getNames() {
        this.databaseService.getTrainee(this.workout.getTraineeId(), new DatabaseService.DatabaseCallback<Trainee>() {
            @Override
            public void onCompleted(Trainee trainee) {
                tvTraineeName.setText(trainee.getFname() + " " + trainee.getLname());
            }

            @Override
            public void onFailed(Exception e) {

            }
        });

        this.databaseService.getCoach(this.workout.getCoachId(), new DatabaseService.DatabaseCallback<Coach>() {
            @Override
            public void onCompleted(Coach coach) {
                tvCoachName.setText(coach.getFname() + " " + coach.getLname());
            }

            @Override
            public void onFailed(Exception e) {

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
}
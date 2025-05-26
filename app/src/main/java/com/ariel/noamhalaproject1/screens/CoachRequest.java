package com.ariel.noamhalaproject1.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ariel.noamhalaproject1.R;
import com.ariel.noamhalaproject1.models.Coach;
import com.ariel.noamhalaproject1.models.Trainee;
import com.ariel.noamhalaproject1.models.User;
import com.ariel.noamhalaproject1.models.Workout;
import com.ariel.noamhalaproject1.services.AuthenticationService;
import com.ariel.noamhalaproject1.services.DatabaseService;

import java.util.ArrayList;

public class CoachRequest extends AppCompatActivity implements View.OnClickListener {

    Intent takeit;
    Coach coach=null;

    Button btnSubmitRequest;
    EditText etNameCoach, etNameTrainee, etGoals, etLocation;
    Spinner sphours;
    DatePicker datePicker;
    public User user = new User();

    private DatabaseService databaseService;
    private AuthenticationService authenticationService;
    private String uid;
    String goals, hour , date , location;
    Boolean status = null;

    ArrayList<Workout> coachWorkouts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_request); // ✅ Must come first

        authenticationService = AuthenticationService.getInstance();
        uid = authenticationService.getCurrentUserId();
        databaseService = DatabaseService.getInstance();

        initViews(); // sets up all view bindings

        // ✅ Spinner setup AFTER layout is loaded
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.hoursArr,
                R.layout.spinner_item_dark
        );
        adapter.setDropDownViewResource(R.layout.spinner_item_dark);
        sphours.setAdapter(adapter);
        sphours.setPopupBackgroundResource(R.drawable.spinner_popup_dark);

        databaseService.getTrainee(uid, new DatabaseService.DatabaseCallback<Trainee>() {
            @Override
            public void onCompleted(Trainee trainee) {
                Toast.makeText(CoachRequest.this, "Trainee " + trainee.toString(), Toast.LENGTH_SHORT).show();
                etNameTrainee.setText(trainee.getFname() + " " + trainee.getLname());
            }

            @Override
            public void onFailed(Exception e) {
            }
        });

        takeit = getIntent();
        coach = (Coach) takeit.getSerializableExtra("coach");

        if (coach != null) {
            etNameCoach.setText(coach.getFname() + " " + coach.getLname());
        }
    }


    private void initViews() {
        etNameTrainee = findViewById(R.id.etNameTrainee);
        etNameCoach = findViewById(R.id.etNameCoach);
        btnSubmitRequest = findViewById(R.id.btnSubmitRequest);
        etGoals = findViewById(R.id.etGoals);
        sphours = findViewById(R.id.sphours);
        etLocation = findViewById(R.id.etLocation);
        datePicker = findViewById(R.id.datePicker);
        btnSubmitRequest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String id = DatabaseService.getInstance().generateWorkoutId();
        String zero = "", zero2 = "";

        if ((datePicker.getMonth() + 1) < 10)
            zero = "0";
        if ((datePicker.getDayOfMonth()) < 10)
            zero2 = "0";

        String selectedDate = datePicker.getYear() + "/" + zero + (datePicker.getMonth() + 1) + "/" + zero2 + datePicker.getDayOfMonth();
        hour = sphours.getSelectedItem().toString();
        goals = etGoals.getText().toString();
        location = etLocation.getText().toString();
        String currentTrainee = AuthenticationService.getInstance().getCurrentUserId();

        // Create the Workout object
        final Workout workout = new Workout(id, currentTrainee, coach.getId(), selectedDate, hour, goals, location, null);

        databaseService.updateWorkout(workout, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void object) {
                Toast.makeText(CoachRequest.this, "Workout Request Submitted Successfully", Toast.LENGTH_SHORT).show();
                resetFields(); // Reset fields after submission
                Intent go = new Intent(CoachRequest.this, TraineeMainPage.class);
                startActivity(go);
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(CoachRequest.this, "Failed to submit request", Toast.LENGTH_SHORT).show();
            }
        });
    }


    // Reset the fields after submission
    private void resetFields() {
        etNameTrainee.setText("");
        etNameCoach.setText("");
        etGoals.setText("");
        etLocation.setText("");
        sphours.setSelection(0);  // Reset spinner to first item
        datePicker.updateDate(2025, 0, 1);  // Reset to default date (e.g., Jan 1, 2025)
    }
}

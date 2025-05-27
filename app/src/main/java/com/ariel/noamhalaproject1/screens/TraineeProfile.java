package com.ariel.noamhalaproject1.screens;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ariel.noamhalaproject1.R;
import com.ariel.noamhalaproject1.models.Trainee;
import com.ariel.noamhalaproject1.services.DatabaseService;
import com.google.android.material.button.MaterialButton;

public class TraineeProfile extends AppCompatActivity {

    private EditText etTraineeName, etTraineePhone, etTraineeEmail, etTraineeCity, etTraineeAge, etTraineeHeight, etTraineeWeight;
    private MaterialButton btnSaveDetails, btnViewHistory, btnDeleteUser;
    private DatabaseService databaseService;
    private String traineeId;
    private Trainee currentTrainee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainee_profile);

        // Initialize UI
        etTraineeName = findViewById(R.id.tv_trainee_name);
        etTraineePhone = findViewById(R.id.tv_phone_trainee);
        etTraineeEmail = findViewById(R.id.tv_email_trainee);
        etTraineeCity = findViewById(R.id.tv_city_trainee);
        etTraineeAge = findViewById(R.id.tv_age_trainee);
        etTraineeHeight = findViewById(R.id.tv_height_trainee);
        etTraineeWeight = findViewById(R.id.tv_weight_trainee);
        btnSaveDetails = findViewById(R.id.btn_save_details_trainee);
        btnViewHistory = findViewById(R.id.btn_history_trainee);
        btnDeleteUser = findViewById(R.id.btn_delete_user);

        // Initialize services
        databaseService = DatabaseService.getInstance();

        // Get traineeId from Intent
        traineeId = getIntent().getStringExtra("traineeId");

        if (traineeId == null || traineeId.isEmpty()) {
            Toast.makeText(this, "Invalid trainee ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Load trainee data
        databaseService.getTrainee(traineeId, new DatabaseService.DatabaseCallback<Trainee>() {
            @Override
            public void onCompleted(Trainee trainee) {
                if (trainee == null) {
                    Toast.makeText(TraineeProfile.this, "Trainee not found", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                currentTrainee = trainee;
                setView(trainee);
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(TraineeProfile.this, "Failed to load trainee", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void setView(Trainee trainee) {
        etTraineeName.setText(trainee.getFname() + " " + trainee.getLname());
        etTraineePhone.setText(trainee.getPhone());
        etTraineeEmail.setText(trainee.getEmail());
        etTraineeCity.setText(trainee.getCity());
        etTraineeAge.setText(String.valueOf(trainee.getAge()));
        etTraineeHeight.setText(String.valueOf(trainee.getHeight()));
        etTraineeWeight.setText(String.valueOf(trainee.getWeight()));

        // Save Button Listener
        btnSaveDetails.setOnClickListener(v -> {
            String name = etTraineeName.getText().toString().trim();
            String phone = etTraineePhone.getText().toString().trim();
            String email = etTraineeEmail.getText().toString().trim();
            String city = etTraineeCity.getText().toString().trim();
            String age = etTraineeAge.getText().toString().trim();
            String height = etTraineeHeight.getText().toString().trim();
            String weight = etTraineeWeight.getText().toString().trim();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(email)
                    || TextUtils.isEmpty(city) || TextUtils.isEmpty(age)
                    || TextUtils.isEmpty(height) || TextUtils.isEmpty(weight)) {
                Toast.makeText(TraineeProfile.this, "All fields must be filled!", Toast.LENGTH_SHORT).show();
                return;
            }

            String[] names = name.split(" ", 2);
            currentTrainee.setFname(names[0]);
            currentTrainee.setLname(names.length > 1 ? names[1] : "");
            currentTrainee.setPhone(phone);
            currentTrainee.setEmail(email);
            currentTrainee.setCity(city);
            currentTrainee.setAge(Integer.parseInt(age));
            currentTrainee.setHeight(Double.parseDouble(height));
            currentTrainee.setWeight(Double.parseDouble(weight));

            databaseService.updateTrainee(currentTrainee, new DatabaseService.DatabaseCallback<Void>() {
                @Override
                public void onCompleted(Void unused) {
                    Toast.makeText(TraineeProfile.this, "Trainee profile updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onFailed(Exception e) {
                    Toast.makeText(TraineeProfile.this, "Failed to update trainee", Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Delete Button Listener (set independently, NOT inside save button listener)
        btnDeleteUser.setOnClickListener(v -> {
            new androidx.appcompat.app.AlertDialog.Builder(TraineeProfile.this)
                    .setTitle("מחק משתמש")
                    .setMessage("אתה בטוח שברצונך למחוק את המשתמש? פעולה זו לא ניתנת לביטול.")
                    .setPositiveButton("מחק", (dialog, which) -> deleteUser())
                    .setNegativeButton("בטל", null)
                    .show();
        });

        // History Button Listener
        btnViewHistory.setOnClickListener(v -> {
            Intent intent = new Intent(TraineeProfile.this, GetTraineeSchedule.class);
            intent.putExtra("traineeId", traineeId);
            startActivity(intent);
        });
    }

    // deleteUser method OUTSIDE setView()
    private void deleteUser() {
        if (currentTrainee == null || traineeId == null) {
            Toast.makeText(this, "Error: No trainee loaded", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseService.deleteTrainee(traineeId, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void unused) {
                Toast.makeText(TraineeProfile.this, "המשתמש נמחק בהצלחה", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(TraineeProfile.this, "שגיאה במחיקת המשתמש", Toast.LENGTH_SHORT).show();
                Log.e("TraineeProfile", "Delete failed", e);
            }
        });
    }
} 
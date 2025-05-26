package com.ariel.noamhalaproject1.screens;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ariel.noamhalaproject1.R;
import com.ariel.noamhalaproject1.models.Coach;
import com.ariel.noamhalaproject1.services.AuthenticationService;
import com.ariel.noamhalaproject1.services.DatabaseService;

public class CoachProfile extends AppCompatActivity {

    private EditText etCoachName, etCoachPhone, etCoachEmail, etCoachCity, etCoachDomain, etCoachPrice, etCoachExperience;
    private com.google.android.material.button.MaterialButton btnSaveDetails, btnViewHistory;
    private DatabaseService databaseService;
    private String coachId;
    private Coach currentCoach;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_workout); // Ensure layout uses EditTexts with correct IDs

        // Initialize views
        etCoachName = findViewById(R.id.tv_coach_name);
        etCoachPhone = findViewById(R.id.tv_phone_coach);
        etCoachEmail = findViewById(R.id.tv_email_coach);
        etCoachCity = findViewById(R.id.tv_city_coach);
        etCoachDomain = findViewById(R.id.tv_domain_coach);
        etCoachPrice = findViewById(R.id.tv_price_coach);
        etCoachExperience = findViewById(R.id.tv_experience_coach);
        btnSaveDetails = findViewById(R.id.btn_save_details);
        btnViewHistory = findViewById(R.id.btn_history_coach);

        databaseService = DatabaseService.getInstance();
        coachId = AuthenticationService.getInstance().getCurrentUserId();

        if (coachId == null || coachId.isEmpty()) {
            Toast.makeText(this, "Invalid coach ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Load coach data
        databaseService.getCoach(coachId, new DatabaseService.DatabaseCallback<Coach>() {
            @Override
            public void onCompleted(Coach coach) {
                if (coach == null) {
                    Toast.makeText(CoachProfile.this, "Coach not found", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                currentCoach = coach;
                setView(coach);
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(CoachProfile.this, "Failed to load coach", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void setView(Coach coach) {
        etCoachName.setText(coach.getFname() + " " + coach.getLname());
        etCoachPhone.setText(coach.getPhone());
        etCoachEmail.setText(coach.getEmail());
        etCoachCity.setText(coach.getCity());
        etCoachDomain.setText(coach.getDomain());
        etCoachPrice.setText(String.valueOf(coach.getPrice()));
        etCoachExperience.setText(String.valueOf(coach.getExperience()));

        btnSaveDetails.setOnClickListener(v -> {
            String name = etCoachName.getText().toString().trim();
            String phone = etCoachPhone.getText().toString().trim();
            String email = etCoachEmail.getText().toString().trim();
            String city = etCoachCity.getText().toString().trim();
            String domain = etCoachDomain.getText().toString().trim();
            String price = etCoachPrice.getText().toString().trim();
            String experience = etCoachExperience.getText().toString().trim();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(email) ||
                    TextUtils.isEmpty(city) || TextUtils.isEmpty(domain) ||
                    TextUtils.isEmpty(price) || TextUtils.isEmpty(experience)) {
                Toast.makeText(CoachProfile.this, "All fields must be filled!", Toast.LENGTH_SHORT).show();
                return;
            }

            String[] names = name.split(" ", 2);
            currentCoach.setFname(names[0]);
            currentCoach.setLname(names.length > 1 ? names[1] : "");
            currentCoach.setPhone(phone);
            currentCoach.setEmail(email);
            currentCoach.setCity(city);
            currentCoach.setDomain(domain);
            currentCoach.setPrice(Double.parseDouble(price));
            currentCoach.setExperience(Integer.parseInt(experience));

            databaseService.updateCoach(currentCoach, new DatabaseService.DatabaseCallback<Void>() {
                @Override
                public void onCompleted(Void unused) {
                    Toast.makeText(CoachProfile.this, "Coach profile updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onFailed(Exception e) {
                    Toast.makeText(CoachProfile.this, "Failed to update coach", Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnViewHistory.setOnClickListener(v -> {
            Intent intent = new Intent(CoachProfile.this, GetCoachSchedule.class);
            intent.putExtra("coachId", coachId); // Pass the coach ID to the schedule screen
            startActivity(intent);
        });

    }
}

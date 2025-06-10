package com.ariel.noamhalaproject1.screens;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ariel.noamhalaproject1.R;
import com.ariel.noamhalaproject1.models.Coach;
import com.ariel.noamhalaproject1.services.AuthenticationService;
import com.ariel.noamhalaproject1.services.DatabaseService;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

public class CoachProfile extends AppCompatActivity {

    private EditText etCoachName, etCoachPhone, etCoachEmail, etCoachCity, etCoachDomain, etCoachPrice, etCoachExperience;
    private MaterialButton btnSaveDetails, btnViewHistory, btn_delete_user;
    private DatabaseService databaseService;
    private String coachId;
    private Coach currentCoach;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_profile); // Use your corrected layout file here

        // Update IDs based on the corrected XML layout
        etCoachName = findViewById(R.id.tv_coach_name);
        etCoachPhone = findViewById(R.id.tv_phone_coach);
        etCoachEmail = findViewById(R.id.tv_email_coach);
        etCoachCity = findViewById(R.id.tv_city_coach);
        etCoachDomain = findViewById(R.id.tv_domain_coach);
        etCoachPrice = findViewById(R.id.tv_price_coach);
        etCoachExperience = findViewById(R.id.tv_experience_coach);
        btnSaveDetails = findViewById(R.id.btn_save_details);
        btnViewHistory = findViewById(R.id.btn_history_coach);
        btn_delete_user = findViewById(R.id.btn_delete_user);

        coachId = getIntent().getStringExtra("coachId");
        if (coachId == null || coachId.isEmpty()) {
            coachId = AuthenticationService.getInstance().getCurrentUserId();
        }

        databaseService = DatabaseService.getInstance();

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
            intent.putExtra("coachId", coachId);
            startActivity(intent);
        });

        btn_delete_user.setOnClickListener(v -> {
            Intent intent = new Intent(CoachProfile.this, GetCoachSchedule.class);
            intent.putExtra("coachId", coachId);
            startActivity(intent);
        });

    }

    private void deleteUser() {
        if (currentCoach == null || coachId == null) {
            Toast.makeText(this, "Error: No coach loaded", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseService.deleteCoach(coachId, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void unused) {
                Toast.makeText(CoachProfile.this, "המשתמש נמחק בהצלחה", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(CoachProfile.this, "שגיאה במחיקת המשתמש", Toast.LENGTH_SHORT).show();
                Log.e("CoachProfile", "Delete failed", e);
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

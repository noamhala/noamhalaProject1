package com.ariel.noamhalaproject1.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ariel.noamhalaproject1.R;
import com.ariel.noamhalaproject1.models.Coach;
import com.ariel.noamhalaproject1.services.AuthenticationService;
import com.ariel.noamhalaproject1.services.DatabaseService;

public class AddDetailsCoach extends AppCompatActivity {

    private static final String TAG = "AddDetailsCoach";

    // Declare views
    private EditText etDomain, etPrice, etExperience;
    private Button btnFinishCoach;

    // Variables to hold coach details
    private String domain;
    private double price;
    private int experience;

    private Coach currentCoach; // The coach object passed from Register activity
    private DatabaseService databaseService;
    private AuthenticationService authenticationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_details_coach);

        // Set up system bars inset handling (padding for system UI)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize services
        databaseService = DatabaseService.getInstance();
        authenticationService = AuthenticationService.getInstance();

        // Initialize views
        initViews();

        // Get the coach object from the intent
        Intent intent = getIntent();
        currentCoach = (Coach) intent.getSerializableExtra("coach");

        // Set OnClickListener for the finish button
        btnFinishCoach.setOnClickListener(view -> finishCoachRegistration());
    }

    private void initViews() {
        etDomain = findViewById(R.id.etDomain); // Domain of expertise (e.g., fitness, yoga, etc.)
        etPrice = findViewById(R.id.etPrice);   // Price per session
        etExperience = findViewById(R.id.etExperience); // Years of experience
        btnFinishCoach = findViewById(R.id.btnFinishCoach);
    }

    private void finishCoachRegistration() {
        // Get values from input fields
        String domainStr = etDomain.getText().toString();
        String priceStr = etPrice.getText().toString();
        String experienceStr = etExperience.getText().toString();

        // Validate inputs
        if (domainStr.isEmpty() || priceStr.isEmpty() || experienceStr.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            domain = domainStr;
            price = Double.parseDouble(priceStr);
            experience = Integer.parseInt(experienceStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update the currentCoach object with additional details
        currentCoach.setTypeUser("מאמן"); // Ensure the user type is set correctly
        currentCoach.setCity(domain); // Use the domain as part of additional info (if applicable)

        // Save the coach to the database
        registerCoach();
    }

    private void registerCoach() {
        Log.d(TAG, "registerCoach: Registering coach...");

        databaseService.createNewCoach(currentCoach, new DatabaseService.DatabaseCallback<Void>() {

            @Override
            public void onCompleted(Void object) {
                Log.d(TAG, "onCompleted: Coach registered successfully");
                Toast.makeText(AddDetailsCoach.this, "Coach details saved!", Toast.LENGTH_SHORT).show();

                // Redirect to MainActivity and clear back stack to prevent user from returning to the register screen
                Intent mainIntent = new Intent(AddDetailsCoach.this, CoachMainPage.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainIntent);
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "onFailed: Failed to register coach", e);
                Toast.makeText(AddDetailsCoach.this, "Failed to save coach details", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

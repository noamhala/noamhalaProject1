package com.ariel.noamhalaproject1.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.ariel.noamhalaproject1.models.User;

public class AddDetailsCoach extends AppCompatActivity {

    // Declare views
    private EditText etDomain, etPrice, etExperience;
    private Button btnFinishCoach;

    // Variables to hold coach details
    private double price;
    private int experience;
    private String domain;

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

        // Initialize views
        initViews();

        // Set OnClickListener for button
        btnFinishCoach.setOnClickListener(view -> finishCoachRegistration());
    }

    private void initViews() {
        etDomain = findViewById(R.id.editTextText);  // For Domain
        etPrice = findViewById(R.id.etPrice);  // For Price per workout
        etExperience = findViewById(R.id.etExperience);  // For Years of experience
        btnFinishCoach = findViewById(R.id.btnFinishTrainee);  // Button ID may vary, adjust accordingly
    }

    private void finishCoachRegistration() {
        // Get the values from the EditText fields
        String domainStr = etDomain.getText().toString();
        String priceStr = etPrice.getText().toString();
        String experienceStr = etExperience.getText().toString();

        // Validate the input
        if (domainStr.isEmpty() || priceStr.isEmpty() || experienceStr.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Convert input to appropriate types
            price = Double.parseDouble(priceStr);
            experience = Integer.parseInt(experienceStr);
            domain = domainStr;
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
            return;
        }

        // Assuming we have the user's coach (this can be passed from the previous activity or inferred)
        User myCoach = getCoach();  // You need to implement or get this User object

        // Create a Coach object with the gathered details
     //   Coach coach = new Coach(myCoach.getId(), myCoach.getFname(), myCoach.getLname(),
    //                myCoach.getPhone(), myCoach.getEmail(), myCoach.getPass(),
     //           myCoach.getGender(), myCoach.getCity(), domain, experience, price);

        // Proceed with storing or using the coach data (e.g., saving to the database, etc.)
        // For now, we will simply display a Toast and move to another screen
        Toast.makeText(this, "Coach details saved", Toast.LENGTH_SHORT).show();

        // Example of transitioning to another screen after successful registration
      //  Intent intent = new Intent(AddDetailsCoach.this, NextActivity.class);  // Replace with your desired activity
   //     startActivity(intent);
    }

    // You can implement this method to retrieve the actual coach, possibly from previous activity's intent or shared preferences
    private User getCoach() {
        // Placeholder method to simulate getting the coach, you should pass this data when necessary
        // For example, if you are passing the coach data from the previous activity via Intent:
        Intent intent = getIntent();
        return (User) intent.getSerializableExtra("coach");  // Get coach from Intent extras
    }
}

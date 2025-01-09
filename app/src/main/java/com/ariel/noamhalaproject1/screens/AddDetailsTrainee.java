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
import com.ariel.noamhalaproject1.models.Trainee;
import com.ariel.noamhalaproject1.models.User;

public class AddDetailsTrainee extends AppCompatActivity {

    // Declare views
    private EditText etWeight, etHeight, etAge;
    private Button btnFinishTrainee;

    // Variables to hold trainee details
    private double height, weight;
    private int age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_details_trainee);

        // Set up system bars inset handling (padding for system UI)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        initViews();

        // Set OnClickListener for button
        btnFinishTrainee.setOnClickListener(view -> finishTraineeRegistration());
    }

    private void initViews() {
        etWeight = findViewById(R.id.etWeight);  // Correct ID
        etHeight = findViewById(R.id.etHeight);  // Correct ID
        etAge = findViewById(R.id.etAge);  // Correct ID (updated from editTextNumber3 to etAge)
        btnFinishTrainee = findViewById(R.id.btnFinishTrainee);
    }

    private void finishTraineeRegistration() {
        // Get the values from the EditText fields
        String weightStr = etWeight.getText().toString();
        String heightStr = etHeight.getText().toString();
        String ageStr = etAge.getText().toString();

        // Validate the input
        if (weightStr.isEmpty() || heightStr.isEmpty() || ageStr.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Convert input to appropriate types
            weight = Double.parseDouble(weightStr);
            height = Double.parseDouble(heightStr);
            age = Integer.parseInt(ageStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
            return;
        }

        // Assuming we have the user's coach (this can be passed from the previous activity or inferred)
        User myCoach = getCoach();  // You need to implement or get this User object

        // Create a Trainee object
        Trainee trainee = new Trainee(weight, height, age, myCoach);

        // Proceed with storing or using the trainee data (e.g., saving to the database, etc.)
        // For now, we will simply display a Toast and move to another screen
        Toast.makeText(this, "Trainee details saved", Toast.LENGTH_SHORT).show();

        // Example of transitioning to another screen after successful registration
        Intent intent = new Intent(AddDetailsTrainee.this, NextActivity.class);  // Replace with your desired activity
        startActivity(intent);
    }

    // You can implement this method to retrieve the actual coach, possibly from previous activity's intent or shared preferences
    private User getCoach() {
        // Placeholder method to simulate getting the coach, you should pass this data when necessary
        return new User("coach_id", "Coach", "Name", "123456789", "coach@example.com", "password", "Male", "City");
    }
}

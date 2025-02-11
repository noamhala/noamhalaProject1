package com.ariel.noamhalaproject1.screens;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ariel.noamhalaproject1.R;

public class CoachRequest extends AppCompatActivity {

    // Declare the UI components
    private EditText etTime, etGoals, etDuration, etLocation;
    private Spinner spIntensityLevel;
    private Button btnSubmitRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_coach_request);

        // Initialize the UI components
        etTime = findViewById(R.id.etTime);
        etGoals = findViewById(R.id.etGoals);
        etDuration = findViewById(R.id.etDuration);
        etLocation = findViewById(R.id.etLocation);
        spIntensityLevel = findViewById(R.id.spIntensityLevel);
        btnSubmitRequest = findViewById(R.id.btnSubmitRequest);

        // Handle system window insets (for edge-to-edge support)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Submit button click listener
        btnSubmitRequest.setOnClickListener(v -> handleSubmitRequest());
    }

    private void handleSubmitRequest() {
        // Get the data from the input fields
        String time = etTime.getText().toString().trim();
        String goals = etGoals.getText().toString().trim();
        String duration = etDuration.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String intensityLevel = spIntensityLevel.getSelectedItem().toString();

        // Validate inputs
        if (time.isEmpty() || goals.isEmpty() || duration.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        } else {
            // Here, you can handle sending the data to the server or other functionality
            // For now, we just display a Toast as a placeholder for the submission

            String requestDetails = "Time: " + time + "\n" +
                    "Goals: " + goals + "\n" +
                    "Duration: " + duration + "\n" +
                    "Location: " + location + "\n" +
                    "Intensity: " + intensityLevel;

            // Show the details in a Toast for now (replace with actual network request)
            Toast.makeText(this, "Request Submitted:\n" + requestDetails, Toast.LENGTH_LONG).show();

            // Optionally, clear the fields after submission
            clearFields();
        }
    }

    private void clearFields() {
        etTime.setText("");
        etGoals.setText("");
        etDuration.setText("");
        etLocation.setText("");
        spIntensityLevel.setSelection(0);  // Reset the spinner selection to the first item
    }
}

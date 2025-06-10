        package com.ariel.noamhalaproject1.screens;

        import android.content.Intent;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.LinearLayout;
        import android.widget.Spinner;
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
        import com.google.firebase.auth.FirebaseAuth;

        public class AddDetailsCoach extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

            private static final String TAG = "AddDetailsCoach";

            // Declare views
            private EditText etDomain, etPrice, etExperience;
            private Button btnFinishCoach;

            // Variables to hold coach details
            Spinner spCoachDomain;
            private double price;
            protected String Domain = "";
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
                spCoachDomain = findViewById(R.id.spCDomain);
                spCoachDomain.setOnItemSelectedListener(this);

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
                    price = Double.parseDouble(priceStr);
                    experience = Integer.parseInt(experienceStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Update the currentCoach object with additional details
                currentCoach.setTypeUser("מאמן");// Ensure the user type is set correctly
                currentCoach.setExperience(experience); // Set the experience
                currentCoach.setPrice(price);// Set the price
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
            // Method to add a new Spinner dynamically
            private void addNewSpinner() {
                // Create a new Spinner instance
                Spinner newSpinner = new Spinner(this);

                // Create an ArrayAdapter for the new spinner
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                        R.array.DomainArr, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // Set the adapter to the new spinner
                newSpinner.setAdapter(adapter);

                // Create layout parameters for the new Spinner
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 20, 0, 0);  // Optional: add margin between spinners

                // Set the layout parameters for the new spinner
                newSpinner.setLayoutParams(params);
            }

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    // Get the selected domain directly
                    String selectedDomain = parent.getItemAtPosition(position).toString();

                    // Update the EditText with the selected domain
                    etDomain.setText(selectedDomain);

                    // Save the selected domain to the currentCoach object
                    currentCoach.setDomain(selectedDomain);  // Set the selected domain in the Coach object
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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


    package com.ariel.noamhalaproject1.screens;

    import android.content.Context;
    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.View;
    import android.widget.AdapterView;
    import android.widget.ArrayAdapter;
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
    import com.ariel.noamhalaproject1.models.Coach;
    import com.ariel.noamhalaproject1.models.Trainee;
    import com.ariel.noamhalaproject1.models.User;
    import com.ariel.noamhalaproject1.models.Workout;
    import com.ariel.noamhalaproject1.services.AuthenticationService;
    import com.ariel.noamhalaproject1.services.DatabaseService;
    import com.ariel.noamhalaproject1.utils.SharedPreferencesUtil;

    import java.util.HashMap;

    public class Register extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

        private static final String TAG = "RegisterActivity";

        private EditText etFname, etLname, etPhone, etEmail, etPass;
        private String fname, lname, phone, email, password;
        private Button btnRegister;



        private AuthenticationService authenticationService;
        private DatabaseService databaseService;

        String city, TypeUser; // TypeUser is the user type selected from the spinner
        Spinner spCity, spTypeUser;



        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_register);

            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            // Services
            authenticationService = AuthenticationService.getInstance();
            databaseService = DatabaseService.getInstance();

            // View bindings
            initViews();

            // 🟦 FIX: Set custom adapters with white text and dark dropdown
            ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(
                    this,
                    R.array.spTypeUser,
                    R.layout.spinner_item_dark  // white text layout
            );
            typeAdapter.setDropDownViewResource(R.layout.spinner_item_dark);
            spTypeUser.setAdapter(typeAdapter);
            spTypeUser.setPopupBackgroundResource(R.drawable.spinner_popup_dark);

            ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(
                    this,
                    R.array.cityArr,
                    R.layout.spinner_item_dark
            );
            cityAdapter.setDropDownViewResource(R.layout.spinner_item_dark);
            spCity.setAdapter(cityAdapter);
            spCity.setPopupBackgroundResource(R.drawable.spinner_popup_dark);

            // Listeners
            btnRegister.setOnClickListener(this);
        }


        private void initViews() {
            etEmail = findViewById(R.id.etEmail);
            etFname = findViewById(R.id.etFname);
            etLname = findViewById(R.id.etLname);
            etPhone = findViewById(R.id.etPhone);
            etPass = findViewById(R.id.etPass);
            spCity = findViewById(R.id.spCity);
            spTypeUser = findViewById(R.id.spTypeUser);  // Spinner for user type
            btnRegister = findViewById(R.id.btnRegister);

            // Set item selected listener for both spinners
            spTypeUser.setOnItemSelectedListener(this); // Spinner for user type
            spCity.setOnItemSelectedListener(this); // Spinner for city (if needed)
        }

        @Override
        public void onClick(View view) {
            // Retrieve input values
            fname = etFname.getText().toString();
            lname = etLname.getText().toString();
            phone = etPhone.getText().toString();
            email = etEmail.getText().toString();
            password = etPass.getText().toString();

            // Validate input
            if (!email.contains("@")) {
                etEmail.setError("Enter a correct email");
            }

            Boolean isValid = true;
            if (fname.length() < 2) {
                etFname.setError("First name is too short");
                isValid = false;
            }
            if (lname.length() < 2) {
                Toast.makeText(Register.this, "Last name is too short", Toast.LENGTH_LONG).show();
                isValid = false;
            }
            if (phone.length() < 9 || phone.length() > 10) {
                Toast.makeText(Register.this, "Invalid phone number", Toast.LENGTH_LONG).show();
                isValid = false;
            }
            if (!email.contains("@")) {
                Toast.makeText(Register.this, "Invalid email address", Toast.LENGTH_LONG).show();
                isValid = false;
            }
            if (password.length() < 6) {
                Toast.makeText(Register.this, "Password is too short", Toast.LENGTH_LONG).show();
                isValid = false;
            }
            if (password.length() > 20) {
                Toast.makeText(Register.this, "Password is too long", Toast.LENGTH_LONG).show();
                isValid = false;
            }
            if (isValid) {
                // Check if the selected TypeUser is "Trainer" or "Trainee"


                    // Proceed with the usual registration flow

                    authenticationService.signUp(email, password, new AuthenticationService.AuthCallback<String>() {
                        @Override
                        public void onCompleted(String id) {
                            // Sign-up success, proceed to save user to the database
                            Log.d("TAG", "createUserWithEmail:success");
                            User newUser = new User(id, fname, lname, phone, email, password, city, TypeUser, new HashMap<>());

                            SharedPreferencesUtil.saveUser(Register.this, newUser);

                            if (TypeUser.equals("מאמן")) {
                                // Create a new Coach object with placeholder values for additional fields

                                Coach newCoach = new Coach(newUser, "domain", 0.0, 0);
                                databaseService.createNewCoach(newCoach, new DatabaseService.DatabaseCallback<Void>() {
                                    @Override
                                    public void onCompleted(Void object) {
                                        // Redirect to AddDetailsCoach activity, passing the Coach object
                                        Intent go = new Intent(getApplicationContext(), AddDetailsCoach.class);
                                        go.putExtra("coach", newCoach); // Pass the Coach object to the next activity
                                        startActivity(go);
                                    }
                                    @Override
                                    public void onFailed(Exception e) {

                                    }
                                });
                            } else if (TypeUser.equals("מתאמן")) {
                                // Create a new Trainee object with placeholder values for additional fields
                                Trainee newTrain = new Trainee(newUser, 0, 0, 0, null);
                                databaseService.createNewTrainee(newTrain, new DatabaseService.DatabaseCallback<Void>() {
                                    @Override
                                    public void onCompleted(Void object) {
                                        // Redirect to AddDetailsTrainee activity, passing the Trainee object
                                        Intent go = new Intent(getApplicationContext(), AddDetailsTrainee.class);
                                        go.putExtra("trainee", newTrain); // Pass the Trainee object to the next activity
                                        startActivity(go);
                                    }
                                    @Override
                                    public void onFailed(Exception e) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailed(Exception e) {
                            // Sign-up failed
                            Log.w("TAG", "createUserWithEmail:failure", e);
                            Toast.makeText(Register.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
        }

        @Override
        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
            // Handle selection for spCity and spTypeUser
            if (parentView.getId() == R.id.spTypeUser) {
                // Capture selected user type
                TypeUser = parentView.getItemAtPosition(position).toString();  // Store the selected user type
            } else if (parentView.getId() == R.id.spCity) {
                // Capture selected city (if needed)
                city = parentView.getItemAtPosition(position).toString();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            // Handle if no item is selected (optional)
        }

    }

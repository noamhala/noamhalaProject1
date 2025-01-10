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
import com.ariel.noamhalaproject1.models.Trainee;
import com.ariel.noamhalaproject1.models.User;
import com.ariel.noamhalaproject1.services.AuthenticationService;
import com.ariel.noamhalaproject1.services.DatabaseService;

public class Register extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = "RegisterActivity";

    private EditText etFname, etLname, etPhone, etEmail, etPass;
    private String fname, lname, phone, email, password;
    private Button btnRegister;

    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;

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

        // Initialize services
        authenticationService = AuthenticationService.getInstance();
        databaseService = DatabaseService.getInstance();
        initViews();

        // SharedPreferences initialization
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        // Set onClick listener
        btnRegister.setOnClickListener(this);

        // Set up the Spinner adapter for spTypeUser (user type selection)
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spTypeUser, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTypeUser.setAdapter(adapter);
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
                        User newUser = new User(id, fname, lname, phone, email, password, city, TypeUser);
                        databaseService.createNewUser(newUser, new DatabaseService.DatabaseCallback<Void>() {
                            @Override
                            public void onCompleted(Void object) {
                                SharedPreferences.Editor editor = sharedpreferences.edit();

                                editor.putString("email", email);
                                editor.putString("password", password);
                                editor.commit();


                                if (TypeUser.equals("מאמן")) {
                                    // Go to AddDetails activity if user is Trainer or Trainee
                                    Intent go = new Intent(getApplicationContext(), AddDetailsCoach.class);

                                    startActivity(go);
                                } else if(TypeUser.equals("מתאמן")) {
                                    Trainee newTrain=new Trainee(newUser, 0,0,0,null);
                                    Intent go = new Intent(getApplicationContext(), AddDetailsTrainee.class);
                                        go.putExtra("trainee",newTrain);
                                    startActivity(go);
                                }


                            }

                            @Override
                            public void onFailed(Exception e) {
                                // Handle failure
                            }
                        });
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

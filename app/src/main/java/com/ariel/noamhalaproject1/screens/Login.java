package com.ariel.noamhalaproject1.screens;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ariel.noamhalaproject1.R;


import com.ariel.noamhalaproject1.models.Coach;
import com.ariel.noamhalaproject1.models.Trainee;
import com.ariel.noamhalaproject1.models.User;
import com.ariel.noamhalaproject1.services.AuthenticationService;
import com.ariel.noamhalaproject1.services.DatabaseService;
import com.ariel.noamhalaproject1.utils.SharedPreferencesUtil;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private EditText etEmail, etPassword;
    private Button btnLog;

    private AuthenticationService authenticationService;
    private DatabaseService databaseService;

    private User user;
    private static final String TAG = "LoginActivity";
    private Trainee trainee2=null;
    private Coach coach=null;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Edge-to-edge handling
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        /// get the instance of the authentication service
        authenticationService = AuthenticationService.getInstance();
        /// get the instance of the database service
        databaseService = DatabaseService.getInstance();

        // Initialize views
        initViews();
        // Pre-fill saved credentials
        loadSavedCredentials();
    }

    private void initViews() {
        etEmail = findViewById(R.id.etEmailLogin);
        etPassword = findViewById(R.id.etPasswordLogin);
        btnLog = findViewById(R.id.btnLogLogin);
        btnLog.setOnClickListener(this);
    }

    private void loadSavedCredentials() {
        user = SharedPreferencesUtil.getUser(Login.this);
        if(user!=null) {

            String email = user.getEmail();
            String pass = user.getPassword();
            etEmail.setText(email);
            etPassword.setText(pass);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnLogLogin) {
            String email = etEmail.getText().toString().trim();
            String pass = etPassword.getText().toString().trim();

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Please enter both email and password.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Attempt sign-in
         else    loginUser(email, pass);
        }
    }

    private void loginUser(String email, String password) {
        authenticationService.signIn(email, password, new AuthenticationService.AuthCallback<String>() {
            /// Callback method called when the operation is completed
            /// @param uid the user ID of the user that is logged in
            @Override
            public void onCompleted(String id) {
                uid=id;
                Log.d(TAG, "onCompleted: User logged in successfully");
                /// get the user data from the database
                databaseService.getTrainee(uid, new DatabaseService.DatabaseCallback<Trainee>() {
                    @Override
                    public void onCompleted(Trainee trainee) {

                       trainee2=trainee;
                        Log.d(TAG, "onCompleted: User data retrieved successfully");
                        /// save the user data to shared preferences
                       SharedPreferencesUtil.saveUser(Login.this, trainee2);
                        /// Redirect to main activity and clear back stack to prevent user from going back to login screen
                        Intent mainIntent = new Intent(Login.this, TraineeMainPage.class);
                        /// Clear the back stack (clear history) and start the MainActivity
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        if(trainee2!=null)
                             startActivity(mainIntent);

                        else {

                            databaseService.getCoach(uid, new DatabaseService.DatabaseCallback<Coach>() {

                                @Override
                                public void onCompleted(Coach object) {

                                    coach=object;
                                    Log.d(TAG, "onCompleted: User data retrieved successfully");
                                    /// save the user data to shared preferences
                                    SharedPreferencesUtil.saveUser(Login.this, coach);

                                    /// Redirect to main activity and clear back stack to prevent user from going back to login screen
                                    Intent mainIntent = new Intent(Login.this, CoachMainPage.class);
                                    /// Clear the back stack (clear history) and start the MainActivity
                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(mainIntent);

                                }

                                @Override
                                public void onFailed(Exception e) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onFailed(Exception e) {
                        trainee2=null;

                    }
                });
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "onFailed: Failed to retrieve user data", e);
                /// Show error message to user
                etPassword.setError("Invalid email or password");
                etPassword.requestFocus();
                /// Sign out the user if failed to retrieve user data
                /// This is to prevent the user from being logged in again
                authenticationService.signOut();
                Log.e(TAG, "onFailed: Failed to log in user", e);
                /// Show error message to user
                etPassword.setError("Invalid email or password");
                etPassword.requestFocus();
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
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}











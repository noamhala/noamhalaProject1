package com.ariel.noamhalaproject1.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class Login extends AppCompatActivity implements View.OnClickListener {

    private EditText etEmail, etPassword;
    private Button btnLog;
    private AuthenticationService authenticationService;
    private DatabaseService databaseService;

    private static final String ADMIN_EMAIL = "noamhala@gmail.com";
    private static final String TAG = "LoginActivity";

    public static boolean isAdmin = false;

    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        authenticationService = AuthenticationService.getInstance();
        databaseService = DatabaseService.getInstance();

        initViews();
        loadSavedCredentials();
    }

    private void initViews() {
        etEmail = findViewById(R.id.etEmailLogin);
        etPassword = findViewById(R.id.etPasswordLogin);
        btnLog = findViewById(R.id.btnLogLogin);
        btnLog.setOnClickListener(this);
    }

    private void loadSavedCredentials() {
        User user = SharedPreferencesUtil.getUser(Login.this);
        if (user != null) {
            etEmail.setText(user.getEmail());
            etPassword.setText(user.getPassword());
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

            loginUser(email, pass);
        }
    }

    private void loginUser(String email, String password) {
        authenticationService.signIn(email, password, new AuthenticationService.AuthCallback<String>() {
            @Override
            public void onCompleted(String id) {
                uid = id;
                isAdmin = email.equalsIgnoreCase(ADMIN_EMAIL);

                if (isAdmin) {
                    startActivity(new Intent(Login.this, AdminPage.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    return; // prevent continuing to Trainee/Coach lookup
                }

                // First try to get Trainee
                databaseService.getTrainee(uid, new DatabaseService.DatabaseCallback<Trainee>() {
                    @Override
                    public void onCompleted(Trainee trainee) {
                        if (trainee != null) {
                            SharedPreferencesUtil.saveUser(Login.this, trainee);
                            Intent mainIntent = new Intent(Login.this, TraineeMainPage.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainIntent);
                        } else {
                            // Try Coach
                            databaseService.getCoach(uid, new DatabaseService.DatabaseCallback<Coach>() {
                                @Override
                                public void onCompleted(Coach coach) {
                                    if (coach != null) {
                                        SharedPreferencesUtil.saveUser(Login.this, coach);
                                        Intent mainIntent = new Intent(Login.this, CoachMainPage.class);
                                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(mainIntent);
                                    } else {
                                        Toast.makeText(Login.this, "User not found.", Toast.LENGTH_SHORT).show();
                                        authenticationService.signOut();
                                    }
                                }

                                @Override
                                public void onFailed(Exception e) {
                                    Log.e(TAG, "Coach fetch failed", e);
                                    Toast.makeText(Login.this, "Error fetching coach.", Toast.LENGTH_SHORT).show();
                                    authenticationService.signOut();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailed(Exception e) {
                        Log.e(TAG, "Trainee fetch failed", e);
                        Toast.makeText(Login.this, "Error fetching trainee.", Toast.LENGTH_SHORT).show();
                        authenticationService.signOut();
                    }
                });
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "Login failed", e);
                etPassword.setError("Invalid email or password");
                etPassword.requestFocus();
                authenticationService.signOut();
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
            startActivity(new Intent(getApplicationContext(), About.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

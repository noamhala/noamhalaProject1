package com.ariel.noamhalaproject1.screens;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ariel.noamhalaproject1.R;
import com.ariel.noamhalaproject1.models.User;
import com.ariel.noamhalaproject1.services.AuthenticationService;
import com.ariel.noamhalaproject1.services.DatabaseService;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileUser extends AppCompatActivity {

    private EditText etEditUserName, etEditUserEmail, etEditUserPhone;
    private Button btnSaveUser;
    private DatabaseService databaseService;
    private String userId;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);

        // Initialize UI components
        etEditUserName = findViewById(R.id.etEditUserName);
        etEditUserEmail = findViewById(R.id.etEditUserEmail);
        etEditUserPhone = findViewById(R.id.etEditUserPhone);
        btnSaveUser = findViewById(R.id.btnSaveUser);

        // Initialize database service
        databaseService = DatabaseService.getInstance();

        AuthenticationService.getInstance();

        // Get user ID from the Intent
        //userId = getIntent().getStringExtra("userId");
        userId = AuthenticationService.getInstance().getCurrentUserId();

        if (userId == null || userId.isEmpty()) {
            // Handle invalid user ID case
            Toast.makeText(this, "Invalid user ID", Toast.LENGTH_SHORT).show();
            finish(); // Exit activity if user ID is invalid
            return;
        }

        // Fetch user data from database
        databaseService.getUser(userId, new DatabaseService.DatabaseCallback<User>() {
            @Override
            public void onCompleted(User user) {
                if (user == null) {
                    // Handle the case where the user is not found in the database
                    Toast.makeText(ProfileUser.this, "User not found", Toast.LENGTH_SHORT).show();
                    finish(); // Exit activity if the user doesn't exist
                    return;
                }

                currentUser = user;
                setView(user); // Proceed with setting the view if user is found
            }

            @Override
            public void onFailed(Exception e) {
                // Handle database failure case
                Toast.makeText(ProfileUser.this, "Failed to load user", Toast.LENGTH_SHORT).show();
                finish(); // Exit activity if the database operation failed
            }
        });
    }

    private void setView(User user) {
        // Only set values if user is not null (added extra check here)
        if (user != null) {
            etEditUserName.setText(user.getFname() + " " + user.getLname());
            etEditUserEmail.setText(user.getEmail());
            etEditUserPhone.setText(user.getPhone());
        }

        // Set click listener for save button
        btnSaveUser.setOnClickListener(v -> {
            String updatedName = etEditUserName.getText().toString().trim();
            String updatedEmail = etEditUserEmail.getText().toString().trim();
            String updatedPhone = etEditUserPhone.getText().toString().trim();

            if (TextUtils.isEmpty(updatedName) || TextUtils.isEmpty(updatedEmail) || TextUtils.isEmpty(updatedPhone)) {
                Toast.makeText(ProfileUser.this, "All fields must be filled!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Update the currentUser object with the new data
            currentUser.setFname(updatedName.split(" ")[0]);
            currentUser.setLname(updatedName.split(" ")[1]);
            currentUser.setEmail(updatedEmail);
            currentUser.setPhone(updatedPhone);

            // Save the updated user data in the database
            databaseService.updateUser(currentUser, new DatabaseService.DatabaseCallback<Void>() {
                @Override
                public void onCompleted(Void v) {
                    Toast.makeText(ProfileUser.this, "User updated successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Finish the activity after successful update
                }

                @Override
                public void onFailed(Exception e) {
                    Toast.makeText(ProfileUser.this, "Failed to update user", Toast.LENGTH_SHORT).show();
                }
            });
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

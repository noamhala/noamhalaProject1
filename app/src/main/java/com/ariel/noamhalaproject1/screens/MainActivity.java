package com.ariel.noamhalaproject1.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ariel.noamhalaproject1.R;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


    }
    public void goReg(View view) {
        // Create an Intent to go to the Register activity
        Intent go = new Intent(getApplicationContext(), Register.class);
        // Start the Register activity
        startActivity(go);
    }
    public void goLogin(View view) {
        // Create an Intent to go to the Login activity
        Intent go = new Intent(getApplicationContext(), Login.class);
        // Start the Login activity
        startActivity(go);
    }




    public void goAbout(View view) {
        // Create an Intent to go to the Login activity
        Intent go = new Intent(getApplicationContext(), About.class);
        // Start the Login activity
        startActivity(go);
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

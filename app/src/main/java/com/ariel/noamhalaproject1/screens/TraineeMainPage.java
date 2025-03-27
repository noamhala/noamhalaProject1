package com.ariel.noamhalaproject1.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ariel.noamhalaproject1.R;

public class TraineeMainPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trainee_main_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void goListCoach(View view) {
        // Create an Intent to go to the Register activity
        Intent go = new Intent(getApplicationContext(), ListCoach.class);
        // Start the Register activity
        startActivity(go);
    }
    public void goProfileUser(View view) {
        Intent go = new Intent(getApplicationContext(), ProfileUser.class);
        // Start the Register activity
        startActivity(go);
    }
    public void goTraineeSchedule(View view) {
        Intent go = new Intent(getApplicationContext(), GetTraineeSchedule.class);
        // Start the Register activity
        startActivity(go);
    }
}
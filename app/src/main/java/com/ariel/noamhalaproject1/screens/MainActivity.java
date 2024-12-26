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



}
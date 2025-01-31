package com.ariel.noamhalaproject1.screens;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ariel.noamhalaproject1.R;
import com.ariel.noamhalaproject1.adapters.CoachAdapter;
import com.ariel.noamhalaproject1.models.Coach;
import com.ariel.noamhalaproject1.services.DatabaseService;

import java.util.List;

public class ListCoach extends AppCompatActivity {

    RecyclerView rvCoaches;
    private DatabaseService databaseService = DatabaseService.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_coach);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initViews();

        databaseService.getCoaches(new DatabaseService.DatabaseCallback<List<Coach>>() {
            @Override
            public void onCompleted(List<Coach> coaches) {
                CoachAdapter coachAdapter = new CoachAdapter(coaches);
                rvCoaches.setAdapter(coachAdapter);
            }

            @Override
            public void onFailed(Exception e) {
                Log.e("ListCoach", e.getMessage());
            }
        });
    }

    private void initViews() {
        rvCoaches = findViewById(R.id.rvCoaches);
        rvCoaches.setLayoutManager(new LinearLayoutManager(this));
    }


}
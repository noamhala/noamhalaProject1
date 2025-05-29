package com.ariel.noamhalaproject1.screens;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
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

public class AdminGetListCoach extends AppCompatActivity {

    RecyclerView rvCoaches;
    private DatabaseService databaseService = DatabaseService.getInstance();
    private CoachAdapter coachAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_get_list_coach);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize RecyclerView
        initViews();

        // Fetch coaches from the database
        databaseService.getCoaches(new DatabaseService.DatabaseCallback<List<Coach>>() {
            @Override
            public void onCompleted(List<Coach> coaches) {
                // Pass isAdmin to the adapter
                coachAdapter = new CoachAdapter(coaches, new CoachAdapter.OnCoachListener() {
                    @Override
                    public void onCoachClick(Coach coach) {
                        Intent intent = new Intent(AdminGetListCoach.this, CoachProfile.class);
                        intent.putExtra("coachId", coach.getId());
                        startActivity(intent);
                    }
                });



                rvCoaches.setAdapter(coachAdapter);
            }

            @Override
            public void onFailed(Exception e) {
                Log.e("AdminGetListCoach", e.getMessage());
            }
        });

        // Set up the search functionality
        SearchView searchView = findViewById(R.id.searchView_admin);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (coachAdapter != null) {
                    coachAdapter.getFilter().filter(newText);
                }
                return true;
            }
        });
    }


    private void initViews() {
        rvCoaches = findViewById(R.id.rvCoaches_admin);
        rvCoaches.setLayoutManager(new LinearLayoutManager(this));
    }
}

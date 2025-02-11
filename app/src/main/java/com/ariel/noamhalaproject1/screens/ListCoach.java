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
import android.widget.SearchView;

import com.ariel.noamhalaproject1.R;
import com.ariel.noamhalaproject1.adapters.CoachAdapter;
import com.ariel.noamhalaproject1.models.Coach;
import com.ariel.noamhalaproject1.services.DatabaseService;

import java.util.List;

public class ListCoach extends AppCompatActivity {

    RecyclerView rvCoaches;
    private DatabaseService databaseService = DatabaseService.getInstance();
    private CoachAdapter coachAdapter;

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

        // Fetch coaches from the database
        databaseService.getCoaches(new DatabaseService.DatabaseCallback<List<Coach>>() {
            @Override
            public void onCompleted(List<Coach> coaches) {
                coachAdapter = new CoachAdapter(coaches);
                rvCoaches.setAdapter(coachAdapter);
            }

            @Override
            public void onFailed(Exception e) {
                Log.e("ListCoach", e.getMessage());
            }
        });

        // Set up the search functionality
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false; // Do nothing when query is submitted
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter the list of coaches as the user types
                if (coachAdapter != null) {
                    coachAdapter.getFilter().filter(newText);
                }
                return true;
            }
        });
    }

    private void initViews() {
        rvCoaches = findViewById(R.id.rvCoaches);
        rvCoaches.setLayoutManager(new LinearLayoutManager(this));
    }
}

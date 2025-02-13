package com.ariel.noamhalaproject1.screens;

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
import com.ariel.noamhalaproject1.adapters.TraineeAdapter;  // Change to TraineeAdapter
import com.ariel.noamhalaproject1.models.Trainee;  // Change to Trainee
import com.ariel.noamhalaproject1.services.DatabaseService;

import java.util.List;

public class TraineeList extends AppCompatActivity {

    RecyclerView rvTrainees;  // RecyclerView for trainees
    private DatabaseService databaseService = DatabaseService.getInstance();  // Instance of DatabaseService
    private TraineeAdapter traineeAdapter;  // Adapter for trainees

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trainee_list);  // Set the layout for trainee list

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();

        // Fetch trainees from the database
        databaseService.getTrainees(new DatabaseService.DatabaseCallback<List<Trainee>>() {  // Call to get trainees
            @Override
            public void onCompleted(List<Trainee> trainees) {
                traineeAdapter = new TraineeAdapter(trainees);  // Set adapter
                rvTrainees.setAdapter(traineeAdapter);
            }

            @Override
            public void onFailed(Exception e) {
                Log.e("TraineeList", e.getMessage());  // Log any error
            }
        });

        // Set up search functionality
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;  // Do nothing when query is submitted
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter the list of trainees as the user types
                if (traineeAdapter != null) {
                    traineeAdapter.getFilter().filter(newText);  // Apply the filter
                }
                return true;
            }
        });
    }

    private void initViews() {
        rvTrainees = findViewById(R.id.rvTrainees);  // Initialize RecyclerView
        rvTrainees.setLayoutManager(new LinearLayoutManager(this));  // Set layout manager
    }
}

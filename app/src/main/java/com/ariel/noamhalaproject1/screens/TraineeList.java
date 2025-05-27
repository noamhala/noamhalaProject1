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
import com.ariel.noamhalaproject1.adapters.TraineeAdapter;
import com.ariel.noamhalaproject1.models.Trainee;
import com.ariel.noamhalaproject1.services.DatabaseService;

import java.util.List;

public class TraineeList extends AppCompatActivity {

    RecyclerView rvTrainees;
    private final DatabaseService databaseService = DatabaseService.getInstance();
    private TraineeAdapter traineeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trainee_list);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();

        databaseService.getTrainees(new DatabaseService.DatabaseCallback<List<Trainee>>() {
            @Override
            public void onCompleted(List<Trainee> trainees) {
                traineeAdapter = new TraineeAdapter(trainees, trainee -> {
                    Intent intent = new Intent(TraineeList.this, TraineeProfile.class);
                    intent.putExtra("traineeId", trainee.getId());
                    startActivity(intent);
                });
                rvTrainees.setAdapter(traineeAdapter);
            }

            @Override
            public void onFailed(Exception e) {
                Log.e("TraineeList", e.getMessage());
            }
        });

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (traineeAdapter != null) {
                    traineeAdapter.getFilter().filter(newText);
                }
                return true;
            }
        });
    }

    private void initViews() {
        rvTrainees = findViewById(R.id.rvTrainees);
        rvTrainees.setLayoutManager(new LinearLayoutManager(this));
    }

}

package com.ariel.noamhalaproject1.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ariel.noamhalaproject1.R;
import com.ariel.noamhalaproject1.models.Coach;
import com.ariel.noamhalaproject1.models.Trainee;
import com.ariel.noamhalaproject1.models.Workout;
import com.ariel.noamhalaproject1.services.DatabaseService;

import java.util.List;

public class    WorkoutAdapter extends ArrayAdapter<Workout> {
    Context context;
    List<Workout> objects;

    public WorkoutAdapter(Context context, List<Workout> objects) {
        super(context, 0, 0, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("WorkoutAdapter", "Position: " + position);

        // Inflate the layout for each item in the ListView
        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.one_workout, parent, false);

        // Find the views to populate
        TextView tvCoach = view.findViewById(R.id.tvCoach);
        TextView tvTrainee = view.findViewById(R.id.tvTrainee);
        TextView tvDate = view.findViewById(R.id.tvDate);
        TextView tvHour = view.findViewById(R.id.tvHour);

        // Get the Workout object for this position
        Workout workout = objects.get(position);
        Log.d("WorkoutAdapter", "Workout: " + workout.toString());

        // Set the date and hour for the workout
        tvDate.setText(workout.getDate());
        tvHour.setText(workout.getHour());

        // Fetch and set the Coach data
        DatabaseService.getInstance().getCoach(workout.getCoachId(), new DatabaseService.DatabaseCallback<Coach>() {
            @Override
            public void onCompleted(Coach coach) {
                if (coach != null) {
                    String coachFullName = coach.getFname() + " " + coach.getLname();
                    tvCoach.setText(coachFullName);
                } else {
                    tvCoach.setText("Coach not found");
                }
            }

            @Override
            public void onFailed(Exception e) {
                Log.e("WorkoutAdapter", "Error fetching coach", e);
                tvCoach.setText("Error fetching coach");
            }
        });

        // Fetch and set the Trainee data
        Log.d("WorkoutAdapter", "Trainee ID: " + workout.getTraineeId());
        DatabaseService.getInstance().getTrainee(workout.getTraineeId(), new DatabaseService.DatabaseCallback<Trainee>() {
            @Override
            public void onCompleted(Trainee trainee) {
                if (trainee != null) {
                    String traineeFullName = trainee.getFname() + " " + trainee.getLname();
                    tvTrainee.setText(traineeFullName);
                } else {
                    tvTrainee.setText("Trainee not found");
                }
            }

            @Override
            public void onFailed(Exception e) {
                Log.e("WorkoutAdapter", "Error fetching trainee", e);
                tvTrainee.setText("Error fetching trainee");
            }
        });

        return view;
    }

}

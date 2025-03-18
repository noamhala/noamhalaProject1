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
import com.ariel.noamhalaproject1.models.Trainee;
import com.ariel.noamhalaproject1.models.Workout;
import com.ariel.noamhalaproject1.services.DatabaseService;

import java.util.List;

public class WorkoutAdapter extends ArrayAdapter<Workout> {
    Context context;
    List<Workout> objects;

    public WorkoutAdapter(Context context, List<Workout> objects) {
        super(context, 0,0, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("WorkoutAdapter", "Position: " + position);
        LayoutInflater layoutInflater = ((Activity) context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.one_workout, parent, false);

        TextView tvDate = view.findViewById(R.id.tvDate);
        TextView tvLocation = view.findViewById(R.id.tvLocation);
        TextView tvHour = view.findViewById(R.id.tvHour);
        TextView tvTrainee = view.findViewById(R.id.tvTrainee);

        Workout temp = objects.get(position);
        Log.d("WorkoutAdapter", "Workout: " + temp.toString());  // Log the workout details

        tvHour.setText(temp.getHour());
        tvDate.setText(temp.getDate());
        tvLocation.setText(temp.getLocation());


        DatabaseService.getInstance().getTrainee(temp.getTraineeId(), new DatabaseService.DatabaseCallback<Trainee>() {
            @Override
            public void onCompleted(Trainee object) {
                tvTrainee.setText(object.getFname());
            }

            @Override
            public void onFailed(Exception e) {

            }
        });

        return view;
    }
}

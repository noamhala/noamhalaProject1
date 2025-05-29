package com.ariel.noamhalaproject1.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.ariel.noamhalaproject1.R;
import com.ariel.noamhalaproject1.models.Coach;
import com.ariel.noamhalaproject1.models.Trainee;
import com.ariel.noamhalaproject1.models.Workout;
import com.ariel.noamhalaproject1.services.DatabaseService;

import java.util.List;

public class WorkoutAdapter extends ArrayAdapter<Workout> {
    private List<Workout> objects;
    private LayoutInflater layoutInflater;
    private OnItemWorkout onItemWorkout;

    public interface OnItemWorkout {
        public boolean isShowAccept();
        public boolean isShowReject();
        public boolean isShowAdd();
        public void onAccept(Workout workout);
        public void onReject(Workout workout);
        public void onDetails(Workout workout);
        public void onAdd(Workout workout);
    }

    public WorkoutAdapter(Context context, List<Workout> objects, OnItemWorkout onItemWorkout) {
            super(context, 0, objects);
            this.onItemWorkout = onItemWorkout;
            this.objects = objects;
            layoutInflater = ((Activity) context).getLayoutInflater();
        }

        // ViewHolder pattern for better performance
        static class ViewHolder {
            TextView tvCoach;
        TextView tvTrainee;
        TextView tvDate;
        TextView tvHour;
        Button btnAccept, btnReject,btnAdd;


        public ViewHolder(View convertView) {
            tvCoach = convertView.findViewById(R.id.tvCoach);
            tvTrainee = convertView.findViewById(R.id.tvTrainee);
            tvDate = convertView.findViewById(R.id.tvDate);
            tvHour = convertView.findViewById(R.id.tvHour);
            btnAccept = convertView.findViewById(R.id.btn_item_workout_accept);
            btnReject = convertView.findViewById(R.id.btn_item_workout_decline);
            btnAdd = convertView.findViewById(R.id.btn_item_workout_add);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // View Holder to reuse views efficiently
        ViewHolder holder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.one_workout, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder); // Save holder to the convertView
        } else {
            holder = (ViewHolder) convertView.getTag(); // Reuse holder
        }

        // Get the Workout object for this position
        Workout workout = objects.get(position);
        Log.d("WorkoutAdapter", "Workout: " + workout.toString());

        // Set the date and hour for the workout
        holder.tvDate.setText(workout.getDate());
        holder.tvHour.setText(workout.getHour());

        // Fetch and set the Coach data
        loadCoachData(workout.getCoachId(), holder.tvCoach);

        // Fetch and set the Trainee data
        loadTraineeData(workout.getTraineeId(), holder.tvTrainee);

        holder.btnAccept.setVisibility(onItemWorkout.isShowAccept() ? View.VISIBLE : View.GONE);
        holder.btnReject.setVisibility(onItemWorkout.isShowReject() ? View.VISIBLE : View.GONE);
        holder.btnAdd.setVisibility(onItemWorkout.isShowAdd() ? View.VISIBLE : View.GONE);

        holder.btnAccept.setOnClickListener(v -> onItemWorkout.onAccept(workout));
        holder.btnReject.setOnClickListener(v -> onItemWorkout.onReject(workout));
        holder.btnAdd.setOnClickListener(v -> onItemWorkout.onAdd(workout));

        convertView.setOnClickListener(v -> onItemWorkout.onDetails(workout));

        return convertView;
    }

    private void loadCoachData(String coachId, TextView tvCoach) {
        DatabaseService.getInstance().getCoach(coachId, new DatabaseService.DatabaseCallback<Coach>() {
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
    }

    private void loadTraineeData(String traineeId, TextView tvTrainee) {
        DatabaseService.getInstance().getTrainee(traineeId, new DatabaseService.DatabaseCallback<Trainee>() {
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
    }
}

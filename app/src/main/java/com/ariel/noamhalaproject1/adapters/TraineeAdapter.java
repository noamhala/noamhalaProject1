package com.ariel.noamhalaproject1.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ariel.noamhalaproject1.R;
import com.ariel.noamhalaproject1.models.Trainee;

import java.util.ArrayList;
import java.util.List;

public class TraineeAdapter extends RecyclerView.Adapter<TraineeAdapter.TraineeViewHolder> implements Filterable {

    private final List<Trainee> trainees;
    private final List<Trainee> traineesFull;
    private final OnTraineeClickListener listener;

    // ✅ Constructor that accepts the click listener
    public TraineeAdapter(List<Trainee> trainees, OnTraineeClickListener listener) {
        this.trainees = trainees;
        this.traineesFull = new ArrayList<>(trainees);
        this.listener = listener;
    }

    @NonNull
    @Override
    public TraineeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trainee_item, parent, false);
        return new TraineeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TraineeViewHolder holder, int position) {
        Trainee trainee = trainees.get(position);
        holder.bind(trainee);
        holder.itemView.setOnClickListener(v -> listener.onTraineeClick(trainee));  // ✅ Attach click here
    }

    @Override
    public int getItemCount() {
        return trainees.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Trainee> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(traineesFull);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (Trainee trainee : traineesFull) {
                        if (trainee.getFname().toLowerCase().contains(filterPattern)
                                || trainee.getLname().toLowerCase().contains(filterPattern)) {
                            filteredList.add(trainee);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;
                results.count = filteredList.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                trainees.clear();
                if (results != null && results.count > 0) {
                    trainees.addAll((List<Trainee>) results.values);
                }
                notifyDataSetChanged();
            }
        };
    }

    // ViewHolder class
    public static class TraineeViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtTraineeName;
        private final TextView txtWeight;
        private final TextView txtHeight;
        private final TextView txtPhoneNumber;

        public TraineeViewHolder(View view) {
            super(view);
            txtTraineeName = view.findViewById(R.id.txtTraineeName);
            txtWeight = view.findViewById(R.id.txtWeight);
            txtHeight = view.findViewById(R.id.txtHeight);
            txtPhoneNumber = view.findViewById(R.id.txtPhoneNumber);
        }

        public void bind(Trainee trainee) {
            txtTraineeName.setText(trainee.getFname() + " " + trainee.getLname());
            txtWeight.setText("Weight: " + trainee.getWeight() + " kg");
            txtHeight.setText("Height: " + trainee.getHeight() + " cm");
            txtPhoneNumber.setText("Phone: " + trainee.getPhone());
        }
    }

    // Listener interface
    public interface OnTraineeClickListener {
        void onTraineeClick(Trainee trainee);
    }
}

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

    private List<Trainee> trainees;
    private List<Trainee> traineesFull;

    public TraineeAdapter(List<Trainee> trainees) {
        this.trainees = trainees;
        this.traineesFull = new ArrayList<>(trainees); // Store the original list for filtering
    }

    @NonNull
    @Override
    public TraineeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for a single trainee item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trainee_item, parent, false);
        return new TraineeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TraineeViewHolder holder, int position) {
        // Get the trainee object from the list
        Trainee trainee = trainees.get(position);
        // Bind the trainee data to the respective views in the ViewHolder
        holder.bind(trainee);
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
                    // If the search query is empty, show all items
                    filteredList.addAll(traineesFull);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    // Filter based on the trainee's name (you can add more conditions as needed)
                    for (Trainee trainee : traineesFull) {
                        if (trainee.getFname().toLowerCase().contains(filterPattern) ||
                                trainee.getLname().toLowerCase().contains(filterPattern)) {
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
                    trainees.addAll((List) results.values);
                }
                notifyDataSetChanged();
            }
        };
    }

    // ViewHolder class to hold and bind trainee item views
    public static class TraineeViewHolder extends RecyclerView.ViewHolder {

        private TextView txtTraineeName;
        private TextView txtWeight;
        private TextView txtHeight;
        private TextView txtPhoneNumber;

        public TraineeViewHolder(View view) {
            super(view);
            // Find the views by their IDs
            txtTraineeName = view.findViewById(R.id.txtTraineeName);
            txtWeight = view.findViewById(R.id.txtWeight);
            txtHeight = view.findViewById(R.id.txtHeight);
            txtPhoneNumber = view.findViewById(R.id.txtPhoneNumber);
        }

        // Bind the data from the trainee object to the UI elements
        public void bind(Trainee trainee) {
            txtTraineeName.setText(trainee.getFname() + " " + trainee.getLname());
            txtWeight.setText("Weight: " + trainee.getWeight() + " kg");
            txtHeight.setText("Height: " + trainee.getHeight() + " cm");
            txtPhoneNumber.setText("Phone: " + trainee.getPhone());
        }
    }

}

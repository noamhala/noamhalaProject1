package com.ariel.noamhalaproject1.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ariel.noamhalaproject1.R;
import com.ariel.noamhalaproject1.models.Coach;
import com.ariel.noamhalaproject1.screens.CoachProfile;
import com.ariel.noamhalaproject1.screens.CoachRequest;
import com.ariel.noamhalaproject1.screens.ListCoach;

import java.util.ArrayList;
import java.util.List;

public class CoachAdapter extends RecyclerView.Adapter<CoachAdapter.CoachViewHolder> implements Filterable {
    private final OnCoachListener listener;
    private List<Coach> coaches;
    private List<Coach> coachesFull; // List for unfiltered data
    private Context context;


    public interface OnCoachListener {
        public void onCoachClick(Coach coach);
    }



    public CoachAdapter(List<Coach> coaches, OnCoachListener listener) {
        this.coaches = coaches;
        this.coachesFull = new ArrayList<>(coaches);
        this.listener = listener;
    }


    @NonNull
    @Override
    public CoachAdapter.CoachViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.coach_item, parent, false);
        view.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        return new CoachViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoachAdapter.CoachViewHolder holder, int position) {
        Coach coach = coaches.get(position);
        holder.bind(coach);

        holder.itemView.setOnClickListener(v -> listener.onCoachClick(coach));
    }

    @Override
    public int getItemCount() {
        return coaches.size();
    }

    // Method for filtering the list
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Coach> filteredList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(coachesFull); // No filter, return full list
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for (Coach coach : coachesFull) {
                        if (coach.getFname().toLowerCase().contains(filterPattern) ||
                                coach.getLname().toLowerCase().contains(filterPattern) ||
                                coach.getDomain().toLowerCase().contains(filterPattern)) {
                            filteredList.add(coach);
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
                coaches.clear();
                if (results != null && results.count > 0) {
                    coaches.addAll((List) results.values);
                }
                notifyDataSetChanged();
            }
        };
    }

    // CoachViewHolder class to bind views
    public class CoachViewHolder extends RecyclerView.ViewHolder {

        private TextView txtCoachName;
        private TextView txtDomain;
        private TextView txtPrice;
        private TextView txtPhoneNumber;
        private TextView TxtExperience;

        private Button btnViewDetails;

        public CoachViewHolder(View view) {
            super(view);
            txtCoachName = view.findViewById(R.id.txtCoachName);
            txtDomain = view.findViewById(R.id.txtDomain);
            TxtExperience = view.findViewById(R.id.TxtExperience);
            txtPrice = view.findViewById(R.id.txtPrice);
            txtPhoneNumber = view.findViewById(R.id.txtPhoneNumber);



        }

        public void bind(Coach coach) {
            txtCoachName.setText(coach.getFname() + " " + coach.getLname());
            txtDomain.setText(coach.getDomain());
            txtPrice.setText(coach.getPrice() + " ₪");
            txtPhoneNumber.setText(coach.getPhone());
            TxtExperience.setText(coach.getExperience() + " years");

        }

    }
}

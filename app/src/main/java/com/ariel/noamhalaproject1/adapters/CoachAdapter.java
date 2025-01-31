package com.ariel.noamhalaproject1.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ariel.noamhalaproject1.R;
import com.ariel.noamhalaproject1.models.Coach;

import java.util.List;

public class CoachAdapter extends RecyclerView.Adapter<CoachAdapter.CoachViewHolder>{
    private List<Coach> Coaches;

    public CoachAdapter(List<Coach> Coaches) {
        this.Coaches = Coaches;
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
        Coach coach = Coaches.get(position);
        holder.bind(coach);
    }


    public List<Coach> getCoaches() {
        return Coaches;
    }

    public void setCoaches(List<Coach> coaches) {
        this.Coaches = coaches;
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return Coaches.size();
    }

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
            txtPrice.setText(coach.getPrice()+" ₪");
            txtPhoneNumber.setText(coach.getPhone());
            TxtExperience.setText(coach.getExperience() + "");



        }
    }
}

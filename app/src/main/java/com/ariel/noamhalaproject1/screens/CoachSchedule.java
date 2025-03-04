package com.ariel.noamhalaproject1.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ariel.noamhalaproject1.R;
import com.ariel.noamhalaproject1.models.Workout;
import com.ariel.noamhalaproject1.services.DatabaseService;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Set;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;


public class CoachSchedule extends AppCompatActivity {

    private CalendarView calendarView;
    private FirebaseDatabase database;
    private DatabaseService databaseService;
    private Set<String> eventDates;  // Set to store event dates

    ArrayList<Workout>workouts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_coach_schedule);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        databaseService=DatabaseService.getInstance();
        calendarView = findViewById(R.id.calendarViewCoach);
        eventDates = new HashSet<>();







    // Fetch events from Firebase
    fetchEventsFromFirebase();

    // Set up the calendar view listener
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
        @Override
        public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
            String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
            if (eventDates.contains(selectedDate)) {
                showEventDetails(selectedDate);
            } else {
                Toast.makeText(CoachSchedule.this, "No events on this date", Toast.LENGTH_SHORT).show();
            }
        }
    });
}

    public void goCoachSchedule(View view) {
        // Create an Intent to go to the Register activity
        Intent go = new Intent(getApplicationContext(), CoachSchedule.class);

        // Start the Register activity
        startActivity(go);
    }


    // Method to fetch events from Firebase
    private void fetchEventsFromFirebase() {

    }

    // Method to highlight dates with events on the calendar view
    private void highlightEventDates() {
        // Firebase stores dates in the format "yyyy-MM-dd", so let's compare with CalendarView's selected date
        for (String eventDate : eventDates) {
            try {
                // Convert the eventDate to Date object to match the calendar's date format
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = sdf.parse(eventDate);

                if (date != null) {
                    // Set the date on the calendar (you'll need to implement this based on your CalendarView capabilities)
                    // Example: Change background color for the event date (this feature depends on the library you use)
                    // For a basic CalendarView, customizations like marking dates directly aren't easily possible
                    // You may need to implement custom logic for this (or use a third-party calendar library like `MaterialCalendarView`)
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    // Show event details when a date with an event is selected
    private void showEventDetails(String date) {
        // Here you can show a dialog, a new activity, or any UI to display event details
        Toast.makeText(this, "Events on " + date, Toast.LENGTH_SHORT).show();
        // Retrieve events for the selected date from the `eventDates` set and display details
    }

}
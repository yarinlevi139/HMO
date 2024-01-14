package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class time_and_date extends AppCompatActivity {
    private Spinner spinnerDate;
    private Spinner spinnerTime;
    private TextView timeTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_and_date);

        // Initialize time spinner and title
        spinnerTime = findViewById(R.id.spinnerTime);
        timeTitle = findViewById(R.id.timeTitle);

        // Get the current date
        Calendar calendar = Calendar.getInstance();
        // Create a list of dates
        List<String> dates = new ArrayList<>();
        // Create a date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        // Add the default message at position 0
        dates.add("Choose the date");
        // Loop through 30 days
        for (int i = 0; i < 30; i++) {
            // Add the formatted date to the list
            dates.add(dateFormat.format(calendar.getTime()));
            // Increment the calendar by one day
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        // Get the spinner from the layout
        spinnerDate = findViewById(R.id.spinnerDate);
        // Create an array adapter for the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dates);
        // Set the drop down view for the spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Set the adapter for the spinner
        spinnerDate.setAdapter(adapter);

        // Set up listener for the date spinner
        spinnerDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Check if a date is selected (not the default selection)
                if (position > 0) {
                    // Show time spinner and title when a date is selected
                    timeTitle.setVisibility(View.VISIBLE);
                    spinnerTime.setVisibility(View.VISIBLE);
                    // TODO: Add code to populate the time spinner based on the selected date if needed
                } else {
                    // Hide time spinner and title when the default selection is chosen
                    timeTitle.setVisibility(View.GONE);
                    spinnerTime.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle the case when nothing is selected (optional)
            }
        });
    }
}

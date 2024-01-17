package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class doctor_schedule extends AppCompatActivity {
    private appointment_adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doc_schedule);

        // Get the CalendarView from the layout
        CalendarView calendarView = findViewById(R.id.calendarView);

        // Set the minimum date to today
        Calendar calendar = Calendar.getInstance();
        calendarView.setMinDate(calendar.getTimeInMillis());

        // Set the maximum date to 7 days from today
        calendar.add(Calendar.DATE, 7);
        calendarView.setMaxDate(calendar.getTimeInMillis());

        // Sample data for the initial list
        List<String> dataList = new ArrayList<>(Arrays.asList("Item 1", "Item 2", "Item 3"));

        // Create a custom adapter and set it to the ListView
        adapter = new appointment_adapter(this, dataList);
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);

        // Set the OnDateChangeListener for the CalendarView
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // Generate a message based on the selected date
                String selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
                String dayOfWeekMessage = generateMessageForDay(selectedDate);

                // Update the adapter's data with the message and notify the change
                adapter.clear();
                adapter.add(dayOfWeekMessage);
                adapter.notifyDataSetChanged();
            }
        });
    }

    // Method to generate a message based on the day of the week
    private String generateMessageForDay(String selectedDate) {
        try {
            // Parse the selected date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = sdf.parse(selectedDate);

            // Get the day of the week
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

            // Return a message based on the day of the week
            switch (dayOfWeek) {
                case Calendar.SUNDAY:
                    return "This is Sunday";
                case Calendar.MONDAY:
                    return "This is Monday";
                case Calendar.TUESDAY:
                    return "This is Tuesday";
                case Calendar.WEDNESDAY:
                    return "This is Wednesday";
                case Calendar.THURSDAY:
                    return "This is Thursday";
                case Calendar.FRIDAY:
                    return "This is Friday";
                case Calendar.SATURDAY:
                    return "This is Saturday";
                default:
                    return "Invalid day";
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return "Error parsing date";
        }
    }
}

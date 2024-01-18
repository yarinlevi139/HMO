package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class doctor_schedule extends AppCompatActivity {
    private static final String COLLECTION_APPOINTMENTS = "Appointments";
    private static final String FIELD_DOC_EMAIL = "docEmail";
    private static final String FIELD_DATE = "date";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_HOUR = "hour";

    private appointment_adapter adapter;
    private FirebaseAuth mAuth;
    private TextView dayOfWeekTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doc_schedule);

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Get the CalendarView from the layout
        CalendarView calendarView = findViewById(R.id.calendarView);
        dayOfWeekTextView = findViewById(R.id.dayOfWeekTextView);

        // Set the minimum date to today
        Calendar calendar = Calendar.getInstance();
        calendarView.setMinDate(calendar.getTimeInMillis());

        // Set the maximum date to 7 days from today
        calendar.add(Calendar.DATE, 7);
        calendarView.setMaxDate(calendar.getTimeInMillis());

        // Sample data for the initial list
        List<String> dataList = new ArrayList<>();

        // Create your custom adapter and set it to the ListView
        adapter = new appointment_adapter(this, dataList);
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);

        // Set the OnDateChangeListener for the CalendarView
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            // Generate a message based on the selected date
            String selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month + 1, year);
            Log.d("SelectedDate", "Selected date: " + selectedDate);

            // Fetch and display appointments for the selected date
            fetchAndDisplayAppointments(selectedDate);

            // Set the day of the week
            setDayOfWeek(selectedDate);
        });
    }

    // Method to fetch and display appointments for the selected date
    // Method to fetch and display appointments for the selected date
    private void fetchAndDisplayAppointments(String selectedDate) {
        // Add your logic here to fetch appointments for the logged-in doctor
        // Use FirebaseAuth to get the logged-in doctor's email
        String loggedInDoctorEmail = mAuth.getCurrentUser().getEmail();

        // Access Firestore to get appointments for the selected date and the logged-in doctor
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(COLLECTION_APPOINTMENTS)
                .whereEqualTo(FIELD_DOC_EMAIL, loggedInDoctorEmail)
                .whereEqualTo(FIELD_DATE, selectedDate)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<String> appointments = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult().getDocuments()) {
                            // Extract appointment details
                            String clientName = document.getString(FIELD_NAME);
                            String time = document.getString(FIELD_HOUR);
                            String appointmentDetails = "Client Name: " + clientName + "\n" +
                                    "Time: " + time;
                            appointments.add(appointmentDetails);
                        }

                        // Sort appointments by hour
                        Collections.sort(appointments, (appointment1, appointment2) -> {
                            String time1 = getTimeFromAppointment(appointment1);
                            String time2 = getTimeFromAppointment(appointment2);
                            return time1.compareTo(time2);
                        });

                        // Debugging: Log the fetched and sorted appointments
                        for (String appointment : appointments) {
                            Log.d("FetchAppointments", "Sorted Appointment: " + appointment);
                        }

                        // Update the adapter's data with the fetched appointments and notify the change
                        runOnUiThread(() -> {
                            adapter.clear();
                            adapter.addAll(appointments);
                            adapter.notifyDataSetChanged();
                        });
                    } else {
                        // Debugging: Log the error message
                        Log.e("FetchAppointments", "Error fetching appointments", task.getException());

                        // Handle the error
                    }
                });
    }

    // Method to extract time from appointment details
    private String getTimeFromAppointment(String appointmentDetails) {
        // Extract the time part from the appointment details (assuming "Time: HH:mm" format)
        String[] parts = appointmentDetails.split("Time: ");
        if (parts.length > 1) {
            return parts[1];
        } else {
            return ""; // Handle the case where time is not present or format is different
        }
    }


    // Method to set the day of the week TextView
    private void setDayOfWeek(String selectedDate) {
        try {
            // Parse the selected date
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sdf.parse(selectedDate));
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

            // Set the day of the week TextView
            String dayOfWeekMessage = getDayOfWeekMessage(dayOfWeek);
            dayOfWeekTextView.setText(dayOfWeekMessage);
        } catch (ParseException e) {
            e.printStackTrace();
            // Handle the parsing exception here
        }
    }

    // Method to get the day of the week message
    private String getDayOfWeekMessage(int dayOfWeek) {
        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                return "Sunday";
            case Calendar.MONDAY:
                return "Monday";
            case Calendar.TUESDAY:
                return "Tuesday";
            case Calendar.WEDNESDAY:
                return "Wednesday";
            case Calendar.THURSDAY:
                return "Thursday";
            case Calendar.FRIDAY:
                return "Friday";
            case Calendar.SATURDAY:
                return "Saturday";
            default:
                return "Invalid day";
        }
    }
}

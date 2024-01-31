/**
 * This class represents the activity for displaying and managing a doctor's schedule.
 * It utilizes Firebase authentication and Firestore to fetch and display appointments for a logged-in doctor.
 */
package com.example.myapplication.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.classes.Appointment;
import com.example.myapplication.R;
import com.example.myapplication.adapters.appointment_adapter;
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

/**
 * The DoctorSchedule class extends AppCompatActivity and represents the doctor's schedule management activity.
 * It integrates Firebase authentication and Firestore to fetch and display appointments for the logged-in doctor.
 */
public class doctor_schedule extends AppCompatActivity {

    // Firebase Firestore collection and field constants
    private static final String COLLECTION_APPOINTMENTS = "Appointments";
    private static final String FIELD_DOC_EMAIL = "docEmail";
    private static final String FIELD_CLIENT_EMAIL = "clientEmail";
    private static final String FIELD_DATE = "date";
    private static final String FIELD_NAME = "name";
    private static final String FIELD_HOUR = "hour";

    // Appointment adapter for displaying appointment information in a ListView
    private appointment_adapter adapter;

    // Firebase authentication instance
    private FirebaseAuth mAuth;

    // TextView for displaying the selected day of the week
    private TextView dayOfWeekTextView;

    /**
     * Called when the activity is first created. Responsible for initializing the UI components,
     * Firebase authentication, setting up CalendarView, and creating the initial data list.
     * @param savedInstanceState A Bundle containing the saved state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_schedule);

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Get the CalendarView from the layout
        CalendarView calendarView = findViewById(R.id.calendarView);
        dayOfWeekTextView = findViewById(R.id.dayOfWeekTextView);

        // Set the minimum date to today
        Calendar calendar = Calendar.getInstance();
        calendarView.setMinDate(calendar.getTimeInMillis());

        // Set the maximum date to 7 days from today
        calendar.add(Calendar.DATE, 30);
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

    /**
     * Fetches and displays appointments for the selected date using Firebase Firestore.
     * Retrieves the logged-in doctor's email, accesses Firestore, and updates the UI with the fetched appointments.
     * @param selectedDate The selected date for fetching appointments.
     */
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
                        // List to store fetched appointments
                        List<String> appointments = new ArrayList<>();
                        int counter = 0;

                        // Loop through fetched documents
                        for (DocumentSnapshot document : task.getResult().getDocuments()) {
                            // Extract appointment details
                            String clientName = document.getString(FIELD_NAME);
                            String time = document.getString(FIELD_HOUR);
                            String clientEmail = document.getString(FIELD_CLIENT_EMAIL);

                            // Add appointment to the adapter's data
                            adapter.pList.add(new Appointment());
                            adapter.setData(counter++, clientEmail, time, selectedDate);

                            // Create appointment details string
                            String appointmentDetails = "Client Name: " + clientName + "\n" +
                                    "Time: " + time;

                            // Add appointment details to the list
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

    /**
     * Extracts time from appointment details.
     * Assumes that the appointment details follow the format "Time: HH:mm".
     * @param appointmentDetails The appointment details string.
     * @return The extracted time string.
     */
    private String getTimeFromAppointment(String appointmentDetails) {
        // Extract the time part from the appointment details (assuming "Time: HH:mm" format)
        String[] parts = appointmentDetails.split("Time: ");
        if (parts.length > 1) {
            return parts[1];
        } else {
            return ""; // Handle the case where time is not present or format is different
        }
    }

    /**
     * Sets the day of the week TextView based on the selected date.
     * Parses the selected date and updates the TextView with the corresponding day of the week.
     * @param selectedDate The selected date for determining the day of the week.
     */
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

    /**
     * Returns the day of the week message based on the provided day of the week integer.
     * @param dayOfWeek The day of the week integer (Calendar constants).
     * @return The corresponding day of the week message.
     */
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

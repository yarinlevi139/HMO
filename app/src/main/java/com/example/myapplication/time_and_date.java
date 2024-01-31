package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class time_and_date extends AppCompatActivity {
    private Spinner spinnerDate;
    private Spinner spinnerTime;
    private TextView timeTitle;

    private Button setAppointment;

    private FirebaseFirestore firestore;

    private FirebaseAuth mAuth;

    private Appointment appointment = new Appointment("", "", "", "", "", "", "", "");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_and_date);

        // Initialize Firebase Firestore
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

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

                    // Get the selected date
                    String selectedDate = dates.get(position);

                    appointment.setDate(selectedDate);
                    checkDocumentExistence(selectedDate);

                } else {
                    timeTitle.setVisibility(View.GONE);
                    spinnerTime.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle the case when nothing is selected (optional)
            }
        });


        spinnerTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Check if a time is selected
                if (position >= 0) {
                    // Get the selected time
                    String selectedTime = spinnerTime.getSelectedItem().toString();
                    appointment.setHour(selectedTime);
                    appointment.setDoctor(getIntent().getStringExtra("Doc"));
                    appointment.setDocType(getIntent().getStringExtra("type"));
                    appointment.setDocEmail(getIntent().getStringExtra("docEmail"));
                    appointment.setClientEmail(mAuth.getCurrentUser().getEmail());
                    getUsernameFromDatabase(appointment);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle the case when nothing is selected (optional)
            }
        });
        setAppointment = findViewById(R.id.setAppointmentButton);
        setAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if a date and time have been selected
                if (appointment.getDate().equals("") || appointment.getHour().equals("Choose time")) {
                    // If not, show a Toast message
                    if (appointment.getDate().equals("")) {
                        Toast.makeText(time_and_date.this, "Please choose a date", Toast.LENGTH_SHORT).show();
                    } else if (appointment.getHour().equals("Choose time")) {
                        Toast.makeText(time_and_date.this, "Please choose a time", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // If a date and time have been selected, set the appointment
                    firestore.collection("Appointments").add(appointment)
                            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(time_and_date.this, "Appointment scheduled successfully", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        // Handle errors
                                    }
                                }
                            });
                }
            }
        });

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void getUsernameFromDatabase(Appointment appointment) {
        // Assuming you have FirebaseAuth and FirebaseFirestore instances
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        // Get the currently logged-in user
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            // The user is logged in
            String email = currentUser.getEmail();

            // Assuming you have a "Clients" collection in Firestore
            firestore.collection("Clients")
                    .whereEqualTo("email_address", email) // Replace "email" with the actual field name in your collection
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                QuerySnapshot querySnapshot = task.getResult();
                                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                    // Assuming you want the first document (if there are multiple)
                                    DocumentSnapshot document = querySnapshot.getDocuments().get(0);

                                    // Retrieve the first name from the document
                                    String firstName = document.getString("first_name");
                                    // Now you have the current username and first name
                                    // Save the firstName into the Appointment object
                                    appointment.setName(firstName);
                                } else {
                                    // No matching document found
                                }
                            } else {
                                // Handle errors
                            }
                        }
                    });
        } else {
            // No user is logged in
        }
    }

    private void checkDocumentExistence(String selectedDate) {
        

        firestore.collection("Appointments")
                .whereEqualTo("date", selectedDate)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                // Document(s) exist for the selected date
                                // Get the list of reserved hours
                                List<String> reservedHours = getReservedHours(querySnapshot);

                                // Populate the spinner with available times
                                populateAvailableTimes(selectedDate, reservedHours);

                            } else {
                                // No document exists for the selected date
                                // Populate with all times from 9:00 - 17:00
                                populateAllTimes(selectedDate);
                            }
                        } else {
                            // Handle errors
                        }
                    }
                });
    }

    // Function to populate the time spinner with all times from 9:00 - 17:00 in 15-minute intervals
    private void populateAllTimes(String selectedDate) {
        // Create a list of times
        List<String> times = new ArrayList<>();

        times.add("Choose time");

        // Loop through the times from 9:00 to 17:00 with 15-minute intervals
        for (int hour = 9; hour <= 16; hour++) {
            for (int minute = 0; minute < 60; minute += 15) {
                String time = String.format("%02d:%02d", hour, minute);
                if (!isTimeBeforeCurrentTime(selectedDate, time)) {
                    times.add(time);
                }
            }
        }

        // Create an array adapter for the time spinner
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, times);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTime.setAdapter(timeAdapter);
    }

    // Function to populate the time spinner with available times excluding already reserved ones
    private void populateAvailableTimes(String selectedDate, List<String> reservedHours) {

        // Create a list of times from 9:00 to 17:00 with 15-minute intervals excluding the reserved hours
        List<String> availableTimes = new ArrayList<>();
        availableTimes.add("Choose time");

        for (int hour = 9; hour <= 16; hour++) {
            for (int minute = 0; minute < 60; minute += 15) {
                String time = String.format("%02d:%02d", hour, minute);
                if (!isTimeBeforeCurrentTime(selectedDate, time) && !reservedHours.contains(time)) {
                    availableTimes.add(time);
                }
            }
        }

        // Create an array adapter for the time spinner
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, availableTimes);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTime.setAdapter(timeAdapter);
    }


    // Function to get reserved hours from a query snapshot
    private List<String> getReservedHours(QuerySnapshot querySnapshot) {
        List<String> reservedHours = new ArrayList<>();

        for (QueryDocumentSnapshot document : querySnapshot) {
            String reservedHour = document.getString("hour");
            reservedHours.add(reservedHour);
        }

        return reservedHours;
    }

    // Function to check if the given time is before the current time
    private boolean isTimeBeforeCurrentTime(String selectedDate, String selectedTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String dateTimeString = selectedDate + " " + selectedTime;

        try {
            Date selectedDateTime = sdf.parse(dateTimeString);
            Date currentDateTime = new Date();

            return selectedDateTime.before(currentDateTime);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
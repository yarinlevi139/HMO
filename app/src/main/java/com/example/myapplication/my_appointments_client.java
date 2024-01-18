package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class my_appointments_client extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myappointments_client);

        TextView appointmentText = findViewById(R.id.appointmentText);
        ListView appointmentList = findViewById(R.id.appointmentList);
        Button backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go back to the previous activity
                finish();
            }
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Appointments")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<String> appointments = new ArrayList<>();
                        List<QueryDocumentSnapshot> sortedDocuments = new ArrayList<>();

                        for (DocumentSnapshot document : task.getResult().getDocuments()) {
                            sortedDocuments.add((QueryDocumentSnapshot) document);
                        }

                        // Sort documents by date and time
                        sortedDocuments.sort((doc1, doc2) -> {
                            String date1 = doc1.getString("date");
                            String time1 = doc1.getString("hour");
                            String date2 = doc2.getString("date");
                            String time2 = doc2.getString("hour");

                            // Compare dates
                            int dateComparison = date1.compareTo(date2);
                            if (dateComparison != 0) {
                                return dateComparison;
                            }

                            // If dates are equal, compare times
                            return time1.compareTo(time2);
                        });

                        // Get current date and time
                        long currentTimeMillis = System.currentTimeMillis();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                        Date currentDate = new Date(currentTimeMillis);

                        // Iterate over sorted documents
                        for (QueryDocumentSnapshot document : sortedDocuments) {
                            String date = document.getString("date");
                            String time = document.getString("hour");

                            // Combine date and time to create a datetime string
                            String appointmentDateTime = date + " " + time;

                            // Parse datetime string to Date
                            Date appointmentDate = null;
                            try {
                                appointmentDate = dateFormat.parse(appointmentDateTime);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            // Check if the appointment is relevant (after the current date and time)
                            if (appointmentDate != null && appointmentDate.after(currentDate)) {
                                String doctor = document.getString("doctor");
                                String hour = document.getString("hour");
                                String name = document.getString("name");
                                String doc_type = document.getString("docType");
                                String appointmentDetails = "Date: " + date + "\n" +
                                        "Time: " + hour + "\n" +
                                        "Doctor's Name: " + doctor + "\n" +
                                        "Doctor's Profession: " + doc_type;
                                appointments.add(appointmentDetails);
                            }
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.cancel_client_appointments, R.id.dateTextView, appointments) {
                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                View view = super.getView(position, convertView, parent);

                                // Set appointment details for each item
                                TextView dateTextView = view.findViewById(R.id.dateTextView);
                                TextView timeTextView = view.findViewById(R.id.timeTextView);
                                Button cancelButton = view.findViewById(R.id.cancelButton);

                                String appointmentDetails = appointments.get(position);
                                // Parse the appointmentDetails string and set the values to respective TextViews

                                // Get the document ID for the appointment
                                String documentId = sortedDocuments.get(position).getId();

                                // Set an OnClickListener for the cancel button
                                // Inside the OnClickListener for the cancel button
                                cancelButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // Show a confirmation dialog
                                        showConfirmationDialog(my_appointments_client.this, documentId, position);
                                    }
                                });
                                return view;
                            }

                            private void showConfirmationDialog(Context context, String documentId, int position) {
                                // Build an AlertDialog
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setMessage("Are you sure you want to cancel this appointment?")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                // User clicked Yes button
                                                // Delete the appointment document
                                                db.collection("Appointments").document(documentId)
                                                        .delete()
                                                        .addOnSuccessListener(aVoid -> {
                                                            // Document successfully deleted
                                                            // Remove the canceled appointment from the list
                                                            appointments.remove(position);

                                                            // Notify the adapter that the data set has changed
                                                            notifyDataSetChanged();
                                                        })
                                                        .addOnFailureListener(e -> {
                                                            // Handle the error
                                                        });
                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                // User clicked No button
                                                // Close the dialog
                                                dialog.dismiss();
                                            }
                                        });

                                // Create and show the AlertDialog
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        };
                        appointmentList.setAdapter(adapter);
                    } else {
                        // Handle the error
                    }
                });
    }
}
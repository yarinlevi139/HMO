package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

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
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String date = document.getString("date");
                            String doctor = document.getString("doctor");
                            String hour = document.getString("hour");
                            String name = document.getString("name");
                            String doc_type = document.getString("docType");
                            appointments.add(date + " " + hour + " - Appointment with " + doctor + " type: " + doc_type);
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, appointments);
                        appointmentList.setAdapter(adapter);
                    } else {
                        // Handle the error
                    }
                });
    }
}

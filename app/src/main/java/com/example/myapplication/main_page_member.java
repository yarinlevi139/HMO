package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class main_page_member extends AppCompatActivity {
    @SuppressLint("SetTextI18n")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_member);
        // Get the email from the Intent
        String email = getIntent().getStringExtra("Email");

        // Get Firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Query the 'clients' collection where 'email' is equal to the user's email
        db.collection("Clients").whereEqualTo("email_address", email).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Get the client's name
                                String name = document.getString("first_name");

                                // Set the welcome text
                                TextView welcomeText = findViewById(R.id.welcomeText);
                                welcomeText.setText("Hello " + name + "!\nHow can we help you today?");
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

        // Assuming you have two buttons in your layout with these IDs
        Button makeAppointmentButton = findViewById(R.id.makeAppointmentButton);
        Button MyappointmentsButton = findViewById(R.id.myAppointmentButton);
        Button backButton = findViewById(R.id.backButton);
        Button my_profile = findViewById(R.id.profile_member_btn);
        Button send_message = findViewById(R.id.messageDoctorButton);

        makeAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start your Make Appointment Activity here
                Intent intent = new Intent(main_page_member.this, MakeAppointmentActivity.class);
                startActivity(intent);
            }
        });

        MyappointmentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // See your Appointments Activity here
                Intent intent = new Intent(main_page_member.this, MyAppointments_client.class);
                startActivity(intent);
            }
        });

        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // See your Appointments Activity here
                //TODO change the class you go to
                Intent intent = new Intent(main_page_member.this, null);
                startActivity(intent);
            }
        });



        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go back to the previous activity
                finish();
            }
        });

        my_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(main_page_member.this, client_profile.class);
                startActivity(intent);
            }
        });
    }
}

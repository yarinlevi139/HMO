package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class main_page_member extends AppCompatActivity {
    @SuppressLint("SetTextI18n")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_member);

        // Get the email from the Intent
        String email = getIntent().getStringExtra("Email");

        // Set the welcome text
        TextView welcomeText = findViewById(R.id.welcomeText);
        welcomeText.setText("Hello " + email + "!\nHow can we help you today?");

        // Assuming you have two buttons in your layout with these IDs
        Button makeAppointmentButton = findViewById(R.id.makeAppointmentButton);
        Button cancelAppointmentButton = findViewById(R.id.cancelAppointmentButton);
        Button backButton = findViewById(R.id.backButton); // New back button

        makeAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start your Make Appointment Activity here
                Intent intent = new Intent(main_page_member.this, MakeAppointmentActivity.class);
                startActivity(intent);
            }
        });

        cancelAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start your Cancel Appointment Activity here
                Intent intent = new Intent(main_page_member.this, CancelAppointmentActivity.class);
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
    }
}

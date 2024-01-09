package com.example.myapplication;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MakeAppointmentActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a ScrollView
        ScrollView scrollView = new ScrollView(this);
        scrollView.setLayoutParams(new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.MATCH_PARENT));

        // Create a LinearLayout to hold the buttons
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.setGravity(Gravity.CENTER_HORIZONTAL); // Center the contents horizontally

        // Add a TextView at the start
        TextView instructionText = new TextView(this);
        instructionText.setText("What do you need?");
        instructionText.setTextSize(24);
        instructionText.setGravity(Gravity.CENTER); // Center the text
        linearLayout.addView(instructionText);

        // Add appointment type buttons
        String[] appointmentTypes = {"Blood Test", "Family Doctor", "Dentist", "Eye Checkup", "Physical Therapy", "Cardiologist", "Neurologist", "Orthopedic"};
        for (String type : appointmentTypes) {
            Button button = new Button(this);
            button.setText(type);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle button click here
                }
            });
            linearLayout.addView(button);
        }

        // Add a "Back" button at the end
        Button backButton = new Button(this);
        backButton.setText("Back");
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go back to the previous activity
                finish();
            }
        });
        linearLayout.addView(backButton);

        // Add the LinearLayout to the ScrollView
        scrollView.addView(linearLayout);

        // Set the ScrollView as the content view
        setContentView(scrollView);
    }
}

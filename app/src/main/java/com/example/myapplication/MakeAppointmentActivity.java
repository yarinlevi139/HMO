package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
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
        Button BloodTest = findViewById(R.id.bloodTestButton);
        Button FamilyDoctor = findViewById(R.id.familyDoctorButton);
        Button Dentist = findViewById(R.id.dentistButton);
        Button EyeCheckup = findViewById(R.id.eyeCheckupButton);

        BloodTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MakeAppointmentActivity.this, docsavailable_bloodtest.class);
                startActivity(intent);
            }
        });

        FamilyDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MakeAppointmentActivity.this, docsavailable_family.class);
                startActivity(intent);
            }
        });

        Dentist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MakeAppointmentActivity.this, docsavailable_dentist.class);
                startActivity(intent);
            }
        });

        EyeCheckup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MakeAppointmentActivity.this, docsavailable_eyesight.class);
                startActivity(intent);
            }
        });

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
    }
}

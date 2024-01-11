package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MakeAppointmentActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.makeappointmentactivity); 

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
        Button backButton = findViewById(R.id.backButton); // Assuming you have a backButton in your XML layout
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go back to the previous activity
                finish();
            }
        });
    }
}

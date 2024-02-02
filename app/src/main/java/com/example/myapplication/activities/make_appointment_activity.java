package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class make_appointment_activity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_appointment_activity);

        // Add appointment type buttons
        Button BloodTest = findViewById(R.id.bloodTestButton);
        Button FamilyDoctor = findViewById(R.id.familyDoctorButton);
        Button Dentist = findViewById(R.id.dentistButton);
        Button EyeCheckup = findViewById(R.id.eyeCheckupButton);

        BloodTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData("blood test");
            }
        });

        FamilyDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData("family doctor");
            }
        });

        Dentist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData("dentist");
            }
        });

        EyeCheckup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData("eye checkup");
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

    /**
     * sends the data to the next activity.
     * in the next activity you will take all the relevant doctors by their type.
     * @param dataValue
     */
    private void sendData(String dataValue) {
        // Create Intent and put extra data
        Intent intent = new Intent(this, doctors_available.class);
        intent.putExtra("message", dataValue);

        // Start the second activity
        startActivity(intent);
    }
}

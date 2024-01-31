package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class main_page_doctor extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page_doctor);
        Button profile = findViewById(R.id.button_profile);
        profile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(main_page_doctor.this, doctor_profile.class);
                startActivity(intent);
            }
        });
        Button schedule = findViewById(R.id.button_schedule);
        schedule.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(main_page_doctor.this, doctor_schedule.class);
                startActivity(intent);
            }
        });
        Button messages = findViewById(R.id.button_messages);
        messages.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(main_page_doctor.this, doctor_messages.class);
                startActivity(intent);
            }
        });


        Button signout = findViewById(R.id.sign_out);
        signout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(main_page_doctor.this, main_activity.class);
                startActivity(intent);
            }
        });


    }

}

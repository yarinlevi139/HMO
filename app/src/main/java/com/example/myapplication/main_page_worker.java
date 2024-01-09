package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public class main_page_worker extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_worker);
        Button profile = findViewById(R.id.button_profile);
        profile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(main_page_worker.this, doctor_profile.class);
                startActivity(intent);
            }
        });
        Button schedule = findViewById(R.id.button_schedule);
        schedule.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(main_page_worker.this, doctor_schedule.class);
                startActivity(intent);
            }
        });
        Button messages = findViewById(R.id.button_messages);
        messages.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(main_page_worker.this, doctor_messages.class);
                startActivity(intent);
            }
        });


    }

}

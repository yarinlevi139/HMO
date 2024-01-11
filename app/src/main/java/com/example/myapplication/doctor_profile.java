package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class doctor_profile extends AppCompatActivity {
    private FirebaseFirestore db;
    private TextView nametv;
    private TextView typetv;
    private TextView emailtv;
    private TextView lastnametv;
    private TextView clinictv;

    private Button back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doc_profile);

        db = FirebaseFirestore.getInstance();
        nametv = findViewById(R.id.profile_name);
        lastnametv = findViewById(R.id.profile_last_name);
        typetv = findViewById(R.id.profile_type);
        emailtv = findViewById(R.id.profile_email);
        clinictv = findViewById(R.id.profile_clinic);
        back_btn = findViewById(R.id.profile_back);

        Button back = findViewById(R.id.profile_back);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(doctor_profile.this, main_page_worker.class);
                startActivity(intent);
            }
        });

        // Get the user's email from Firebase Auth
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userEmail = user.getEmail();

            // Retrieve user data from Firestore
            retrieveUserData(userEmail);
        } else {

        }

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void retrieveUserData(String userEmail) {
        db.collection("doctors")
                .whereEqualTo("email", userEmail)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // DocumentSnapshot will contain the data
                            String name = document.getString("name");
                            String lastName = document.getString("last_name");
                            String type = document.getString("type");
                            String email = document.getString("email");
                            String clinic = document.getString("clinic");

                            // Display the retrieved information
                            displayUserData(name, lastName, type, email, clinic);
                        }
                    } else {
                        // Handle Firestore query failure
                        Toast.makeText(doctor_profile.this, "Error getting user data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void displayUserData(String name, String lastName, String type, String email, String clinic) {
        // Update TextViews with the retrieved data
        nametv.setText("Name: " + name);
        lastnametv.setText("Last Name: " + lastName);
        typetv.setText("Type: " + type);
        emailtv.setText("Email: " + email);
        clinictv.setText("Clinic: " + clinic);
    }
}

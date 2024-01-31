package com.example.myapplication.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class client_profile extends AppCompatActivity {
    private FirebaseFirestore db;

    private TextView greet;
    private TextView first_name;
    private TextView last_name;
    private TextView age;
    private TextView id;
    private TextView email_address;

    private Button back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_profile);
        db = FirebaseFirestore.getInstance();
        greet = findViewById(R.id.greeting);
        first_name = findViewById(R.id.first_name);
        last_name = findViewById(R.id.last_name);
        age = findViewById(R.id.age);
        id = findViewById(R.id.id);
        email_address = findViewById(R.id.email_address);
        back_btn = findViewById(R.id.profile_back_client);

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
        db.collection("Clients")
                .whereEqualTo("email_address", userEmail)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // DocumentSnapshot will contain the data
                            String first_name = document.getString("first_name");
                            String last_name = document.getString("last_name");
                            String age = document.getString("age");
                            String id = document.getString("id");
                            String email_address = document.getString("email_address");

                            // Display the retrieved information
                            displayUserData(first_name, last_name, age, id, email_address);
                        }
                    } else {
                        // Handle Firestore query failure
                        Toast.makeText(client_profile.this, "Error getting user data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void displayUserData(String firstName, String lastName, String Age, String ID, String email) {
        // Update TextViews with the retrieved data
        greet.setText("Hello " + firstName);
        first_name.setText("    First Name: " + firstName);
        last_name.setText("    Last Name: " + lastName);
        age.setText("    Age: " + Age);
        id.setText("    ID: " + ID);
        email_address.setText("    Email: " + email);
    }
}


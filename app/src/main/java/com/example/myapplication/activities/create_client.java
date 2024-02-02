package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.LinkedHashMap;
import java.util.Map;

public class create_client extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_client_profile);


        EditText first_name = findViewById(R.id.first_name);
        EditText last_name = findViewById(R.id.last_name);
        EditText id = findViewById(R.id.id);
        EditText age = findViewById(R.id.age);

        // Get the email from the Intent
        String emailAddress = getIntent().getStringExtra("Email");

        Button done_btn = findViewById(R.id.done);

        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = first_name.getText().toString();
                String lastName = last_name.getText().toString();
                String idNumber = id.getText().toString();
                String ageNumber = age.getText().toString();

                //Check if name or last name is empty
                if (firstName.isEmpty() || lastName.isEmpty())
                {
                    Toast.makeText(create_client.this, "Fill in the blank parts", Toast.LENGTH_LONG).show();
                    return;
                }

                // Validate the age and ID
                if (!isValidAge(ageNumber)) {
                    Toast.makeText(create_client.this, "Please enter a valid age", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!isValidId(idNumber)) {
                    Toast.makeText(create_client.this, "Please enter a valid ID", Toast.LENGTH_LONG).show();
                    return;
                }

                // Check if the ID already exists since ID is Primary Key
                checkIdExistence(idNumber, exists -> {
                    if (exists) {
                        Toast.makeText(create_client.this, "Id already exists", Toast.LENGTH_LONG).show();
                    } else {
                        // Continue with profile creation
                        Map<String, Object> user = new LinkedHashMap<>();
                        user.put("id", idNumber);
                        user.put("first_name", firstName);
                        user.put("last_name", lastName);
                        user.put("age", ageNumber);
                        user.put("email_address", emailAddress);

                        db.collection("Clients")
                                .document(idNumber)
                                .set(user)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(create_client.this, "Profile added successfully", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(create_client.this, main_activity.class);
                                    startActivity(intent);
                                })
                                .addOnFailureListener(e -> Toast.makeText(create_client.this, "Error adding profile", Toast.LENGTH_LONG).show());
                    }
                });
            }
        });

    }

    /**
     * check if the id already exist since the id is PK
     * @param idNumber
     * @param listener
     */
    private void checkIdExistence(String idNumber, OnIdCheckListener listener) {
        DocumentReference docRef = db.collection("Clients").document(idNumber);

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                listener.onIdCheck(task.getResult().exists());
            } else {
                Toast.makeText(create_client.this, "Error checking document: " + task.getException(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private interface OnIdCheckListener {
        void onIdCheck(boolean exists);
    }


    /**
     * function that checks if the age is valid.
     * @param age
     * @return
     */
    private boolean isValidAge(String age) {
        try {
            int ageNumber = Integer.parseInt(age);
            return ageNumber >= 0 && ageNumber <= 120; // Assuming a valid age is between 0 and 120
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * function that checks if the ID is written in correct way.
     * @param id
     * @return
     */
    private boolean isValidId(String id) {
        // Check if the ID is exactly 9 digits long and contains only digits
        return id.length() == 9 && id.matches("\\d+");
    }
}

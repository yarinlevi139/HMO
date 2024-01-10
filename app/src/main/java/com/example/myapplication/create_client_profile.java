package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.LinkedHashMap;
import java.util.Map;

public class create_client_profile extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_client_profile);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

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

                // Validate the age and ID
                if (!isValidAge(ageNumber)) {
                    Toast.makeText(create_client_profile.this, "Please enter a valid age", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!isValidId(idNumber)) {
                    Toast.makeText(create_client_profile.this, "Please enter a valid ID", Toast.LENGTH_LONG).show();
                    return;
                }

                Map<String, Object> user = new LinkedHashMap<>();
                user.put("id", idNumber);
                user.put("first_name", firstName);
                user.put("last_name", lastName);
                user.put("age", ageNumber);
                user.put("email_address", emailAddress);

                db.collection("Clients")
                        .document(idNumber) // Use the client's id as the document id
                        .set(user)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(create_client_profile.this, "Profile added successfully", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(create_client_profile.this, MainActivity.class);
                            startActivity(intent);
                        })
                        .addOnFailureListener(e -> Toast.makeText(create_client_profile.this, "Error adding profile", Toast.LENGTH_LONG).show());
            }
        });
    }

    private boolean isValidAge(String age) {
        try {
            int ageNumber = Integer.parseInt(age);
            return ageNumber >= 0 && ageNumber <= 120; // Assuming a valid age is between 0 and 120
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidId(String id) {
        // Check if the ID is exactly 9 digits long and contains only digits
        return id.length() == 9 && id.matches("\\d+");
    }
}

package com.example.myapplication.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.classes.Message;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class member_messages extends AppCompatActivity {
    private Spinner doctorSpinner;
    private EditText messageEditText;
    private Button sendMessageButton;

    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_msg);

        firestore = FirebaseFirestore.getInstance();

        doctorSpinner = findViewById(R.id.spinnerDoctors);
        messageEditText = findViewById(R.id.message);
        sendMessageButton = findViewById(R.id.send_button);

        // Initialize the doctor spinner with a default value
        List<String> doctorNames = new ArrayList<>();
        doctorNames.add("Select a doctor");

        // Populate the doctor names list
        populateDoctorNames(doctorNames);

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the message text from the EditText
                String replyMessage = messageEditText.getText().toString();
                String DocName = doctorSpinner.getSelectedItem().toString();

                // Check if the client chose a doctor
                if (!DocName.trim().equals("Select a doctor")){
                    // Check if the reply message is empty
                    if (replyMessage.trim().isEmpty()) {
                        // Show a notification to the client
                        Toast.makeText(member_messages.this, "Message cannot be empty", Toast.LENGTH_SHORT).show();
                    }else{
                        sendMessage();
                    }
                }else{
                    // Show a notification to the client
                    Toast.makeText(member_messages.this, "You must choose a doctor", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void populateDoctorNames(List<String> doctorNames) {
        firestore.collection("doctors")
                .get()
                .addOnCompleteListener(new OnCompleteListener<com.google.firebase.firestore.QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<com.google.firebase.firestore.QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Add doctor names to the list only if the "name" field is not null
                            for (DocumentSnapshot document : task.getResult()) {
                                String doctorName = document.getString("name");

                                // Add to the list only if the name is not null
                                if (doctorName != null && !doctorName.isEmpty()) {
                                    doctorNames.add(doctorName);
                                }
                            }

                            // Create an ArrayAdapter with the doctor names list
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, doctorNames);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            // Set the adapter to the spinner
                            doctorSpinner.setAdapter(adapter);
                        } else {
                            // Handle failure
                            Toast.makeText(member_messages.this, "Error getting doctors", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void sendMessage() {
        String selectedDoctor = doctorSpinner.getSelectedItem().toString();
        String messageText = messageEditText.getText().toString();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Assuming you have the email and name of the currently logged in member
        assert currentUser != null;
        String senderEmail = currentUser.getEmail();

        // Get the current user's name from Firestore
        firestore.collection("Clients")
                .whereEqualTo("email_address", senderEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String senderName = document.getString("first_name");

                                // Get the receiver's information from Firestore
                                firestore.collection("doctors")
                                        .whereEqualTo("name", selectedDoctor)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        String receiverEmail = document.getString("email");
                                                        String receiverName = document.getString("name");

                                                        // Create a Message object and store it in Firestore
                                                        Message message = new Message();
                                                        message.setSender(senderName);
                                                        message.setReceiver(receiverName);
                                                        message.setSenderEmail(senderEmail);
                                                        message.setReceiverEmail(receiverEmail);
                                                        message.setMessageText(messageText);

                                                        firestore.collection("messages")
                                                                .add(message)
                                                                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                                                        if (task.isSuccessful()) {
                                                                            Toast.makeText(member_messages.this, "Message sent successfully", Toast.LENGTH_SHORT).show();
                                                                            finish();
                                                                        } else {
                                                                            Toast.makeText(member_messages.this, "Error sending message", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                } else {
                                                    Toast.makeText(member_messages.this, "Error fetching doctor information", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        } else {
                            Toast.makeText(member_messages.this, "Error fetching member information", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}

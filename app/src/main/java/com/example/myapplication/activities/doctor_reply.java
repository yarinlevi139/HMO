package com.example.myapplication.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.classes.Message;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class doctor_reply extends AppCompatActivity {

    private TextView receivedMessageTextView;
    private EditText replyEditText;
    private Button sendButton;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_answer);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        receivedMessageTextView = findViewById(R.id.textView2);
        replyEditText = findViewById(R.id.message);
        sendButton = findViewById(R.id.button2);

        // Retrieve the received message from the intent
        String receivedMessage = getIntent().getStringExtra("receivedMessage");
        receivedMessageTextView.setText(receivedMessage);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                // Get the message text from the EditText
                String replyMessage = replyEditText.getText().toString();

                // Check if the reply message is empty
                if (replyMessage.trim().isEmpty()) {
                    // Show a notification to the doctor
                    Toast.makeText(doctor_reply.this, "Message cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    // Get the sender and receiver details
                    String senderName = getIntent().getStringExtra("receiver");
                    String senderEmail = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
                    String receivedMessage = getIntent().getStringExtra("receivedMessage");
                    receivedMessageTextView.setText(senderName + ": " + receivedMessage);
                    String receiverName = getIntent().getStringExtra("sender1");
                    String receiverEmail = getIntent().getStringExtra("sender_email1");
                    String messageID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

                    // Create a new Message object
                    Message reply = new Message(senderName, receiverName, senderEmail, receiverEmail, replyMessage, messageID);

                    // Add the reply to the Firestore database
                    addReplyToFirestore(reply);
                }
            }
        });
    }

    private void addReplyToFirestore(Message reply) {
        // Add the reply to the Firestore database
        db.collection("messages")
                .add(reply)
                .addOnSuccessListener(documentReference -> {
                    // Handle success
                    // Optionally, you can show a success message or navigate back to the previous page
                    finish(); // Close the current activity after sending the reply
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    // Optionally, you can show an error message
                });
    }

}


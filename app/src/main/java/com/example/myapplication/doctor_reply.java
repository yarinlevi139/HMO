package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

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
            @Override
            public void onClick(View v) {
                // Get the sender and receiver details
                String senderName = "Doctor";  // Update with the actual doctor name
                String receiverName = getIntent().getStringExtra("sender1");
                String senderEmail = mAuth.getCurrentUser().getEmail();
                String receiverEmail = getIntent().getStringExtra("sender_email1");

                // Get the message text from the EditText
                String replyMessage = replyEditText.getText().toString();

                // Create a new Message object
                Message reply = new Message(senderName, receiverName, senderEmail, receiverEmail, replyMessage);

                // Add the reply to the Firestore database
                addReplyToFirestore(reply);
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


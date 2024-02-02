package com.example.myapplication.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
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
        setContentView(R.layout.doctor_reply);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        receivedMessageTextView = findViewById(R.id.textView2);
        replyEditText = findViewById(R.id.message);
        sendButton = findViewById(R.id.button2);

        String receivedMessage = getIntent().getStringExtra("receivedMessage");
        receivedMessageTextView.setText(receivedMessage);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                String replyMessage = replyEditText.getText().toString();

                if (replyMessage.trim().isEmpty()) {
                    Toast.makeText(doctor_reply.this, "Message cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    String senderName = getIntent().getStringExtra("receiver");
                    String senderEmail = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
                    String receivedMessage = getIntent().getStringExtra("receivedMessage");
                    receivedMessageTextView.setText("Message Sent!");
                    String receiverName = getIntent().getStringExtra("sender1");
                    String receiverEmail = getIntent().getStringExtra("sender_email1");
                    String oldMessageID = getIntent().getStringExtra("msgID");
                    String messageID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

                    Message reply = new Message(senderName, receiverName, senderEmail, receiverEmail, replyMessage, messageID);

                    addReplyToFirestore(reply, oldMessageID);
                }
            }
        });
    }

    /**
     *
     * @param reply
     * @param oldMessageID
     */
    private void addReplyToFirestore(Message reply, String oldMessageID) {
        db.collection("messages")
                .add(reply)
                .addOnSuccessListener(documentReference -> {
                    String deletedMessageId = oldMessageID;
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("deletedMessageId", deletedMessageId);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    // Optionally, you can show an error message
                });
    }
}

package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class doctor_messages extends AppCompatActivity {

    private static final String TAG = "DoctorMessagesActivity";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String sender1;

    private String sender_email1;
    private ListView messagesListView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> messagesList;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doc_messages);
        Button back = findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(doctor_messages.this, main_page_worker.class);
                startActivity(intent);
            }
        });


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        messagesListView = findViewById(R.id.messagesListView); // Replace with your ListView ID
        messagesList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, messagesList);
        messagesListView.setAdapter(adapter);

        // Fetch and display messages
        fetchAndDisplayMessages();
    }

    private void fetchAndDisplayMessages() {
        String doctorEmail = mAuth.getCurrentUser().getEmail();

        db.collection("messages")
                .whereEqualTo("receiverEmail", doctorEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            messagesList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String sender = document.getString("sender");
                                String messageText = document.getString("messageText");
                                String sender_email = document.getString("senderEmail");

                                messagesList.add(sender + ": " + messageText);
                            }

                            // Create a custom adapter
                            CustomAdapter customAdapter = new CustomAdapter(messagesList);
                            messagesListView.setAdapter(customAdapter);

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private class CustomAdapter extends ArrayAdapter<String> {

        public CustomAdapter(ArrayList<String> messagesList) {
            super(doctor_messages.this, R.layout.message_item, messagesList);
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View listItemView = convertView;
            if (listItemView == null) {
                listItemView = LayoutInflater.from(getContext()).inflate(R.layout.message_item, parent, false);
            }

            // Get the current message
            String currentMessage = getItem(position);

            // Set the text for the TextView
            TextView messageTextView = listItemView.findViewById(R.id.messageTextView);
            messageTextView.setText(currentMessage);

            // Set onClickListener for the "Reply" button
            Button replyButton = listItemView.findViewById(R.id.replyButton);
            replyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Add code to handle the "Reply" button click
                    // For now, just redirect to another page
                    Intent intent = new Intent(doctor_messages.this, doctor_reply.class);
                    intent.putExtra("sender1", sender1);
                    intent.putExtra("sender_email1", sender_email1);
                    startActivity(intent);
                }
            });

            return listItemView;
        }
    }
}
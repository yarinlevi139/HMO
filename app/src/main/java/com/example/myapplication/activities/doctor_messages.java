package com.example.myapplication.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.myapplication.classes.Message;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class doctor_messages extends AppCompatActivity {

    private static final String TAG = "DoctorMessagesActivity";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private ListView messagesListView;
    private ArrayAdapter<String> adapter;
    private ArrayList<Message> messagesList;

    public class MessagesAdapter extends ArrayAdapter<Message> {
        public MessagesAdapter(Context context, ArrayList<Message> messages) {
            super(context, 0, messages);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Message message = getItem(position);

            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.message_item, parent, false);
            }

            // Lookup view for data population
            TextView messageTextView = convertView.findViewById(R.id.messageTextView);

            // Populate the data into the template view using the data object
            messageTextView.setText(message.getSender() + ": " + message.getMessageText());

            // Set onClickListener for the "Reply" button
            Button replyButton = convertView.findViewById(R.id.replyButton);
            replyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Add code to handle the "Reply" button click
                    // For now, just redirect to another page
                    Intent intent = new Intent(doctor_messages.this, doctor_reply.class);
                    intent.putExtra("sender1", message.getSender());
                    intent.putExtra("sender_email1", message.getSenderEmail());
                    intent.putExtra("receivedMessage", message.getMessageText());
                    intent.putExtra("receiver",message.getReceiver());
                    startActivity(intent);
                }
            });
            // Return the completed view to render on screen
            return convertView;
        }
    }

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
        MessagesAdapter adapter = new MessagesAdapter(this, messagesList);
        messagesListView.setAdapter(adapter);
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
                                Message message = document.toObject(Message.class);
                                message.setId(document.getId());
                                messagesList.add(message);
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


    private class CustomAdapter extends ArrayAdapter<Message> {

        public CustomAdapter(ArrayList<Message> messagesList) {
            super(doctor_messages.this, R.layout.message_item, messagesList);
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View listItemView = convertView;
            if (listItemView == null) {
                listItemView = LayoutInflater.from(getContext()).inflate(R.layout.message_item, parent, false);
            }

            // Get the current Message object
            Message currentMessage = getItem(position);

            // Set the text for the TextView
            TextView messageTextView = listItemView.findViewById(R.id.messageTextView);
            messageTextView.setText(currentMessage.getSender() + ": " + currentMessage.getMessageText());

            // Set onClickListener for the "Reply" button
            Button replyButton = listItemView.findViewById(R.id.replyButton);
            replyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Add code to handle the "Reply" button click
                    Intent intent = new Intent(doctor_messages.this, doctor_reply.class);
                    intent.putExtra("sender1", currentMessage.getSender());
                    intent.putExtra("sender_email1", currentMessage.getSenderEmail());
                    intent.putExtra("receivedMessage", currentMessage.getMessageText());
                    intent.putExtra("receiver",currentMessage.getReceiver());

                    startActivity(intent);

                    // Delete the message from Firestore
                    db.collection("messages").document(currentMessage.getId())
                            .delete()
                            .addOnSuccessListener(aVoid -> {
                                // Document successfully deleted
                                // Remove the deleted message from the list
                                remove(currentMessage);

                                // Notify the adapter that the data set has changed
                                notifyDataSetChanged();
                            })
                            .addOnFailureListener(e -> {
                                // Handle the error
                            });
                }
            });


            return listItemView;
        }
    }

}
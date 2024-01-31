package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
    private CustomAdapter customAdapter;
    private ArrayList<Message> messagesList;

    Message currentMessage;

    private static final int REQUEST_CODE_REPLY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_messages);
        Button back = findViewById(R.id.back_button);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(doctor_messages.this, main_page_doctor.class);
                startActivity(intent);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        messagesListView = findViewById(R.id.messagesListView);
        messagesList = new ArrayList<>();
        customAdapter = new CustomAdapter(messagesList);
        messagesListView.setAdapter(customAdapter);

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
                            customAdapter.notifyDataSetChanged();
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

            currentMessage = getItem(position);

            TextView messageTextView = listItemView.findViewById(R.id.messageTextView);
            messageTextView.setText(currentMessage.getSender() + ": " + currentMessage.getMessageText());

            Button replyButton = listItemView.findViewById(R.id.replyButton);
            replyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(doctor_messages.this, doctor_reply.class);
                    intent.putExtra("sender1", currentMessage.getSender());
                    intent.putExtra("sender_email1", currentMessage.getSenderEmail());
                    intent.putExtra("receivedMessage", currentMessage.getMessageText());
                    intent.putExtra("receiver", currentMessage.getReceiver());
                    intent.putExtra("msgID", currentMessage.getId());

                    startActivityForResult(intent, REQUEST_CODE_REPLY);
                }
            });

            return listItemView;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_REPLY && resultCode == RESULT_OK) {
            String deletedMessageId = data.getStringExtra("deletedMessageId");
            deleteFromAdapter(deletedMessageId);
        }
    }

    private void deleteFromAdapter(String messageId) {
        for (Message message : messagesList) {
            if (message.getId().equals(messageId)) {
                messagesList.remove(message);
                customAdapter.notifyDataSetChanged();
                break;
            }
        }

        db.collection("messages").document(messageId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    // Document successfully deleted
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                });
    }
}

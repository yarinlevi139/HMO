package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class member_check_messages extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String TAG = "member_message";
    private FirebaseFirestore db;

    private ListView messagesListView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> messagesList;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_view_messages);

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
        String member_email = mAuth.getCurrentUser().getEmail();

        db.collection("messages")
                .whereEqualTo("receiverEmail", member_email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            messagesList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String sender = document.getString("sender");
                                String messageText = document.getString("messageText");
                                messagesList.add(sender + ": " + messageText);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}

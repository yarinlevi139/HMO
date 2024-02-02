package com.example.myapplication.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class client_view_messages extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String TAG = "member_message";
    private FirebaseFirestore db;

    private ListView messagesListView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> messagesList;
    private ArrayList<String> documentIdsList;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.client_view_messages);

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        documentIdsList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        messagesListView = findViewById(R.id.messagesListView);
        messagesList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, messagesList);
        messagesListView.setAdapter(adapter);
        messagesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected message and its document ID
                String selectedMessage = adapter.getItem(position);
                String documentId = documentIdsList.get(position);

                // Create and show the AlertDialog
                new AlertDialog.Builder(client_view_messages.this)
                        .setTitle("Delete message")
                        .setMessage("Do you want to delete this message?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Delete the selected message from Firebase
                                deleteMessage(documentId, selectedMessage);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        // Fetch and display messages
        fetchAndDisplayMessages();


    }

    /**
     * deletes the document message from the DB.
     * @param documentId
     * @param message
     */
    private void deleteMessage(String documentId, String message) {
        db.collection("messages")
                .document(documentId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                        // Remove the message and its document ID from the lists and notify the adapter
                        int index = messagesList.indexOf(message);
                        messagesList.remove(index);
                        documentIdsList.remove(index);
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }

    /**
     * this function visualize the message to the customer
     */
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
                            documentIdsList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String sender = document.getString("sender");
                                String messageText = document.getString("messageText");
                                messagesList.add(sender + ": " + messageText);
                                documentIdsList.add(document.getId());
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}

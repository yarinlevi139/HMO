package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class docsavailable extends AppCompatActivity {

    private ListView doctorsListView;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.docsavailable);

        doctorsListView = findViewById(R.id.doctorsListView);
        backButton = findViewById(R.id.backButton);

        String temp = getIntent().getStringExtra("message");

        TextView instructionText = findViewById(R.id.instructionText);
        instructionText.setText("Here is the list of doctors who are available for " + temp + ":");

        // Replace 'YourFirebaseCollection' and 'YourDoctorType' with your actual Firebase collection and doctor type
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference doctorsCollection = db.collection("doctors");

        doctorsCollection.whereEqualTo("type", temp)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<String> doctorNames = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Assuming "name" is the field you want to display
                                String doctorName = document.getString("name");
                                doctorNames.add(doctorName);
                            }

                            doctorlist_adapter adapter = new doctorlist_adapter(docsavailable.this, doctorNames);
                            doctorsListView.setAdapter(adapter);
                        } else {
                            // Handle errors
                            // You might want to display a message or take appropriate action
                        }
                    }
                });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

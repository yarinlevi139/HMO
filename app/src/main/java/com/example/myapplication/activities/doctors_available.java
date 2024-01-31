package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.adapters.doctorlist_adapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class doctors_available extends AppCompatActivity {

    private ListView doctorsListView;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctors_available);

        doctorsListView = findViewById(R.id.doctorsListView);
        backButton = findViewById(R.id.backButton);

        String doctorType = getIntent().getStringExtra("message");

        TextView instructionText = findViewById(R.id.instructionText);
        instructionText.setText("Here is the list of doctors who are available for " + doctorType + ":");

        // Replace 'YourFirebaseCollection' and 'YourDoctorType' with your actual Firebase collection and doctor type
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference doctorsCollection = db.collection("doctors");

        doctorsCollection.whereEqualTo("type", doctorType)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<String> doctorNames = new ArrayList<>();
                            List<String> doctorLastName = new ArrayList<>();
                            List<String> doctorEmail = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String doctorName = document.getString("name");
                                String docLastName = document.getString("last_name");
                                String docEmail = document.getString("email");
                                doctorNames.add(doctorName);
                                doctorLastName.add(docLastName);
                                doctorEmail.add(docEmail);
                            }

                            doctorlist_adapter adapter = new doctorlist_adapter(doctors_available.this, doctorNames);
                            doctorsListView.setAdapter(adapter);

                            doctorsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    // Perform the action you want when an item is clicked
                                    String selectedDoctor = doctorNames.get(position);
                                    String selectedDocLastName = doctorLastName.get(position);
                                    String selectedDocEmail = doctorEmail.get(position);

                                    Intent n = new Intent(doctors_available.this, choose_time_date.class);
                                    n.putExtra("Doc", selectedDoctor);
                                    n.putExtra("type", doctorType);
                                    n.putExtra("docLastName", selectedDocLastName);
                                    n.putExtra("docEmail", selectedDocEmail);
                                    startActivity(n);
                                }
                            });

                        } else {
                            Toast.makeText(getApplicationContext(),"Error while gathering available doctors",Toast.LENGTH_LONG);
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

package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class appointment_adapter extends ArrayAdapter<String> {

    List<Appointment> pList = new ArrayList<>();
    private FirebaseFirestore firestore;

    public appointment_adapter(Context context, List<String> items) {
        super(context, R.layout.list_item, items);
        firestore = FirebaseFirestore.getInstance();
    }

    public void setData(int position, String email, String time, String date) {
        pList.get(position).setClientEmail(email);
        pList.get(position).setHour(time);
        pList.get(position).setDate(date);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.list_item, parent, false);
        }

        // Get the TextView and Button from the custom layout
        TextView textView = view.findViewById(R.id.textView);
        Button button = view.findViewById(R.id.doctor_cancel_appointment);

        // Set the text and button click listener as needed
        String itemText = getItem(position);
        textView.setText(itemText);

        // Set a click listener for the button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the email, hour, and date for the selected appointment
                String email = pList.get(position).getClientEmail();
                String time = pList.get(position).getHour();
                String date = pList.get(position).getDate();

                // Delete the appointment from Firestore
                deleteAppointmentFromFirestore(email, time, date);

                // Remove the item from the ListView
                remove(getItem(position));
                notifyDataSetChanged();
            }
        });

        return view;
    }

    private void deleteAppointmentFromFirestore(String email, String time, String date) {
        firestore.collection("Appointments")
                .whereEqualTo("clientEmail", email)
                .whereEqualTo("hour", time)
                .whereEqualTo("date", date)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (com.google.firebase.firestore.QueryDocumentSnapshot document : task.getResult()) {
                            firestore.collection("Appointments").document(document.getId()).delete();
                        }
                    }
                });
    }
}

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

import java.util.List;


public class appointment_adapter extends ArrayAdapter<Appointment> {

    public appointment_adapter(Context context, List<Appointment> items) {
        super(context, R.layout.list_item, items);
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
        Button button = view.findViewById(R.id.button);

        // Set the text and button click listener as needed
        Appointment appointment = getItem(position);
        if (appointment != null) {
            String itemText = "Client Name: " + appointment.getName() + "\n" +
                    "Time: " + appointment.getHour();
            textView.setText(itemText);

            // Set a click listener for the button (you can customize this)
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle button click
                    // You can use 'position' to identify the clicked item if needed
                }
            });
        }

        return view;
    }
}

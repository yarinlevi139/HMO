package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class doctorlist_adapter extends BaseAdapter {
    private List<String> doctorNames;
    private LayoutInflater inflater;

    public doctorlist_adapter(Context context, List<String> doctorNames) {
        this.doctorNames = doctorNames;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return doctorNames.size();
    }

    @Override
    public Object getItem(int position) {
        return doctorNames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        TextView doctorNameTextView = view.findViewById(android.R.id.text1);
        doctorNameTextView.setText(doctorNames.get(position));

        return view;
    }
}
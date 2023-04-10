package com.ouapproj.ShakJoRDVapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.ouapproj.ShakJoRDVapp.R;

public class Orientation extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orientation);

        Intent i = getIntent();
        String taskTitle = i.getStringExtra("Desc");
        String date = i.getStringExtra("Date");
        String time = i.getStringExtra("Time");
        String location = i.getStringExtra("Location");
        String person = i.getStringExtra("Person");
        String contact = i.getStringExtra("Contact");

        TextView taskTitleTextView = findViewById(R.id.taskTitleTextView);
        taskTitleTextView.setText(taskTitle);

        TextView dateTextView = findViewById(R.id.dateTextView);
        dateTextView.setText(date);

        TextView timeTextView = findViewById(R.id.timeTextView);
        timeTextView.setText(time);

        TextView locationTextView = findViewById(R.id.locationTextView);
        locationTextView.setText(location);

        TextView personTextView = findViewById(R.id.personTextView);
        personTextView.setText(person);

        TextView contactTextView = findViewById(R.id.contactTextView);
        contactTextView.setText(contact);

    }
}
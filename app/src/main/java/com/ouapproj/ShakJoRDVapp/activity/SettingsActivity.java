package com.ouapproj.ShakJoRDVapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ouapproj.ShakJoRDVapp.R;
import com.ouapproj.ShakJoRDVapp.bottomSheetFragment.CreateTaskBottomSheetFragment;

import butterknife.BindView;

public class SettingsActivity extends BaseActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);



        TextView box1,box2,box3,box4,box5,box6;

        box1 = findViewById(R.id.text2mins);
        box2 = findViewById(R.id.text3mins);
        box3 = findViewById(R.id.text30mins);
        box4 = findViewById(R.id.text1hr);
        box5 = findViewById(R.id.text1day);
        box6 = findViewById(R.id.text2days);

        box1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingsActivity.this, "Alert delay: 2 mins", Toast.LENGTH_SHORT).show();

                CreateTaskBottomSheetFragment.setDelayVal(120000L);


            }
        });

        box2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingsActivity.this, "Alert delay: 3 mins", Toast.LENGTH_SHORT).show();
                CreateTaskBottomSheetFragment.setDelayVal(180000L);
            }
        });

        box3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingsActivity.this, "Alert delay: 30 mins", Toast.LENGTH_SHORT).show();
                Long time = 30*60*1000L;
                CreateTaskBottomSheetFragment.setDelayVal(time);
            }
        });

        box4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingsActivity.this, "Alert delay: 1 hour", Toast.LENGTH_SHORT).show();
                Long time = 60*60*1000L;
                CreateTaskBottomSheetFragment.setDelayVal(time);
            }
        });


        box5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingsActivity.this, "Alert delay: 1 day", Toast.LENGTH_SHORT).show();
                Long time = 24*30*60*1000L;
                CreateTaskBottomSheetFragment.setDelayVal(time);
            }
        });

        box6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingsActivity.this, "Alert delay: 2 days", Toast.LENGTH_SHORT).show();
                Long time = 2*24*30*60*1000L;
                CreateTaskBottomSheetFragment.setDelayVal(time);
            }
        });

    }
}
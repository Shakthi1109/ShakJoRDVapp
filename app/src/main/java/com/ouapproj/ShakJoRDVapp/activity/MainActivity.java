package com.ouapproj.ShakJoRDVapp.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ouapproj.ShakJoRDVapp.R;
import com.ouapproj.ShakJoRDVapp.adapter.TaskAdapter;
import com.ouapproj.ShakJoRDVapp.bottomSheetFragment.CreateTaskBottomSheetFragment;
import com.ouapproj.ShakJoRDVapp.broadcastReceiver.AlarmBroadcastReceiver;
import com.ouapproj.ShakJoRDVapp.database.DatabaseClient;
import com.ouapproj.ShakJoRDVapp.model.Task;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements CreateTaskBottomSheetFragment.setRefreshListener {

    @BindView(R.id.taskRecycler)
    RecyclerView taskRecycler;
    @BindView(R.id.addTask)
    TextView addTask;
    TaskAdapter taskAdapter;
    List<Task> tasks = new ArrayList<>();
    @BindView(R.id.noDataImage)
    ImageView noDataImage;
    @BindView(R.id.calendar)
    ImageView calendar;

    @BindView(R.id.mic)
    ImageView mic;

    @BindView(R.id.paint)
    ImageView paint;

    @BindView(R.id.appbg)
    LinearLayout appbg;


    private boolean isResume;
    MediaPlayer mediaPlayer;

    private final int[] colors = {R.color.bg1, android.R.color.holo_green_light,
            android.R.color.holo_orange_light, R.color.bg2,R.color.bg3,R.color.bg4, R.color.bg6};

    private int currentColorIndex = 0;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setUpAdapter();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ComponentName receiver = new ComponentName(this, AlarmBroadcastReceiver.class);
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
        Glide.with(getApplicationContext()).load(R.drawable.first_note).into(noDataImage);

//        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NOTIFICATION_POLICY);
//
//        if(permissionCheck != PackageManager.PERMISSION_GRANTED){
//            requestPermissions(new String[]{Manifest.permission.ACCESS_NOTIFICATION_POLICY},81);
//        }

        addTask.setOnClickListener(view -> {
            CreateTaskBottomSheetFragment createTaskBottomSheetFragment = new CreateTaskBottomSheetFragment();
            createTaskBottomSheetFragment.setTaskId(0, false, this, MainActivity.this);
            createTaskBottomSheetFragment.show(getSupportFragmentManager(), createTaskBottomSheetFragment.getTag());
        });

        getSavedTasks();

        calendar.setOnClickListener(view -> {
//            ShowCalendarViewBottomSheet showCalendarViewBottomSheet = new ShowCalendarViewBottomSheet();
//            showCalendarViewBottomSheet.show(getSupportFragmentManager(), showCalendarViewBottomSheet.getTag());

            Intent in = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(in);
        });

        mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.thankme);
        mediaPlayer.setLooping(true);



        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isResume){
                    isResume=true;
                    mic.setImageDrawable(getResources().getDrawable(R.drawable.mic));
                    mediaPlayer.start();

                }
                else{
                    isResume=false;
                    mic.setImageDrawable(getResources().getDrawable(R.drawable.mute));
                    mediaPlayer.pause();

                }
            }
        });

        paint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentColorIndex = (currentColorIndex + 1) % colors.length;

                // Get the current color
                int colorResId = colors[currentColorIndex];
                appbg.setBackgroundResource(colorResId);
            }
        });


//      for LANG FR & ENG
//        mic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(!isResume){
//                    isResume=true;
//                    mic.setImageDrawable(getResources().getDrawable(R.drawable.mic));
//
//                }
//                else{
//                    isResume=false;
//                    mic.setImageDrawable(getResources().getDrawable(R.drawable.mute));
//
//                }
//            }
//        });
    }

    public void setUpAdapter() {
        taskAdapter = new TaskAdapter(this, tasks, this);
        taskRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        taskRecycler.setAdapter(taskAdapter);
    }

    private void getSavedTasks() {

        class GetSavedTasks extends AsyncTask<Void, Void, List<Task>> {
            @Override
            protected List<Task> doInBackground(Void... voids) {
                tasks = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .dataBaseAction()
                        .getAllTasksList();
                return tasks;
            }

            @Override
            protected void onPostExecute(List<Task> tasks) {
                super.onPostExecute(tasks);
                noDataImage.setVisibility(tasks.isEmpty() ? View.VISIBLE : View.GONE);
                setUpAdapter();
            }
        }

        GetSavedTasks savedTasks = new GetSavedTasks();
        savedTasks.execute();
    }

    @Override
    public void refresh() {
        getSavedTasks();
    }
}

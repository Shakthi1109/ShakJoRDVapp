package com.ouapproj.ShakJoRDVapp.bottomSheetFragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.ouapproj.ShakJoRDVapp.R;
import com.ouapproj.ShakJoRDVapp.activity.MainActivity;
import com.ouapproj.ShakJoRDVapp.broadcastReceiver.AlarmBroadcastReceiver;
import com.ouapproj.ShakJoRDVapp.database.DatabaseClient;
import com.ouapproj.ShakJoRDVapp.model.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

public class CreateTaskBottomSheetFragment extends BottomSheetDialogFragment {


    private static final int RESULT_PICK_CONTACT = 1;
    Unbinder unbinder;
    @BindView(R.id.addTaskTitle)
    EditText addTaskTitle;
    @BindView(R.id.addTaskDescription)
    EditText addTaskDescription;
    @BindView(R.id.taskDate)
    EditText taskDate;
    @BindView(R.id.taskTime)
    EditText taskTime;
    @BindView(R.id.taskEvent)
    EditText taskEvent;

    @BindView(R.id.addTask)
    Button addTask;
    int taskId;
    boolean isEdit;
    Task task;
    int mYear, mMonth, mDay;
    int mHour, mMinute;
    setRefreshListener setRefreshListener;
    AlarmManager alarmManager;
    TimePickerDialog timePickerDialog;
    DatePickerDialog datePickerDialog;
    MainActivity activity;
    public static int count = 0;

    public static long delay =120000;

    private Calendar calendar;
    private PendingIntent pendingIntent;

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    public void setTaskId(int taskId, boolean isEdit, setRefreshListener setRefreshListener, MainActivity activity) {
        this.taskId = taskId;
        this.isEdit = isEdit;
        this.activity = activity;
        this.setRefreshListener = setRefreshListener;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint({"RestrictedApi", "ClickableViewAccessibility", "SuspiciousIndentation"})
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.fragment_create_task, null);
        unbinder = ButterKnife.bind(this, contentView);
        dialog.setContentView(contentView);
        alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
        addTask.setOnClickListener(view -> {
            if(validateFields())
            createTask();
        });
        if (isEdit) {
            showTaskFromId();
        }

        taskDate.setOnTouchListener((view, motionEvent) -> {
            if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(getActivity(),
                        (view1, year, monthOfYear, dayOfMonth) -> {
                            taskDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            datePickerDialog.dismiss();
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
            return true;
        });

        taskTime.setOnTouchListener((view, motionEvent) -> {
            if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                timePickerDialog = new TimePickerDialog(getActivity(),
                        (view12, hourOfDay, minute) -> {
                            taskTime.setText(hourOfDay + ":" + minute);
                            timePickerDialog.dismiss();
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
            return true;
        });


        taskEvent.setOnTouchListener((view, motionEvent) -> {

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS);

                    if(permissionCheck == PackageManager.PERMISSION_GRANTED){
                        // Get Contacts
                        Intent in = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                        startActivityForResult(in, RESULT_PICK_CONTACT);
                    }
                    else {
                        requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},80);
                    }
                }

            return true;
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode==RESULT_OK)
        {
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    contactPicked (data);
                    break;
            }
        }
        else
        {
            Toast.makeText(getActivity(), "Failed to pick contact",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void contactPicked(Intent data) {
        Cursor cursor = null;

        try {
            String phoneNo = null;
            Uri uri = data.getData ();
            cursor = getContext().getContentResolver().query (uri, null, null,null,null);
            cursor.moveToFirst ();
            int phoneIndex =0, phoneName=0;
            String contactName="";

            phoneIndex = cursor.getColumnIndex (ContactsContract.CommonDataKinds.Phone.NUMBER);
            phoneName = cursor.getColumnIndex (ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            phoneNo = cursor.getString (phoneIndex);
            contactName=cursor.getString(phoneName);
            taskEvent.setText (contactName+" : "+phoneNo);


        } catch (Exception e) {
            e.printStackTrace ();
        }
    }

    public boolean validateFields() {
        if(addTaskTitle.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(activity, "Please enter a valid title", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(taskDate.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(activity, "Please enter date", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(taskTime.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(activity, "Please enter time", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void createTask() {
        class saveTaskInBackend extends AsyncTask<Void, Void, Void> {
            @SuppressLint("WrongThread")
            @Override
            protected Void doInBackground(Void... voids) {
                Task createTask = new Task();
                createTask.setTaskTitle(addTaskTitle.getText().toString());
                createTask.setTaskDescrption(addTaskDescription.getText().toString());
                createTask.setDate(taskDate.getText().toString());
                createTask.setLastAlarm(taskTime.getText().toString());
                createTask.setEvent(taskEvent.getText().toString());

                if (!isEdit)
                    DatabaseClient.getInstance(getActivity()).getAppDatabase()
                            .dataBaseAction()
                            .insertDataIntoTaskList(createTask);
                else
                    DatabaseClient.getInstance(getActivity()).getAppDatabase()
                            .dataBaseAction()
                            .updateAnExistingRow(taskId, addTaskTitle.getText().toString(),
                                    addTaskDescription.getText().toString(),
                                    taskDate.getText().toString(),
                                    taskTime.getText().toString(),
                                    taskEvent.getText().toString());

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                  createAnAlarm();
                    push();

                }
                setRefreshListener.refresh();
                Toast.makeText(getActivity(), "Your event is been added", Toast.LENGTH_SHORT).show();
                dismiss();

            }
        }
        saveTaskInBackend st = new saveTaskInBackend();
        st.execute();
    }


//    @RequiresApi(api = Build.VERSION_CODES.M)
//    public void createAnAlarm() {
//        try {
//            String[] items1 = taskDate.getText().toString().split("-");
//            String dd = items1[0];
//            String month = items1[1];
//            String year = items1[2];
//
//            String[] itemTime = taskTime.getText().toString().split(":");
//            String hour = itemTime[0];
//            String min = itemTime[1];
//
//            Calendar cur_cal = new GregorianCalendar();
//            cur_cal.setTimeInMillis(System.currentTimeMillis());
//
//            Calendar cal = new GregorianCalendar();
//            cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
//            cal.set(Calendar.MINUTE, Integer.parseInt(min));
//            cal.set(Calendar.SECOND, 0);
//            cal.set(Calendar.MILLISECOND, 0);
//            cal.set(Calendar.DATE, Integer.parseInt(dd));
//
//            Intent alarmIntent = new Intent(activity, AlarmBroadcastReceiver.class);
//            alarmIntent.putExtra("TITLE", addTaskTitle.getText().toString());
//            alarmIntent.putExtra("DESC", addTaskDescription.getText().toString());
//            alarmIntent.putExtra("DATE", taskDate.getText().toString());
//            alarmIntent.putExtra("TIME", taskTime.getText().toString());
//
//            //PendingIntent pendingIntent = PendingIntent.getBroadcast(activity,count, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
////                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
////                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
////                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
////                } else {
////                    alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
////                }
////                count ++;
//                PendingIntent intent = PendingIntent.getBroadcast(activity, count, alarmIntent, 0);
//                alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() - 120000, intent);
//
////                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
////                        alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() - 5000, intent);
////                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
////                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() - 5000, intent);
////                        } else {
////                            alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() - 5000, intent);
////                        }
////                    }
////                count ++;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static void setDelayVal(Long val){
        delay = val;
    }

    public void push(){

        String[] items1 = taskDate.getText().toString().split("-");
        String dd = items1[0];
        String month = items1[1];
        String year = items1[2];

        String[] itemTime = taskTime.getText().toString().split(":");
        String hour = itemTime[0];
        String min = itemTime[1];

        Calendar cur_cal = new GregorianCalendar();
        cur_cal.setTimeInMillis(System.currentTimeMillis());

        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
        cal.set(Calendar.MINUTE, Integer.parseInt(min));
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.DATE, Integer.parseInt(dd));

        NotificationManager mNotificationManager =
                (NotificationManager) this.getActivity().getSystemService(getContext().NOTIFICATION_SERVICE);

        final NotificationManagerCompat[] notificationManagerCompat = new NotificationManagerCompat[1];
        final Notification[] notification = new Notification[1];

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel("YOUR_CHANNEL_ID",
                    "YOUR_CHANNEL_NAME", NotificationManager.IMPORTANCE_DEFAULT);

            channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DESCRIPTION");
            mNotificationManager.createNotificationChannel(channel);
        }

        Intent notifIntent = new Intent(super.getContext(),MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(super.getContext(),1,notifIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(super.getContext(), "YOUR_CHANNEL_ID")
                .setSmallIcon(R.drawable.notif_bell)
                .setContentTitle("Meeting Reminder !")
                .setContentText("You have a meeting at "+taskTime.getText().toString()+" with "+taskEvent.getText().toString())// message for notification
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

//        String tlala = cal.
//        Log.d("A:");
//        Log.e("B:"+cur_cal.getTimeInMillis());
//        Log.e("C:"+delay);

        Long lastDelay;
        if(cal.getTimeInMillis()-cur_cal.getTimeInMillis()-delay<0){
            lastDelay = 0L;
        }
        else{
            lastDelay = cal.getTimeInMillis()-cur_cal.getTimeInMillis()-delay;
        }

        final Handler handler = new Handler();
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        notification[0] = mBuilder.build();
                        notificationManagerCompat[0] = NotificationManagerCompat.from(activity.getBaseContext());

                        notificationManagerCompat[0].notify(1, notification[0]);
                    }
                });
            }

        }, lastDelay );



    }

    private void showTaskFromId() {
        class showTaskFromId extends AsyncTask<Void, Void, Void> {
            @SuppressLint("WrongThread")
            @Override
            protected Void doInBackground(Void... voids) {
                task = DatabaseClient.getInstance(getActivity()).getAppDatabase()
                        .dataBaseAction().selectDataFromAnId(taskId);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                setDataInUI();
            }
        }
        showTaskFromId st = new showTaskFromId();
        st.execute();
    }

    private void setDataInUI() {
        addTaskTitle.setText(task.getTaskTitle());
        addTaskDescription.setText(task.getTaskDescrption());
        taskDate.setText(task.getDate());
        taskTime.setText(task.getLastAlarm());
        taskEvent.setText(task.getEvent());
    }

    public interface setRefreshListener {
        void refresh();
    }
}

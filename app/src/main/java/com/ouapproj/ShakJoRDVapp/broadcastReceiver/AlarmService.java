package com.ouapproj.ShakJoRDVapp.broadcastReceiver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.ouapproj.ShakJoRDVapp.R;
import com.ouapproj.ShakJoRDVapp.activity.MainActivity;

public class AlarmService extends Service {
    private static final int NOTIFICATION_ID = 3;

    public AlarmService(String name) {
        super();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        onHandleIntent(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    protected void onHandleIntent(Intent intent) {

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationManagerCompat notificationManagerCompat;
        Notification notification;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel("YOUR_CHANNEL_ID",
                    "YOUR_CHANNEL_NAME", NotificationManager.IMPORTANCE_DEFAULT);

            channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DESCRIPTION");
            mNotificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "YOUR_CHANNEL_ID")
                .setSmallIcon(R.drawable.notif_bell) // notification icon
                .setContentTitle("Meeting Alert !") // title for notification
                .setContentText("You have a meeting. Click me to know more.")// message for notification
                .setAutoCancel(true); // clear notification after click

//        Intent i = new Intent(getApplicationContext(), MainActivity.class);
//        PendingIntent pi = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
//        mBuilder.setContentIntent(pi);
//        mNotificationManager.notify(0, mBuilder.build());

        notification = mBuilder.build();
        notificationManagerCompat = NotificationManagerCompat.from(this);

        notificationManagerCompat.notify(1,notification);




    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

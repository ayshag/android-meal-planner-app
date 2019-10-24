package com.example.fitnessapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class NotificationPublisher extends BroadcastReceiver {
private NotificationHelper notificationHelper;
    @Override
    public void onReceive(Context context, Intent intent) {
         notificationHelper = new NotificationHelper(context);

         sendOnChanel();
    }
    public void sendOnChanel(){

        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
        notificationHelper.getManager().notify(1,nb.build());
        Toast.makeText(notificationHelper, "Build", Toast.LENGTH_SHORT).show();
    }
}

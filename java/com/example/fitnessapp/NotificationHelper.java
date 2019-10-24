package com.example.fitnessapp;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

public class NotificationHelper extends ContextWrapper {
    public static final String channel="Ch1";
    public static final String channelName="FitnessApp";
    private NotificationManager nManager;

    public NotificationHelper(Context base) {
        super(base);
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
        createChannels();}
    }
    @TargetApi(Build.VERSION_CODES.O)
    public void createChannels(){
        NotificationChannel notificationChannel = new NotificationChannel(channel,channelName, NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.enableLights(true);
        notificationChannel.enableVibration(true);
        notificationChannel.setLightColor(R.color.colorPrimary);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(notificationChannel);
    }

    public NotificationManager getManager(){
        if(nManager == null){
            nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return nManager;
    }
    public NotificationCompat.Builder getChannelNotification(){
        return new NotificationCompat.Builder(getApplicationContext(),channel)
                .setContentTitle("FitnessApp")
                .setContentText("Live Healthier! Remember to follow your meal plan!")
                .setSmallIcon(R.drawable.ic_notifications_app);
    }
}

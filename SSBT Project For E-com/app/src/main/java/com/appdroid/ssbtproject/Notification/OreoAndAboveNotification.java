package com.appdroid.ssbtproject.Notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.appdroid.ssbtproject.R;
import com.google.firebase.messaging.RemoteMessage;

public class OreoAndAboveNotification extends ContextWrapper {

    private static  final String ID = "some_id";
    private static  final String NAME = "FirebaseAPP";

    private NotificationManager notificationManager;

    public OreoAndAboveNotification(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannel();
            Log.d("called","a");
        }
    }


    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {

        NotificationChannel notificationChannel = new NotificationChannel(ID,NAME, NotificationManager.IMPORTANCE_DEFAULT );
        notificationChannel.enableLights(true);
        notificationChannel.enableVibration(true);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(notificationChannel);


    }
    public NotificationManager getManager(){
        if (notificationManager == null){
            notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }



    @TargetApi(Build.VERSION_CODES.O)
    public Notification getONotifications(PendingIntent pendingIntent, Uri soundUri, RemoteMessage.Notification notification, Bitmap bitmap){


        if (notification.getImageUrl() == null){
            Notification notifications = new NotificationCompat.Builder(getApplicationContext(),ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(notification.getTitle())
                    .setContentIntent(pendingIntent)
                    .setContentText(notification.getBody())
                    .setLargeIcon(bitmap)
                    .setSound(soundUri)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(notification.getBody()))
                    .build();

            return notifications;
        }else {
            Notification notifications = new NotificationCompat.Builder(getApplicationContext(),ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(notification.getTitle())
                    .setContentIntent(pendingIntent)
                    .setContentText(notification.getBody())
                    .setLargeIcon(bitmap)
                    .setSound(soundUri)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(bitmap)
                            .bigLargeIcon(null))
                    .build();

            return notifications;
        }
    }

}

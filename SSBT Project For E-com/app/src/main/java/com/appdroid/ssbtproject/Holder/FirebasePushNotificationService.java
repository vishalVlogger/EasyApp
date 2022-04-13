package com.appdroid.ssbtproject.Holder;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.appdroid.ssbtproject.Activity.SplashActivity;
import com.appdroid.ssbtproject.Notification.OreoAndAboveNotification;
import com.appdroid.ssbtproject.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class FirebasePushNotificationService extends FirebaseMessagingService {
    PendingIntent pIntent;
    int m;

    @Override
    public void onMessageReceived(@NonNull @NotNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        Random random = new Random();
        m= random.nextInt(9999-1000)+1000;

        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        pIntent = (PendingIntent) PendingIntent.getActivities(this, m, new Intent[]{intent}, PendingIntent.FLAG_ONE_SHOT);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            Log.d("yetay lka bho","yetay");
            sentOAndAboveNotification(remoteMessage);
        }else {
            sentNormalNotification(remoteMessage);
          //  Toast.makeText(this, "lower", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("NewApi")
    private void sentOAndAboveNotification(RemoteMessage remoteMessage) {

        String imgUri  = remoteMessage.getData().get("flag");
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        Log.d("whaisuser",""+notification.getImageUrl());

        //int i = Integer.parseInt(user.replaceAll("[\\D]",""));

        Bitmap bitmap  =  getBitmapFromURL(String.valueOf(notification.getImageUrl()));

        Uri defSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        OreoAndAboveNotification notification1 = new OreoAndAboveNotification(this);
        Notification builder1 = notification1.getONotifications(pIntent,defSoundUri,notification,bitmap);

     /*   Notification notificationSummery = new NotificationCompat.Builder(this,ID)
                .setSmallIcon(Integer.parseInt(icon))
                .setStyle(new NotificationCompat.InboxStyle()
                        .addLine(title)
                        .setBigContentTitle(body)
                        .setSummaryText("News Boy"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setGroup("chetan")
                .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_CHILDREN)
                .setGroupSummary(true)
                .build(); */




/*
        int j = 0;

        if (i>0){
            j=i;
        }*/
        Random random = new Random();
        int m = random.nextInt(9999-1000)+1000;
        //SystemClock.sleep(2000);
        notification1.getManager().notify(m,builder1);
        //  notification1.getManager().notify(m,notificationSummery);
    }

    private void sentNormalNotification(RemoteMessage remoteMessage) {

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        Log.d("what is user",""+notification.getImageUrl());

        Uri defSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        if (notification.getImageUrl() == null){

         //   Bitmap bitmap = getBitmapFromURL(String.valueOf(notification.getImageUrl()));
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText(notification.getBody())
                    .setContentTitle(notification.getTitle())
                    .setLargeIcon(getBitmapFromURL(String.valueOf(notification.getImageUrl())))
                    .setPriority(Notification.PRIORITY_MAX)
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(getBitmapFromURL(String.valueOf(notification.getImageUrl())))
                            .bigLargeIcon(null))
                    .setAutoCancel(true)
                    .setSound(defSoundUri)
                    .setContentIntent(pIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(m,builder.build());

        }else {

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText(notification.getBody())
                    .setContentTitle(notification.getTitle())
                    .setPriority(Notification.PRIORITY_MAX)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(notification.getBody()))
                    .setAutoCancel(true)
                    .setSound(defSoundUri)
                    .setContentIntent(pIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(m,builder.build());
        }



    }

    private Bitmap getBitmapFromURL(String imgUri) {

        try {
            URL url = new URL(imgUri);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }

    }

}

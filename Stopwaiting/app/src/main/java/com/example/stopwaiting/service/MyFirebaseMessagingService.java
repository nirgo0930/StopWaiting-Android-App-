package com.example.stopwaiting.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.stopwaiting.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        //token을 서버로 전송
    }


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        //수신한 메시지를 처리
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

        NotificationCompat.Builder builder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String CHANNEL_ID = getString(R.string.channel_name);
            CharSequence CHANNEL_NAME = getString(R.string.channel_name);

            if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }
            builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        } else {
            builder = new NotificationCompat.Builder(getApplicationContext());
        }

        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();
        if (!remoteMessage.getData().isEmpty()) {
            Log.e("-------------------------isEmpty", "NO");
            Long qId = Long.valueOf(remoteMessage.getData().get("qId"));
            int myNum = Integer.valueOf( remoteMessage.getData().get("myNum"));

            if (myNum == -1) {


            }
        }

        builder.setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setLocalOnly(true);

        Notification notification = builder.build();
        notificationManager.notify(1, notification);


//        String token = FirebaseMessaging.getInstance().getToken().getResult();
//        Toast.makeText(this, "token", Toast.LENGTH_SHORT).show();
    }
}
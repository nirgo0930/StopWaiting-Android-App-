package com.example.stopwaitingadmin.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.stopwaitingadmin.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
//    private final String NOTICE_DEFAULT = "DEFAULT";    //알림 채널 생성을 위한 채널 아이디 디폴트값

    // 동시에 모든 디바이스 알람 울리는것 방지를 위해 일반 사용자 어플에 추가한 것
    // 웨이팅 개설 요청이 왔을 때 모든 관리자에게 알림 발송을 위해 제거
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        //token을 서버로 전송
    }


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

//        //알림 채널 생성
//        createNotificationChannel(NOTICE_DEFAULT, "default channel", NotificationManager.IMPORTANCE_HIGH);

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

        builder.setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_HIGH)          //알림 중요도
                .setSmallIcon(R.drawable.ic_launcher_background)        //알림의 아이콘
                .setAutoCancel(true)                                    //true - 클릭시 상단바 알림 삭제
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE) //소리, 진동
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);   //잠금화면 알림 노출

        Notification notification = builder.build();
        notificationManager.notify(1, notification);


//        String token = FirebaseMessaging.getInstance().getToken().getResult();
//        Toast.makeText(this, "token", Toast.LENGTH_SHORT).show();
    }

//    // 알림 채널 생성 함수
//    void createNotificationChannel(String channelId, String channelName, int importance)
//    {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//        {
//            NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
//            notificationManager.createNotificationChannel(new NotificationChannel(channelId, channelName, importance));
//        }
//    }
//
//    // 알림 생성 함수
//    void createNotification(String channelId, int id, String title, String text, Intent intent)
//    {
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setSmallIcon(R.drawable.ic_launcher_foreground)
//                .setContentTitle(title)
//                .setContentText(text)
//                .setContentIntent(pendingIntent)    // 클릭시 설정된 PendingIntent 가 실행된다
//                .setAutoCancel(true)                // true 면 클릭시 알림이 삭제된다
//                //.setTimeoutAfter(10000)            // 밀리세컨드 이후에 알림 삭제
//                //.setStyle(new NotificationCompat.BigTextStyle().bigText(text))
//                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
//                //.setWhen(System.currentTimeMillis()+10000)   // 30초 후?
//                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);   // 잠금화면 알람 노출
//
//        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
//        notificationManager.notify(id, builder.build());
//    }
}
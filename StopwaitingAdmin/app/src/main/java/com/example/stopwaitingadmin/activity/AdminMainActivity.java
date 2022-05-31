package com.example.stopwaitingadmin.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.stopwaitingadmin.R;

public class AdminMainActivity extends AppCompatActivity {
    public static Activity adminMainActivity;
    private final String NOTICE_DEFAULT = "DEFAULT";
    private Intent mainIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_main);

        adminMainActivity = AdminMainActivity.this;
        mainIntent = getIntent();

        //알림 채널 생성
        createNotificationChannel(NOTICE_DEFAULT, "default channel", NotificationManager.IMPORTANCE_HIGH);

        Intent ToLoginIntent = new Intent(this, LoginActivity.class);       // 클릭시 실행할 activity 를 지정
        ToLoginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        Button waitingList = findViewById(R.id.btn_WaitList);
        waitingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ToWaitingListIntent = new Intent(AdminMainActivity.this, WaitingListActivity.class);
                startActivity(ToWaitingListIntent);

                //startActivityForResult(intent, WAITING_LOCATION_REQUEST_CODE);
            }
        });

        Button reportedUser = findViewById(R.id.btn_reported_user);
        reportedUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ToReportedUserIntent = new Intent(AdminMainActivity.this, ReportedUserActivity.class);
                startActivity(ToReportedUserIntent);

                //startActivityForResult(intent, WAITING_LOCATION_REQUEST_CODE);
            }
        });

        Button btn_create = findViewById(R.id.btn_create);
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNotification(NOTICE_DEFAULT, 1, "웨이팅", "웨이팅 이름" + "(개설자 이름)", ToLoginIntent);

                //3초 딜레이
//                new java.util.Timer().schedule(
//                        new java.util.TimerTask() {
//                            @Override
//                            public void run() {
//                                createNotification(NOTICE_DEFAULT, 1, "웨이팅", "웨이팅 이름" + "(개설자 이름)", ToLoginIntent);
//                            }
//                        },ㅅ
//                        3000
//                );

                //createNotification(DEFAULT, 1, "title", "text", intent);
                //finish();
            }
        });
    }

    void createNotificationChannel(String channelId, String channelName, int importance)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId, channelName, importance));
        }
    }

    void createNotification(String channelId, int id, String title, String text, Intent intent)
    {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(pendingIntent)    // 클릭시 설정된 PendingIntent 가 실행된다
                .setAutoCancel(true)                // true 면 클릭시 알림이 삭제된다
                //.setTimeoutAfter(10000)            // 밀리세컨드 이후에 알림 삭제
                //.setStyle(new NotificationCompat.BigTextStyle().bigText(text))
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE) // 소리, 진동 설정
                //.setWhen(System.currentTimeMillis()+10000)   // 30초 후?
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);   // 잠금화면 알람 노출

        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(id, builder.build());
    }

}

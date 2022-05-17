package com.example.mapapitest;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private TextView mTextView;
//    private ActivityMainBinding binding;

    private ListView listView;

    private ArrayAdapter<String> textWaitingAdapter;
    private ArrayList<String> waitingList;
    private Intent timeIntent;

    //임시 타입
    private String str;

    private String str2;
    private String str3;
    private String[] strList;
    private ArrayList<String> screenList;

    private String type;

    private String time;
    private String count;
    private String location;

    public static final String NOTIFICATION_CHANNEL_ID = "4665";

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listView = findViewById(R.id.listView);

        waitingList = new ArrayList<>();

        screenList = new ArrayList<>();

        //이부분 서버에서 받아오는 리스트 부분
        str= "time/11:00/미용실";
        str2="time/12:00/북카페";
        str3="time/15:00/특식 배부";
        String str4="normal/5/zz";
        List<String> strs = new ArrayList<>();
        strs.add(str);
        strs.add(str2);
        strs.add(str3);
        strs.add(str4);


//        strList = str.split("/");
//
//        time=strList[1];
//        location = strList[2];
//
//        String text=time+" "+location;
//
//        //str =  "time/11:00/미용실";
//        //str2= "time/12:00/북카페";
//        waitingList.add(str);
//        waitingList.add(str2);`

        for(int i=0; i<4; i++){ //나중에 서버에서 받아온 수만큼 for문 돌리기
            strList = strs.get(i).split("/");
            if(strList[0].equals("normal")){
                screenList.add("\t"+strList[1]+"명\t\t\t"+strList[2]);
            }
            else{
                screenList.add("\t"+strList[1]+"\t\t"+strList[2]);
            }
        }

        textWaitingAdapter = new ArrayAdapter<String>(this, R.layout.listview_item, screenList);
        //textWaitingAdapter = new ListViewAdapter(this,R.layout.listview_item, screenList);

        listView.setAdapter(textWaitingAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //waitingList.get(i);

                type = strList[0];
                Intent intent = new Intent(getApplicationContext(), WaitingDetailActivity.class);
                if(type.equals("time")){
                    // 13:00 북카페
//                    time = strList[1];
//                    location = strList[2];
                    intent.putExtra("text",strs.get(i));
                    startActivity(intent);
                }

                else if(type.equals("normal")){
                    // 5 북카페
//                    count = strList[1];
//                    location = strList[2];
//                    intent.putExtra("count",count);
//                    intent.putExtra("location",location);
                    intent.putExtra("text",strs.get(i));
                    startActivity(intent);
                }

            }
        });

//        Notification.Builder mBuilder =
//                new Notification.Builder(MainActivity.this)
//                .setSmallIcon(R.drawable.moomin)
//                .setContentTitle("제목")
//                .setContentText("내용");


        //////////
        int NOTIFICATION_ID = 234;

        findViewById(R.id.btnTest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap mLargeIcon =
                        BitmapFactory.decodeResource(getResources(), R.drawable.moomin22);

                PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0,
                        new Intent(getApplicationContext(), MainActivity.class),
                        PendingIntent.FLAG_UPDATE_CURRENT);
////
//                NotificationCompat.Builder mBuilder =
//                        new NotificationCompat.Builder(MainActivity.this)
//                                .setSmallIcon(R.drawable.moomin)
//                                .setContentTitle("제목")
//                                .setContentText("내용")
//                                .setDefaults(Notification.DEFAULT_SOUND)
//                                .setLargeIcon(mLargeIcon)
//                                .setPriority(Notification.PRIORITY_DEFAULT)
//                                .setAutoCancel(true)
//                                .setContentIntent(pendingIntent);

                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                String id = "my_channel_01";
                CharSequence name ="my_channel";
                int importance = NotificationManager.IMPORTANCE_LOW;
                NotificationChannel mChannel = new NotificationChannel(id, name, importance);
                mChannel.enableLights(true);
                notificationManager.createNotificationChannel(mChannel);
//
                NotificationCompat.Builder notification = new NotificationCompat.Builder(MainActivity.this, id).setContentTitle("Title");


                notificationManager.notify(4665, notification.build());

            }
        });
    }

}
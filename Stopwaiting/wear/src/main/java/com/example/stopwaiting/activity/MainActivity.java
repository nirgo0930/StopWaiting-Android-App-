package com.example.stopwaiting.activity;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.stopwaiting.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private TextView mTextView;
    private ListView listView;
    private ArrayAdapter<String> textWaitingAdapter;
    private ArrayList<String> waitingList;

    //임시 타입
    private String str;

    private String str2;
    private String str3;
    private String[] strList;
    private ArrayList<String> screenList;

    private String type;

    public static Application mainApp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainApp = getApplication();
        setContentView(R.layout.activity_main);
        Log.e("boot", DataApplication.currentUserInfo.getStudentCode() + "/"
                + DataApplication.myWaiting.size() + "/" + DataApplication.waitingInfos.size());

        listView = findViewById(R.id.listView);
        waitingList = new ArrayList<>();
        screenList = new ArrayList<>();

        //이부분 서버에서 받아오는 리스트 부분
        str = "time/11:00/미용실";
        str2 = "time/12:00/북카페";
        str3 = "time/15:00/특식 배부";
        String str4 = "normal/5/zz";
        List<String> strs = new ArrayList<>();
        strs.add(str);
        strs.add(str2);
        strs.add(str3);
        strs.add(str4);

        for (int i = 0; i < 4; i++) { //나중에 서버에서 받아온 수만큼 for문 돌리기
            strList = strs.get(i).split("/");
            if (strList[0].equals("normal")) {
                screenList.add("\t" + strList[1] + "명\t\t\t" + strList[2]);
            } else {
                screenList.add("\t" + strList[1] + "\t\t" + strList[2]);
            }
        }
        textWaitingAdapter = new ArrayAdapter<String>(this, R.layout.listview_item, screenList);

        listView.setAdapter(textWaitingAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //waitingList.get(i);

                type = strList[0];
                Intent intent = new Intent(getApplicationContext(), WaitingDetailActivity.class);
                if (type.equals("time")) {
                    // 13:00 북카페
//                    time = strList[1];
//                    location = strList[2];
                    intent.putExtra("text", strs.get(i));
                    startActivity(intent);
                } else if (type.equals("normal")) {
                    // 5 북카페
//                    count = strList[1];
//                    location = strList[2];
//                    intent.putExtra("count",count);
//                    intent.putExtra("location",location);
                    intent.putExtra("text", strs.get(i));
                    startActivity(intent);
                }

            }
        });

    }

}

package com.example.stopwaiting.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.stopwaiting.R;
import com.example.stopwaiting.databinding.ActivityMainBinding;
import com.example.stopwaiting.dto.WearQueueDTO;
import com.example.stopwaiting.service.SendMessage;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    public static ArrayAdapter<String> textWaitingAdapter;
    private static ArrayList<String> waitingList;

    //임시 타입
    private static String str = null;
    private static String[] strList;
    private static ArrayList<String> screenList;
    public static List<String> strs;

    //public static Application mainApp;

    private ActivityMainBinding binding;

    public static final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            // 원래 하려던 동작 (UI변경 작업 등),
            Log.e("msg", "msg");

            textWaitingAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("boot", DataApplication.currentUserInfo.getStudentCode() + "/"
                + DataApplication.myWaiting.size());

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        waitingList = new ArrayList<>();
        screenList = new ArrayList<>();
        strs = new ArrayList<>();
        if (strs.size() == 0) {
            screenList.add("신청한 웨이팅이 없어요!");
        }

        textWaitingAdapter = new ArrayAdapter<String>(this, R.layout.listview_item, screenList);
        binding.listView.setAdapter(textWaitingAdapter);

        screenOpen();

        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (strList != null) {
                    Intent intent = new Intent(getApplicationContext(), WaitingDetailActivity.class);
                    intent.putExtra("text", strs.get(i));
                    startActivity(intent);
                }
            }
        });

        binding.btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "새로고침~~", Toast.LENGTH_SHORT).show();

                refreshScreen();

            }
        });

    }

    public void refreshScreen(){
        String datapath = "/my_path";
        String message = "refresh";
        new SendMessage(datapath, message, MainActivity.this).start();
        Log.e("refresh", "refresh 보냄");
    }

    public static void screenOpen() {
        waitingList = new ArrayList<>();
        ArrayList<String> tempList = new ArrayList<>();
        strs = new ArrayList<>();
        handler.sendMessage(handler.obtainMessage());

        //서버에서 받아오는 리스트 부분
        for(WearQueueDTO selectQueue : DataApplication.myWaiting){
            String postData = selectQueue.getQueueName() + "/" +
                    selectQueue.getLatitude() + "/" +
                    selectQueue.getLongitude() + "/" +
                    Long.toString(selectQueue.getWId());

            if (selectQueue.getTime().equals("NORMAL")) {
                str = "normal/" + selectQueue.getMyNum() + "/" +
                        postData;
            } else {//time인 경우
                str = "time/" + selectQueue.getTime() + "/" +
                        postData;
            }
            strs.add(str);
        }

        for (int i = 0; i < strs.size(); i++) { //나중에 서버에서 받아온 수만큼 for문 돌리기
            strList = strs.get(i).split("/");
            if (strList[0].equals("normal")) {
                tempList.add("\t" + strList[1] + "명\t\t\t" + strList[2]);
            } else {
                tempList.add("\t" + strList[1] + "\t\t" + strList[2]);
            }
            Log.e("add_cnt", String.valueOf(tempList.get(i).toString()));
            handler.sendMessage(handler.obtainMessage());
        }

        if (strs.size() == 0) {
            tempList.add("신청한 웨이팅이 없어요!");
        }
        screenList.clear();
        screenList.addAll(tempList);
        handler.sendMessage(handler.obtainMessage());
    }

}



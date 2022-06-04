package com.example.stopwaiting.activity;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.stopwaiting.R;
import com.example.stopwaiting.databinding.ActivityMainBinding;
import com.example.stopwaiting.service.SendMessage;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends Activity {

    private TextView mTextView;
    public static ListView listView;
    public static ArrayAdapter<String> textWaitingAdapter;
    private static ArrayList<String> waitingList;

    //임시 타입
    private static String str = null;
    private static String[] strList;
    private static ArrayList<String> screenList;
    public static List<String> strs;
    private String type;

    public static Application mainApp;

    private ActivityMainBinding binding;

    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor autoEdit;

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
//        mainApp = getApplication();
//        setContentView(R.layout.activity_main);
        Log.e("boot", DataApplication.currentUserInfo.getStudentCode() + "/"
                + DataApplication.myWaiting.size());

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //listView = findViewById(R.id.listView);
        waitingList = new ArrayList<>();
        screenList = new ArrayList<>();
        strs = new ArrayList<>();
        if (strs.size() == 0) {
            screenList.add("신청한 웨이팅이 없어요!");
        }

        textWaitingAdapter = new ArrayAdapter<String>(this, R.layout.listview_item, screenList);
        binding.listView.setAdapter(textWaitingAdapter);

        //listView.setAdapter(textWaitingAdapter);
        screenOpen();
//        textWaitingAdapter = new ArrayAdapter<String>(this, R.layout.listview_item, screenList);
//        listView.setAdapter(textWaitingAdapter);
//        screenOpen(textWaitingAdapter, screenList);

        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (strList != null) {
                    type = strList[0];
                    Intent intent = new Intent(getApplicationContext(), WaitingDetailActivity.class);
                    intent.putExtra("text", strs.get(i));
                    startActivity(intent);
                }
            }
        });

        binding.btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String datapath = "/my_path";
                String message = "refresh";
                new SendMessage(datapath, message, MainActivity.this).start();
                Log.e("refresh", "refresh 보냄");

            }
        });

//        ImageButton btn_refresh = (ImageButton) findViewById(R.id.btn_refresh);
//        btn_refresh.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String datapath = "/my_path";
//                String message = "refresh";
//                new SendMessage(datapath, message, MainActivity.this).start();
//                Log.e("refresh", "refresh 보냄");
//
//            }
//        });


    }

    public static void screenOpen() {
        waitingList = new ArrayList<>();
        ArrayList<String> tempList = new ArrayList<>();
        strs = new ArrayList<>();
        handler.sendMessage(handler.obtainMessage());
        //이부분 서버에서 받아오는 리스트 부분
        for (int i = 0; i < DataApplication.myWaiting.size(); i++) {
            if (DataApplication.myWaiting.get(i).getTime().equals("NORMAL")) {
                str = "normal/" + DataApplication.myWaiting.get(i).getMyNum() + "/" +
                        DataApplication.myWaiting.get(i).getQueueName() + "/" +
                        DataApplication.myWaiting.get(i).getLatitude() + "/" +
                        DataApplication.myWaiting.get(i).getLongitude() + "/" +
                        Long.toString(DataApplication.myWaiting.get(i).getQId());
            } else {//time인 경우
                str = "time/" + DataApplication.myWaiting.get(i).getTime() + "/" +
                        DataApplication.myWaiting.get(i).getQueueName() + "/" +
                        DataApplication.myWaiting.get(i).getLatitude() + "/" +
                        DataApplication.myWaiting.get(i).getLongitude() + "/" +
                        Long.toString(DataApplication.myWaiting.get(i).getQId());
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

//    class SendMessage extends Thread {
//        String path;
//        String message;
//        Context context;
//
//        SendMessage(String p, String m) {
//            path = p;
//            message = m;
//        }
//
//        public void run() {
//            context = getApplicationContext();
//            //Get all the nodes//
//
//            Task<List<Node>> nodeListTask =
//                    Wearable.getNodeClient(context).getConnectedNodes();
//            try {
//
//                //Block on a task and get the result synchronously//
//
//                List<Node> nodes = Tasks.await(nodeListTask);
//
//                //Send the message to each device//
//
//                for (Node node : nodes) {
//                    Task<Integer> sendMessageTask =
//                            Wearable.getMessageClient(context).sendMessage(node.getId(), path, message.getBytes());
//                    try {
//                        Integer result = Tasks.await(sendMessageTask);
//                        //Handle the errors//
//                    } catch (ExecutionException exception) {
//                        //TO DO//
//                    } catch (InterruptedException exception) {
//                        //TO DO//
//                    }
//
//                }
//
//            } catch (ExecutionException exception) {
//
//                //TO DO//
//
//            } catch (InterruptedException exception) {
//
//                //TO DO//
//
//            }
//        }
//    }


}



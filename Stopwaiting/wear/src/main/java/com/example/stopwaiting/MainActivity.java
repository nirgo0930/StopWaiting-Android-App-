package com.example.stopwaiting;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

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


////Create an OnClickListener//
//        talkButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String onClickMessage = "I just sent the handheld a message " + sentMessageNumber++;
//                textView.setText(onClickMessage);
////Make sure you’re using the same path value//
//
//                String datapath = "/my_path";
//                new SendMessage(datapath, onClickMessage).start();
//
//            }
//        });

//Register the local broadcast receiver//
        IntentFilter newFilter = new IntentFilter(Intent.ACTION_SEND);
        Receiver messageReceiver = new Receiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, newFilter);



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
        //textWaitingAdapter = new ListViewAdapter(this,R.layout.listview_item, screenList);

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

    private class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //to do

        }

        class SendMessage extends Thread {
            String path;
            String message;
//Constructor///

            SendMessage(String p, String m) {
                path = p;
                message = m;
            }

//Send the message via the thread. This will send the message to all the currently-connected devices//

            public void run() {
//Get all the nodes//
                Task<List<Node>> nodeListTask = Wearable.getNodeClient(MainActivity.mainApp.getApplicationContext()).getConnectedNodes();
                try {
//Block on a task and get the result synchronously//
                    List<Node> nodes = Tasks.await(nodeListTask);
//Send the message to each device//
                    for (Node node : nodes) {
                        Task<Integer> sendMessageTask =
                                Wearable.getMessageClient(MainActivity.mainApp.getApplicationContext()).sendMessage(node.getId(), path, message.getBytes());
                        try {
                            Integer result = Tasks.await(sendMessageTask);
//Handle the errors//
                        } catch (ExecutionException exception) {
//TO DO//
                        } catch (InterruptedException exception) {
//TO DO//
                        }
                    }
                } catch (ExecutionException exception) {
//TO DO//
                } catch (InterruptedException exception) {

//TO DO//
                }
            }
        }
    }
}

package com.example.stopwaiting;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends Activity {

    private TextView mTextView;

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

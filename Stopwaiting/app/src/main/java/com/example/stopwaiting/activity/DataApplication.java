package com.example.stopwaiting.activity;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.example.stopwaiting.dto.ImgItem;
import com.example.stopwaiting.dto.UserInfo;
import com.example.stopwaiting.dto.WaitingInfo;
import com.example.stopwaiting.dto.WaitingQueue;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DataApplication extends Application {
    static public ArrayList<WaitingInfo> testDBList = new ArrayList<>();
    static public ArrayList<ImgItem> testImageDBList = new ArrayList<>();
    static public ArrayList<WaitingQueue> testWaitingQueueDBList = new ArrayList<>();
    static public ArrayList<WaitingQueue> myWaiting = new ArrayList<>();
    static public Long qCnt;
    static public boolean isTest = true;

    static public String serverURL = "http://dodam123.dothome.co.kr/Login.php";
    static public RequestQueue requestQueue;
    static public UserInfo currentUser = new UserInfo();

    public ArrayList<WaitingInfo> getTestDBList() {
        return testDBList;
    }

    public void setTestDBList(ArrayList<WaitingInfo> testDBList) {
        this.testDBList = testDBList;
    }

    public ArrayList<ImgItem> getTestImageDBList() {
        return testImageDBList;
    }

    public void setTestImageDBList(ArrayList<ImgItem> testImageDBList) {
        this.testImageDBList = testImageDBList;
    }

    public static ArrayList<WaitingQueue> getTestWaitingQueueDBList() {
        return testWaitingQueueDBList;
    }

    public static void setTestWaitingQueueDBList(ArrayList<WaitingQueue> testWaitingQueueDBList) {
        DataApplication.testWaitingQueueDBList = testWaitingQueueDBList;
    }

    public boolean firstIsLater(String a, String b) {
        int hourA = Integer.valueOf(a.substring(0, 2));
        int minA = Integer.valueOf(a.substring(3));
        int hourB = Integer.valueOf(b.substring(0, 2));
        int minB = Integer.valueOf(b.substring(3));
        if (hourA > hourB) {
            return true;
        } else if (hourA == hourB) {
            if (minA > minB) {
                return true;
            }
        }
        return false;
    }

    protected Handler myHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Bundle stuff = msg.getData();
            stuff.getString("messageText");

            return true;
        }
    });

    public class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(getApplicationContext(), "receive test 입니다!", Toast.LENGTH_SHORT).show();
        }
    }

    class NewThread extends Thread {
        String path;
        String message;

        NewThread(String p, String m) {
            path = p;
            message = m;
        }

        public void run() {
            Task<List<Node>> wearableList = Wearable.getNodeClient(getApplicationContext()).getConnectedNodes();
            try {
                List<Node> nodes = Tasks.await(wearableList);
                for (Node node : nodes) {
                    Task<Integer> sendMessageTask =
                            Wearable.getMessageClient(getApplicationContext()).sendMessage(node.getId(), path, message.getBytes());
                    try {
                        Integer result = Tasks.await(sendMessageTask);

                    } catch (ExecutionException exception) {
                        //TO DO: Handle the exception//


                    } catch (InterruptedException exception) {

                    }

                }
            } catch (ExecutionException exception) {
                //TO DO: Handle the exception//

            } catch (InterruptedException exception) {
                //TO DO: Handle the exception//
            }

        }
    }

    public void talkClick() {
        String message = "Sending message.... ";

        new NewThread("/my_path", message).start();

    }
}

package com.example.stopwaiting.activity;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.example.stopwaiting.dto.ImgItem;
import com.example.stopwaiting.dto.UserInfo;
import com.example.stopwaiting.dto.WaitingInfo;
import com.example.stopwaiting.dto.WaitingQueue;
import com.example.stopwaiting.dto.WearQueueDTO;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Base64;

public class DataApplication extends Application {
    static public ArrayList<WaitingInfo> testDBList = new ArrayList<>();
    static public ArrayList<ImgItem> testImageDBList = new ArrayList<>();
    static public ArrayList<WaitingQueue> testWaitingQueueDBList = new ArrayList<>();

    static public Long qCnt;
    static public boolean isTest = true;

    static public String serverURL = "http://192.168.238.68:8080/api/v1";
    private String path = "/my_path";
    static public RequestQueue requestQueue;
    static public UserInfo currentUser = new UserInfo();
    static public ArrayList<WaitingQueue> myWaiting = new ArrayList<>();

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

    public class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(getApplicationContext(), "receive test 입니다!", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendRefresh() {
        PutDataMapRequest dataMap = PutDataMapRequest.create(path + "/userInfo");
        dataMap.getDataMap().putAsset("currentUser", null);
        PutDataRequest request = dataMap.asPutDataRequest();
        Task<DataItem> putTask = Wearable.getDataClient(getApplicationContext()).putDataItem(request);


        dataMap = PutDataMapRequest.create(path + "/myWaiting_first");
        dataMap.getDataMap().putAsset("myWaiting", null);
        request = dataMap.asPutDataRequest();
        putTask = Wearable.getDataClient(getApplicationContext()).putDataItem(request);

        dataMap = PutDataMapRequest.create(path + "/myWaiting");
        dataMap.getDataMap().putAsset("myWaiting", null);
        request = dataMap.asPutDataRequest();
        putTask = Wearable.getDataClient(getApplicationContext()).putDataItem(request);
    }

    public void sendUserInfo() {
        byte[] serializedMember = null;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(currentUser);
                serializedMember = baos.toByteArray();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Asset temp = Asset.createFromBytes(Base64.getEncoder().encode(serializedMember));
        PutDataMapRequest dataMap = PutDataMapRequest.create(path + "/userInfo");
        dataMap.getDataMap().putAsset("currentUser", temp);
        PutDataRequest request = dataMap.asPutDataRequest();
        Task<DataItem> putTask = Wearable.getDataClient(getApplicationContext()).putDataItem(request);
    }

    public void sendMyQueueInfo(ArrayList<WaitingInfo> waitingList) {
        byte[] serializedMember = null;

        WaitingInfo selectInfo = null;
        for (int i = 0; i < myWaiting.size(); i++) {
            for (int j = 0; j < waitingList.size(); j++) {
                if (waitingList.get(j).getName().equals(myWaiting.get(i).getQueueName())) {
                    selectInfo = waitingList.get(j);
                    break;
                }
            }

            WearQueueDTO selectItem = new WearQueueDTO(currentUser, myWaiting.get(i), selectInfo);

            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                    oos.writeObject(selectItem);
                    serializedMember = baos.toByteArray();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Asset temp = Asset.createFromBytes(Base64.getEncoder().encode(serializedMember));
            PutDataMapRequest dataMap;
            if (i == 0) {
                dataMap = PutDataMapRequest.create(path + "/myWaiting_first");
            } else {
                dataMap = PutDataMapRequest.create(path + "/myWaiting");
            }
            dataMap.getDataMap().putAsset("myWaiting", temp);

            PutDataRequest request = dataMap.asPutDataRequest();
            Task<DataItem> putTask = Wearable.getDataClient(getApplicationContext()).putDataItem(request);
        }
    }
}

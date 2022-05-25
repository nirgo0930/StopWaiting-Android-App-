package com.example.stopwaiting.service;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.example.stopwaiting.activity.DataApplication;
import com.example.stopwaiting.activity.MainActivity;
import com.example.stopwaiting.dto.UserInfo;
import com.example.stopwaiting.dto.WearQueueDTO;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MessageService extends WearableListenerService {
    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                String path = event.getDataItem().getUri().getPath();
                DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
                switch (path) {
                    case "/my_path/userInfo":
                        if (loadUserInfoFromAsset(dataMapItem.getDataMap().getAsset("currentUser")) != null) {
                            DataApplication.currentUserInfo = loadUserInfoFromAsset(dataMapItem.getDataMap().getAsset("currentUser"));
                            Log.e("test", "1++");
                        }
                        Log.e("test", "1");
                        break;
                    case "/my_path/myWaiting_first":
                        DataApplication.myWaiting = new ArrayList<>();
                        Log.e("test", "2--");
                    case "/my_path/myWaiting":
                        if (loadQueueInfoFromAsset(dataMapItem.getDataMap().getAsset("myWaiting")) != null) {
                            DataApplication.myWaiting.add(loadQueueInfoFromAsset(dataMapItem.getDataMap().getAsset("myWaiting")));
                            Log.e("test", "2++/" + loadQueueInfoFromAsset(dataMapItem.getDataMap().getAsset("myWaiting")).getQueueName());
                        }
                        Log.e("test", "2");
                        break;
                }
            }
        }
        String temp = "";
        for (int i = 0; i < DataApplication.myWaiting.size(); i++) {
            temp += DataApplication.myWaiting.get(i).getQueueName() + "/";
        }
        Log.e("data_changed", DataApplication.currentUserInfo.getStudentCode() + "/"
                + DataApplication.myWaiting.size() + "/" + temp);
    }

    public static UserInfo loadUserInfoFromAsset(Asset asset) {
        UserInfo data = null;
        if (asset == null) {
            return null;
        }
        // convert asset into a file descriptor and block until it's ready
        InputStream assetInputStream = null;
        try {
            assetInputStream = Tasks.await(Wearable.getDataClient(MainActivity.mainApp.getApplicationContext()).getFdForAsset(asset)).getInputStream();

            InputStreamReader inputStreamReader = new InputStreamReader(assetInputStream);
            Stream<String> streamOfString = new BufferedReader(inputStreamReader).lines();
            String streamToString = streamOfString.collect(Collectors.joining());
            byte[] serializedMember = Base64.getDecoder().decode(streamToString);
            try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(serializedMember))) {
                Object temp = ois.readObject();
                data = (UserInfo) temp;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (assetInputStream == null) {
            Log.w(TAG, "Requested an unknown Asset.");
            return null;
        }
        // decode the stream into a bitmap
        return data;
    }

    public WearQueueDTO loadQueueInfoFromAsset(Asset asset) {
        WearQueueDTO data = null;
        if (asset == null) {
            return null;
        }
        // convert asset into a file descriptor and block until it's ready
        InputStream assetInputStream = null;
        try {
            assetInputStream = Tasks.await(Wearable.getDataClient(getApplicationContext()).getFdForAsset(asset)).getInputStream();

            InputStreamReader inputStreamReader = new InputStreamReader(assetInputStream);
            Stream<String> streamOfString = new BufferedReader(inputStreamReader).lines();
            String streamToString = streamOfString.collect(Collectors.joining());
            byte[] serializedMember = Base64.getDecoder().decode(streamToString);
            try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(serializedMember))) {
                Object temp = ois.readObject();
                data = (WearQueueDTO) temp;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (assetInputStream == null) {
            Log.w(TAG, "Requested an unknown Asset.");
            return null;
        }
        // decode the stream into a bitmap
        return data;
    }
}

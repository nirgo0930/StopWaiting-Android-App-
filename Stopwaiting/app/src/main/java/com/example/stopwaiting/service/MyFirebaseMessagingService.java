package com.example.stopwaiting.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.stopwaiting.R;
import com.example.stopwaiting.activity.DataApplication;
import com.example.stopwaiting.dto.WaitingQueue;
import com.example.stopwaiting.dto.WearQueueDTO;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Base64;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        //수신한 메시지를 처리
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

        NotificationCompat.Builder builder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String CHANNEL_ID = getString(R.string.channel_name);
            CharSequence CHANNEL_NAME = getString(R.string.channel_name);

            if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }
            builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        } else {
            builder = new NotificationCompat.Builder(getApplicationContext());
        }

        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();
        if (!remoteMessage.getData().isEmpty()) {
            Log.e("-------------------------isEmpty", "NO");
            Long qId = Long.valueOf(remoteMessage.getData().get("qId"));
            int myNum = Integer.valueOf(remoteMessage.getData().get("myNum"));

            refreshMyNum(qId, myNum);
        }

        builder.setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setAutoCancel(true);

        notificationManager.notify(1, builder.build());


//        String token = FirebaseMessaging.getInstance().getToken().getResult();
//        Toast.makeText(this, "token", Toast.LENGTH_SHORT).show();
    }

    public void refreshMyNum(Long qId, int myNum) {
        byte[] serializedMember = null;

        WearQueueDTO tempQ = new WearQueueDTO();
        tempQ.setQId(qId);
        tempQ.setMyNum(myNum);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(tempQ);
                serializedMember = baos.toByteArray();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Asset temp = Asset.createFromBytes(Base64.getEncoder().encode(serializedMember));
        PutDataMapRequest dataMap;

        dataMap = PutDataMapRequest.create("/my_path/refresh");
        dataMap.getDataMap().putAsset("refreshData", temp);

        PutDataRequest request = dataMap.asPutDataRequest();
        Task<DataItem> putTask = Wearable.getDataClient(getApplicationContext()).putDataItem(request);

        if (DataApplication.isTest && myNum == -1) {
            for (int i = 0; i < DataApplication.testWaitingQueueDBList.size(); i++) {
                if (DataApplication.testWaitingQueueDBList.get(i).getQId().equals(qId)) {
                    WaitingQueue tempQu = DataApplication.testWaitingQueueDBList.get(i);
                    tempQu.removeWPerson(DataApplication.currentUser.getStudentCode());
                    DataApplication.testWaitingQueueDBList.set(i, tempQu);
                    break;
                }
            }

        }
    }
}
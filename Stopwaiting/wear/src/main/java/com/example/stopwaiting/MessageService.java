package com.example.stopwaiting;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.widget.Toast;

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
import java.util.Base64;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MessageService extends WearableListenerService {

//    @Override
//    public void onMessageReceived(MessageEvent messageEvent) {
//
//        if (messageEvent.getPath().equals("/my_path")) {
//            final String message = new String(messageEvent.getData());
//
////Broadcast the received data layer messages//
//
//            Intent messageIntent = new Intent();
//            messageIntent.setAction(Intent.ACTION_SEND);
//            messageIntent.putExtra("message", message);
//            LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);
//
//
//            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//
//        } else {
//            super.onMessageReceived(messageEvent);
//        }
//    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_CHANGED &&
                    event.getDataItem().getUri().getPath().equals("/my_path")) {
                DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
                Asset profileAsset = dataMapItem.getDataMap().getAsset("profileImage");

                UserInfo userInfo = loadUserInfoFromAsset(profileAsset);
            }
        }
    }

    public UserInfo loadUserInfoFromAsset(Asset asset) {
        UserInfo member = null;

        if (asset == null) {
            throw new IllegalArgumentException("Asset must be non-null");
        }
        // convert asset into a file descriptor and block until it's ready
        InputStream assetInputStream = null;
        int cnt = 0;
        try {
            Toast.makeText(getApplicationContext(), String.valueOf(cnt++), Toast.LENGTH_SHORT).show();
            assetInputStream = Tasks.await(Wearable.getDataClient(getApplicationContext()).getFdForAsset(asset)).getInputStream();

            InputStreamReader inputStreamReader = new InputStreamReader(assetInputStream);
            Stream<String> streamOfString = new BufferedReader(inputStreamReader).lines();
            String streamToString = streamOfString.collect(Collectors.joining());

            byte[] serializedMember = Base64.getDecoder().decode(streamToString);
            Toast.makeText(getApplicationContext(), serializedMember.toString(), Toast.LENGTH_SHORT).show();

            try (ByteArrayInputStream bais = new ByteArrayInputStream(serializedMember)) {
                try (ObjectInputStream ois = new ObjectInputStream(bais)) {
                    Object objectMember = ois.readObject();

                    member = (UserInfo) objectMember;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
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
        return member;
    }

}

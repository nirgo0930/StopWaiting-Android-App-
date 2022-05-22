package com.example.stopwaiting;

import android.content.Intent;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

public class MessageService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

        if (messageEvent.getPath().equals("/my_path")) {
            final String message = new String(messageEvent.getData());

//Broadcast the received data layer messages//

            Intent messageIntent = new Intent();
            messageIntent.setAction(Intent.ACTION_SEND);
            messageIntent.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);
            Toast.makeText(getApplicationContext(),"받아왔지롱",Toast.LENGTH_SHORT).show();

        }
        else {
            super.onMessageReceived(messageEvent);
        }
    }

}

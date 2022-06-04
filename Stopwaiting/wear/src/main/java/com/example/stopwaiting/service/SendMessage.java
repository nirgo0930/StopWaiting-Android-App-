package com.example.stopwaiting.service;

import android.content.Context;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class SendMessage extends Thread{
    String path;
    String message;
    Context context;

    public SendMessage(String p, String m, Context c) {
        path = p;
        message = m;
        context = c;
    }

    public void run() {
        //Get all the nodes//

        Task<List<Node>> nodeListTask =
                Wearable.getNodeClient(context).getConnectedNodes();
        try {

            //Block on a task and get the result synchronously//

            List<Node> nodes = Tasks.await(nodeListTask);

            //Send the message to each device//

            for (Node node : nodes) {
                Task<Integer> sendMessageTask =
                        Wearable.getMessageClient(context).sendMessage(node.getId(), path, message.getBytes());
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

package com.example.stopwaiting.service;

import android.content.Context;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.util.List;
import java.util.concurrent.ExecutionException;

import lombok.SneakyThrows;

public class SendMessage extends Thread{
    String path;
    String message;
    Context context;

    public SendMessage(String p, String m, Context c) {
        path = p;
        message = m;
        context = c;
    }

    @SneakyThrows
    public void run() {

        Task<List<Node>> nodeListTask =
                Wearable.getNodeClient(context).getConnectedNodes();
        try {

            List<Node> nodes = Tasks.await(nodeListTask);

            for (Node node : nodes) {
                Task<Integer> sendMessageTask =
                        Wearable.getMessageClient(context).sendMessage(node.getId(), path, message.getBytes());
                try {
                    Integer result = Tasks.await(sendMessageTask);
                } catch (ExecutionException exception) {

                } catch (InterruptedException exception) {
                }

            }

        } catch (ExecutionException exception) {
            throw new ExecutionException("ExecutionException",exception);
        } catch (InterruptedException exception) {

        }
    }
}

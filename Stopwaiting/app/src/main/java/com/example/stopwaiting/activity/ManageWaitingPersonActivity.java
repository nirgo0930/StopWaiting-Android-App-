package com.example.stopwaiting.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stopwaiting.R;
import com.example.stopwaiting.adapter.ManageWaitingPersonAdapter;
import com.example.stopwaiting.dto.UserInfo;
import com.example.stopwaiting.dto.WaitingQueue;

import java.util.ArrayList;

public class ManageWaitingPersonActivity extends AppCompatActivity {
    public static Activity manageWaitingPersonActivity;
    public static Long qId;
    private WaitingQueue wQueue;
    private RecyclerView recyclerView;
    private ManageWaitingPersonAdapter mListAdapter;
    private ArrayList<UserInfo> userList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiting_list);
        Intent intent = getIntent();
        qId = intent.getLongExtra("qId", 0L);
        manageWaitingPersonActivity = ManageWaitingPersonActivity.this;

        TextView txtTitle = findViewById(R.id.txtTitle);
        TextView txtNotice = findViewById(R.id.txtNotice);
        recyclerView = findViewById(R.id.recyclerView);

        txtTitle.setText("대기 명단");
        txtNotice.setText("길게 눌러서 명단에서 제거");

        wQueue = new WaitingQueue();
        userList = new ArrayList<>();

        for (int i = 0; i < ((DataApplication) getApplication()).testWaitingQueueDBList.size(); i++) {
            if (((DataApplication) getApplication()).testWaitingQueueDBList.get(i).getQId().equals(qId)) {
                wQueue = (((DataApplication) getApplication()).testWaitingQueueDBList.get(i));
                userList = wQueue.getWaitingPersonList();
                break;
            }
        }
        mListAdapter = new ManageWaitingPersonAdapter(this, userList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mListAdapter);


    }
}

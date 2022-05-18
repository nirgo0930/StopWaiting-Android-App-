package com.example.stopwaiting.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stopwaiting.Adapter.ManageWaitingPersonAdapter;
import com.example.stopwaiting.R;
import com.example.stopwaiting.DTO.WaitingQueue;

import java.util.ArrayList;

public class ManageWaitingPersonActivity extends AppCompatActivity {
    public static Activity manageWaitingPersonActivity;
    public static String qName;
    private WaitingQueue wQueue;
    private RecyclerView recyclerView;
    private ManageWaitingPersonAdapter mListAdapter;
    private ArrayList<String> nameList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiting_list);
        Intent intent = getIntent();
        qName = intent.getStringExtra("qName");
        manageWaitingPersonActivity = ManageWaitingPersonActivity.this;

        TextView txtTitle = findViewById(R.id.txtTitle);
        TextView txtNotice = findViewById(R.id.txtNotice);
        recyclerView =findViewById(R.id.recyclerView);


        txtTitle.setText("대기 명단");
        txtNotice.setText("길게 눌러서 명단에서 제거");

        wQueue = new WaitingQueue();
        nameList = new ArrayList<>();

        for (int i = 0; i < ((DataApplication) getApplication()).testWaitingQueueDBList.size(); i++) {
            if (((DataApplication) getApplication()).testWaitingQueueDBList.get(i).getQueueName().equals(qName)) {
                wQueue = (((DataApplication) getApplication()).testWaitingQueueDBList.get(i));
                nameList = wQueue.getWaitingPersonList();
                break;
            }
        }
        mListAdapter = new ManageWaitingPersonAdapter(this, nameList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mListAdapter);


    }
}

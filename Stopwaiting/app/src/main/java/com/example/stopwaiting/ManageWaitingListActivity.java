package com.example.stopwaiting;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ManageWaitingListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<WaitingQueue> mWaitingQueue;
    private ArrayList<WaitingListItem> mWaitingList;
    private MyWaitingListAdapter mListAdapter;
    public static Activity manageWaitingActivity;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiting_list);
        manageWaitingActivity = ManageWaitingListActivity.this;
        TextView result = findViewById(R.id.txtResult);
        recyclerView = findViewById(R.id.recyclerView);
        mWaitingList = new ArrayList<>();

        mWaitingQueue = new ArrayList<>();
        mWaitingQueue = ((DataApplication) getApplication()).myWaiting;

        if (mWaitingQueue.size() > 0) {
            for (int i = 0; i < mWaitingQueue.size(); i++) {
                WaitingInfo tempInfo = new WaitingInfo();
                for (int j = 0; j < ((DataApplication) getApplication()).testDBList.size(); j++) {
                    WaitingInfo temp = ((DataApplication) getApplication()).testDBList.get(j);
                    if (temp.getName().equals(mWaitingQueue.get(i).getQueueName())) {
                        tempInfo = temp;
                        break;
                    }
                }

                ImgItem tempImg = new ImgItem();
                for (int k = 0; k < ((DataApplication) getApplication()).testImageDBList.size(); k++) {
                    tempImg = ((DataApplication) getApplication()).testImageDBList.get(k);
                    if (tempImg.getName().equals(mWaitingQueue.get(i).getQueueName())) {
                        break;
                    }
                }

                mWaitingList.add(new WaitingListItem(tempImg.getUri(), mWaitingQueue.get(i).getQueueName(),
                        mWaitingQueue.get(i).getWaitingPersonList().indexOf(((DataApplication) getApplication()).userId),
                        tempInfo.getLocDetail()));

            }
            result.setText("개설한 웨이팅은 총 " + mWaitingList.size() + "건 입니다.");
        } else {

            result.setText("개설한 웨이팅이 없습니다.");
        }

        mListAdapter = new MyWaitingListAdapter(this, mWaitingList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mListAdapter);

        mListAdapter.notifyDataSetChanged();
    }
}
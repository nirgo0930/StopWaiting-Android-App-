package com.example.stopwaiting.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stopwaiting.dto.ImgItem;
import com.example.stopwaiting.adapter.MyWaitingListAdapter;
import com.example.stopwaiting.R;
import com.example.stopwaiting.dto.WaitingInfo;
import com.example.stopwaiting.dto.WaitingListItem;
import com.example.stopwaiting.dto.WaitingQueue;

import java.util.ArrayList;

public class CheckMyWaitingActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<WaitingQueue> mWaitingQueue;
    private ArrayList<WaitingListItem> mWaitingList;
    private MyWaitingListAdapter mListAdapter;
    public static Activity myWaitingActivity;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiting_list);
        myWaitingActivity = CheckMyWaitingActivity.this;
        TextView txtNotice = findViewById(R.id.txtNotice);
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
                        mWaitingQueue.get(i).getWaitingPersonList().indexOf(((DataApplication) getApplication()).currentUser),
                        tempInfo.getLocDetail()));

            }
            txtNotice.setText("신청한 웨이팅은 총 " + mWaitingList.size() + "건 입니다.");
        } else {
            txtNotice.setText("신청한 웨이팅이 없습니다.");
        }
        mListAdapter = new MyWaitingListAdapter(this, mWaitingList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mListAdapter);

        mListAdapter.notifyDataSetChanged();
    }

    public void myWaitingRequest(){

    }
}

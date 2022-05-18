package com.example.stopwaiting.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stopwaiting.DTO.ImgItem;
import com.example.stopwaiting.Adapter.ManageWaitingListAdapter;
import com.example.stopwaiting.R;
import com.example.stopwaiting.DTO.WaitingInfo;
import com.example.stopwaiting.DTO.WaitingListItem;
import com.example.stopwaiting.DTO.WaitingQueue;

import java.util.ArrayList;

public class ManageWaitingListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<WaitingQueue> mWaitingQueue;
    private ArrayList<WaitingListItem> mWaitingList;
    private ManageWaitingListAdapter mListAdapter;
    public static Activity manageWaitingActivity;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiting_list);
        manageWaitingActivity = ManageWaitingListActivity.this;
        TextView result = findViewById(R.id.txtNotice);
        TextView title = findViewById(R.id.txtTitle);
        title.setText("개설한 웨이팅");
        recyclerView = findViewById(R.id.recyclerView);
        mWaitingList = new ArrayList<>();

        mWaitingQueue = new ArrayList<>();
        for (int i = 0; i < ((DataApplication) getApplication()).testDBList.size(); i++) {
            WaitingInfo tempInfo = ((DataApplication) getApplication()).testDBList.get(i);
            if (tempInfo.getAdmin().equals(((DataApplication) getApplication()).userId)) {
                String qName = tempInfo.getName();
                for (int j = 0; j < ((DataApplication) getApplication()).testWaitingQueueDBList.size(); j++) {
                    WaitingQueue tempQ = ((DataApplication) getApplication()).testWaitingQueueDBList.get(j);
                    if (tempQ.getQueueName().equals(qName)) {
                        mWaitingQueue.add(tempQ);
                    }
                }
            }
        }

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
                boolean check = false;
                for (int j = 0; j < mWaitingList.size(); j++) {
                    if (mWaitingList.get(j).getName().equals(mWaitingQueue.get(i).getQueueName())) {
                        check = true;
                        break;
                    }
                }
                if (!check) {
                    mWaitingList.add(new WaitingListItem(tempImg.getUri(), mWaitingQueue.get(i).getQueueName(),
                            mWaitingQueue.get(i).getWaitingPersonList().size(),
                            tempInfo.getLocDetail()));
                }

            }
            result.setText("개설한 웨이팅은 총 " + mWaitingList.size() + "건 입니다.");
        } else {

            result.setText("개설한 웨이팅이 없습니다.");
        }

        mListAdapter = new ManageWaitingListAdapter(this, mWaitingList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mListAdapter);

        mListAdapter.notifyDataSetChanged();
    }
}
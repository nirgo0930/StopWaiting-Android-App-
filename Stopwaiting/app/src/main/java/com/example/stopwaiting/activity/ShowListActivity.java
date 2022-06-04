package com.example.stopwaiting.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stopwaiting.R;
import com.example.stopwaiting.adapter.ShowListAdapter;
import com.example.stopwaiting.dto.WaitingInfo;

import java.util.ArrayList;

public class ShowListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    private ArrayList<WaitingInfo> mWaitingList;
    private ShowListAdapter mListAdapter;
    private TextView txtNotice;
    public static Activity myWaitingActivity;

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiting_list);
        myWaitingActivity = ShowListActivity.this;
        txtNotice = findViewById(R.id.txtNotice);
        recyclerView = findViewById(R.id.recyclerView);
        mWaitingList = new ArrayList<>();

        waitingListRequest();

        mListAdapter = new ShowListAdapter(this, mWaitingList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mListAdapter);

        mListAdapter.notifyDataSetChanged();
    }

    public void waitingListRequest() {
        if (DataApplication.waitingList.size() > 0) {
            for (WaitingInfo tempInfo : DataApplication.waitingList) {
                mWaitingList.add(tempInfo);
            }
            txtNotice.setText("신청한 웨이팅은 총 " + mWaitingList.size() + "건 입니다.");
        } else {
            txtNotice.setText("신청한 웨이팅이 없습니다.");
        }
    }

}

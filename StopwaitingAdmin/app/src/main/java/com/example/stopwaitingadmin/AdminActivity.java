package com.example.stopwaitingadmin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {
    private List<AdminWaitingListItem> mItemList;
    private RecyclerView mListView;
    private AdminWaitingListAdapter mListAdapter;
    public static TextView txtNotice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_main);
        Intent intent = getIntent();
        LoginActivity.login_Activity.finish();
        txtNotice = findViewById(R.id.txtNotice);

        mItemList = new ArrayList<>();
        mListView = (RecyclerView) findViewById(R.id.recyclerView);
        mListAdapter = new AdminWaitingListAdapter(this, mItemList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mListView.setLayoutManager(layoutManager);
        mListView.setAdapter(mListAdapter);

        // List 설정
        bindList();

        mListAdapter.notifyDataSetChanged();

        txtNotice.setText("승인 대기중인 웨이팅이 " + String.valueOf(mListAdapter.mItemList.size()) + "건 존재합니다.");

    }


    private void bindList() {
        mItemList.add(new AdminWaitingListItem(R.drawable.empty_icon, "미용실", "한유현", "학생회관 B208"));
        mItemList.add(new AdminWaitingListItem(R.drawable.empty_icon, "특식배부", "방진성", "디지털관 DB134"));
        mItemList.add(new AdminWaitingListItem(R.drawable.empty_icon, "북카페", "이윤석", "학생회관 B113"));
    }

}
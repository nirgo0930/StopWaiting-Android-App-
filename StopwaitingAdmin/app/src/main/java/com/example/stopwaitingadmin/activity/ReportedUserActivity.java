package com.example.stopwaitingadmin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stopwaitingadmin.R;
import com.example.stopwaitingadmin.adapter.ReportedUserListAdapter;
import com.example.stopwaitingadmin.dto.ReportedUserListItem;

import java.util.ArrayList;
import java.util.List;

public class ReportedUserActivity extends AppCompatActivity {
    private List<ReportedUserListItem> mItemList;
    private RecyclerView mListView;
    private ReportedUserListAdapter mListAdapter;
    public static TextView txtNotice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reported_user_list);
        Intent intent = getIntent();
        LoginActivity.login_Activity.finish();
        txtNotice = findViewById(R.id.txtNotice);

        mItemList = new ArrayList<>();
        mListView = (RecyclerView) findViewById(R.id.recyclerView);
        mListAdapter = new ReportedUserListAdapter(this, mItemList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mListView.setLayoutManager(layoutManager);
        mListView.setAdapter(mListAdapter);

        // List 설정
        bindList();

        mListAdapter.notifyDataSetChanged();

        txtNotice.setText("신고된 회원이 " + String.valueOf(mItemList.size()) + "명 존재합니다.");

    }


    private void bindList() {
        mItemList.add(new ReportedUserListItem(20171250, "한유현", 1));
        mItemList.add(new ReportedUserListItem(12345678, "방진성", 1));
        mItemList.add(new ReportedUserListItem(135792468, "이윤석", 2));
    }

}
package com.example.stopwaitingadmin.activity;

import com.example.stopwaitingadmin.R;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stopwaitingadmin.adapter.AdminWaitingListAdapter;
import com.example.stopwaitingadmin.dto.AdminWaitingListItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WaitingListActivity extends AppCompatActivity {
    private List<AdminWaitingListItem> mWaitingItemList;
    private RecyclerView mWaitingListView;
    private AdminWaitingListAdapter mWaitingListAdapter;
    public static TextView txtNotice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_wait_list);
        LoginActivity.login_Activity.finish();

        waitingItemQueueRequest();

        txtNotice = findViewById(R.id.txtWaitingListNotice);

        mWaitingItemList = new ArrayList<>();
        mWaitingListView = findViewById(R.id.WaitingListRecyclerView);
        mWaitingListAdapter = new AdminWaitingListAdapter(this, mWaitingItemList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mWaitingListView.setLayoutManager(layoutManager);
        mWaitingListView.setAdapter(mWaitingListAdapter);

        // List 설정
        bindList();

        mWaitingListAdapter.notifyDataSetChanged();

        txtNotice.setText("승인 대기중인 웨이팅이 " + String.valueOf(mWaitingItemList.size()) + "건 존재합니다.");

    }


    private void bindList() {
        mWaitingItemList.add(new AdminWaitingListItem(1L, R.drawable.empty_icon, "미용실", "한유현", "학생회관 B208"));
        mWaitingItemList.add(new AdminWaitingListItem(2L, R.drawable.empty_icon, "특식배부", "방진성", "디지털관 DB134"));
        mWaitingItemList.add(new AdminWaitingListItem(3L, R.drawable.empty_icon, "북카페", "이윤석", "학생회관 B113"));
    }

    public void waitingItemQueueRequest(){
        JSONObject jsonBodyObj = new JSONObject();

        final String requestBody = String.valueOf(jsonBodyObj.toString());
    }

}
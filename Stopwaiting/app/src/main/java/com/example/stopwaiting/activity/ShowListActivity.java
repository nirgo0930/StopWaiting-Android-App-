package com.example.stopwaiting.activity;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stopwaiting.adapter.ShowListAdapter;
import com.example.stopwaiting.databinding.WaitingListBinding;
import com.example.stopwaiting.dto.WaitingInfo;

import java.util.ArrayList;

public class ShowListActivity extends AppCompatActivity {
    private ArrayList<WaitingInfo> mWaitingList;
    private ShowListAdapter mListAdapter;
    public static Activity showListActivity;

    private WaitingListBinding binding;

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = WaitingListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        showListActivity = ShowListActivity.this;
        mWaitingList = new ArrayList<>();

        binding.txtTitle.setText("개설된 웨이팅");

        waitingListRequest();

//        for (WaitingInfo info : mWaitingList) {
//            Log.e("info", info.getName());
//            if (info.getUrlList().size() == 0) {
//                info.addImage(Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + R.drawable.empty_icon).toString());
//            }
//        }

        mListAdapter = new ShowListAdapter(this, mWaitingList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setAdapter(mListAdapter);

        mListAdapter.notifyDataSetChanged();
    }

    public void waitingListRequest() {
        if (DataApplication.waitingList.size() > 0) {
            mWaitingList = DataApplication.waitingList;

            binding.txtNotice.setText("개설된 웨이팅은 총 " + mWaitingList.size() + "건 입니다.");
        } else {
            binding.txtNotice.setText("개설된 웨이팅이 없습니다.");
        }
    }

}

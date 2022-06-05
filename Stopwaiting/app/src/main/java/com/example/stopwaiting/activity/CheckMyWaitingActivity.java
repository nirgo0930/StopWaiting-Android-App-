package com.example.stopwaiting.activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stopwaiting.R;
import com.example.stopwaiting.adapter.MyWaitingListAdapter;
import com.example.stopwaiting.databinding.WaitingListBinding;
import com.example.stopwaiting.dto.ImgItem;
import com.example.stopwaiting.dto.WaitingInfo;
import com.example.stopwaiting.dto.WaitingListItem;
import com.example.stopwaiting.dto.WaitingQueue;

import java.util.ArrayList;

public class CheckMyWaitingActivity extends AppCompatActivity {
    private ArrayList<WaitingListItem> mWaitingList;
    private MyWaitingListAdapter mListAdapter;
    public static Activity myWaitingActivity;

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

        myWaitingActivity = CheckMyWaitingActivity.this;
        mWaitingList = new ArrayList<>();

        myWaitingRequest();

        mListAdapter = new MyWaitingListAdapter(this, mWaitingList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setAdapter(mListAdapter);

        mListAdapter.notifyDataSetChanged();
    }

    public void myWaitingRequest() {
        if (DataApplication.myWaiting.size() > 0) {
            for (WaitingQueue myQueue : DataApplication.myWaiting) {
                WaitingInfo tempInfo = new WaitingInfo();
                for (WaitingInfo selectInfo : ((DataApplication) getApplication()).waitingList) {
                    if (selectInfo.getName().equals(myQueue.getQueueName())) {
                        tempInfo = selectInfo;
                        break;
                    }
                }
                ImgItem tempImg = new ImgItem();
                if (tempInfo.getUrlList().size() > 0) {
                    Log.e("imgURL", tempInfo.getUrlList().get(0));
                    mWaitingList.add(new WaitingListItem(tempInfo.getUrlList().get(0), myQueue.getQueueName(), myQueue.getQId(),
                            myQueue.getWaitingPersonList().indexOf(DataApplication.currentUser), tempInfo.getLocDetail()));

                } else {
                    Log.e("imgURL", "android.resource://" + R.class.getPackage().getName() + "/" + R.drawable.empty_icon);
                    tempImg.setSUri(Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + R.drawable.empty_icon).toString());
                    mWaitingList.add(new WaitingListItem(tempImg.getUri(), myQueue.getQueueName(), myQueue.getQId(),
                            myQueue.getWaitingPersonList().indexOf(DataApplication.currentUser), tempInfo.getLocDetail()));
                }
            }
            binding.txtNotice.setText("신청한 웨이팅은 총 " + mWaitingList.size() + "건 입니다.");
        } else {
            binding.txtNotice.setText("신청한 웨이팅이 없습니다.");
        }
    }

}

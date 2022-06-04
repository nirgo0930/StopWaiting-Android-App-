package com.example.stopwaiting.activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stopwaiting.R;
import com.example.stopwaiting.adapter.MyWaitingListAdapter;
import com.example.stopwaiting.databinding.WaitingListBinding;
import com.example.stopwaiting.dto.ImgItem;
import com.example.stopwaiting.dto.WaitingInfo;
import com.example.stopwaiting.dto.WaitingListItem;

import java.util.ArrayList;

public class CheckMyWaitingActivity extends AppCompatActivity {
    //private RecyclerView recyclerView;

    private ArrayList<WaitingListItem> mWaitingList;
    private MyWaitingListAdapter mListAdapter;
    //private TextView txtNotice;
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
//        txtNotice = findViewById(R.id.txtNotice);
//        recyclerView = findViewById(R.id.recyclerView);
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
            for (int i = 0; i < DataApplication.myWaiting.size(); i++) {
                WaitingInfo tempInfo = new WaitingInfo();
                for (int j = 0; j < ((DataApplication) getApplication()).waitingList.size(); j++) {
                    WaitingInfo temp = ((DataApplication) getApplication()).waitingList.get(j);
                    if (temp.getName().equals(DataApplication.myWaiting.get(i).getQueueName())) {
                        tempInfo = temp;
                        break;
                    }
                }
                ImgItem tempImg = new ImgItem();
                if (tempInfo.getUrlList().size() > 0) {
                    tempImg.setSUri(tempInfo.getUrlList().get(0));
                } else {
                    tempImg.setUri(Uri.parse("drawable://" + R.drawable.empty_icon));
                }
                mWaitingList.add(new WaitingListItem(tempImg.getUri(), DataApplication.myWaiting.get(i).getQueueName(), DataApplication.myWaiting.get(i).getQId(),
                        DataApplication.myWaiting.get(i).getWaitingPersonList().indexOf(DataApplication.currentUser), tempInfo.getLocDetail()));
            }
            binding.txtNotice.setText("신청한 웨이팅은 총 " + mWaitingList.size() + "건 입니다.");
        } else {
            binding.txtNotice.setText("신청한 웨이팅이 없습니다.");
        }
    }

}

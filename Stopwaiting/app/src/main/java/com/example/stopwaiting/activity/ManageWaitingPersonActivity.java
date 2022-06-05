package com.example.stopwaiting.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stopwaiting.adapter.ManageWaitingPersonAdapter;
import com.example.stopwaiting.databinding.WaitingListBinding;
import com.example.stopwaiting.dto.UserInfo;

import java.util.ArrayList;

public class ManageWaitingPersonActivity extends AppCompatActivity {
    public static Activity manageWaitingPersonActivity;
    public static Long qId;
    //private RecyclerView recyclerView;

    private WaitingListBinding binding;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = WaitingListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        qId = intent.getLongExtra("qId", 0L);
        manageWaitingPersonActivity = ManageWaitingPersonActivity.this;

        //TextView txtTitle = findViewById(R.id.txtTitle);
        //TextView txtNotice = findViewById(R.id.txtNotice);
        //recyclerView = findViewById(R.id.recyclerView);

        binding.txtTitle.setText("대기 명단");
        binding.txtNotice.setText("길게 눌러서 명단에서 제거");

        ArrayList<UserInfo> userList = new ArrayList<>();
        userList = ManageWaitingActivity.selectQ.getWaitingPersonList();

        ManageWaitingPersonAdapter mListAdapter = new ManageWaitingPersonAdapter(this, userList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setAdapter(mListAdapter);
    }
}

package com.example.stopwaitingadmin.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.stopwaitingadmin.R;

public class AdminMainActivity extends AppCompatActivity {
    public static Activity adminMainActivity;
    private Intent mainIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_main);

        adminMainActivity = AdminMainActivity.this;
        mainIntent = getIntent();

        Button waitingList = findViewById(R.id.btn_WaitList);
        waitingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ToWaitingListIntent = new Intent(AdminMainActivity.this, WaitingListActivity.class);
                startActivity(ToWaitingListIntent);

                //startActivityForResult(intent, WAITING_LOCATION_REQUEST_CODE);
            }
        });

        Button reportedUser = findViewById(R.id.btn_reported_user);
        reportedUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ToReportedUserIntent = new Intent(AdminMainActivity.this, ReportedUserActivity.class);
                startActivity(ToReportedUserIntent);

                //startActivityForResult(intent, WAITING_LOCATION_REQUEST_CODE);
            }
        });
    }

}

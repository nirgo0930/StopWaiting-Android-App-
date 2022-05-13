package com.example.stopwaiting;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class ManageWaitingActivity extends AppCompatActivity {
    public static Activity manageWaitingActivity;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.manage_waiting);
        manageWaitingActivity = ManageWaitingActivity.this;


    }
}

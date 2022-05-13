package com.example.stopwaiting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MyPageActivity extends AppCompatActivity {
    public static Activity myPageActivity;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage);

        myPageActivity = MyPageActivity.this;
        ImageView img = findViewById(R.id.imageView);
        img.setImageResource(R.drawable.human_icon);
        if (SettingInfoActivity.setting_info_Activity != null) {
            if (SettingTimeActivity.setting_time_Activity != null) {
                SettingTimeActivity.setting_time_Activity.finish();
            }
            SettingLocationActivity.setting_loc_Activity.finish();
            SettingInfoActivity.setting_info_Activity.finish();
        }
        Intent myIntent = getIntent();
        TextView name = findViewById(R.id.txtMyName);
        name.setText(((DataApplication) getApplication()).userId + " 님");

        Button myWaiting = findViewById(R.id.btnMyWaiting);
        myWaiting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = myIntent;
                intent.setClass(MyPageActivity.this, CheckMyWaitingActivity.class);

                startActivity(intent);
            }
        });

        Button createWaiting = findViewById(R.id.btnNewWaiting);
        createWaiting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = myIntent;
                intent.setClass(MyPageActivity.this, SettingLocationActivity.class);

                startActivity(intent);
            }
        });

        Button manageWaiting = findViewById(R.id.btnManageWaiting);
        manageWaiting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = myIntent;
                intent.setClass(MyPageActivity.this, ManageWaitingActivity.class);

                startActivity(intent);
            }
        });

        Button logout = findViewById(R.id.btnLogOut);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyPageActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
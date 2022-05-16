package com.example.stopwaiting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ManageWaitingActivity extends AppCompatActivity {
    public static Activity manageWaitingActivity;
    private Button btnCheckOut;
    private Button btnCheckIn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_waiting);
        Intent intent = getIntent();
        manageWaitingActivity = ManageWaitingActivity.this;

        btnCheckIn = (Button) findViewById(R.id.btnCheckIn);
        btnCheckOut = (Button) findViewById(R.id.btnCheckOut);


        btnCheckIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ManageWaitingActivity.this, ScanQRActivity.class);
                startActivity(intent);
            }
        });

        btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}

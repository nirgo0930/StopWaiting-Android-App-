package com.example.stopwaiting.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.stopwaiting.R;

public class WaitingDetailActivity extends Activity {

    private TextView mTextView;
//private ActivityMain2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        //binding = ActivityMain2Binding.inflate(getLayoutInflater());
        //setContentView(binding.getRoot());

        Intent intent = getIntent();
//        String time = intent.getStringExtra("time");
//        String location = intent.getStringExtra("location");


        String text = intent.getStringExtra("text");
        String[] textList = text.split("/");

        TextView textLoc = findViewById(R.id.textLoc);
        TextView textTime = findViewById(R.id.textTime);
        if(textList[0].equals("normal")){
            textTime.setText(textList[1]+"명 남음");
        }
        else{
            textTime.setText(textList[1]);
        }
        textLoc.setText(textList[2]);

        //취소하기
        Button btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        //QR인식
        Button btnQR = (Button) findViewById(R.id.btnQR);
        btnQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), QRActivity.class);
                startActivity(intent);
            }
        });

        //위치확인
        Button btnLoc = (Button) findViewById(R.id.btnLoc);
        btnLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
            }
        });

    }
}
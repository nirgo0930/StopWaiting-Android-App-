package com.example.stopwaiting.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.stopwaiting.R;

public class WaitingDetailActivity extends Activity {

    private TextView mTextView;
    private double latitude;
    private double longitude;
    private String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        Intent intent = getIntent();

        String text = intent.getStringExtra("text");
        String[] textList = text.split("/");

        location = textList[2];
        latitude = Double.parseDouble(textList[3]);
        longitude = Double.parseDouble(textList[4]);
        TextView textLoc = findViewById(R.id.textLoc);
        TextView textTime = findViewById(R.id.textTime);

        if(textList[0].equals("normal")){
            textTime.setText(textList[1]+"명 남음");
        }
        else{
            textTime.setText(textList[1]);
        }
        textLoc.setText(location);

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
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                intent.putExtra("location",location);
                startActivity(intent);
            }
        });

    }
}
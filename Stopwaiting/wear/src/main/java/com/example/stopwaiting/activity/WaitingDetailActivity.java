package com.example.stopwaiting.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.stopwaiting.databinding.ActivityDetailBinding;
import com.example.stopwaiting.service.SendMessage;

public class WaitingDetailActivity extends Activity {

    private double latitude;
    private double longitude;
    private String location;
    private String qId;

    private ActivityDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();

        String text = intent.getStringExtra("text");
        String[] textList = text.split("/");

        location = textList[2];
        latitude = Double.parseDouble(textList[3]);
        longitude = Double.parseDouble(textList[4]);
        qId = textList[5];

        if(textList[0].equals("normal")){
            if(textList[1].equals("0")){
                binding.textTime.setText(DataApplication.currentUserInfo.getName()+"님이 체크인하실 차례에용");
            }
            else{
                binding.textTime.setText(textList[1]+"명 남았어용");
            }
        }
        else{
            binding.textTime.setText(textList[1]);
        }
        binding.textLoc.setText(location);

        //취소하기
        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(WaitingDetailActivity.this)
                        .setTitle(location)
                        .setMessage("해당 웨이팅을 취소하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //예약 취소
                                String datapath = "/my_path";
                                String onClickMessage = String.valueOf(DataApplication.currentUserInfo.getStudentCode())
                                        + "/"+ qId;
                                new SendMessage(datapath,onClickMessage,WaitingDetailActivity.this).start();
                                onBackPressed();

                                Toast.makeText(getApplicationContext(), "예약 취소 완료 \n 새로고침 해주세요", Toast.LENGTH_SHORT).show();
                                Log.e("test", onClickMessage);
                            }
                        })
                        .setNegativeButton("취소",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });

        //QR인식
        binding.btnQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), QRActivity.class);
                startActivity(intent);
            }
        });

        //위치확인
        binding.btnLoc.setOnClickListener(new View.OnClickListener() {
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
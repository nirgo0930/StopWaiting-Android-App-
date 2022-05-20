package com.example.stopwaiting.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.stopwaiting.dto.ImgItem;
import com.example.stopwaiting.R;
import com.example.stopwaiting.dto.WaitingInfo;
import com.example.stopwaiting.dto.WaitingQueue;

import java.util.ArrayList;

public class WaitingNormalActivity extends AppCompatActivity {
    private int pivot;
    private ArrayList<ImgItem> imgItems;
    private TextView name, imgCnt, waitCnt, locDetail, info;
    private ImageView imageView;
    private WaitingInfo mWaitingInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiting_normal);
        Intent intent = getIntent();

        name = findViewById(R.id.txtWaitingName);
        locDetail = findViewById(R.id.txtLocDeatail);
        name.setText(intent.getStringExtra("name"));

        mWaitingInfo = new WaitingInfo();
        for (int i = 0; i < ((DataApplication) getApplication()).testDBList.size(); i++) {
            WaitingInfo temp = ((DataApplication) getApplication()).testDBList.get(i);
            if (temp.getName().equals(name.getText())) {
                mWaitingInfo = temp;
            }
        }
        locDetail.setText(mWaitingInfo.getLocDetail());

        imgItems = new ArrayList<>();
        for (int i = 0; i < ((DataApplication) getApplication()).testImageDBList.size(); i++) {
            ImgItem temp = ((DataApplication) getApplication()).testImageDBList.get(i);
            if (temp.getName().equals(name.getText())) {
                imgItems.add(temp);
            }
        }

        imgCnt = findViewById(R.id.txtImgCnt);
        imageView = findViewById(R.id.imageView);
        pivot = 0;
        String content = "";
        if (imgItems.size() > 0) {
            for (int i = 0; i < imgItems.size(); i++) {
                content = content + "·";
            }
            imgCnt.setText(content);
            setImg();
        } else {
            content = "·";
            imgCnt.setText(content);
            imageView.setImageResource(R.drawable.empty_icon);
        }

        waitCnt = findViewById(R.id.txtWaitCnt);
        for (int i = 0; i < ((DataApplication) getApplication()).testWaitingQueueDBList.size(); i++) {
            WaitingQueue temp = ((DataApplication) getApplication()).testWaitingQueueDBList.get(i);
            if (temp.getQueueName().equals(name.getText()) && temp.getTime().equals("normal")) {
                if (temp.getWaitingPersonList() != null) {
                    waitCnt.setText("현재 " + String.valueOf(temp.getWaitingPersonList().size()) + "명 대기중");
                } else {
                    waitCnt.setText("현재 대기 인원이 없습니다.");
                }
                break;
            }
        }

        info = findViewById(R.id.txtInfo);
        info.setText(mWaitingInfo.getInfo());


        findViewById(R.id.btnLeft).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imgItems.size() > 1) {
                    if (pivot > 0) {
                        pivot--;
                    } else {
                        pivot = imgItems.size() - 1;
                    }
                    setImg();
                }
            }
        });

        findViewById(R.id.btnRight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imgItems.size() > 1) {
                    if (pivot < imgItems.size() - 1) {
                        pivot++;
                    } else {
                        pivot = 0;
                    }
                    setImg();
                }
            }
        });

        findViewById(R.id.btnOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < ((DataApplication) getApplication()).testWaitingQueueDBList.size(); i++) {
                    WaitingQueue temp = ((DataApplication) getApplication()).testWaitingQueueDBList.get(i);
                    if (temp.getQueueName().equals(name.getText()) && temp.getTime().equals("normal")) {
                        if (temp.getWaitingPersonList() != null) {
                            int check = temp.addWPerson(((DataApplication) getApplication()).currentUser);
                            switch (check) {
                                case 0:
                                    ((DataApplication) getApplication()).testWaitingQueueDBList.set(i, temp);
                                    finish();
                                    break;
                                case 1:
                                    Toast.makeText(getApplicationContext(), "이미 등록한 웨이팅입니다.", Toast.LENGTH_SHORT).show();
                                    break;
                                case 2:
                                    Toast.makeText(getApplicationContext(), "최대 인원인 웨이팅입니다.", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                        break;
                    }
                }
            }
        });
    }

    public void setImg() {
        String content = imgCnt.getText().toString();
        SpannableString spannableString = new SpannableString(content);

        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF6702")), pivot, pivot + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), pivot, pivot + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        imgCnt.setText(spannableString);

        Glide.with(getApplicationContext())
                .load(imgItems.get(pivot).getUri())
                .into(imageView);
    }
}
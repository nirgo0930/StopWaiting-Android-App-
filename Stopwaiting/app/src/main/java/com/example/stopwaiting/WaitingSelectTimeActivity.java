package com.example.stopwaiting;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class WaitingSelectTimeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private int pivot;
    private ArrayList<ImgItem> imgItems;
    private ImageView imageView;
    private TextView name, imgCnt, choice, waitCnt, timeDetail, locDetail, info;
    private Spinner spinner;
    private WaitingInfo mWaitingInfo;
    private ArrayList<String> timeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiting_time);
        Intent intent = getIntent();

        name = findViewById(R.id.txtWaitingName);
        name.setText(intent.getStringExtra("name"));
        waitCnt = findViewById(R.id.txtWaitCnt);
        locDetail = findViewById(R.id.txtLocDeatail);
        setImages_Buttons();

        mWaitingInfo = new WaitingInfo();
        for (int i = 0; i < ((DataApplication) getApplication()).testDBList.size(); i++) {
            WaitingInfo temp = ((DataApplication) getApplication()).testDBList.get(i);
            if (temp.getName().equals(name.getText())) {
                mWaitingInfo = temp;
            }
        }
        locDetail.setText(mWaitingInfo.getLocDetail());

        info = findViewById(R.id.txtInfo);
        info.setText(mWaitingInfo.getInfo());

        spinner = findViewById(R.id.spnTime);
        choice = findViewById(R.id.txtSelectTime);
        if (spinner != null) {
            spinner.setOnItemSelectedListener(this);
        }

        timeList = mWaitingInfo.getTimetable();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, timeList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        timeDetail = findViewById(R.id.txtInfo);
        timeDetail.setText(mWaitingInfo.getLocDetail());

        findViewById(R.id.btnOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < ((DataApplication) getApplication()).testWaitingQueueDBList.size(); i++) {
                    WaitingQueue temp = ((DataApplication) getApplication()).testWaitingQueueDBList.get(i);
                    if (temp.getQueueName().equals(name.getText()) && temp.getTime().equals(choice.getText())) {
                        if (temp.getWaitingPersonList() != null) {
                            int check = temp.addWPerson(((DataApplication) getApplication()).userId);
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

    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        String spinner_item = adapterView.getItemAtPosition(pos).toString();

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String nowTime = sdf.format(new Date(System.currentTimeMillis()));

        waitCnt.setText("현재 대기 인원이 없습니다.");

        if (((DataApplication) this.getApplication()).firstIsLater(spinner_item, nowTime)) {
            for (int i = 0; i < ((DataApplication) getApplication()).testWaitingQueueDBList.size(); i++) {
                WaitingQueue temp = ((DataApplication) getApplication()).testWaitingQueueDBList.get(i);
                if (temp.getQueueName().equals(name.getText()) && temp.getTime().equals(spinner_item)) {
                    if (temp.getWaitingPersonList() != null) {
                        waitCnt.setText("현재 " + String.valueOf(temp.getWaitingPersonList().size()) + "명 대기중");
                    } else {
                        waitCnt.setText("현재 대기 인원이 없습니다.");
                    }
                    break;
                }
            }
            choice.setText(spinner_item);
        } else {
            onNothingSelected(adapterView);
            Toast.makeText(this, "선택한 시간은 예약이 불가능합니다.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
        choice.setText("예약할 시간을 선택해 주세요.");
    }

    public void setImages_Buttons() {
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
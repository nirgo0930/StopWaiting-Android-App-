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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.stopwaiting.R;
import com.example.stopwaiting.dto.ImgItem;
import com.example.stopwaiting.dto.WaitingInfo;
import com.example.stopwaiting.dto.WaitingQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WaitingSelectTimeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private int pivot, mStatusCode;
    private WaitingInfo mWaitingInfo;
    private ImageView imageView;
    private TextView name, imgCnt, choice, waitCnt, timeDetail, locDetail, info;
    private Button btnOk;
    private Spinner spinner;
    private ArrayList<ImgItem> imgItems;
    private ArrayList<String> urlItems, timeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiting_time);
        Intent intent = getIntent();

        info = findViewById(R.id.txtInfo);
        name = findViewById(R.id.txtWaitingName);
        locDetail = findViewById(R.id.txtLocDeatail);
        waitCnt = findViewById(R.id.txtWaitCnt);
        timeDetail = findViewById(R.id.txtInfo);
        choice = findViewById(R.id.txtSelectTime);
        spinner = findViewById(R.id.spnTime);

        mWaitingInfo = new WaitingInfo();
        for (int i = 0; i < DataApplication.waitingList.size(); i++) {
            if (DataApplication.waitingList.get(i).getWaitingId().equals(intent.getLongExtra("id", 0))) {
                mWaitingInfo = DataApplication.waitingList.get(i);
            }
        }

        setImages_Buttons();

        if (spinner != null) {
            spinner.setOnItemSelectedListener(this);
        }

        timeList = mWaitingInfo.getTimetable();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, timeList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        name.setText(mWaitingInfo.getName());
        locDetail.setText(mWaitingInfo.getLocDetail());
        info.setText(mWaitingInfo.getInfo());
        timeDetail.setText(mWaitingInfo.getLocDetail());

        btnOk = findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                waitingRequest();
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
            btnOk.setClickable(true);
        } else {
            onNothingSelected(adapterView);
            Toast.makeText(this, "선택한 시간은 예약이 불가능합니다.", Toast.LENGTH_SHORT).show();
            btnOk.setClickable(false);
        }
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
        choice.setText("예약할 시간을 선택해 주세요.");
    }

    public void setImages_Buttons() {
        imgItems = new ArrayList<>();

        for (int i = 0; i < mWaitingInfo.getUrlList().size(); i++) {
            ImgItem temp = new ImgItem();
            temp.setSUri(mWaitingInfo.getUrlList().get(i));
            imgItems.add(temp);
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

    public void waitingRequest() {
        if (DataApplication.isTest) {
            for (int i = 0; i < ((DataApplication) getApplication()).testWaitingQueueDBList.size(); i++) {
                WaitingQueue temp = ((DataApplication) getApplication()).testWaitingQueueDBList.get(i);
                if (temp.getQueueName().equals(name.getText()) && temp.getTime().equals(choice.getText())) {
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
        } else {
            JSONObject jsonBodyObj = new JSONObject();
            try {
                jsonBodyObj.put("studentCode", DataApplication.currentUser.getStudentCode());
                jsonBodyObj.put("waitingId", mWaitingInfo.getWaitingId());
                jsonBodyObj.put("type", mWaitingInfo.getType());
                jsonBodyObj.put("time", choice.getText());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            final String requestBody = String.valueOf(jsonBodyObj.toString());

            StringRequest request = new StringRequest(Request.Method.GET, DataApplication.serverURL + "/addwaiting",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String jsonObject) {
                            int statusCode = mStatusCode;
                            switch (statusCode) {
                                case HttpURLConnection.HTTP_OK:
                                    Toast.makeText(getApplicationContext(), "정상 등록되었습니다.", Toast.LENGTH_SHORT).show();
                                    finish();
                                    break;
                                case HttpURLConnection.HTTP_NOT_FOUND:
                                    Toast.makeText(getApplicationContext(), "이미 대기중인 웨이팅입니다.", Toast.LENGTH_SHORT).show();
                                    break;
                                case HttpURLConnection.HTTP_INTERNAL_ERROR:
                                    Toast.makeText(getApplicationContext(), "최대 인원인 웨이팅입니다.", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    return headers;
                }

                @Override
                public byte[] getBody() {
                    try {
                        if (requestBody != null && requestBody.length() > 0 && !requestBody.equals("")) {
                            return requestBody.getBytes("utf-8");
                        } else {
                            return null;
                        }
                    } catch (UnsupportedEncodingException uee) {
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    if (response != null) {
                        mStatusCode = response.statusCode;
                    }
                    return super.parseNetworkResponse(response);
                }
            };

            request.setShouldCache(false);
            DataApplication.requestQueue.add(request);
        }
    }
}
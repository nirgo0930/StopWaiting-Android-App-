package com.example.stopwaiting.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.stopwaiting.R;
import com.example.stopwaiting.DTO.WaitingInfo;
import com.example.stopwaiting.DTO.WaitingQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ManageWaitingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public static Activity manageWaitingActivity;
    private ArrayList<WaitingQueue> wQueue;
    private ArrayList<String> timeList;
    private WaitingInfo wInfo;
    private WaitingQueue selectQ;
    private Button btnShowList, btnCheckIn, btnRefresh;
    private TextView txtWaitingCnt, txtChoice, txtNextName, txtWaitingName;
    private Spinner spinner;
    private ArrayAdapter<String> mAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_waiting);
        Intent intent = getIntent();
        manageWaitingActivity = ManageWaitingActivity.this;
        wQueue = new ArrayList<>();
        wInfo = new WaitingInfo();
        selectQ = new WaitingQueue();


        btnCheckIn = findViewById(R.id.btnCheckIn);
        btnShowList = findViewById(R.id.btnShowList);
        btnRefresh = findViewById(R.id.btnRefresh);
        txtWaitingCnt = findViewById(R.id.txtWaitingCnt);
        txtChoice = findViewById(R.id.txtSelectTime);
        txtNextName = findViewById(R.id.txtNextName);
        txtWaitingName = findViewById(R.id.txtWaitingName);
        spinner = findViewById(R.id.spnTime);

        waitingInfoRequest(intent.getStringExtra("name"));

        for (int i = 0; i < ((DataApplication) getApplication()).testDBList.size(); i++) {
            if (((DataApplication) getApplication()).testDBList.get(i).getName().equals(intent.getStringExtra("name"))) {
                wInfo = ((DataApplication) getApplication()).testDBList.get(i);
                break;
            }
        }

        for (int i = 0; i < ((DataApplication) getApplication()).testWaitingQueueDBList.size(); i++) {
            if (((DataApplication) getApplication()).testWaitingQueueDBList.get(i).getQueueName().equals(wInfo.getName())) {
                wQueue.add(((DataApplication) getApplication()).testWaitingQueueDBList.get(i));
            }
        }
        txtWaitingName.setText(wInfo.getName());
        selectQ = wQueue.get(0);

        btnCheckIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ManageWaitingActivity.this, ScanQRActivity.class);
                startActivityForResult(intent, 4000);
            }
        });

        btnShowList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManageWaitingActivity.this, ManageWaitingPersonActivity.class);
                intent.putExtra("qName", selectQ.getQueueName());

                startActivityForResult(intent, 5000);
            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });

        timeList = new ArrayList<>();
        if (wInfo.getTimetable() != null) {
            timeList = wInfo.getTimetable();
            if (spinner != null) {
                spinner.setOnItemSelectedListener(this);
            }

            mAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, timeList);
            mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(mAdapter);
        } else {
            timeList.add("normal");
        }

        refresh();
    }

    void refresh() {
        for (int i = 0; i < ((DataApplication) getApplication()).testDBList.size(); i++) {
            if (((DataApplication) getApplication()).testDBList.get(i).getName().equals(wInfo.getName())) {
                wInfo = ((DataApplication) getApplication()).testDBList.get(i);
                break;
            }
        }

        for (int i = 0; i < ((DataApplication) getApplication()).testWaitingQueueDBList.size(); i++) {
            if (((DataApplication) getApplication()).testWaitingQueueDBList.get(i).getQueueName().equals(wInfo.getName())) {
                wQueue.add(((DataApplication) getApplication()).testWaitingQueueDBList.get(i));
            }
        }
        timeList = new ArrayList<>();
        if (wInfo.getTimetable() != null) {
            timeList = wInfo.getTimetable();
            mAdapter.notifyDataSetChanged();
        } else {
            timeList.add("normal");
        }

        WaitingQueue temp = new WaitingQueue();

        String wCnt = "";
        String next = "";

        if (wQueue.get(0).getTime().equals("normal")) {
            temp = wQueue.get(0);

            if (temp.getWaitingPersonList().size() != 0) {
                wCnt = (temp.getWaitingPersonList().size()) + " 명";
                next = temp.getWaitingPersonList().get(0);
            } else {
                wCnt = "0 명";
                next = "-";
            }
        } else {
            for (int i = 0; i < wQueue.size(); i++) {
                temp = wQueue.get(i);
                if (temp.getTime().equals(txtChoice.getText().toString())) {
                    if (temp.getWaitingPersonList().size() != 0) {
                        wCnt = (temp.getWaitingPersonList().size()) + " 명";
                        next = temp.getWaitingPersonList().get(0);
                    } else {
                        wCnt = "0 명";
                        next = "-";
                    }

                    selectQ = temp;
                    break;
                }
            }
        }

        txtWaitingCnt.setText(wCnt);
        txtNextName.setText(next);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 4000) {
            if (resultCode == RESULT_OK) {
                String qr = data.getStringExtra("qr");
                if (qr.equals(((DataApplication) getApplication()).userCode)) {
                    String tempName = "test"; //DB에서 일치하는 학번 검색

                    if (selectQ.getWaitingPersonList().size() != 0) {
                        int selectNum = ((DataApplication) getApplication()).testWaitingQueueDBList.indexOf(selectQ);
                        Toast.makeText(this, String.valueOf(selectNum), Toast.LENGTH_SHORT).show();
                        int check = selectQ.removeWPerson(tempName);
                        switch (check) {
                            case 0:
                                ((DataApplication) getApplication()).testWaitingQueueDBList.set(selectNum, selectQ);
                                Toast.makeText(getApplicationContext(), tempName + " 님 어서오세요.", Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                Toast.makeText(getApplicationContext(), "등록된 웨이팅이 아닙니다.", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }
            } else {
                refresh();
            }
        } else if (requestCode == 5000) {
            refresh();
        }
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        String spinner_item = adapterView.getItemAtPosition(pos).toString();
        txtChoice.setText(spinner_item);

        refresh();
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
        txtChoice.setText("예약할 시간을 선택해 주세요.");
    }

    public void waitingInfoRequest(String sName) {
        StringRequest waitingRequest = new StringRequest(Request.Method.POST, ((DataApplication) getApplication()).serverURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {
                                wInfo = new WaitingInfo();

                                wInfo.setId(jsonObject.getLong("id"));
                                wInfo.setAdmin(jsonObject.getString("admin"));
                                wInfo.setName(jsonObject.getString("name"));
                                wInfo.setLatitude(jsonObject.getDouble("lat"));
                                wInfo.setLongitude(jsonObject.getDouble("lon"));
                                wInfo.setLocDetail(jsonObject.getString("locDetail"));
                                wInfo.setInfo(jsonObject.getString("info"));
                                wInfo.setType(jsonObject.getString("type"));
                                wInfo.setMaxPerson(jsonObject.getInt("max"));
                                if (wInfo.getType().equals("time")) {
                                    ArrayList<String> timetable = new ArrayList();
                                    JSONArray timeArray = jsonObject.getJSONArray("timetable");
                                    for (int j = 0; j < timeArray.length(); j++) {
                                        timetable.add(timeArray.getString(j));
                                    }
                                    wInfo.setTimetable(timetable);
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "로딩에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "로딩에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("type", "waitinginfo");
                params.put("name", sName);

                return params;
            }
        };

        waitingRequest.setShouldCache(false);
        ((DataApplication) getApplication()).requestQueue.add(waitingRequest);
    }

    public void queueInfoRequest(String sName) {
        StringRequest loginRequest = new StringRequest(Request.Method.POST, ((DataApplication) getApplication()).serverURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {
                                wInfo = new WaitingInfo();

                                wInfo.setId(jsonObject.getLong("id"));
                                wInfo.setAdmin(jsonObject.getString("admin"));
                                wInfo.setName(jsonObject.getString("name"));
                                wInfo.setLatitude(jsonObject.getDouble("lat"));
                                wInfo.setLongitude(jsonObject.getDouble("lon"));
                                wInfo.setLocDetail(jsonObject.getString("locDetail"));
                                wInfo.setInfo(jsonObject.getString("info"));
                                wInfo.setType(jsonObject.getString("type"));
                                wInfo.setMaxPerson(jsonObject.getInt("max"));
                                if (wInfo.getType().equals("time")) {
                                    ArrayList<String> timetable = new ArrayList();
                                    JSONArray timeArray = jsonObject.getJSONArray("timetable");
                                    for (int j = 0; j < timeArray.length(); j++) {
                                        timetable.add(timeArray.getString(j));
                                    }
                                    wInfo.setTimetable(timetable);
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "로딩에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "로딩에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("type", "waitinginfo");
                params.put("name", sName);

                return params;
            }
        };

        loginRequest.setShouldCache(false);
        ((DataApplication) getApplication()).requestQueue.add(loginRequest);
    }
}

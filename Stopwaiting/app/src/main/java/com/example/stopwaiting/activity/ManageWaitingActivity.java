package com.example.stopwaiting.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.stopwaiting.databinding.ManageWaitingBinding;
import com.example.stopwaiting.dto.UserInfo;
import com.example.stopwaiting.dto.WaitingInfo;
import com.example.stopwaiting.dto.WaitingQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ManageWaitingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public static Activity manageWaitingActivity;
    private ArrayList<WaitingQueue> wQueue;
    private ArrayList<String> timeList;
    private WaitingInfo wInfo;
    public static WaitingQueue selectQ;
    private ArrayAdapter<String> mAdapter;

    private ManageWaitingBinding binding;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ManageWaitingBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        Intent intent = getIntent();
        manageWaitingActivity = ManageWaitingActivity.this;
        wQueue = new ArrayList<>();
        wInfo = new WaitingInfo();
        timeList = new ArrayList<>();
        selectQ = new WaitingQueue();

        waitingInfoRequest(intent.getLongExtra("qId", 0L));

        if (binding.spnTime != null) {
            binding.spnTime.setOnItemSelectedListener(this);
        }
        mAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, timeList);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnTime.setAdapter(mAdapter);

        binding.btnCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageWaitingActivity.this, ScanQRActivity.class);
                startActivityForResult(intent, 4000);
            }
        });

        binding.btnShowList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManageWaitingActivity.this, ManageWaitingPersonActivity.class);
                intent.putExtra("qId", selectQ.getQId());

                startActivityForResult(intent, 5000);
            }
        });

        binding.btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });

        refresh();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 4000) {
            if (resultCode == RESULT_OK) {
                Long qr = Long.valueOf(data.getStringExtra("qr"));
                checkInRequest(qr, selectQ.getQId());
            }
        } else if (requestCode == 5000) {

        }

        refresh();
    }

    void refresh() {
        waitingInfoRequest(wInfo.getWaitingId());
        queueListRequest(wInfo.getWaitingId());

        binding.txtWaitingName.setText(wInfo.getName());

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
                next = temp.getWaitingPersonList().get(0).getName();
            } else {
                wCnt = "0 명";
                next = "-";
            }
        } else {
            for (int i = 0; i < wInfo.getQueueList().size(); i++) {
                temp = wQueue.get(i);
                if (temp.getTime().equals(binding.txtSelectTime.getText().toString())) {
                    if (temp.getWaitingPersonList().size() != 0) {
                        wCnt = (temp.getWaitingPersonList().size()) + " 명";
                        next = temp.getWaitingPersonList().get(0).getName();
                    } else {
                        wCnt = "0 명";
                        next = "-";
                    }

                    break;
                }
            }
        }
        selectQ = temp;


        binding.txtWaitingCnt.setText(wCnt);
        binding.txtNextName.setText(next);
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        String spinner_item = adapterView.getItemAtPosition(pos).toString();
        binding.txtSelectTime.setText(spinner_item);

        refresh();
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
        binding.txtSelectTime.setText("예약할 시간을 선택해 주세요.");
    }

    public void waitingInfoRequest(Long qId) {
        if (DataApplication.isTest) {
            for (WaitingInfo tempInfo : DataApplication.testDBList) {
//            for (int i = 0; i < DataApplication.testDBList.size(); i++) {
                for (Long id : tempInfo.getQueueList()) {
                    if (id.equals(qId)) {
                        wInfo = tempInfo;
                        return;
                    }
                }
            }
        } else {
            JSONObject jsonBodyObj = new JSONObject();
            try {
                jsonBodyObj.put("id", qId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            final String requestBody = String.valueOf(jsonBodyObj.toString());

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, DataApplication.serverURL + "/waitingInfo", null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            try {
                                JSONArray dataArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject dataObject = dataArray.getJSONObject(i);
                                    WaitingInfo data = new WaitingInfo();

                                    data.setWaitingId(dataObject.getLong("id"));
                                    data.setAdminId(dataObject.getLong("adminId"));
                                    data.setName(dataObject.getString("name"));
                                    data.setLatitude(dataObject.getDouble("latitude"));
                                    data.setLongitude(dataObject.getDouble("longitude"));
                                    data.setLocDetail(dataObject.getString("locDetail"));
                                    data.setInfo(dataObject.getString("information"));
                                    data.setType(dataObject.getString("type"));
                                    data.setMaxPerson(dataObject.getInt("maxPerson"));
                                    if (data.getType().equals("time")) {
                                        ArrayList<String> timetable = new ArrayList();
                                        JSONArray timeArray = dataObject.getJSONArray("timetable");
                                        for (int j = 0; j < timeArray.length(); j++) {
                                            timetable.add(timeArray.getString(j));
                                        }
                                        data.setTimetable(timetable);
                                    }

                                    wInfo = data;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
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
            };

            request.setShouldCache(false);
            DataApplication.requestQueue.add(request);
        }
    }

    public void queueListRequest(Long wId) {
        wQueue = new ArrayList<>();
        if (DataApplication.isTest) {
            for (WaitingQueue tempQ : DataApplication.testWaitingQueueDBList) {
                for (Long tempQID : wInfo.getQueueList()) {
                    if (tempQ.getQId().equals(tempQID)) {
                        wQueue.add(tempQ);
                    }
                }
            }
        } else {
            JSONObject jsonBodyObj = new JSONObject();
            try {
                jsonBodyObj.put("id", wId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            final String requestBody = String.valueOf(jsonBodyObj.toString());

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, DataApplication.serverURL + "/waitingQueueList", null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            try {
                                JSONArray dataArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject dataObject = dataArray.getJSONObject(i);
                                    WaitingQueue data = new WaitingQueue();

                                    data.setQId(dataObject.getLong("qId"));
                                    data.setQueueName(dataObject.getString("qName"));
                                    data.setTime(dataObject.getString("time"));
                                    data.setMaxPerson(dataObject.getInt("maxPerson"));

                                    ArrayList<UserInfo> tempPersonList = new ArrayList<>();
                                    JSONArray personArray = jsonObject.getJSONArray("data");
                                    for (int j = 0; j < personArray.length(); j++) {
                                        JSONObject personObject = personArray.getJSONObject(i);
                                        UserInfo tempPerson = new UserInfo();

                                        tempPerson.setStudentCode(personObject.getLong("studentCode"));
                                        tempPerson.setName(personObject.getString("name"));
                                        tempPerson.setTel(personObject.getString("telephone"));

                                        tempPersonList.add(tempPerson);
                                    }
                                    data.setWaitingPersonList(tempPersonList);

                                    wQueue.add(data);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
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
            };

            request.setShouldCache(false);
            DataApplication.requestQueue.add(request);
        }
    }

    public void checkInRequest(Long qr, Long qId) {
        if (DataApplication.isTest) {
            if (selectQ.getWaitingPersonList().size() != 0) {
                UserInfo temp = new UserInfo();
                for (int i = 0; i < selectQ.getWaitingPersonList().size(); i++) {
                    if (selectQ.getWaitingPersonList().get(i).getStudentCode().equals(qr)) {
                        temp = selectQ.getWaitingPersonList().get(i);
                        if (selectQ.removeWPerson(temp.getStudentCode())) {
                            DataApplication.testWaitingQueueDBList.set(DataApplication.testWaitingQueueDBList.indexOf(selectQ), selectQ);
                            Toast.makeText(getApplicationContext(), temp.getName() + " 님 어서오세요.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "등록된 웨이팅이 아닙니다.", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                }
            } else {
                Toast.makeText(getApplicationContext(), "대기 중인 사람이 없습니다.", Toast.LENGTH_SHORT).show();
            }
        } else {
            JSONObject jsonBodyObj = new JSONObject();
            try {
                jsonBodyObj.put("qrCode", qr);
                jsonBodyObj.put("qId", qId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            final String requestBody = String.valueOf(jsonBodyObj.toString());

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, DataApplication.serverURL + "/checkIn", null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            try {
                                String userName = jsonObject.getString("name");
                                Toast.makeText(getApplicationContext(), userName + " 님 어서오세요.", Toast.LENGTH_SHORT).show();


                                Toast.makeText(getApplicationContext(), "등록된 웨이팅이 아닙니다.", Toast.LENGTH_SHORT).show();
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
            };

            request.setShouldCache(false);
            DataApplication.requestQueue.add(request);
        }
    }
}

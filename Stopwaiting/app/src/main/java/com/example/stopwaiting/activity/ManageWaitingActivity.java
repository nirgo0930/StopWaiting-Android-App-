package com.example.stopwaiting.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

        binding.spnTime.setOnItemSelectedListener(this);

        waitingInfoRequest(intent.getLongExtra("wId", 0L));
        queueListRequest(intent.getLongExtra("wId", 0L));

        binding.txtWaitingName.setText(wInfo.getName());

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
        waitingInfoAllRequest();
        myWaitingRequest();

        waitingInfoRequest(wInfo.getWaitingId());
        queueListRequest(wInfo.getWaitingId());

        WaitingQueue temp = new WaitingQueue();

        String wCnt = "";
        String next = "";

        if (wQueue.get(0).getTime().equals("NORMAL")) {
            temp = wQueue.get(0);

            if (temp.getWaitingPersonList().size() != 0) {
                wCnt = (temp.getWaitingPersonList().size()) + " 명";
                next = temp.getWaitingPersonList().get(0).getName();
            } else {
                wCnt = "0 명";
                next = "-";
            }
        } else {
            for (int i = 0; i < wQueue.size(); i++) {
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
        binding.txtSelectTime.setText(adapterView.getItemAtPosition(pos).toString());
        Log.e("select", String.valueOf(binding.txtSelectTime.getText()));

        WaitingQueue temp = new WaitingQueue();

        String wCnt = "";
        String next = "";

        if (wQueue.get(0).getTime().equals("NORMAL")) {
            temp = wQueue.get(0);

            if (temp.getWaitingPersonList().size() != 0) {
                wCnt = (temp.getWaitingPersonList().size()) + " 명";
                next = temp.getWaitingPersonList().get(0).getName();
            } else {
                wCnt = "0 명";
                next = "-";
            }
        } else {
            for (int i = 0; i < wQueue.size(); i++) {
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

    public void onNothingSelected(AdapterView<?> adapterView) {
        binding.txtSelectTime.setText("예약할 시간을 선택해 주세요.");
    }

    public void waitingInfoRequest(Long wId) {
        for (WaitingInfo tempInfo : DataApplication.waitingList) {
            if (tempInfo.getWaitingId().equals(wId)) {
                wInfo = tempInfo;
                if (wInfo.getTimetable() != null) {
                    timeList = wInfo.getTimetable();
                    mAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, timeList);
                    mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.spnTime.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                } else {
                    timeList.add("NORMAL");
                }
                return;
            }
        }
    }

    public void queueListRequest(Long wId) {
        wQueue = new ArrayList<>();
        timeList = new ArrayList<>();
        for (WaitingQueue tempQ : ManageWaitingListActivity.mWaitingQueueList) {
            if (tempQ.getWId().equals(wInfo.getWaitingId())) {
                wQueue.add(tempQ);
            }
        }
    }

    public void checkInRequest(Long qr, Long qId) {
        if (DataApplication.isTest) {
            for (int i = 0; i < DataApplication.testWaitingQueueDBList.size(); i++) {
                if (DataApplication.testWaitingQueueDBList.get(i).getQId().equals(qId)) {
                    WaitingQueue temp = DataApplication.testWaitingQueueDBList.get(i);

                    DataApplication.myWaiting.remove(temp);

                    temp.removeWPerson(qr);
                    DataApplication.testWaitingQueueDBList.set(i, temp);

                    break;
                }
            }
        } else {
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE,
                    DataApplication.serverURL + "/queue/" + qId + "/" + qr, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            Toast.makeText(getApplicationContext(), qr + " 님 어서오세요.", Toast.LENGTH_SHORT).show();

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "등록된 웨이팅이 아닙니다.", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };

            request.setShouldCache(false);
            DataApplication.requestQueue.add(request);
        }
    }

    public void myWaitingRequest() {
        JSONObject jsonBodyObj = new JSONObject();
        try {
            jsonBodyObj.put("id", DataApplication.currentUser.getStudentCode());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody = String.valueOf(jsonBodyObj.toString());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                ((DataApplication) getApplication()).serverURL + "/user/" + DataApplication.currentUser.getStudentCode() + "/queue", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.e("my", jsonObject.toString());
                        Toast.makeText(getApplicationContext(), "신청한 웨이팅 조회.", Toast.LENGTH_SHORT).show();
                        try {
                            JSONArray dataArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < dataArray.length(); i++) {
                                WaitingQueue data = new WaitingQueue();
                                JSONObject dataObject = dataArray.getJSONObject(i);
                                data.setQId(dataObject.getLong("id"));

                                JSONObject timeObject = dataObject.getJSONObject("waitingQueue").getJSONObject("timetable");
                                data.setTime(timeObject.getString("time"));

                                JSONObject waitingObject = timeObject.getJSONObject("waitingInfo");
                                data.setWId(waitingObject.getLong("id"));
                                data.setQueueName(waitingObject.getString("name"));
                                data.setMaxPerson(waitingObject.getInt("maxPerson"));

                                ArrayList<UserInfo> tempUserList = new ArrayList<>();
                                JSONArray userArray = dataObject.getJSONObject("waitingQueue").getJSONArray("userQueues");
                                for (int j = 0; j < userArray.length(); j++) {
                                    UserInfo tempUser = new UserInfo();
                                    JSONObject userObject = userArray.getJSONObject(j).getJSONObject("user");
                                    tempUser.setStudentCode(userObject.getLong("id"));

                                    tempUserList.add(tempUser);
                                }
                                data.setWaitingPersonList(tempUserList);

                                DataApplication.myWaiting.add(data);
                            }
                        } catch (JSONException e) {
                            Log.e("error", e.toString());
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "신청한 웨이팅 조회 실패.", Toast.LENGTH_SHORT).show();
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

    public void waitingInfoAllRequest() {
        ((DataApplication) getApplication()).waitingList = new ArrayList<>();

        JSONObject jsonBodyObj = new JSONObject();
        final String requestBody = String.valueOf(jsonBodyObj.toString());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ((DataApplication) getApplication()).serverURL + "/waitinginfo/confirmed", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            JSONArray dataArray = jsonObject.getJSONArray("data");

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject dataObject = dataArray.getJSONObject(i);
                                Log.e("temp", dataObject.toString());
                                WaitingInfo data = new WaitingInfo();
                                data.setWaitingId(dataObject.getLong("id"));
                                data.setAdminId(dataObject.getLong("adminId"));
                                data.setName(dataObject.getString("name"));
                                data.setLatitude(dataObject.getDouble("latitude"));
                                data.setLongitude(dataObject.getDouble("longitude"));
                                data.setLocDetail(dataObject.getString("locationDetail"));
                                data.setInfo(dataObject.getString("information"));
                                data.setType(dataObject.getString("type"));
                                data.setMaxPerson(dataObject.getInt("maxPerson"));

                                ArrayList<String> timetable = new ArrayList();
                                JSONArray timeArray = dataObject.getJSONArray("timetables");
                                for (int j = 0; j < timeArray.length(); j++) {
                                    JSONObject timeObj = timeArray.getJSONObject(j);

                                    timetable.add(timeObj.getString("time"));
                                }
                                data.setTimetable(timetable);

                                ArrayList<String> urlList = new ArrayList();
                                if (dataObject.getJSONArray("images") != null) {
                                    JSONArray imageArray = dataObject.getJSONArray("images");
                                    for (int j = 0; j < imageArray.length(); j++) {
                                        JSONObject imgInfo = imageArray.getJSONObject(j);

                                        urlList.add(((DataApplication) getApplication()).imgURL + imgInfo.getString("fileurl"));

                                    }
                                }
                                data.setUrlList(urlList);

                                ((DataApplication) getApplication()).waitingList.add(data);

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

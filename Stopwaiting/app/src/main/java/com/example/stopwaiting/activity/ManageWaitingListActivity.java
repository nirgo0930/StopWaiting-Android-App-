package com.example.stopwaiting.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.stopwaiting.R;
import com.example.stopwaiting.adapter.ManageWaitingListAdapter;
import com.example.stopwaiting.dto.ImgItem;
import com.example.stopwaiting.dto.UserInfo;
import com.example.stopwaiting.dto.WaitingInfo;
import com.example.stopwaiting.dto.WaitingListItem;
import com.example.stopwaiting.dto.WaitingQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ManageWaitingListActivity extends AppCompatActivity {
    private TextView txtResult, txtTitle;
    private RecyclerView recyclerView;
    private ArrayList<WaitingQueue> mWaitingQueueList;
    private ArrayList<WaitingListItem> mWaitingList;
    private ManageWaitingListAdapter mListAdapter;
    private WaitingInfo tempWaitingInfo;
    private ImgItem tempImgInfo;
    public static Activity manageWaitingActivity;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiting_list);
        manageWaitingActivity = ManageWaitingListActivity.this;

        txtResult = findViewById(R.id.txtNotice);
        txtTitle = findViewById(R.id.txtTitle);
        recyclerView = findViewById(R.id.recyclerView);

        mWaitingList = new ArrayList<>();
        mWaitingQueueList = new ArrayList<>();
        mListAdapter = new ManageWaitingListAdapter(this, mWaitingList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mListAdapter);

        txtTitle.setText("개설한 웨이팅");

        myManageWaitingInfoRequest(); //set mWaitingQueueList

        if (mWaitingQueueList.size() > 0) {
            for (int i = 0; i < mWaitingQueueList.size(); i++) {
                waitingInfoRequest(mWaitingQueueList.get(i).getQueueName());
                WaitingInfo tempInfo = tempWaitingInfo;

                imgRequest(mWaitingQueueList.get(i).getQueueName());
                ImgItem tempImg = tempImgInfo;

                boolean check = false;
                for (int j = 0; j < mWaitingList.size(); j++) {
                    if (mWaitingList.get(j).getName().equals(mWaitingQueueList.get(i).getQueueName())) {
                        check = true;
                        break;
                    }
                }
                if (!check) {
                    mWaitingList.add(new WaitingListItem(tempImg.getUri(), mWaitingQueueList.get(i).getQueueName(), mWaitingQueueList.get(i).getQId(),
                            mWaitingQueueList.get(i).getWaitingPersonList().size(), tempInfo.getLocDetail()));
                }

            }
            txtResult.setText("개설한 웨이팅은 총 " + mWaitingList.size() + "건 입니다.");
        } else {
            txtResult.setText("개설한 웨이팅이 없습니다.");
        }

        mListAdapter.notifyDataSetChanged();
    }

    public void myManageWaitingInfoRequest() {
        if (DataApplication.isTest) {
            for (int i = 0; i < DataApplication.testDBList.size(); i++) {
                WaitingInfo tempInfo = DataApplication.testDBList.get(i);
                if (tempInfo.getAdminId().equals(DataApplication.currentUser.getStudentCode())) {
                    String qName = tempInfo.getName();
                    for (int j = 0; j < DataApplication.testWaitingQueueDBList.size(); j++) {
                        WaitingQueue tempQ = DataApplication.testWaitingQueueDBList.get(j);
                        if (tempQ.getQueueName().equals(qName)) {
                            mWaitingQueueList.add(tempQ);
                        }
                    }
                }
            }
        } else {
            JSONObject jsonBodyObj = new JSONObject();
            try {
                jsonBodyObj.put("id", DataApplication.currentUser.getStudentCode());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            final String requestBody = String.valueOf(jsonBodyObj.toString());

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, DataApplication.serverURL + "/manageWaitingList",
                    null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        JSONArray dataArray = jsonObject.getJSONArray("data");
                        mWaitingList = new ArrayList<>();
                        mWaitingQueueList = new ArrayList<>();
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject dataObject = dataArray.getJSONObject(i);
                            WaitingQueue tempQ = new WaitingQueue();

                            tempQ.setQId(dataObject.getLong("id"));
                            tempQ.setQueueName(dataObject.getString("name"));
                            tempQ.setMaxPerson(dataObject.getInt("maxPerson"));
                            tempQ.setTime(dataObject.getString("time"));

                            JSONArray personArray = jsonObject.getJSONArray("personList");
                            ArrayList<UserInfo> tempP = new ArrayList<>();
                            for (int j = 0; j < personArray.length(); j++) {
                                JSONObject personObject = personArray.getJSONObject(j);
                                UserInfo tempUser = new UserInfo();
                                tempUser.setStudentCode(personObject.getLong("sCode"));
                                tempUser.setName(personObject.getString("name"));
                                tempUser.setTel(personObject.getString("tel"));

                                tempP.add(tempUser);
                            }
                            tempQ.setWaitingPersonList(tempP);

                            mWaitingQueueList.add(tempQ);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
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

    public void waitingInfoRequest(String waitingName) {
        if (DataApplication.isTest) {
            for (int j = 0; j < DataApplication.testDBList.size(); j++) {
                WaitingInfo temp = DataApplication.testDBList.get(j);
                if (temp.getName().equals(waitingName)) {
                    tempWaitingInfo = temp;
                }
            }
        } else {
            WaitingInfo tempInfo = new WaitingInfo();
            JSONObject jsonBodyObj = new JSONObject();
            try {
                jsonBodyObj.put("name", waitingName);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            final String requestBody = String.valueOf(jsonBodyObj.toString());

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, DataApplication.serverURL + "/waitingInfo",
                    null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        JSONArray dataArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject dataObject = dataArray.getJSONObject(i);

                            tempInfo.setWaitingId(dataObject.getLong("id"));
                            tempInfo.setAdminId(dataObject.getLong("adminId"));
                            tempInfo.setName(dataObject.getString("name"));
                            tempInfo.setLatitude(dataObject.getDouble("latitude"));
                            tempInfo.setLongitude(dataObject.getDouble("longitude"));
                            tempInfo.setLocDetail(dataObject.getString("locDetail"));
                            tempInfo.setInfo(dataObject.getString("information"));
                            tempInfo.setType(dataObject.getString("type"));
                            tempInfo.setMaxPerson(dataObject.getInt("maxPerson"));
                            if (tempInfo.getType().equals("time")) {
                                ArrayList<String> timetable = new ArrayList();
                                JSONArray timeArray = dataObject.getJSONArray("timetable");
                                for (int j = 0; j < timeArray.length(); j++) {
                                    timetable.add(timeArray.getString(j));
                                }
                                tempInfo.setTimetable(timetable);
                            }

                            tempWaitingInfo = tempInfo;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
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

    public void imgRequest(String waitingName) {
        if (DataApplication.isTest) {
            for (int i = 0; i < DataApplication.testImageDBList.size(); i++) {
                tempImgInfo = DataApplication.testImageDBList.get(i);
                if (tempImgInfo.getName().equals(waitingName)) {
                    break;
                }
            }
        } else {
            ImgItem tempInfo = new ImgItem();
            JSONObject jsonBodyObj = new JSONObject();
            try {
                jsonBodyObj.put("name", waitingName);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            final String requestBody = String.valueOf(jsonBodyObj.toString());

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, DataApplication.serverURL + "/waitingInfo",
                    null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        JSONArray dataArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject dataObject = dataArray.getJSONObject(i);

                            tempInfo.setId(dataObject.getLong("id"));
                            tempInfo.setName(dataObject.getString("name"));

                            tempImgInfo = tempInfo;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
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
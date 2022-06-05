package com.example.stopwaiting.activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
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
import com.example.stopwaiting.databinding.WaitingListBinding;
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
    private ArrayList<WaitingQueue> mWaitingQueueList;
    private ArrayList<WaitingListItem> mWaitingList;
    private ManageWaitingListAdapter mListAdapter;
    private WaitingInfo tempWaitingInfo;
    public static Activity manageWaitingActivity;

    private WaitingListBinding binding;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = WaitingListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        manageWaitingActivity = ManageWaitingListActivity.this;

        tempWaitingInfo = new WaitingInfo();
        mWaitingList = new ArrayList<>();
        mWaitingQueueList = new ArrayList<>();
        mListAdapter = new ManageWaitingListAdapter(this, mWaitingList);


        myWaitingRequest();

        if (mWaitingQueueList.size() > 0) {
            for (WaitingQueue selectQueue : mWaitingQueueList) {
                waitingInfoRequest(selectQueue.getQId());
                ImgItem tempImgInfo = new ImgItem();
                if (tempWaitingInfo.getUrlList().size() > 0) {
                    tempImgInfo.setSUri(tempWaitingInfo.getUrlList().get(0));
                } else {
                    tempImgInfo.setSUri(Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + R.drawable.empty_icon).toString());
                }

                boolean check = false;
                for (int j = 0; j < mWaitingList.size(); j++) {
                    if (mWaitingList.get(j).getName().equals(selectQueue.getQueueName())) {
                        check = true;
                        break;
                    }
                }
                if (!check) {
                    mWaitingList.add(new WaitingListItem(tempImgInfo.getUri(), selectQueue.getQueueName(), selectQueue.getQId(),
                            selectQueue.getWaitingPersonList().size(), tempWaitingInfo.getLocDetail()));
                }
            }
            binding.txtNotice.setText("개설한 웨이팅은 총 " + mWaitingList.size() + "건 입니다.");
        } else {
            binding.txtNotice.setText("개설한 웨이팅이 없습니다.");
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setAdapter(mListAdapter);

        binding.txtTitle.setText("개설한 웨이팅");

        mListAdapter.notifyDataSetChanged();
    }

    public void myWaitingRequest() {
        if (DataApplication.isTest) {
            for (WaitingInfo tempInfo : DataApplication.testDBList) {
                if (tempInfo.getAdminId().equals(DataApplication.currentUser.getStudentCode())) {
                    for (WaitingQueue tempQ : DataApplication.testWaitingQueueDBList) {
                        for (Long selectQID : tempInfo.getQueueList()) {
                            if (tempQ.getQId().equals(selectQID)) {
                                mWaitingQueueList.add(tempQ);
                            }
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

    public void waitingInfoRequest(Long qId) {
        if (DataApplication.isTest) {
            for (int i = 0; i < DataApplication.testDBList.size(); i++) {
                WaitingInfo tempInfo = DataApplication.testDBList.get(i);
                for (Long id : tempInfo.getQueueList()) {
                    if (id.equals(qId)) {
                        tempWaitingInfo = tempInfo;
                        return;
                    }
                }
            }
        } else {
            WaitingInfo tempInfo = new WaitingInfo();
            JSONObject jsonBodyObj = new JSONObject();
            try {
                jsonBodyObj.put("queueId", qId);
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

}
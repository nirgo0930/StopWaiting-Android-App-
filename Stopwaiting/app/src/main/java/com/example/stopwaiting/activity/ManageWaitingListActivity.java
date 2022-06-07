package com.example.stopwaiting.activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.example.stopwaiting.dto.UserInfo;
import com.example.stopwaiting.dto.WaitingInfo;
import com.example.stopwaiting.dto.WaitingListItem;
import com.example.stopwaiting.dto.WaitingQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ManageWaitingListActivity extends AppCompatActivity {
    public static ArrayList<WaitingQueue> mWaitingQueueList;
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
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setAdapter(mListAdapter);
        binding.txtTitle.setText("개설한 웨이팅");
        adminWaitingRequest();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        adminWaitingRequest();
    }

    public void adminWaitingRequest() {
        mWaitingQueueList = new ArrayList<>();
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
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, DataApplication.serverURL + "/queue/" + DataApplication.currentUser.getStudentCode(),
                    null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        JSONArray dataArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject queueObject = dataArray.getJSONObject(i);
                            JSONObject timeObject = queueObject.getJSONObject("timetable");
                            JSONObject waitingInfoObject = timeObject.getJSONObject("waitingInfo");

                            WaitingQueue data = new WaitingQueue();

                            data.setTime(timeObject.getString("time"));
                            data.setQId(queueObject.getLong("id"));

                            data.setQueueName(waitingInfoObject.getString("name"));
                            data.setWId(waitingInfoObject.getLong("id"));
                            data.setMaxPerson(waitingInfoObject.getInt("maxPerson"));

                            ArrayList<UserInfo> tempUserList = new ArrayList<>();

                            JSONArray userArray = queueObject.getJSONArray("userQueues");

                            for (int j = 0; j < userArray.length(); j++) {
                                JSONObject userObject = userArray.getJSONObject(j).getJSONObject("user");

                                UserInfo tempUser = new UserInfo();
                                tempUser.setStudentCode(userObject.getLong("id"));
                                tempUser.setTel(userObject.getString("phoneNumber"));
                                tempUser.setName(userObject.getString("name"));

                                tempUserList.add(tempUser);
                            }

                            data.setWaitingPersonList(tempUserList);

                            mWaitingQueueList.add(data);
                        }
                        mListAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Log.e("set_Data", String.valueOf(e));
                        e.printStackTrace();
                    }
                    setList();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("errorCode", error.toString());
                    Toast.makeText(getApplicationContext(), "로딩에 실패하였습니다.", Toast.LENGTH_SHORT).show();
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

    public void setTempWaitingInfo(WaitingQueue selectQueue) {
        for (WaitingInfo tempInfo : DataApplication.waitingList) {
            if (tempInfo.getName().equals(selectQueue.getQueueName())) {
                tempWaitingInfo = tempInfo;
                return;
            }
        }
    }

    public void setList() {
        Log.e("Size", String.valueOf(mWaitingQueueList.size()));
        if (mWaitingQueueList.size() > 0) {
            for (WaitingQueue selectQueue : mWaitingQueueList) {
                setTempWaitingInfo(selectQueue);
                Log.e("temp", tempWaitingInfo.getName());

                String imgUrl;
                if (tempWaitingInfo.getUrlList().size() > 0) {
                    imgUrl = tempWaitingInfo.getUrlList().get(0);
                    Log.e("img", tempWaitingInfo.getUrlList().get(0));
                } else {
                    imgUrl = Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + R.drawable.empty_icon).toString();
                }

                boolean check = false;
                for (int j = 0; j < mWaitingList.size(); j++) {
                    if (mWaitingList.get(j).getName().equals(selectQueue.getQueueName())) {
                        check = true;
                        break;
                    }
                }
                if (!check) {
                    mWaitingList.add(new WaitingListItem(imgUrl, selectQueue.getQueueName(), selectQueue.getQId(), selectQueue.getWId(),
                            selectQueue.getWaitingPersonList().size(), tempWaitingInfo.getLocDetail()));
                }
            }
            binding.txtNotice.setText("개설한 웨이팅은 총 " + mWaitingList.size() + "건 입니다.");
        } else {
            binding.txtNotice.setText("개설한 웨이팅이 없습니다.");
        }

        mListAdapter.notifyDataSetChanged();
    }
}
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
import com.android.volley.toolbox.StringRequest;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ManageWaitingListActivity extends AppCompatActivity {
    private TextView txtResult, txtTitle;
    private RecyclerView recyclerView;
    private ArrayList<WaitingQueue> mWaitingQueueList;
    private ArrayList<WaitingListItem> mWaitingList;
    private ManageWaitingListAdapter mListAdapter;
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

//        myManageWaitingInfoRequest(((DataApplication) getApplication()).currentUser.getStudentCode());
        for (int i = 0; i < ((DataApplication) getApplication()).testDBList.size(); i++) {
            WaitingInfo tempInfo = ((DataApplication) getApplication()).testDBList.get(i);
            if (tempInfo.getAdminId().equals(((DataApplication) getApplication()).currentUser.getName())) {
                String qName = tempInfo.getName();
                for (int j = 0; j < ((DataApplication) getApplication()).testWaitingQueueDBList.size(); j++) {
                    WaitingQueue tempQ = ((DataApplication) getApplication()).testWaitingQueueDBList.get(j);
                    if (tempQ.getQueueName().equals(qName)) {
                        mWaitingQueueList.add(tempQ);
                    }
                }
            }
        }

        if (mWaitingQueueList.size() > 0) {
            for (int i = 0; i < mWaitingQueueList.size(); i++) {
                WaitingInfo tempInfo = new WaitingInfo();
                for (int j = 0; j < ((DataApplication) getApplication()).testDBList.size(); j++) {
                    WaitingInfo temp = ((DataApplication) getApplication()).testDBList.get(j);
                    if (temp.getName().equals(mWaitingQueueList.get(i).getQueueName())) {
                        tempInfo = temp;
                        break;
                    }
                }

                ImgItem tempImg = new ImgItem();
                for (int k = 0; k < ((DataApplication) getApplication()).testImageDBList.size(); k++) {
                    tempImg = ((DataApplication) getApplication()).testImageDBList.get(k);
                    if (tempImg.getName().equals(mWaitingQueueList.get(i).getQueueName())) {
                        break;
                    }
                }

                boolean check = false;
                for (int j = 0; j < mWaitingList.size(); j++) {
                    if (mWaitingList.get(j).getName().equals(mWaitingQueueList.get(i).getQueueName())) {
                        check = true;
                        break;
                    }
                }
                if (!check) {
                    mWaitingList.add(new WaitingListItem(tempImg.getUri(), mWaitingQueueList.get(i).getQueueName(),
                            mWaitingQueueList.get(i).getWaitingPersonList().size(),
                            tempInfo.getLocDetail()));
                }

            }
            txtResult.setText("개설한 웨이팅은 총 " + mWaitingList.size() + "건 입니다.");
        } else {
            txtResult.setText("개설한 웨이팅이 없습니다.");
        }


        mListAdapter.notifyDataSetChanged();
    }

    public void myManageWaitingInfoRequest(Long userId) {
        StringRequest request = new StringRequest(Request.Method.POST, ((DataApplication) getApplication()).serverURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {
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
                params.put("id", userId.toString());

                return params;
            }
        };

        request.setShouldCache(false);
        ((DataApplication) getApplication()).requestQueue.add(request);
    }

    public void imagesRequest(Long qId) {

    }
}
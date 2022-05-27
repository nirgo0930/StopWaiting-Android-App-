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
import com.example.stopwaiting.dto.ImgItem;
import com.example.stopwaiting.adapter.MyWaitingListAdapter;
import com.example.stopwaiting.R;
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

public class CheckMyWaitingActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<WaitingQueue> mWaitingQueue;
    private ArrayList<WaitingListItem> mWaitingList;
    private MyWaitingListAdapter mListAdapter;
    private TextView txtNotice;
    public static Activity myWaitingActivity;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiting_list);
        myWaitingActivity = CheckMyWaitingActivity.this;
        txtNotice = findViewById(R.id.txtNotice);
        recyclerView = findViewById(R.id.recyclerView);
        mWaitingList = new ArrayList<>();
        mWaitingQueue = new ArrayList<>();

        myWaitingRequest();


        mListAdapter = new MyWaitingListAdapter(this, mWaitingList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mListAdapter);

        mListAdapter.notifyDataSetChanged();
    }

    public void myWaitingRequest() {
        if (DataApplication.isTest) {
            mWaitingQueue = ((DataApplication) getApplication()).myWaiting;

            if (mWaitingQueue.size() > 0) {
                for (int i = 0; i < mWaitingQueue.size(); i++) {
                    WaitingInfo tempInfo = new WaitingInfo();
                    for (int j = 0; j < ((DataApplication) getApplication()).testDBList.size(); j++) {
                        WaitingInfo temp = ((DataApplication) getApplication()).testDBList.get(j);
                        if (temp.getName().equals(mWaitingQueue.get(i).getQueueName())) {
                            tempInfo = temp;
                            break;
                        }
                    }

                    ImgItem tempImg = new ImgItem();
                    for (int k = 0; k < ((DataApplication) getApplication()).testImageDBList.size(); k++) {
                        tempImg = ((DataApplication) getApplication()).testImageDBList.get(k);
                        if (tempImg.getName().equals(mWaitingQueue.get(i).getQueueName())) {
                            break;
                        }
                    }
                    mWaitingList.add(new WaitingListItem(tempImg.getUri(), mWaitingQueue.get(i).getQueueName(),
                            mWaitingQueue.get(i).getWaitingPersonList().indexOf(((DataApplication) getApplication()).currentUser),
                            tempInfo.getLocDetail()));
                }
                txtNotice.setText("신청한 웨이팅은 총 " + mWaitingList.size() + "건 입니다.");
            } else {
                txtNotice.setText("신청한 웨이팅이 없습니다.");
            }
        } else {
            JSONObject jsonBodyObj = new JSONObject();
            try {
                jsonBodyObj.put("id", DataApplication.currentUser.getStudentCode());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            final String requestBody = String.valueOf(jsonBodyObj.toString());

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, DataApplication.serverURL + "/myWaiting", null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            try {
                                JSONArray dataArray = jsonObject.getJSONArray("data");

                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject dataObject = dataArray.getJSONObject(i);

                                    WaitingQueue data = new WaitingQueue();

//                                    data.setQId(dataObject.getLong("qId"));
//                                    data.set
//
//                                    data.setWaitingId(dataObject.getLong("id"));
//                                    data.setAdminId(dataObject.getLong("adminId"));
//                                    data.setName(dataObject.getString("name"));
//                                    data.setLatitude(dataObject.getDouble("latitude"));
//                                    data.setLongitude(dataObject.getDouble("longitude"));
//                                    data.setLocDetail(dataObject.getString("locDetail"));
//                                    data.setInfo(dataObject.getString("information"));
//                                    data.setType(dataObject.getString("type"));
//                                    data.setMaxPerson(dataObject.getInt("maxPerson"));
//                                    if (data.getType().equals("time")) {
//                                        ArrayList<String> timetable = new ArrayList();
//                                        JSONArray timeArray = dataObject.getJSONArray("timetable");
//                                        for (int j = 0; j < timeArray.length(); j++) {
//                                            timetable.add(timeArray.getString(j));
//                                        }
//                                        data.setTimetable(timetable);
//                                    }
//                                    mWaitingList.add(data);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show();
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

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
import com.example.stopwaiting.adapter.MyWaitingListAdapter;
import com.example.stopwaiting.dto.ImgItem;
import com.example.stopwaiting.dto.WaitingInfo;
import com.example.stopwaiting.dto.WaitingListItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CheckMyWaitingActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    private ArrayList<WaitingListItem> mWaitingList;
    private MyWaitingListAdapter mListAdapter;
    private TextView txtNotice;
    public static Activity myWaitingActivity;

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiting_list);
        myWaitingActivity = CheckMyWaitingActivity.this;
        txtNotice = findViewById(R.id.txtNotice);
        recyclerView = findViewById(R.id.recyclerView);
        mWaitingList = new ArrayList<>();

        myWaitingRequest();

        mListAdapter = new MyWaitingListAdapter(this, mWaitingList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mListAdapter);

        mListAdapter.notifyDataSetChanged();
    }

    public void myWaitingRequest() {
        if (DataApplication.isTest) {
            if (DataApplication.myWaiting.size() > 0) {
                for (int i = 0; i < DataApplication.myWaiting.size(); i++) {
                    WaitingInfo tempInfo = new WaitingInfo();
                    for (int j = 0; j < ((DataApplication) getApplication()).testDBList.size(); j++) {
                        WaitingInfo temp = ((DataApplication) getApplication()).testDBList.get(j);
                        if (temp.getName().equals(DataApplication.myWaiting.get(i).getQueueName())) {
                            tempInfo = temp;
                            break;
                        }
                    }

                    ImgItem tempImg = new ImgItem();
                    for (int k = 0; k < ((DataApplication) getApplication()).testImageDBList.size(); k++) {
                        tempImg = ((DataApplication) getApplication()).testImageDBList.get(k);
                        if (tempImg.getName().equals(DataApplication.myWaiting.get(i).getQueueName())) {
                            break;
                        }
                    }

                    mWaitingList.add(new WaitingListItem(tempImg.getUri(), DataApplication.myWaiting.get(i).getQueueName(), DataApplication.myWaiting.get(i).getQId(),
                            DataApplication.myWaiting.get(i).getWaitingPersonList().indexOf(DataApplication.currentUser), tempInfo.getLocDetail()));
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
                                    WaitingListItem data = new WaitingListItem();

                                    data.setImgUrl(dataObject.getString("imgURL"));
                                    data.setQId(dataObject.getLong("qId"));
                                    data.setName(dataObject.getString("qName"));
                                    data.setWaitingCnt(dataObject.getInt("myCnt"));
                                    data.setLocDetail(dataObject.getString("locDetail"));

                                    mWaitingList.add(data);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (mWaitingList.size() > 0) {
                                txtNotice.setText("신청한 웨이팅은 총 " + mWaitingList.size() + "건 입니다.");
                            } else {
                                txtNotice.setText("신청한 웨이팅이 없습니다.");
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "내 웨이팅 조회에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                            txtNotice.setText("신청한 웨이팅이 없습니다.");
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

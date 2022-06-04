package com.example.stopwaitingadmin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.stopwaitingadmin.R;
import com.example.stopwaitingadmin.adapter.ReportedUserListAdapter;
import com.example.stopwaitingadmin.databinding.ReportedUserListBinding;
import com.example.stopwaitingadmin.dto.AdminWaitingListItem;
import com.example.stopwaitingadmin.dto.ReportedUserListItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportedUserActivity extends AppCompatActivity {
    private List<ReportedUserListItem> mReportedUserList;
    private RecyclerView mReportedUserListView;
    private ReportedUserListAdapter mReportedUserListAdapter;

    private ReportedUserListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ReportedUserListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        LoginActivity.login_Activity.finish();
//        txtNotice = findViewById(R.id.txtReportedUserNotice);

        rePortedUserQueueRequest();

        mReportedUserList = new ArrayList<>();
        mReportedUserListView = (RecyclerView) findViewById(R.id.ReportedUserRecyclerView);
        mReportedUserListAdapter = new ReportedUserListAdapter(this, mReportedUserList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mReportedUserListView.setLayoutManager(layoutManager);
        mReportedUserListView.setAdapter(mReportedUserListAdapter);

        // List 설정
        bindList();

        mReportedUserListAdapter.notifyDataSetChanged();

        binding.txtReportedUserNotice.setText("신고된 회원이 " + String.valueOf(mReportedUserList.size()) + "명 존재합니다.");

    }


    private void bindList() {
        mReportedUserList.add(new ReportedUserListItem(20171250, "한유현", 1));
        mReportedUserList.add(new ReportedUserListItem(12345678, "방진성", 1));
        mReportedUserList.add(new ReportedUserListItem(135792468, "이윤석", 2));
    }

    public void rePortedUserQueueRequest(){
        JSONObject jsonBodyObj = new JSONObject();

        final String requestBody = String.valueOf(jsonBodyObj.toString());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, DataApplication.serverURL + "/user/reported", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            JSONArray dataArray = jsonObject.getJSONArray("data");

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject dataObject = dataArray.getJSONObject(i);

                                ReportedUserListItem data = new ReportedUserListItem();

                                data.setReportedUserNum(dataObject.getInt("UserNum"));
                                data.setReportedUserName(dataObject.getString("UserName"));
                                data.setReportedCount(dataObject.getInt("reportedCount"));

//                                ArrayList<UserInfo> tempUserList = new ArrayList<>();
//                                JSONArray userArray = jsonObject.getJSONArray("waitingPersonList");
//                                for (int j = 0; j < userArray.length(); j++) {
//                                    JSONObject userObject = dataArray.getJSONObject(i);
//
//                                    UserInfo tempUser = new UserInfo();
//                                    tempUser.setStudentCode(userObject.getLong("id"));
//
//                                    tempUserList.add(tempUser);
//                                }
//                                data.setWaitingPersonList(tempUserList);

                                DataApplication.reportedUserQueue.add(data);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "신고된 사용자 목록 조회 실패", Toast.LENGTH_SHORT).show();
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
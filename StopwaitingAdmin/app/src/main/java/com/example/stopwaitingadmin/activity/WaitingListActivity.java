package com.example.stopwaitingadmin.activity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.stopwaitingadmin.R;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stopwaitingadmin.adapter.AdminWaitingListAdapter;
import com.example.stopwaitingadmin.databinding.AdminWaitListBinding;
import com.example.stopwaitingadmin.dto.AdminWaitingListItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WaitingListActivity extends AppCompatActivity {
    private List<AdminWaitingListItem> mWaitingItemList;
    private RecyclerView mWaitingListView;
    private AdminWaitingListAdapter mWaitingListAdapter;
    public static TextView txtNotice;

    private AdminWaitListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AdminWaitListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        LoginActivity.login_Activity.finish();

        waitingItemQueueRequest();

        txtNotice = findViewById(R.id.txtWaitingListNotice);

        mWaitingItemList = new ArrayList<>();
        mWaitingListView = findViewById(R.id.WaitingListRecyclerView);
        mWaitingListAdapter = new AdminWaitingListAdapter(this, mWaitingItemList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mWaitingListView.setLayoutManager(layoutManager);
        mWaitingListView.setAdapter(mWaitingListAdapter);

        // List 설정
        bindList();

        mWaitingListAdapter.notifyDataSetChanged();

        binding.txtWaitingListNotice.setText("승인 대기중인 웨이팅이 " + String.valueOf(mWaitingItemList.size()) + "건 존재합니다.");

    }


    private void bindList() {
        mWaitingItemList.add(new AdminWaitingListItem(1L, R.drawable.empty_icon, "미용실", "한유현", "학생회관 B208"));
        mWaitingItemList.add(new AdminWaitingListItem(2L, R.drawable.empty_icon, "특식배부", "방진성", "디지털관 DB134"));
        mWaitingItemList.add(new AdminWaitingListItem(3L, R.drawable.empty_icon, "북카페", "이윤석", "학생회관 B113"));
    }

    public void waitingItemQueueRequest(){
        JSONObject jsonBodyObj = new JSONObject();

        final String requestBody = String.valueOf(jsonBodyObj.toString());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, DataApplication.serverURL + "/waitinginfo/hold", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            JSONArray dataArray = jsonObject.getJSONArray("data");

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject dataObject = dataArray.getJSONObject(i);

                                AdminWaitingListItem data = new AdminWaitingListItem();

                                data.setImgId(dataObject.getInt("imgId"));
                                data.setTxtName(dataObject.getString("txtName"));
                                data.setTxtUser(dataObject.getString("txtUser"));
                                data.setTxtLocation(dataObject.getString("txtLocation"));

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

                                DataApplication.adminWaitingQueue.add(data);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "승인 대기중인 웨이팅 목록 조회 실패", Toast.LENGTH_SHORT).show();
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
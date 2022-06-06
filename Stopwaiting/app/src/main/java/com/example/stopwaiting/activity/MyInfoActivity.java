package com.example.stopwaiting.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.stopwaiting.databinding.MyinfoBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MyInfoActivity extends AppCompatActivity {
    public static Activity myInfoActivity;

    private MyinfoBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = MyinfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        myInfoActivity = MyInfoActivity.this;

        binding.txtMyName.setText(((DataApplication) getApplication()).currentUser.getName() + " 님");
        binding.txtUserName.setText("이름 : " + DataApplication.currentUser.getName());
        binding.txtUserNum.setText("학번 : " + String.valueOf(DataApplication.currentUser.getStudentCode()));
        binding.txtUserPhone.setText("전화번호 : " + DataApplication.currentUser.getTel());

        Intent myIntent = getIntent();

        binding.btnChangeMyInfo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = myIntent;
                intent.setClass(MyInfoActivity.this, UpdateMyInfoActivity.class);

                startActivity(intent);
            }
        });

        binding.btnDeleteUser.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                UserDeleteRequest();

                Intent intent = myIntent;
                intent.setClass(MyInfoActivity.this, LoginActivity.class);

                startActivity(intent);
            }
        });

//        MyInfoRequest();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        binding = MyinfoBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());

        binding.txtMyName.setText(((DataApplication) getApplication()).currentUser.getName() + " 님");
        binding.txtUserName.setText("이름 : " + DataApplication.currentUser.getName());
        binding.txtUserNum.setText("학번 : " + String.valueOf(DataApplication.currentUser.getStudentCode()));
        binding.txtUserPhone.setText("전화번호 : " + DataApplication.currentUser.getTel());

    }

    public void UserDeleteRequest() {

        JSONObject jsonBodyObj = new JSONObject();

        final String requestBody = String.valueOf(jsonBodyObj.toString());

        StringRequest request = new StringRequest(Request.Method.DELETE, DataApplication.serverURL + "/user/" + DataApplication.currentUser.getStudentCode(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String jsonObject) {
                        Toast.makeText(getApplicationContext(), "회원 정보를 삭제하였습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MyInfoActivity.this, MyPageActivity.class);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "회원 정보 삭제에 실패하였습니다.", Toast.LENGTH_SHORT).show();
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
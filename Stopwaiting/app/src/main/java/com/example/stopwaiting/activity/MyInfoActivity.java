package com.example.stopwaiting.activity;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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

        binding.txtUserName.setText(DataApplication.currentUser.getName());
        binding.txtUserNum.setText(Math.toIntExact(DataApplication.currentUser.getStudentCode()));
        binding.txtUserPhone.setText(DataApplication.currentUser.getTel());

//        MyInfoRequest();
    }

}
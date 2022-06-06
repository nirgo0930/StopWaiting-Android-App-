package com.example.stopwaiting.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.stopwaiting.databinding.SigninBinding;
import com.example.stopwaiting.databinding.UpdatemyinfoBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class UpdateMyInfoActivity extends AppCompatActivity {
    private UpdatemyinfoBinding binding;

    @Override
    protected void onStop() {
        super.onStop();
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = UpdatemyinfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.txtMyName.setText(DataApplication.currentUser.getName());

        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateRequest();
            }
        });

        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    public void UpdateRequest() {
        if (!binding.updatePassword.getText().toString().equals(binding.updatePwck.getText().toString())) {
            Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            JSONObject jsonBodyObj = new JSONObject();
            try {
                jsonBodyObj.put("password", binding.updatePassword.getText().toString());
                jsonBodyObj.put("phoneNumber", binding.updateTelNumber.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            final String requestBody = String.valueOf(jsonBodyObj.toString());

            StringRequest request = new StringRequest(Request.Method.PUT, DataApplication.serverURL + "/user/" + DataApplication.currentUser.getStudentCode(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String jsonObject) {
                            DataApplication.currentUser.setTel(binding.updateTelNumber.getText().toString());

                            Toast.makeText(getApplicationContext(), "회원 정보 수정에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(UpdateMyInfoActivity.this, MyInfoActivity.class);
                            startActivity(intent);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "회원 정보 수정에 실패하였습니다.", Toast.LENGTH_SHORT).show();
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

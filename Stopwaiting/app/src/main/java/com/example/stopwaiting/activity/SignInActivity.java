package com.example.stopwaiting.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.stopwaiting.R;
import com.example.stopwaiting.databinding.SigninBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends AppCompatActivity {
    //private EditText edtName, edtPw, edtPwCk, edtSCode, edtTelNum;
    private boolean dupCheck = false;

    private SigninBinding binding;

    @Override
    protected void onStop() {
        super.onStop();
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SigninBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        edtName = findViewById(R.id.edtName);
//        edtPw = findViewById(R.id.edtPassword);
//        edtPwCk = findViewById(R.id.edtPwck);
//        edtSCode = findViewById(R.id.edtStudentCode);
//        edtTelNum = findViewById(R.id.edtTelNumber);

        binding.btnNewSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInRequest();
            }
        });

        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.btnCheckDuplicate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dupCheckRequest();
            }
        });
    }

    public void signInRequest() {
        if (!binding.edtPassword.getText().toString().equals(binding.edtPwck.getText().toString())) {
            Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!dupCheck) {
            Toast.makeText(getApplicationContext(), "중복된 학번입니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject jsonBodyObj = new JSONObject();
        try {
            jsonBodyObj.put("id", Long.valueOf(binding.edtStudentCode.getText().toString()));
            jsonBodyObj.put("password", binding.edtPassword.getText().toString());
            jsonBodyObj.put("name", binding.edtName.getText().toString());
            jsonBodyObj.put("phoneNumber", binding.edtTelNumber.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody = String.valueOf(jsonBodyObj.toString());

        StringRequest request = new StringRequest(Request.Method.POST, DataApplication.serverURL + "/signup",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String jsonObject) {
                        Toast.makeText(getApplicationContext(), "회원가입에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignInActivity.this, LoginActivity.class);
                        startActivity(intent);
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

    public void dupCheckRequest() {
        JSONObject jsonBodyObj = new JSONObject();
        try {
            jsonBodyObj.put("id", Long.parseLong(binding.edtStudentCode.getText().toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody = String.valueOf(jsonBodyObj.toString());

        StringRequest request = new StringRequest(Request.Method.POST, DataApplication.serverURL + "/checkid",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String jsonObject) {
                        Toast.makeText(getApplicationContext(), "사용가능한 학번입니다.", Toast.LENGTH_SHORT).show();
                        dupCheck = true;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "사용할 수 없는 학번입니다.", Toast.LENGTH_SHORT).show();
                        dupCheck = false;
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

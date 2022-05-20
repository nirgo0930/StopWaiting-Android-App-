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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends AppCompatActivity {
    private EditText edtName, edtPw, edtPwCk, edtSCode, edtTelNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);

        edtName = findViewById(R.id.edtName);
        edtPw = findViewById(R.id.edtPassword);
        edtPwCk = findViewById(R.id.edtPwck);
        edtSCode = findViewById(R.id.edtStudentCode);
        edtTelNum = findViewById(R.id.edtTelNumber);

        findViewById(R.id.btnNewSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInRequest();
            }
        });

        findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.btnCheckDuplicate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dupCheckRequest(edtSCode.getText().toString());
            }
        });
    }

    public void signInRequest() {
        if (!edtPw.getText().toString().equals(edtPwCk.getText().toString())) {
            Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest request = new StringRequest(Request.Method.POST, ((DataApplication) getApplication()).serverURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {
                                Toast.makeText(getApplicationContext(), "회원가입에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignInActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show();
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
//                        textView.setText(error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("type", "signin");
                params.put("id", edtSCode.getText().toString());
                params.put("pw", edtPw.getText().toString());
                params.put("name", edtName.getText().toString());
                params.put("tel", edtTelNum.getText().toString());

                return params;
            }
        };

        request.setShouldCache(false);
        ((DataApplication) getApplication()).requestQueue.add(request);
    }

    public void dupCheckRequest(String sCode) {
        StringRequest request = new StringRequest(Request.Method.POST, ((DataApplication) getApplication()).serverURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {
                                Toast.makeText(getApplicationContext(), "사용가능한 학번입니다.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "사용할 수 없는 학번입니다.", Toast.LENGTH_SHORT).show();
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
//                        textView.setText(error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("type", "dupcheck");
                params.put("id", sCode);

                return params;
            }
        };

        request.setShouldCache(false);
        ((DataApplication) getApplication()).requestQueue.add(request);
    }
}

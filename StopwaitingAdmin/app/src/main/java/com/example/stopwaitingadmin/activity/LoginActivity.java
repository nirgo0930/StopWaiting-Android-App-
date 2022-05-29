package com.example.stopwaitingadmin.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.stopwaitingadmin.R;
import com.example.stopwaitingadmin.dto.AdminWaitingListItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private EditText edt_id, edt_password;
    private Button btn_login;
    public static Activity login_Activity;
    private final String sharedID = "Login";

    String[] permission_list = {
            Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onStop() {
        super.onStop();
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        login_Activity = LoginActivity.this;

        checkPermissions(permission_list);

        if (DataApplication.requestQueue == null)
            DataApplication.requestQueue = Volley.newRequestQueue(getApplicationContext());

        edt_id = findViewById(R.id.edtId);
        edt_password = findViewById(R.id.edtPw);
        btn_login = findViewById(R.id.btnLogin);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences(sharedID, Activity.MODE_PRIVATE);

                SharedPreferences.Editor autoLogin = sharedPreferences.edit();

                autoLogin.putString("inputId", edt_id.getText().toString());
                autoLogin.putString("inputPwd", edt_password.getText().toString());

                autoLogin.commit();

                loginRequest();

                // 이전 로그인 모듈
//                String userID = edt_id.getText().toString();
//                String userPass = edt_password.getText().toString();
//
//                if (userID.equals("test") && userPass.equals("test")) {
//                    Toast.makeText(getApplicationContext(), "로그인에 성공하였습니다.\n 잠시만 기다려주세요", Toast.LENGTH_SHORT).show();
//                    Intent loginIntent = new Intent(LoginActivity.this, AdminMainActivity.class);
//                    loginIntent.putExtra("userID", userID);
//                    loginIntent.putExtra("userPass", userPass);
//                    startActivity(loginIntent);
//                } else { // 로그인에 실패한 경우
//                    Toast.makeText(getApplicationContext(), "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
//                    return;
//                }
            }
        });
    }

    public void checkPermissions(String[] permissions) {
        ArrayList<String> targetList = new ArrayList<String>();

        for (int i = 0; i < permissions.length; i++) {
            String curPermission = permissions[i];
            int permissionCheck = ContextCompat.checkSelfPermission(this, curPermission);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this, curPermission + " 권한 있음", Toast.LENGTH_SHORT).show();
            } else {
//                Toast.makeText(this, curPermission + " 권한 없음", Toast.LENGTH_SHORT).show();
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, curPermission)) {
//                    Toast.makeText(this, curPermission + " 권한 설명 필요함.", Toast.LENGTH_SHORT).show();
                } else {
                    targetList.add(curPermission);
                }
            }
        }

        String[] targets = new String[targetList.size()];
        targetList.toArray(targets);

        // 위험 권한 부여 요청
        if (targets.length > 0) {
            ActivityCompat.requestPermissions(this, targets, 101);
        }
    }

    public void loginRequest() {
        if (DataApplication.isTest) {
            //로그인 정보 확인
            if (edt_id.getText().toString().equals("test") && edt_password.getText().toString().equals("test")) {
                // 로그인에 성공한 경우
                Toast.makeText(getApplicationContext(), "로그인에 성공하였습니다.\n 잠시만 기다려주세요", Toast.LENGTH_SHORT).show();

                Intent loginIntent = new Intent(LoginActivity.this, AdminMainActivity.class);
                startActivity(loginIntent);
            } else { // 로그인에 실패한 경우
                Toast.makeText(getApplicationContext(), "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            JSONObject jsonBodyObj = new JSONObject();
            try {
                jsonBodyObj.put("id", Long.valueOf(edt_id.getText().toString()));
                jsonBodyObj.put("password", edt_password.getText().toString());
//                jsonBodyObj.put("token", token);    //토큰
            } catch (JSONException e) {
                e.printStackTrace();
            }
            final String requestBody = String.valueOf(jsonBodyObj.toString());

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, ((DataApplication) getApplication()).serverURL + "/login", null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            Toast.makeText(getApplicationContext(), "로그인에 성공하였습니다.\n 잠시만 기다려주세요", Toast.LENGTH_SHORT).show();

//                                ((DataApplication) getApplication()).currentUser = temp;

                            Intent intent = new Intent(LoginActivity.this, AdminMainActivity.class);
                            startActivity(intent);

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
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
            ((DataApplication) getApplication()).requestQueue.add(request);
        }

    }
}
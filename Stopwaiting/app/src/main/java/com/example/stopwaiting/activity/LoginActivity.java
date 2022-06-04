package com.example.stopwaiting.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.stopwaiting.R;
import com.example.stopwaiting.databinding.LoginBinding;
import com.example.stopwaiting.dto.UserInfo;
import com.example.stopwaiting.dto.WaitingInfo;
import com.example.stopwaiting.dto.WaitingQueue;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
//    private EditText edt_id, edt_password;
//    private Button btn_login, btn_new;
    public static Activity login_Activity;
    private String sharedID = "Login";
    private String token;

    private LoginBinding binding;

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
        binding = LoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //setContentView(R.layout.login);
        login_Activity = LoginActivity.this;

        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String data) {
                token = data;
                Log.e("-----------------toekn", token);
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences(sharedID, Activity.MODE_PRIVATE);
        checkPermissions(permission_list);

        if (((DataApplication) getApplication()).requestQueue == null)
            ((DataApplication) getApplication()).requestQueue = Volley.newRequestQueue(getApplicationContext());

//        edt_id = findViewById(R.id.edtId);
//        edt_password = findViewById(R.id.edtPw);
//        btn_login = findViewById(R.id.btnLogin);
//        btn_new = findViewById(R.id.btnNewSignIn);

        String loginId = sharedPreferences.getString("inputId", null);
        String loginPwd = sharedPreferences.getString("inputPwd", null);

        if (loginId != null && loginPwd != null) {
            Toast.makeText(getApplicationContext(), loginId + "님 자동로그인 입니다!", Toast.LENGTH_SHORT).show();
            //edt_id.setText(loginId);
            //edt_password.setText(loginPwd);
            binding.edtId.setText(loginId);
            binding.edtPw.setText(loginPwd);

            loginRequest();
        }
        // 회원가입 버튼을 클릭 시 수행
        binding.btnNewSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences(sharedID, Activity.MODE_PRIVATE);

                SharedPreferences.Editor autoLogin = sharedPreferences.edit();

                autoLogin.putString("inputId", binding.edtId.getText().toString());
                autoLogin.putString("inputPwd", binding.edtPw.getText().toString());

                autoLogin.commit();

                loginRequest();
            }
        });
    }

    public void getTestInfo() {
        String root = "android.resource://" + R.class.getPackage().getName() + "/";

        ((DataApplication) this.getApplication()).qCnt = (long) 1;
        //test data
        ArrayList<String> temp = new ArrayList<>();
        temp.add("13:00");
        temp.add("14:00");
        temp.add("15:00");
        ((DataApplication) this.getApplication()).testDBList.add(new WaitingInfo
                (20170873L, 1L, 36.144760, 128.393884, "미용실", "학생회관 B208", "미용실입니다.", "TIME", 5, temp, new ArrayList<>()));
        for (int i = 0; i < temp.size(); i++) {
            ((DataApplication) getApplication()).testWaitingQueueDBList.add(new WaitingQueue(((DataApplication) this.getApplication()).qCnt++, "미용실", temp.get(i), 5));
        }

        ((DataApplication) this.getApplication()).testDBList.add(new WaitingInfo(20170873L, 2L, 36.145619, 128.392535,
                "특식배부", "디지털관 330", "컴소공 특식배부.", "NORMAL", 3, new ArrayList<>()));
        ((DataApplication) getApplication()).testWaitingQueueDBList.add(new WaitingQueue(((DataApplication) this.getApplication()).qCnt++, "특식배부", "NORMAL", 3));


        ArrayList<String> tempList = new ArrayList<>();
        tempList.add(Uri.parse(root + R.drawable.haircut_cost).toString());
        tempList.add(Uri.parse(root + R.drawable.human_icon).toString());


        ((DataApplication) this.getApplication()).testDBList.add(new WaitingInfo(20170526L, 3L, 36.145123, 128.394244,
                "북카페", "학생회관 B218", "북카페입니다.", "NORMAL", 10, tempList));
        WaitingQueue tempQ = new WaitingQueue(((DataApplication) this.getApplication()).qCnt++, "북카페", "NORMAL", 10);
        tempQ.addWPerson(((DataApplication) getApplication()).currentUser);
        ((DataApplication) getApplication()).testWaitingQueueDBList.add(tempQ);

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 101:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(this, "첫 번째 권한을 사용자가 승인함", Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(this, "첫 번째 권한 거부됨", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    public void loginRequest() {
        if (((DataApplication) getApplication()).isTest) {
            if (binding.edtPw.getText().toString().equals("test")) {
                Toast.makeText(getApplicationContext(), "로그인에 성공하였습니다.\n 잠시만 기다려주세요", Toast.LENGTH_SHORT).show();

                ((DataApplication) getApplication()).currentUser = new UserInfo("test", Long.valueOf(binding.edtId.getText().toString()), "01094536639");

                if (((DataApplication) getApplication()).isFirstBoot && ((DataApplication) getApplication()).isTest) {
                    getTestInfo();
                    ((DataApplication) getApplication()).isFirstBoot = false;
                }

                Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(loginIntent);
            } else { // 로그인에 실패한 경우
                Toast.makeText(getApplicationContext(), "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            JSONObject jsonBodyObj = new JSONObject();
            try {
                jsonBodyObj.put("id", Long.valueOf(binding.edtId.getText().toString()));
                jsonBodyObj.put("password", binding.edtPw.getText().toString());
                jsonBodyObj.put("token", token);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            final String requestBody = String.valueOf(jsonBodyObj.toString());

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, ((DataApplication) getApplication()).serverURL + "/login", null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            try {
                                Toast.makeText(getApplicationContext(), "로그인에 성공하였습니다.\n 잠시만 기다려주세요", Toast.LENGTH_SHORT).show();

                                UserInfo temp = new UserInfo();
                                temp.setStudentCode(jsonObject.getLong("id"));
                                temp.setName(jsonObject.getString("name"));
                                temp.setTel(jsonObject.getString("phoneNumber"));

                                ((DataApplication) getApplication()).currentUser = temp;

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
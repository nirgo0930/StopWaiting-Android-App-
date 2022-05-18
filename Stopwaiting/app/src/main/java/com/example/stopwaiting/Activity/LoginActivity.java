package com.example.stopwaiting.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.stopwaiting.DTO.ImgItem;
import com.example.stopwaiting.R;
import com.example.stopwaiting.DTO.WaitingInfo;
import com.example.stopwaiting.DTO.WaitingQueue;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    private EditText edt_id, edt_pass;
    private Button btn_login, btn_new;
    public static Activity login_Activity;

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

        edt_id = findViewById(R.id.edtId);
        edt_pass = findViewById(R.id.edtPw);
        btn_login = findViewById(R.id.btnLogin);
        btn_new = findViewById(R.id.btnNewSignIn);

        // 회원가입 버튼을 클릭 시 수행
        btn_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userID = edt_id.getText().toString();
                String userPass = edt_pass.getText().toString();

                if (userID.equals("test") && userPass.equals("test")) {
                    Toast.makeText(getApplicationContext(), "로그인에 성공하였습니다.\n 잠시만 기다려주세요", Toast.LENGTH_SHORT).show();
                    ((DataApplication) getApplication()).userId = userID;
                    ((DataApplication) getApplication()).userCode = "20170873";
                    getTestInfo();

                    Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(loginIntent);
                } else { // 로그인에 실패한 경우
                    Toast.makeText(getApplicationContext(), "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

//                Response.Listener<String> responseListener = new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            boolean success = jsonObject.getBoolean("success");
//                            if (success) { // 로그인에 성공한 경우
//                                String userID = jsonObject.getString("userID");
//                                String userPass = jsonObject.getString("userPassword");
//
//                                Toast.makeText(getApplicationContext(), "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                                intent.putExtra("userID", userID);
//                                intent.putExtra("userPass", userPass);
//                                startActivity(intent);
//                            } else { // 로그인에 실패한 경우
//                                Toast.makeText(getApplicationContext(), "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                };

//                LoginRequest loginRequest = new LoginRequest(userID, userPass, responseListener);
//                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
//                queue.add(loginRequest);
            }
        });

        checkPermissions(permission_list);
    }

    public void getTestInfo() {
        //test data
        ArrayList<String> temp = new ArrayList<>();
        temp.add("13:00");
        temp.add("14:00");
        temp.add("15:00");
        ((DataApplication) this.getApplication()).testDBList.add(new WaitingInfo
                ("test", 1L, 36.144760, 128.393884, "미용실", "학생회관 B208", "미용실입니다.", "time", 5, temp));
        for (int i = 0; i < temp.size(); i++) {
            ((DataApplication) getApplication()).testWaitingQueueDBList.add(new WaitingQueue("미용실", temp.get(i), 5));
        }

        ((DataApplication) this.getApplication()).testDBList.add(new WaitingInfo
                ("test", 2L, 36.145619, 128.392535, "특식배부", "디지털관 330", "컴소공 특식배부.", "normal", 3));
        ((DataApplication) getApplication()).testWaitingQueueDBList.add(new WaitingQueue("특식배부", "normal", 3));

        ((DataApplication) this.getApplication()).testDBList.add(new WaitingInfo
                ("admin", 3L, 36.145123, 128.394244, "북카페", "학생회관 B218", "북카페입니다.", "normal", 10));
        ((DataApplication) getApplication()).testWaitingQueueDBList.add(new WaitingQueue("북카페", "normal", 10));

        String root = "android.resource://" + R.class.getPackage().getName() + "/";
        ((DataApplication) this.getApplication()).testImageDBList.add(new ImgItem("북카페", (long) 1, Uri.parse(root + R.drawable.haircut_cost)));
        ((DataApplication) this.getApplication()).testImageDBList.add(new ImgItem("북카페", (long) 2, Uri.parse(root + R.drawable.human_icon)));
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
}
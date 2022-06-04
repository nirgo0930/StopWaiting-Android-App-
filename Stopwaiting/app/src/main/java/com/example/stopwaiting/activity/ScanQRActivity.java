package com.example.stopwaiting.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.stopwaiting.R;
import com.example.stopwaiting.databinding.ScanQrBinding;
import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScanQRActivity extends AppCompatActivity {
    private IntentIntegrator qrScan;

    private ScanQrBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ScanQrBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        qrScan = new IntentIntegrator(this);
        qrScan.setCameraId(1);
//        qrScan.setOrientationLocked(true); // default가 세로모드인데 휴대폰 방향에 따라 가로, 세로로 자동 변경됩니다.
        qrScan.setPrompt("QR코드를 사각형 안에 넣어주세요.");
        qrScan.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();

                Intent returnData = new Intent();
                returnData.putExtra("qr", result.getContents());
                setResult(Activity.RESULT_OK, returnData);
            }
            finish();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
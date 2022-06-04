package com.example.stopwaiting.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.example.stopwaiting.databinding.ActivityQrBinding;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QRActivity extends Activity {
    private String text;

    private ActivityQrBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityQrBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        text = String.valueOf(DataApplication.currentUserInfo.getStudentCode());

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try{
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,160,160);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            binding.qrcode.setImageBitmap(bitmap);
        }catch (Exception e){}
    }
}
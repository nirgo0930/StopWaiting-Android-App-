package com.example.stopwaiting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ManageWaitingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public static Activity manageWaitingActivity;
    private ArrayList<WaitingQueue> wQueue;
    private WaitingInfo wInfo;
    private Button btnCheckOut;
    private Button btnCheckIn;
    private TextView txtWaitingCnt, choice;
    private Spinner spinner;
    private ArrayList<String> timeList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_waiting);
        Intent intent = getIntent();
        manageWaitingActivity = ManageWaitingActivity.this;
        wQueue = new ArrayList<>();
        wInfo = new WaitingInfo();

        for (int i = 0; i < ((DataApplication) getApplication()).testDBList.size(); i++) {
            if (((DataApplication) getApplication()).testDBList.get(i).getName().equals(intent.getStringExtra("name"))) {
                wInfo = ((DataApplication) getApplication()).testDBList.get(i);
                break;
            }
        }

        for (int i = 0; i < ((DataApplication) getApplication()).testWaitingQueueDBList.size(); i++) {
            if (((DataApplication) getApplication()).testWaitingQueueDBList.get(i).equals(wInfo.getName())) {
                wQueue.add(((DataApplication) getApplication()).testWaitingQueueDBList.get(i));
            }
        }
        btnCheckIn = (Button) findViewById(R.id.btnCheckIn);
        btnCheckOut = (Button) findViewById(R.id.btnCheckOut);
        txtWaitingCnt = findViewById(R.id.txtWaitingCnt);
        choice = findViewById(R.id.txtSelectTime);
        spinner = findViewById(R.id.spnTime);
        choice = findViewById(R.id.txtSelectTime);

        btnCheckIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ManageWaitingActivity.this, ScanQRActivity.class);
                startActivityForResult(intent, 4000);
            }
        });

        btnCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        timeList = new ArrayList<>();
        if (wInfo.getTimetable() != null) {
            timeList = wInfo.getTimetable();
            if (spinner != null) {
                spinner.setOnItemSelectedListener(this);
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, timeList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        } else {
            timeList.add("normal");
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (requestCode == 4000) {
            if (resultCode == RESULT_OK) {
                if (result != null) {
                    if (result.getContents() == null) {
                    } else {
                        String selectQr = data.getStringExtra("qr");
                        if (selectQr == ((DataApplication) getApplication()).userCode) {
                            String tempName = "test";

                        }
                    }
                } else {
                    super.onActivityResult(requestCode, resultCode, data);
                }
            }
        }
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        String spinner_item = adapterView.getItemAtPosition(pos).toString();

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String nowTime = sdf.format(new Date(System.currentTimeMillis()));

        txtWaitingCnt.setText("0 명");

        if (((DataApplication) this.getApplication()).firstIsLater(spinner_item, nowTime)) {
            for (int i = 0; i < wQueue.size(); i++) {
                WaitingQueue temp = wQueue.get(i);
                if (temp.getTime().equals(spinner_item)) {
                    if (temp.getWaitingPersonList() != null) {
                        txtWaitingCnt.setText(String.valueOf(temp.getWaitingPersonList().size()) + " 명");
                    } else {
                        txtWaitingCnt.setText("0 명");
                    }
                    break;
                }
            }
            choice.setText(spinner_item);
        } else {
            onNothingSelected(adapterView);
            Toast.makeText(this, "선택한 시간은 예약이 불가능합니다.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
        choice.setText("예약할 시간을 선택해 주세요.");
    }
}

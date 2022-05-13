package com.example.stopwaiting;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class SettingTimeActivity extends AppCompatActivity {
    private EditText edtMax;
    private ListView listView;
    private TimePicker timePicker;
    private Button btnComplete;
    private ArrayList<String> mTimeList;
    private ArrayAdapter<String> mArrayAdapter;
    private ArrayList<Uri> uriList;
    private Intent timeIntent;
    public static Activity setting_time_Activity;
    private final static int TIME_PICKER_INTERVAL = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_time);

        timeIntent = getIntent();
        setting_time_Activity = SettingTimeActivity.this;

        edtMax = findViewById(R.id.edtMaxPerson);
        listView = findViewById(R.id.listView);
        timePicker = findViewById(R.id.timepicker);
        btnComplete = findViewById(R.id.btnComplete);
        setTimePickerInterval(timePicker);

        mTimeList = new ArrayList<>();
        uriList = new ArrayList<>();
        uriList = timeIntent.getParcelableArrayListExtra("image");
        if (timeIntent.getParcelableArrayListExtra("image") != null) {
            uriList = timeIntent.getParcelableArrayListExtra("image");
        }

        findViewById(R.id.btnAdd).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                createTextView();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                mTimeList.remove(i);
                mArrayAdapter.notifyDataSetChanged();
                return true;
            }
        });
        mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mTimeList);
        listView.setAdapter(mArrayAdapter);

        bindComplete();
    }

    private void bindComplete() {
        btnComplete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //서버 전송
                ((DataApplication) getApplication()).testDBList.add(new WaitingInfo(11L,
                        timeIntent.getDoubleExtra("latitude", 0),
                        timeIntent.getDoubleExtra("longitude", 0),
                        timeIntent.getStringExtra("name"), timeIntent.getStringExtra("detail"), timeIntent.getStringExtra("info"),
                        "time", timeIntent.getIntExtra("maxPerson", 1), mTimeList));
                //
                if (uriList != null) {
                    String wname = timeIntent.getStringExtra("name");
                    for (int i = 0; i < uriList.size(); i++) {
                        ((DataApplication) getApplication()).testImageDBList.add(new ImgItem(wname, (long) i + 1, uriList.get(i)));
                    }
                }
                for (int i = 0; i < mTimeList.size(); i++) {
                    ((DataApplication) getApplication()).testWaitingQueueDBList.add(
                            new WaitingQueue(timeIntent.getStringExtra("name"), mTimeList.get(i), timeIntent.getIntExtra("maxPerson", 1)));
                }
                Intent temp = new Intent(SettingTimeActivity.this, MyPageActivity.class);
                temp.putExtra("userID", timeIntent.getStringExtra("userID"));
                MyPageActivity.myPageActivity.finish();
                startActivity(temp);
            }
        });
    }

    private void createTextView() {
        String hour = String.valueOf(timePicker.getHour());
        if (timePicker.getHour() < 10) {
            hour = "0" + hour;
        }
        String minute = String.valueOf(timePicker.getMinute() * TIME_PICKER_INTERVAL);
        if (timePicker.getMinute() * TIME_PICKER_INTERVAL < 10) {
            minute = "0" + minute;
        }
        mTimeList.add(hour + ":" + minute); //10:00 11:30
        mArrayAdapter.notifyDataSetChanged();
    }

    private void setTimePickerInterval(TimePicker timePicker) {
        try {
            NumberPicker minutePicker = (NumberPicker) timePicker.findViewById(Resources.getSystem().getIdentifier(
                    "minute", "id", "android"));
            minutePicker.setMinValue(0);
            minutePicker.setMaxValue((60 / TIME_PICKER_INTERVAL) - 1);
            List<String> displayedValues = new ArrayList<String>();
            for (int i = 0; i < 60; i += TIME_PICKER_INTERVAL) {
                displayedValues.add(String.format("%02d", i));
            }
            minutePicker.setDisplayedValues(displayedValues.toArray(new String[0]));
        } catch (Exception e) {
            Log.e("error", "Exception: " + e);
        }
    }
}

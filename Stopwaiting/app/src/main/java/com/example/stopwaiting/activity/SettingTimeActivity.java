package com.example.stopwaiting.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.stopwaiting.R;
import com.example.stopwaiting.dto.WaitingInfo;
import com.example.stopwaiting.dto.WaitingQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private int mStatusCode = 0;


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


        btnComplete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                addWaitingRequest();
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

    public void addWaitingRequest() {
        if (DataApplication.isTest) {
            ArrayList<String> imgList = new ArrayList<>();
            if (uriList.size() > 0) {
                for (int i = 0; i < uriList.size(); i++) {
                    imgList.add(uriList.get(i).toString());
                }
            }
            DataApplication.testDBList.add(new WaitingInfo(DataApplication.currentUser.getStudentCode(), 11L,
                    timeIntent.getDoubleExtra("latitude", 0),
                    timeIntent.getDoubleExtra("longitude", 0),
                    timeIntent.getStringExtra("name"), timeIntent.getStringExtra("detail"), timeIntent.getStringExtra("info"),
                    "TIME", timeIntent.getIntExtra("maxPerson", 1), mTimeList, imgList));

            for (int i = 0; i < mTimeList.size(); i++) {
                DataApplication.testWaitingQueueDBList.add(new WaitingQueue(DataApplication.qCnt++,
                        timeIntent.getStringExtra("name"), mTimeList.get(i),
                        timeIntent.getIntExtra("maxPerson", 1)));
            }
            Intent temp = new Intent(SettingTimeActivity.this, MyPageActivity.class);
            temp.putExtra("userID", timeIntent.getStringExtra("userID"));
            MyPageActivity.myPageActivity.finish();
            startActivity(temp);
        } else {
            JSONObject jsonBodyObj = new JSONObject();
            try {
                jsonBodyObj.put("admin", DataApplication.currentUser.getStudentCode());
                jsonBodyObj.put("latitude", timeIntent.getDoubleExtra("latitude", 0));
                jsonBodyObj.put("longitude", timeIntent.getDoubleExtra("longitude", 0));
                jsonBodyObj.put("waitingName", timeIntent.getStringExtra("name"));
                jsonBodyObj.put("locationDetail", timeIntent.getStringExtra("detail"));
                jsonBodyObj.put("information", timeIntent.getStringExtra("info"));
                jsonBodyObj.put("maxPerson", timeIntent.getIntExtra("maxPerson", 0));
                jsonBodyObj.put("type", "time");

                JSONArray timeArray = new JSONArray();
                for (int i = 0; i < mTimeList.size(); i++) {
                    timeArray.put(mTimeList.get(i));
                }
                jsonBodyObj.put("timetable", timeArray);

                JSONArray imgArray = new JSONArray();
                for (int i = 0; i < uriList.size(); i++) {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriList.get(i));
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] imageBytes = baos.toByteArray();
                    String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                    imgArray.put(encodedImage);
                }
                jsonBodyObj.put("images", imgArray);

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            final String requestBody = String.valueOf(jsonBodyObj.toString());

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, DataApplication.serverURL + "/waitinginfo", null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            int statusCode = mStatusCode;
                            switch (statusCode) {
                                case HttpURLConnection.HTTP_OK:
                                    Toast.makeText(getApplicationContext(), "정상 등록되었습니다.", Toast.LENGTH_SHORT).show();

                                    Intent temp = new Intent(SettingTimeActivity.this, MyPageActivity.class);
                                    MyPageActivity.myPageActivity.finish();
                                    startActivity(temp);
                                    break;
//                                case HttpURLConnection.HTTP_NOT_FOUND:
                                //do stuff
//                                    break;
//                                case HttpURLConnection.HTTP_INTERNAL_ERROR:
                                //do stuff
//                                    break;
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
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

                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    if (response != null) {
                        mStatusCode = response.statusCode;
                    }
                    return super.parseNetworkResponse(response);
                }
            };

            request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            request.setShouldCache(false);
            DataApplication.requestQueue.add(request);
        }
    }
}

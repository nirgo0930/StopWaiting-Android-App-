package com.example.stopwaiting.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.stopwaiting.databinding.SettingTimeBinding;
import com.example.stopwaiting.dto.WaitingInfo;
import com.example.stopwaiting.dto.WaitingQueue;
import com.example.stopwaiting.service.MultipartRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingTimeActivity extends AppCompatActivity {
    //private ListView listView;
    //private TimePicker timePicker;
    //private Button btnComplete;
    private ArrayList<String> mTimeList;
    private ArrayAdapter<String> mArrayAdapter;
    private ArrayList<Uri> uriList;
    private Intent timeIntent;
    public static Activity setting_time_Activity;
    private final static int TIME_PICKER_INTERVAL = 10;
    private int mStatusCode = 0;

    private SettingTimeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SettingTimeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        timeIntent = getIntent();
        setting_time_Activity = SettingTimeActivity.this;

//        listView = findViewById(R.id.listView);
//        timePicker = findViewById(R.id.timepicker);
//        btnComplete = findViewById(R.id.btnComplete);
        setTimePickerInterval(binding.timepicker);

        mTimeList = new ArrayList<>();
        uriList = new ArrayList<>();
        uriList = timeIntent.getParcelableArrayListExtra("image");
        if (timeIntent.getParcelableArrayListExtra("image") != null) {
            uriList = timeIntent.getParcelableArrayListExtra("image");
        }


        binding.btnAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                createTextView();
            }
        });

        binding.listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                mTimeList.remove(i);
                mArrayAdapter.notifyDataSetChanged();
                return true;
            }
        });
        mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mTimeList);
        binding.listView.setAdapter(mArrayAdapter);


        binding.btnComplete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                addWaitingRequest();
            }
        });
    }

    private void createTextView() {
        String hour = String.valueOf(binding.timepicker.getHour());
        if (binding.timepicker.getHour() < 10) {
            hour = "0" + hour;
        }
        String minute = String.valueOf(binding.timepicker.getMinute() * TIME_PICKER_INTERVAL);
        if (binding.timepicker.getMinute() * TIME_PICKER_INTERVAL < 10) {
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

            ArrayList<Long> tempList = new ArrayList<>();
            for (int i = 0; i < mTimeList.size(); i++) {
                tempList.add(DataApplication.qCnt);
                DataApplication.testWaitingQueueDBList.add(new WaitingQueue(11L, DataApplication.qCnt++,
                        timeIntent.getStringExtra("name"), mTimeList.get(i),
                        timeIntent.getIntExtra("maxPerson", 1)));
            }
            DataApplication.testDBList.add(new WaitingInfo(DataApplication.currentUser.getStudentCode(), 11L,
                    timeIntent.getDoubleExtra("latitude", 0),
                    timeIntent.getDoubleExtra("longitude", 0),
                    timeIntent.getStringExtra("name"), timeIntent.getStringExtra("detail"), timeIntent.getStringExtra("info"),
                    "TIME", timeIntent.getIntExtra("maxPerson", 1), mTimeList, new ArrayList<>(), tempList));


            addImageRequest(11L);
        } else {
            JSONObject jsonBodyObj = new JSONObject();
            try {
                jsonBodyObj.put("adminId", DataApplication.currentUser.getStudentCode());
                jsonBodyObj.put("latitude", timeIntent.getDoubleExtra("latitude", 0));
                jsonBodyObj.put("longitude", timeIntent.getDoubleExtra("longitude", 0));
                jsonBodyObj.put("name", timeIntent.getStringExtra("name"));
                jsonBodyObj.put("locationDetail", timeIntent.getStringExtra("detail"));
                jsonBodyObj.put("information", timeIntent.getStringExtra("info"));
                jsonBodyObj.put("maxPerson", timeIntent.getIntExtra("maxPerson", 0));
                jsonBodyObj.put("type", "TIME");

                JSONArray timeArray = new JSONArray();
                for (int i = 0; i < mTimeList.size(); i++) {
                    timeArray.put(mTimeList.get(i));
                }
                jsonBodyObj.put("timetables", timeArray);


            } catch (JSONException e) {
                e.printStackTrace();
            }
            final String requestBody = String.valueOf(jsonBodyObj.toString());

            StringRequest request = new StringRequest(Request.Method.POST, DataApplication.serverURL + "/waitinginfo",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String jsonObject) {
                            Log.e("response", jsonObject);
                            if (uriList.size() > 0) {
                                addImageRequest(Long.valueOf(jsonObject));
                            } else {
                                Intent temp = new Intent(SettingTimeActivity.this, MyPageActivity.class);
                                MyPageActivity.myPageActivity.finish();
                                startActivity(temp);
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
            };

            request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            request.setShouldCache(false);
            DataApplication.requestQueue.add(request);
        }
    }

    private void addImageRequest(Long waitingId) {
        if (DataApplication.isTest) {
            ArrayList<String> imgList = new ArrayList<>();
            if (uriList.size() > 0) {
                for (int i = 0; i < uriList.size(); i++) {
                    imgList.add(uriList.get(i).toString());
                }
            }
            for (int i = 0; i < DataApplication.testDBList.size(); i++) {
                if (DataApplication.testDBList.get(i).getWaitingId().equals(waitingId)) {
                    WaitingInfo temp = DataApplication.testDBList.get(i);
                    temp.setUrlList(imgList);
                    break;
                }
            }

            Intent temp = new Intent(SettingTimeActivity.this, MyPageActivity.class);
            MyPageActivity.myPageActivity.finish();
            startActivity(temp);
        } else {
            MultipartRequest multipartRequest = new MultipartRequest(Request.Method.POST, DataApplication.serverURL + "/waitinginfo/" + waitingId + "/images",
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            Intent temp = new Intent(SettingTimeActivity.this, MyPageActivity.class);
                            MyPageActivity.myPageActivity.finish();
                            startActivity(temp);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    NetworkResponse networkResponse = error.networkResponse;
                    String errorMessage = "Unknown error";
                    if (networkResponse == null) {
                        if (error.getClass().equals(TimeoutError.class)) {
                            errorMessage = "Request timeout";
                        } else if (error.getClass().equals(NoConnectionError.class)) {
                            errorMessage = "Failed to connect server";
                        }
                    } else {
                        String result = new String(networkResponse.data);
                        try {
                            JSONObject response = new JSONObject(result);
                            String status = response.getString("status");
                            String message = response.getString("message");

                            Log.e("Error Status", status);
                            Log.e("Error Message", message);

                            if (networkResponse.statusCode == 404) {
                                errorMessage = "Resource not found";
                            } else if (networkResponse.statusCode == 401) {
                                errorMessage = message + " Please login again";
                            } else if (networkResponse.statusCode == 400) {
                                errorMessage = message + " Check your inputs";
                            } else if (networkResponse.statusCode == 500) {
                                errorMessage = message + " Something is getting wrong";
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.i("Error", errorMessage);
                    error.printStackTrace();
                }
            }) {
                @Override
                protected Map<String, DataPart[]> getByteData() {
                    Map<String, DataPart[]> params = new HashMap<>();

                    if (uriList != null) {
                        DataPart[] temp = new DataPart[uriList.size()];
                        for (int i = 0; i < uriList.size(); i++) {
                            Bitmap bitmap = null;
                            try {
                                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriList.get(i));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] imageBytes = baos.toByteArray();


                            temp[i] = new MultipartRequest.DataPart(waitingId + "_" + String.valueOf(i) + ".jpg", imageBytes, "image/jpeg");
                        }
                        params.put("files", temp);
                    }

                    return params;
                }
            };

            DataApplication.requestQueue.add(multipartRequest);
        }
    }
}
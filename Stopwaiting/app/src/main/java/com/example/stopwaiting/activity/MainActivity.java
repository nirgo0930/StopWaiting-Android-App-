package com.example.stopwaiting.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.stopwaiting.R;
import com.example.stopwaiting.dto.WaitingInfo;
import com.example.stopwaiting.dto.WaitingQueue;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.util.FusedLocationSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, Overlay.OnClickListener {
    private NaverMap naverMap;
    private FusedLocationSource locationSource;
    private ArrayList<Marker> markers;
    public static ArrayList<WaitingInfo> waitingList;

    public static Activity mainActivity;
    public static Context context_main;
    private Intent mainIntent;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private static final int WAITING_LOCATION_REQUEST_CODE = 2000;
    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage);

        mainIntent = getIntent();
        mainActivity = MainActivity.this;
        context_main = this;

        TextView userId = findViewById(R.id.txtUser);
        userId.setText(((DataApplication) getApplication()).currentUser.getName() + " 님");

        markers = new ArrayList<>();
        waitingList = new ArrayList<>();

        waitingInfoAllRequest();

        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        Button myPage = findViewById(R.id.btnMypage);
        myPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = mainIntent;
                intent.setClass(MainActivity.this, MyPageActivity.class);

                startActivityForResult(intent, WAITING_LOCATION_REQUEST_CODE);
            }
        });

        Button refresh = findViewById(R.id.btnRefresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh();
            }
        });

        refresh();
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        naverMap.setLocationSource(locationSource);  //현재위치 표시
        ActivityCompat.requestPermissions(this, PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE);

        waitingInfoAllRequest();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) {
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
                return;
            } else {
                naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onClick(@NonNull Overlay overlay) {
        if (overlay instanceof InfoWindow) {
            InfoWindow infoWindow = (InfoWindow) overlay;
            if (infoWindow.getAdapter() != null) {
                Intent infoIntent;
                WaitingInfo temp = new WaitingInfo();
                temp.setWaitingId(Long.valueOf(infoWindow.getMarker().getTag().toString()));
                temp = waitingList.get(waitingList.indexOf(temp));

                if (temp.getType() == "time") {
                    infoIntent = new Intent(MainActivity.this, WaitingSelectTimeActivity.class);
                } else {
                    infoIntent = new Intent(MainActivity.this, WaitingNormalActivity.class);
                }
                infoIntent.putExtra("name", temp.getName());

                startActivity(infoIntent);
            }
            return true;
        }
        return false;
    }

    public void setInfo(WaitingInfo waitingInfo) {
        InfoWindow infoWindow = new InfoWindow();
        Marker marker = new Marker();

        marker.setPosition(new LatLng(waitingInfo.getLatitude(), waitingInfo.getLongitude()));
        marker.setMap(naverMap);
        marker.setWidth(1);
        marker.setHeight(1);

        marker.setTag(String.valueOf(waitingInfo.getWaitingId()));

        markers.add(marker);

        infoWindow.open(marker);
        infoWindow.setOnClickListener(this);

        setInfoWindowText(infoWindow, waitingInfo.getName());
    }

    public void setInfoWindowText(InfoWindow info, String str) {
        info.setAdapter(new InfoWindow.DefaultTextAdapter(this) {
            @NonNull
            @Override
            public CharSequence getText(@NonNull InfoWindow infoWindow) {
                return str;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        refresh();

        if (requestCode == WAITING_LOCATION_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String selectName = data.getStringExtra("name");

                for (int i = 0; i < waitingList.size(); i++) {
                    WaitingInfo temp = waitingList.get(i);
                    if (temp.getName().equals(selectName)) {
                        CameraUpdate cameraUpdate = CameraUpdate.scrollAndZoomTo(
                                new LatLng(temp.getLatitude(), temp.getLongitude()), 15)
                                .animate(CameraAnimation.Fly, 1000);
                        naverMap.moveCamera(cameraUpdate);

                        break;
                    }

                }
            } else {

            }
        } else if (requestCode == 1) {

        }
    }

    public void refresh() {
        for (int i = 0; i < markers.size(); i++) {
            markers.get(i).setMap(null);
        }
        markers = new ArrayList<>();
        waitingList = new ArrayList<>();
        waitingInfoAllRequest();
        myWaitingQueueRequest();

        ActivityCompat.requestPermissions(mainActivity, PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE);


        setWearOS();
    }


    public void myWaitingQueueRequest() {
        ((DataApplication) getApplication()).myWaiting = new ArrayList<>();
        if (DataApplication.isTest) {
            for (int i = 0; i < ((DataApplication) getApplication()).testWaitingQueueDBList.size(); i++) {
                WaitingQueue tempDBQ = ((DataApplication) getApplication()).testWaitingQueueDBList.get(i);
                for (int j = 0; j < tempDBQ.getWaitingPersonList().size(); j++) {
                    if (tempDBQ.getWaitingPersonList().get(j).getStudentCode().equals(((DataApplication) getApplication()).currentUser.getStudentCode()) &&
                            !(((DataApplication) getApplication()).myWaiting.contains(tempDBQ))) {
                        ((DataApplication) getApplication()).myWaiting.add(tempDBQ);
                    }
                }


            }
        } else {

        }

    }

    public void waitingInfoAllRequest() {
        waitingList = new ArrayList<>();
        if (DataApplication.isTest) {
            waitingList = ((DataApplication) this.getApplication()).getTestDBList();
        } else {
            JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST, ((DataApplication) getApplication()).serverURL, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            try {
                                JSONArray dataArray = jsonObject.getJSONArray("data");

                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject dataObject = dataArray.getJSONObject(i);

                                    WaitingInfo data = new WaitingInfo();

                                    data.setWaitingId(dataObject.getLong("id"));
                                    data.setAdminId(dataObject.getLong("adminId"));
                                    data.setName(dataObject.getString("name"));
                                    data.setLatitude(dataObject.getDouble("latitude"));
                                    data.setLongitude(dataObject.getDouble("longitude"));
                                    data.setLocDetail(dataObject.getString("locDetail"));
                                    data.setInfo(dataObject.getString("information"));
                                    data.setType(dataObject.getString("type"));
                                    data.setMaxPerson(dataObject.getInt("maxPerson"));
                                    if (data.getType().equals("time")) {
                                        ArrayList<String> timetable = new ArrayList();
                                        JSONArray timeArray = dataObject.getJSONArray("timetable");
                                        for (int j = 0; j < timeArray.length(); j++) {
                                            timetable.add(timeArray.getString(j));
                                        }
                                        data.setTimetable(timetable);
                                    }
                                    waitingList.add(data);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "로딩에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("type", "waitinginfo_all");

                    return params;
                }
            };

            loginRequest.setShouldCache(false);
            ((DataApplication) getApplication()).requestQueue.add(loginRequest);
        }
        for (int i = 0; i < waitingList.size(); i++) {
            setInfo(waitingList.get(i));
        }
    }

    public void setWearOS() {
        ((DataApplication) getApplication()).sendUserInfo();

        ArrayList<WaitingInfo> tempList = new ArrayList<>();
        for (int i = 0; i < waitingList.size(); i++) {
            for (int j = 0; j < ((DataApplication) getApplication()).myWaiting.size(); j++) {
                WaitingInfo tempA = new WaitingInfo();
                tempA.setName(((DataApplication) getApplication()).myWaiting.get(j).getQueueName());
                if (waitingList.get(i).getName().equals(((DataApplication) getApplication()).myWaiting.get(j).getQueueName()) &&
                        !tempList.contains(tempA.getName())) {
                    tempList.add(waitingList.get(i));
                    break;
                }
            }
        }

        ((DataApplication) getApplication()).sendMyQueueInfo(tempList);
    }
}

package com.example.stopwaiting.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import com.example.stopwaiting.R;
import com.example.stopwaiting.DTO.WaitingInfo;
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

import java.util.ArrayList;

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
        userId.setText(((DataApplication) getApplication()).userId + " 님");

        markers = new ArrayList<>();
        waitingList = new ArrayList<>();

        getMarkerInfo();

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
                for (int i = 0; i < markers.size(); i++) {
                    markers.get(i).setMap(null);
                }
                markers = new ArrayList<>();
                getMarkerInfo();

                ActivityCompat.requestPermissions(mainActivity, PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE);
            }
        });

    }

    public void getMarkerInfo() {
        waitingList = new ArrayList<>();
        waitingList = ((DataApplication) this.getApplication()).getTestDBList();

        for (int i = 0; i < waitingList.size(); i++) {
            setInfo(waitingList.get(i));
        }
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        naverMap.setLocationSource(locationSource);  //현재위치 표시
        ActivityCompat.requestPermissions(this, PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE);

        getMarkerInfo();
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
                temp.setId(Long.valueOf(infoWindow.getMarker().getTag().toString()));
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

        marker.setTag(String.valueOf(waitingInfo.getId()));

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

        if (requestCode == WAITING_LOCATION_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String selectName = data.getStringExtra("name");
//                Toast.makeText(this, selectName, Toast.LENGTH_SHORT).show();

                for (int i = 0; i < ((DataApplication) getApplication()).testDBList.size(); i++) {
                    WaitingInfo temp = ((DataApplication) getApplication()).testDBList.get(i);
//                    Toast.makeText(this, temp.getLatitude() + " " + temp.getLatitude(), Toast.LENGTH_SHORT).show();
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

}

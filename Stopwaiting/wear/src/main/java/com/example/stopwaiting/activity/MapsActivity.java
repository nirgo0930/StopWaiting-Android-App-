package com.example.stopwaiting.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.wear.widget.SwipeDismissFrameLayout;

import com.example.stopwaiting.R;
import com.example.stopwaiting.databinding.ActivityMapsBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends Activity implements OnMapReadyCallback {

    /**
     * Map is initialized when it"s fully loaded and ready to be used.
     *
     * @see #onMapReady(com.google.android.gms.maps.GoogleMap)
     */
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private FusedLocationProviderClient locationSource;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private static final int WAITING_LOCATION_REQUEST_CODE = 2000;
    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private double latitude;
    private double longitude;


    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final SwipeDismissFrameLayout swipeDismissRootFrameLayout =
                binding.swipeDismissRootContainer;
        final FrameLayout mapFrameLayout = binding.mapContainer;

        swipeDismissRootFrameLayout.addCallback(new SwipeDismissFrameLayout.Callback() {
            @Override
            public void onDismissed(SwipeDismissFrameLayout layout) {
                // Hides view before exit to avoid stutter.
                layout.setVisibility(View.GONE);
                finish();
            }
        });

        //시스템 창 삽입을 사용할 수 있게 되면 이를 고려하여 여백을 조정
        swipeDismissRootFrameLayout.setOnApplyWindowInsetsListener(
                new View.OnApplyWindowInsetsListener() {
                    @Override
                    public WindowInsets onApplyWindowInsets(View view, WindowInsets insets) {
                        insets = swipeDismissRootFrameLayout.onApplyWindowInsets(insets);

                        FrameLayout.LayoutParams params =
                                (FrameLayout.LayoutParams) mapFrameLayout.getLayoutParams();

                        // 지도를 여백으로 유지하는 FrameLayout 컨테이너에 웨어러블 인셋 설정
                        params.setMargins(
                                insets.getSystemWindowInsetLeft(),
                                insets.getSystemWindowInsetTop(),
                                insets.getSystemWindowInsetRight(),
                                insets.getSystemWindowInsetBottom());
                        mapFrameLayout.setLayoutParams(params);

                        return insets;
                    }
                });

        // MapFragment를 가져오고 지도가 준비되면 알림을 받을 비동기 수신기를 설정
        MapFragment mapFragment =
                (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //자기 위치 반환
        locationSource = LocationServices.getFusedLocationProviderClient(this);

            // 마쉬멜로우 이상버전부터 권한 요청
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                        new String[]
                                {Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION},
                        1000);
            }
            return;
    }

    // 권한 체크 이후로직
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grandResults) {
        // READ_PHONE_STATE의 권한 체크 결과를 불러온다
        if (requestCode == 1000) {
            boolean check_result = true;

            // 모든 퍼미션을 허용했는지 체크
            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }

            // 권한 체크에 동의를 하지 않으면 안드로이드 종료
            if (check_result == true) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                mMap.setMyLocationEnabled(true);
            } else {
                finish();
            }
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        // 맵 준비 완료
        mMap = googleMap;

        Intent intent = getIntent();
        latitude =intent.getDoubleExtra("latitude",0);
        longitude = intent.getDoubleExtra("longitude", 0);
        String name = intent.getStringExtra("location");

        final LatLng[] location = {new LatLng(latitude, longitude)};
        Marker marker = mMap.addMarker(new MarkerOptions().position(location[0]).title(name));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location[0]));
        binding.btnMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.animateCamera(CameraUpdateFactory.newLatLng(location[0]));
            }
        });
        //줌버튼 입력
        mMap.getUiSettings().setZoomControlsEnabled(true);
        
        //초기줌 설정
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location[0], 16));

    }

}
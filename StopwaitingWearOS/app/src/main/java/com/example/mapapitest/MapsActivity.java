package com.example.mapapitest;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.wear.widget.SwipeDismissFrameLayout;

import com.example.mapapitest.databinding.ActivityMapsBinding;

public class MapsActivity extends Activity implements OnMapReadyCallback {

    /**
     * Map is initialized when it"s fully loaded and ready to be used.
     *
     * @see #onMapReady(com.google.android.gms.maps.GoogleMap)
     */
    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final SwipeDismissFrameLayout swipeDismissRootFrameLayout =
                binding.swipeDismissRootContainer;
        final FrameLayout mapFrameLayout = binding.mapContainer;

        // Enables the Swipe-To-Dismiss Gesture via the root layout (SwipeDismissFrameLayout).
        // Swipe-To-Dismiss is a standard pattern in Wear for closing an app and needs to be
        // manually enabled for any Google Maps Activity. For more information, review our docs:
        // https://developer.android.com/training/wearables/ui/exit.html
        swipeDismissRootFrameLayout.addCallback(new SwipeDismissFrameLayout.Callback() {
            @Override
            public void onDismissed(SwipeDismissFrameLayout layout) {
                // Hides view before exit to avoid stutter.
                layout.setVisibility(View.GONE);
                finish();
            }
        });

        // Adjusts margins to account for the system window insets when they become available.
        swipeDismissRootFrameLayout.setOnApplyWindowInsetsListener(
                new View.OnApplyWindowInsetsListener() {
                    @Override
                    public WindowInsets onApplyWindowInsets(View view, WindowInsets insets) {
                        insets = swipeDismissRootFrameLayout.onApplyWindowInsets(insets);

                        FrameLayout.LayoutParams params =
                                (FrameLayout.LayoutParams) mapFrameLayout.getLayoutParams();

                        // Sets Wearable insets to FrameLayout container holding map as margins
                        params.setMargins(
                                insets.getSystemWindowInsetLeft(),
                                insets.getSystemWindowInsetTop(),
                                insets.getSystemWindowInsetRight(),
                                insets.getSystemWindowInsetBottom());
                        mapFrameLayout.setLayoutParams(params);

                        return insets;
                    }
                });

        // Obtain the MapFragment and set the async listener to be notified when the map is ready.
        MapFragment mapFragment =
                (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Map is ready to be used.
        mMap = googleMap;

        // Inform user how to close app (Swipe-To-Close).
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(getApplicationContext(), R.string.intro_text, duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

        //얘네들 서버로 받아서 for문돌리든가 해야할듯
       // Adds a marker in Sydney, Australia and moves the camera.
        LatLng salon = new LatLng(36.144760, 128.393884);
        mMap.addMarker(new MarkerOptions().position(salon).title("미용실"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(salon));

        LatLng special_meal = new LatLng(36.145619, 128.392535);
        mMap.addMarker(new MarkerOptions().position(special_meal).title("특식 배부"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(special_meal));

        LatLng book_cafe = new LatLng(36.145123, 128.394244);
        mMap.addMarker(new MarkerOptions().position(book_cafe).title("북카페"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(book_cafe));

        //mMap.setMyLocationEnabled();
        //mMap.getUiSettings().isMyLocationButtonEnabled();
        mMap.getUiSettings().setZoomControlsEnabled(true);
        //googleMap.getUiSettings().setMyLocationButtonEnabled(true);
       // mMap.getUiSettings().setMyLocationButtonEnabled(true);
//        googleMap.setMyLocationEnabled();
//        mMap.setMyLocationEnabled();
//        mMap.isMyLocationEnabled()=true;
//        googleMap.getUiSettings().isZoomControlsEnabled(true);
        //초기줌 설정
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(book_cafe, 16));

    }
}
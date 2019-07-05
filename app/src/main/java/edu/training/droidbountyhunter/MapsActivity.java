package edu.training.droidbountyhunter;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import edu.training.droidbountyhunter.models.Fugitive;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private Fugitive fugitive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        fugitive = getIntent().getParcelableExtra("fugitive");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setTitle(fugitive.getName());
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
       this.googleMap = googleMap;

        // Add a marker and move the camera
        LatLng position;
        if (fugitive.getLatitude() == 0d && fugitive.getLongitude() == 0d) {
            position = new LatLng(-34, 151);
        }else {
            position = new LatLng(fugitive.getLatitude(), fugitive.getLongitude());
        }

        this.googleMap.addMarker(new MarkerOptions().position(position).title(fugitive.getName()));
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 12));
    }
}

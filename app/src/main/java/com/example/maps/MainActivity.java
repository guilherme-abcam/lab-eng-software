package com.example.maps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
    GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
    ActivityCompat.OnRequestPermissionsResultCallback {

    public GoogleMap mapJ;
    public LatLng localization = new LatLng(-23.591439, -48.022892); // Localização inicial
    private Button btnPositionJ;
    private GeoDataClient geoDataClient; // Lugares próximos das coordenadas
    private FusedLocationProviderClient fusedLocationProviderClient; // Prova a Geolocalização
    private static boolean LOCATION_PERMISSION_REQUEST_CODE = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        geoDataClient = Places.getGeoDataClient(MainActivity.this, null);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        Botoes();
    }

    public void Botoes() {
        btnPositionJ = (Button)findViewById(R.id.btnPosition);
        btnPositionJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableMyLocation();
                updateLocation();
            }
        });
    }


    private void enableMyLocation() {
        if((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED)) {
            LOCATION_PERMISSION_REQUEST_CODE = PermissionUtils.validate
                    (this, 1, Manifest.permission.ACCESS_FINE_LOCATION);
            LOCATION_PERMISSION_REQUEST_CODE = PermissionUtils.validate
                    (this, 1, Manifest.permission.ACCESS_COARSE_LOCATION);

            
            updateLocation();
        }
    }


    private void updateLocation() {
        try {
            LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    mapJ.addMarker(new MarkerOptions().position(latLng).title("Olá mundo!"));
                    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 18);

                    mapJ.animateCamera(update);
                    mapJ.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        } catch (SecurityException securityException) {
            Toast.makeText(this, securityException.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mapJ = googleMap;
        mapJ.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(localization, 18);
        mapJ.animateCamera(update);

        Circle circle = mapJ.addCircle(new CircleOptions()
                .center(localization)
                .radius(100)
                .strokeColor(Color.RED)
                .fillColor(Color.TRANSPARENT));

        mapJ.addMarker(new MarkerOptions().position(localization).title("SFC"));
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }


    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}


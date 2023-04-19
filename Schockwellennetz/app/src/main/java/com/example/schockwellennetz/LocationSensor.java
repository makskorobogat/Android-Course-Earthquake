package com.example.schockwellennetz;

import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class LocationSensor extends AppCompatActivity{
    FusedLocationProviderClient fusedLocationProviderClient;
    TextView latitudeTextView, longitTextView;
    SupportMapFragment supportMapFragment;

    final static String KEY_LONGITUDE = "KEY1";
    final static String KEY_LATITUDE = "KEY2";

    final static String KEY_LOCATION_ORIGIN_LONGITUDE = "KEY3";
    final static String KEY_LOCATION_ORIGIN_LATITUDE = "KEY4";

    public double longt;
    public double lat;

    public double longitude_origin;
    public double latitude_origin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_location_sensor);

        Intent intent = getIntent();
        longitude_origin = intent.getDoubleExtra(KEY_LOCATION_ORIGIN_LONGITUDE, 0);
        latitude_origin = intent.getDoubleExtra(KEY_LOCATION_ORIGIN_LATITUDE, 0);


        latitudeTextView = findViewById(R.id.latTextView);
        longitTextView = findViewById(R.id.lonTextView);
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        getLocation();
    }

    private void getLocation() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                fusedLocationProviderClient.getLastLocation()
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    //turns null if device location is turned off
                                    lat = location.getLatitude();
                                    longt = location.getLongitude();

                                    Intent intent = new Intent();
                                    intent.putExtra(KEY_LONGITUDE, Double.toString(longt));
                                    intent.putExtra(KEY_LATITUDE, Double.toString(lat));

                                    intent.putExtra(KEY_LOCATION_ORIGIN_LONGITUDE, longitude_origin); //Double.toString(longitude_origin));
                                    intent.putExtra(KEY_LOCATION_ORIGIN_LATITUDE, latitude_origin);//Double.toString(latitude_lat));
                                    setResult(RESULT_OK, intent);
                                    finish();
                                    
                                    //public void onBackPressed();
                                }
                            }
                        });
                //todo: add onfailurelistener

            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }
    /*public void onBackPressed()
    {
        // return location to shakesensor
        Intent intent = new Intent();
        intent.putExtra(KEY_LONGITUDE, Double.toString(longt));
        intent.putExtra(KEY_LATITUDE, Double.toString(lat));
        setResult(RESULT_OK, intent);
        finish();
    }*/
}
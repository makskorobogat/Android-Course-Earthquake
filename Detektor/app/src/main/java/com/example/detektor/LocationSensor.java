package com.example.detektor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LocationSensor extends AppCompatActivity{
    FusedLocationProviderClient fusedLocationProviderClient;
    TextView latitudeTextView, longitTextView;
    SupportMapFragment supportMapFragment;

    final static String KEY_LATITUDE_EPICENTER = "KEY1";
    final static String KEY_LONGITUDE_EPICENTER = "KEY2";
    private FirebaseDatabase firebaseDatabase;
    /*public double longitude_epicenter;
    public double latitude_epicenter;*/
    double epilong;
    double epilat;
    LocationData epiLocation;

    private DatabaseReference epizentrum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_sensor);
        firebaseDatabase = FirebaseDatabase.getInstance();
        /*Intent intent = getIntent();
        longitude_epicenter = intent.getDoubleExtra(KEY_LATITUDE_EPICENTER, 0);
        latitude_epicenter = intent.getDoubleExtra(KEY_LONGITUDE_EPICENTER, 0);*/


        latitudeTextView = findViewById(R.id.latTextView);
        longitTextView = findViewById(R.id.lonTextView);
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        epizentrum = firebaseDatabase.getReference("epizentrum");

        epizentrum.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    epiLocation = snapshot.getValue(LocationData.class);
                    showLocation(epiLocation.latitude, epiLocation.longitude);
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }



    private void showLocation(double latitude_epicenter, double longitude_epicenter) {
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                // Initialize lat lng
                LatLng latLng = new LatLng(latitude_epicenter, longitude_epicenter);
                // Create marker options
                MarkerOptions options = new MarkerOptions().position(latLng).title("I am there");
                // Zoom map
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                // Add marker on map
                googleMap.addMarker(options);
            }
        });


//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
//                    == PackageManager.PERMISSION_GRANTED) {
//                // get the location here
//                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
//                            @Override
//                            public void onSuccess(Location location) {
//                                if (location != null) {
//                                    //turns null if device location is turned off
//                                    /*Double lat = location.getLatitude();
//                                    Double longt = location.getLongitude();
//                                    latitudeTextView.setText(lat.toString());
//                                    longitTextView.setText(longt.toString());*/
//
//                                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
//                                        @Override
//                                        public void onMapReady(GoogleMap googleMap) {
//                                            // Initialize lat lng
//                                            LatLng latLng = new LatLng(latitude_epicenter, longitude_epicenter);
//                                            // Create marker options
//                                            MarkerOptions options = new MarkerOptions().position(latLng).title("I am there");
//                                            // Zoom map
//                                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
//                                            // Add marker on map
//                                            googleMap.addMarker(options);
//                                        }
//                                    });
//
//                                    Toast.makeText(LocationSensor.this, "Success", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
                //todo: add onfailurelistener

//            } else {
//                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//            }
 //       }
    }
}


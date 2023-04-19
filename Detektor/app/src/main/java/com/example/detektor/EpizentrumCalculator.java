package com.example.detektor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
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

import static java.lang.Float.parseFloat;

public class EpizentrumCalculator extends AppCompatActivity {
    final static String KEY_LONG1 = "KEY1";
    final static String KEY_LAT1= "KEY2";
    final static String KEY_LONG2 = "KEY3";
    final static String KEY_LAT2= "KEY4";
    final static String KEY_LONG3 = "KEY5";
    final static String KEY_LAT3= "KEY6";

    // todo: remove test values
    float x1 = (float) 49.4677;
    float y1 = (float) 11.098;
    float x2 = (float) 51.37;
    float y2 = (float) 12.393;
    float x3 = (float) 50.116;
    float y3 = (float) 8.671;
    float epi1 = (float) 41.871940;
    float epi2 = (float) 12.567380;


    FusedLocationProviderClient fusedLocationProviderClient;
    TextView latitudeTextView, longitTextView;
    SupportMapFragment supportMapFragment;

    private FirebaseDatabase firebaseDatabase;

    private DatabaseReference epizentrum;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_epizentrum_calculator);

        firebaseDatabase = FirebaseDatabase.getInstance();

        latitudeTextView = findViewById(R.id.latTextView);
        longitTextView = findViewById(R.id.lonTextView);
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        epizentrum = firebaseDatabase.getReference("epizentrum");

        epizentrum.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Double epiLat = snapshot.child("latitude").getValue(Double.class);
                    Double epiLong = snapshot.child("longtude").getValue(Double.class);

                    latitudeTextView.setText(epiLat.toString());
                    longitTextView.setText(epiLong.toString());

                    showLocation(epiLat, epiLong);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // get Location Values from three Sensors
        Intent intent = getIntent();
        float long1 = parseFloat(intent.getStringExtra(KEY_LONG1));
        float lat1 = parseFloat(intent.getStringExtra(KEY_LAT1));

        float long2 = parseFloat(intent.getStringExtra(KEY_LONG2));
        float lat2 = parseFloat(intent.getStringExtra(KEY_LAT2));

        float long3 = parseFloat(intent.getStringExtra(KEY_LONG3));
        float lat3 = parseFloat(intent.getStringExtra(KEY_LAT3));

        // calculate Epizentrum
        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }

        Python py = Python.getInstance();

        PyObject pyobj = py.getModule("main 2");

        PyObject obj = pyobj.callAttr("main", lat1, long1, lat2, long2, lat3, long3, epi1, epi2);
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
    }
}
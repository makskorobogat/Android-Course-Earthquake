package com.example.detektor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button btn_startdetector;
    private Button btn_stopdetector;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference sensorRef;

    private int phoneCount = 3;
    private ArrayList<SensorData> sensorList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseDatabase = FirebaseDatabase.getInstance();
        sensorRef = firebaseDatabase.getReference("sensorData");

        btn_startdetector = findViewById(R.id.btnStartDetector);
        btn_startdetector.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    Intent intent = new Intent(v.getContext(), Detector.class);
                    startService(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Location permission not supported!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        btn_stopdetector = findViewById(R.id.btnStopDetector);
        btn_stopdetector.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Detector.class);
                stopService(intent);
            }
        });

        sensorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (snapshot.getChildrenCount() == phoneCount) {
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            //get phone data here
                            SensorData sensorData = postSnapshot.getValue(SensorData.class);
                            sensorList.add(sensorData);
                        }

                        SensorData data1 = sensorList.get(0);
                        SensorData data2 = sensorList.get(1);
                        SensorData data3 = sensorList.get(2);

                        Intent intent = new Intent(MainActivity.this, EpizentrumCalculator.class);

                        intent.putExtra(EpizentrumCalculator.KEY_LONG1, data1.getLongitude());
                        intent.putExtra(EpizentrumCalculator.KEY_LAT1, data1.getLatitude());
                        intent.putExtra(EpizentrumCalculator.KEY_LONG2, data2.getLongitude());
                        intent.putExtra(EpizentrumCalculator.KEY_LAT2, data2.getLatitude());
                        intent.putExtra(EpizentrumCalculator.KEY_LONG3, data3.getLongitude());
                        intent.putExtra(EpizentrumCalculator.KEY_LAT3, data3.getLatitude());

                        startActivity(intent);

                        //test:
//                            double latitude_epicenter = 50.827845;
//                            double longitude_epicenter = 12.9213683;
//                            LocationData locationData = new LocationData(longitude_epicenter, latitude_epicenter );
//                            epicenterLat.push().setValue(locationData);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, Detector.class));
    }
}
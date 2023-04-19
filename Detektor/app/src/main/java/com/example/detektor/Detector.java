package com.example.detektor;
import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.maps.SupportMapFragment;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDateTime;
import java.util.AbstractSequentialList;
import java.util.ArrayList;
import java.util.Map;

public class Detector extends Service implements SensorEventListener {
//    private Button btnn;
//
//    float x1 = (float) 49.4677;
//    float y1 = (float) 11.098;
//    float x2 = (float) 51.37;
//    float y2 = (float) 12.393;
//    float x3 = (float) 50.116;
//    float y3 = (float) 8.671;
//    float epi1 = (float) 51.318;
//    float epi2 = (float) 9.489;


    private SensorManager mSensorManager;
    private Sensor sensor;
    private int sensorType = Sensor.TYPE_ACCELEROMETER;
    private DatabaseReference sensorRef;
    private FirebaseDatabase firebaseDatabase;
    FusedLocationProviderClient fusedLocationProviderClient;

    private double lat = 0;
    private double longt = 0;
    private double location_arr[] = new double[2];

    private double accelerationCurrentValue;
    private double accelerationPreviousValue;
    private double changeInAccelleration;
    private long oldtime = 0;
    private double[] accelShortArray = new double[4];
    private double longitude, latitude, amplitude;
    private int counter = 0;

    private long phoneCount = 0;


    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service started", Toast.LENGTH_SHORT).show();
        mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);

        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
//        if (!Python.isStarted()) {
//            Python.start(new AndroidPlatform(Detector.this));
//        }
//
//        Python py = Python.getInstance();
//
//        PyObject pyobj = py.getModule("main 2");
//
//        PyObject obj = pyobj.callAttr("main", x1, y1, x2, y2, x3, y3, epi1, epi2);

        //Vibrator vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);

        // initialize sensor object

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager.getSensorList(sensorType).size() == 0) {
            sensor = null;
        } else {
            sensor = mSensorManager.getSensorList(sensorType).get(0);
        }
        firebaseDatabase = FirebaseDatabase.getInstance();
        sensorRef = firebaseDatabase.getReference("sensorData");

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                if (dataSnapshot.exists()) {
                    //VibrationData rcvMessage = dataSnapshot.getValue(VibrationData.class);
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    Log.w("Message", "Test: " + map.toString());
                    Toast.makeText(getApplicationContext(), "Message received: " + map.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Title", "loadPost:onCancelled", databaseError.toException());
            }
        };
        sensorRef.addValueEventListener(postListener);


        sensorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    phoneCount = snapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission")
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        //Toast.makeText(this, "Message received", Toast.LENGTH_SHORT).show();
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];

        // normalize the three given values
        accelerationCurrentValue = Math.sqrt((x * x + y * y + z * z));
        changeInAccelleration = Math.abs(accelerationPreviousValue - accelerationCurrentValue);
        accelerationPreviousValue = accelerationCurrentValue;

        Log.w("Acc", "Value: " + changeInAccelleration);
        accelShortArray[counter] = changeInAccelleration;


        if (checkArray(0.05, 0.15) && System.currentTimeMillis() > oldtime + 5 * 1000)//accelShortAverValue>accelLongAverValue && changeInAccelleration<0.2 && accelShortAverValue>0.05 && allowDetection)
        {
            //txt_detection.setText("Vibration detected "+changeInAccelleration+".");
            Toast.makeText(this, "Vibration detected", Toast.LENGTH_SHORT).show();
            Log.w("Detector", "Vibration detected" + changeInAccelleration);
            oldtime = System.currentTimeMillis();

            getLocation();

        }
        //else
        //txt_detection.setText("No vibration detected.");

        if (counter < 3) {
            counter++;
        } else {
            counter = 0;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    private double findMaxValue() {

        double max = accelShortArray[0];

        for (int i = 1; i < accelShortArray.length; i++) {
            if (accelShortArray[i] > max) {
                max = accelShortArray[i];
            }
        }
        return max;
    }

    private boolean checkArray(double lowerBound, double higherBound) {
        for (double v : accelShortArray) {
            if (v < lowerBound || v > higherBound) {
                return false;
            }
        }
        return true;
    }


    public void onDestroy() {
        super.onDestroy();
        mSensorManager.unregisterListener(this, sensor);
        Toast.makeText(this, "Service done", Toast.LENGTH_SHORT).show();


    }


    @SuppressLint("MissingPermission")
    public void getLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            //if (getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        //turns null if device location is turned off
                        lat = location.getLatitude();
                        longt = location.getLongitude();

                        longitude = longt;
                        latitude = lat;
                        Log.w("Message", "Get location " + longt + " " + lat);

                        final LocalDateTime dateTime = LocalDateTime.now();
                        amplitude = findMaxValue();
                        //VibrationData vibData = new VibrationData(dateTime.toString(), "", "", amplitude);
                        SensorData sensorData = new SensorData(dateTime.toString(), Double.toString(longitude), Double.toString(latitude), Double.toString(amplitude));
                        Log.w("Message", "Longitude: " + longitude);
                        Log.w("Message", "Latitude: " + latitude);
                        Log.w("Message", "Amplitude: " + amplitude);
                        Log.w("Message", "Date: " + dateTime);
                        //sensorRef.setValue(vibData);

                        if (phoneCount < 3 ) {
                            sensorRef.push().setValue(sensorData);
                        }
                    } else
                        Log.w("Message", "Location null");
                }
            });
            Log.w("Message", "Test");
        }
    }

}


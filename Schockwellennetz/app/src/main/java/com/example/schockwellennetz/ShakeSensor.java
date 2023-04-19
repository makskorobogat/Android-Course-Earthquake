package com.example.schockwellennetz;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.util.Date;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ShakeSensor extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor sensor;
    private int sensorType = Sensor.TYPE_ACCELEROMETER;

    //FirebaseManager myFirebaseManager;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference triggerRef;       //Auslöser

    private final static int REQUEST_CODE = 747;

    private double accelerationCurrentValue;
    private double accelerationPreviousValue;
    private double changeInAccelleration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake_sensor);

        // initialize sensor object
        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        if(mSensorManager.getSensorList(sensorType).size() == 0)
        {
            sensor = null;
        }
        else {
            sensor = mSensorManager.getSensorList(sensorType).get(0);
        }

        // get reference to database
        firebaseDatabase = FirebaseDatabase.getInstance();              // get reference to database
        triggerRef = firebaseDatabase.getReference("Trigger");

        //myFirebaseManager = new FirebaseManager();
        //myFirebaseManager.addTriggerEventListener();
    }

    protected void onResume() {
        super.onResume();
        if (sensor != null) {
            if(mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME))
            {
            }
            else {
            }
        }
    }

    protected void onPause() {
        super.onPause();
        if(sensor != null)
        {
            mSensorManager.unregisterListener(this, sensor);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];

        // normalize the three given values
        accelerationCurrentValue = Math.sqrt((x*x+y*y+z*z));
        changeInAccelleration = Math.abs(accelerationPreviousValue - accelerationCurrentValue);

        accelerationPreviousValue = accelerationCurrentValue;

        if(changeInAccelleration > 10) {
            // get device location
            Intent intent = new Intent(this,LocationSensor.class);
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == REQUEST_CODE) {
            if(resultCode == RESULT_OK){
                String latitude = intent.getStringExtra(LocationSensor.KEY_LATITUDE);
                String longitude = intent.getStringExtra(LocationSensor.KEY_LONGITUDE);
                final LocalDateTime dateTime = LocalDateTime.now();
                int amplitude = 1;
                DateTime myDateTime = new DateTime(dateTime.getDayOfMonth(), dateTime.getMonthValue(), dateTime.getYear(), dateTime.getHour(), dateTime.getMinute(), dateTime.getSecond(), dateTime.getNano());
                ShockwaveData data = new ShockwaveData(myDateTime, longitude, latitude, amplitude);

                // send shockwave data to firebase
                //myFirebaseManager.addShockwaveData(data);
                triggerRef.setValue(data);
            }
            if (resultCode == RESULT_CANCELED) {
            }
        }
    } //onActivityResul
}
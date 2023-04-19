package com.example.detektor;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Date;

public class ShakeSensor extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor sensor;
    private int sensorType = Sensor.TYPE_ACCELEROMETER;

    ProgressBar prog_shakeMeter;
    TextView txt_accelaration;
    TextView txt_prevAccel;
    TextView txt_currentAccel;

    private double accelerationCurrentValue;
    private double accelerationPreviousValue;
    private double changeInAccelleration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake_sensor);

        txt_accelaration = findViewById(R.id.txt_accel);
        txt_currentAccel = findViewById(R.id.txt_currentAccel);
        txt_prevAccel = findViewById(R.id.txt_prevAccel);
        prog_shakeMeter = findViewById(R.id.prog_shakeMeter);

        // initialize sensor object
        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        if(mSensorManager.getSensorList(sensorType).size() == 0)
        {
            sensor = null;
        }
        else {
            sensor = mSensorManager.getSensorList(sensorType).get(0);
        }
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

        // update text views
        txt_currentAccel.setText("Current = " + Math.round(accelerationCurrentValue*100)/100.0 );
        txt_prevAccel.setText("Prev = " + Math.round(accelerationPreviousValue*100)/100.0 );
        txt_accelaration.setText("Acceleration change = " + Math.round(changeInAccelleration*100)/100.0 );

        accelerationPreviousValue = accelerationCurrentValue;


        prog_shakeMeter.setProgress((int) changeInAccelleration);
        // change colors based on amount of shaking
        if(changeInAccelleration > 12) {
            txt_accelaration.setBackgroundColor(Color.RED);
        }
        else if (changeInAccelleration > 5) {
            txt_accelaration.setBackgroundColor(Color.parseColor("#fcad03"));
        }
        else if (changeInAccelleration > 1) {
            txt_accelaration.setBackgroundColor(Color.YELLOW);
        }
        else {
            txt_accelaration.setBackgroundColor(getResources().getColor(R.color.design_default_color_background));
        }


        if(changeInAccelleration >10) {
            // Show Current Location
            Intent intent = new Intent(this,LocationSensor.class);
            startActivity(intent);
        }

        // Send ShockwaveData to database
//        if(changeInAccelleration > 12) {
//            Date currentTime = Calendar.getInstance().getTime();
//        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
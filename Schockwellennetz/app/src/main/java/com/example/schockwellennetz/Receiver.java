package com.example.schockwellennetz;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Receiver extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference triggerRef;       //Auslöser
    private DatabaseReference sensorRef;        //Empfänger
    //private DatabaseReference phoneCountRef;

    private final static int REQUEST_CODE = 747;
    private final static int CALCULATION_KEY = 001;

    final static String SHOCKWAVEDATA_KEY = "SHOCKWAVEDATA_KEY";
    private int velocity_km_per_s = 5; //km/s
    private TextView txtDistance;

    Vibrator vibrator;
    Timer timer = new Timer("Timer");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver);

        vibrator  = (Vibrator)getSystemService(VIBRATOR_SERVICE);

        firebaseDatabase = FirebaseDatabase.getInstance();              // get reference to database
        sensorRef = firebaseDatabase.getReference("Sensor");
        triggerRef = firebaseDatabase.getReference("Trigger");
        txtDistance = findViewById(R.id.txtDistance);

        triggerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    // shockwave received
                    ShockwaveData shockwaveData = snapshot.getValue(ShockwaveData.class);

                    /*String longitude = shockwaveData.longitude;
                    String latitude = shockwaveData.latitude;*/


                    // todo: remove test ob am echten handy schockwelle angekommen ist
                    txtDistance.setText("Schockwelle empfangen");


                    // todo: verzögerungszeit anhand dem ort berechnen
                    calculateVibrationDelayTime(shockwaveData);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendSensorData() {
        // get device location
        Intent intent = new Intent(this,LocationSensor.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void calculateVibrationDelayTime(ShockwaveData shockwaveData) {
        String longitude = shockwaveData.longitude;
        String latitude = shockwaveData.latitude;

        double longitude_origin =  Double.parseDouble(longitude);
        double latitude_origin =  Double.parseDouble(latitude);

        // get device location
        Intent intent = new Intent(this,LocationSensor.class);
        intent.putExtra(LocationSensor.KEY_LOCATION_ORIGIN_LONGITUDE, longitude_origin);
        intent.putExtra(LocationSensor.KEY_LOCATION_ORIGIN_LATITUDE, latitude_origin);
        startActivityForResult(intent, CALCULATION_KEY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // location for calculating delay time
        if (requestCode == CALCULATION_KEY) {
            if(resultCode == RESULT_OK){
                //Ort des Empfängers
                double latitude = intent.getDoubleExtra(LocationSensor.KEY_LATITUDE, 0);
                double longitude = intent.getDoubleExtra(LocationSensor.KEY_LONGITUDE, 0);

                double longitude_origin = intent.getDoubleExtra(LocationSensor.KEY_LOCATION_ORIGIN_LONGITUDE, 0);
                double latitude_origin = intent.getDoubleExtra(LocationSensor.KEY_LOCATION_ORIGIN_LATITUDE,0);

                double distance_km = 111.324 * Math.acos(Math.sin(latitude) * Math.sin(latitude_origin) + Math.cos(latitude) * Math.cos(latitude_origin) * Math.cos(longitude_origin - longitude));
                // https://www.kompf.de/gps/distcalc.html

                txtDistance.setText(Double.toString(distance_km));

                double delay = distance_km/velocity_km_per_s;

                vibrate(delay);
            }
            if (resultCode == RESULT_CANCELED) {
                // Write your code if there's no result
            }
        }

        // location for sending sensor data to firebase
        /*if (requestCode == REQUEST_CODE) {
            if(resultCode == RESULT_OK){
                String latitude = intent.getStringExtra(LocationSensor.KEY_LATITUDE);
                String longitude = intent.getStringExtra(LocationSensor.KEY_LONGITUDE);
                final LocalDateTime dateTime = LocalDateTime.now();
                int amplitude = 1;
                DateTime myDateTime = new DateTime(dateTime.getDayOfMonth(), dateTime.getMonthValue(), dateTime.getYear(), dateTime.getHour(), dateTime.getMinute(), dateTime.getSecond(), dateTime.getNano());

                SensorData sensorData = new SensorData(myDateTime, longitude, latitude, amplitude);

                // send sensor data to firebase, push generates key inside firebase
                sensorRef.push().setValue(sensorData);
            }
            if (resultCode == RESULT_CANCELED) {
                // Write your code if there's no result
            }
        }*/
    } //onActivityResul

    private void vibrate(double delay){
        if(Build.VERSION.SDK_INT >= 26){
            TimerTask task = new TimerTask() {
                public void run() {
                    long[] VibratePattern = new long[]{0, 400, 800};
                    int[] Amplitudes = new int[]{0, 255, 0};
                    vibrator.vibrate(200);

                    /*if(vibrator.hasAmplitudeControl()){
                        VibrationEffect effect = VibrationEffect.createWaveform(VibratePattern, Amplitudes, -1);
                        vibrator.vibrate(effect);
                    }
                    else{
                        vibrator.vibrate(200);
                    }*/
                    //System.out.println("Task performed on: " + new Date() + "n" + "Thread's name: " + Thread.currentThread().getName());
                }
            };

            timer.schedule(task, (long) delay);

        }
    }
}

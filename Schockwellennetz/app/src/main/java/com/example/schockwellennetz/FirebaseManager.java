package com.example.schockwellennetz;

import android.content.Intent;
import android.content.ReceiverCallNotAllowedException;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

@RequiresApi(api = Build.VERSION_CODES.O)
public class FirebaseManager {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference triggerRef;       //Auslöser
    private DatabaseReference sensorRef;        //Empfänger
    private DatabaseReference phoneCountRef;
    //private int phoneCount = new Integer(0);

    public FirebaseManager() {
        firebaseDatabase = FirebaseDatabase.getInstance();              // get reference to database
        sensorRef = firebaseDatabase.getReference("Sensor");
        triggerRef = firebaseDatabase.getReference("Trigger");
        phoneCountRef = firebaseDatabase.getReference("PhoneCount");
//
//        // Add Database Eventlisteners
//        sensorRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()) {
//                    //SensorData sensorData = snapshot.getValue(SensorData.class);
////                    if(dataSnapshot.getChildrenCount() == phoneCount)
////                    {
////                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren())
////                        {
////                            //get phone data here
////                        }
////                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//        triggerRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists())
//                {
//                    // shockwave received
//                    //ShockwaveData shockwaveData = snapshot.getValue(ShockwaveData.class);
//
////                    //Send Data to Receiver
////                    Intent intent = new Intent(FirebaseManager.this, Receiver.class);
////                    intent.putExtra(Receiver.SHOCKWAVEDATA_KEY, shockwaveData);
////                    startActivity(intent);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

    public void addTriggerEventListener() {
        triggerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    // shockwave received
                    ShockwaveData shockwaveData = snapshot.getValue(ShockwaveData.class);

                    String longitude = shockwaveData.longitude;
                    String latitude = shockwaveData.latitude;

                    //todo: rufe vom Receiver.class die funktion sendSensorData auf


//                    //Send Data to Receiver
//                    Intent intent = new Intent(FirebaseManager.this, Receiver.class);
//                    intent.putExtra(Receiver.SHOCKWAVEDATA_KEY, shockwaveData);
//                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addSensorEventListener() {
        sensorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    //SensorData sensorData = snapshot.getValue(SensorData.class);
//                    if(dataSnapshot.getChildrenCount() == phoneCount)
//                    {
//                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren())
//                        {
//                            //get phone data here
//                        }
//                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addSensorData(SensorData sensorData) {
        // push generates key inside firebase
        sensorRef.push().setValue(sensorData);
        //sensorRef.setValue(sensorData);
    }

    public void addShockwaveData(ShockwaveData shockwaveData) {
        triggerRef.setValue(shockwaveData);
    }

}




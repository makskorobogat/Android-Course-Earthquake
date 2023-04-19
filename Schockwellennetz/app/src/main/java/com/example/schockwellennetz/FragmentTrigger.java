package com.example.schockwellennetz;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentTrigger#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentTrigger extends Fragment {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference noTrigRegRef;       //check if an trigger already exists
    private boolean noTriggerRegistered = true;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentTrigger() {
        // Required empty public constructor
    }

    private Button btn_Trigger;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Trigger.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentTrigger newInstance(String param1, String param2) {
        FragmentTrigger fragment = new FragmentTrigger();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_trigger, container, false);

        btn_Trigger = v.findViewById(R.id.buttonTrigger);

        firebaseDatabase = FirebaseDatabase.getInstance();              // get reference to database
        noTrigRegRef = firebaseDatabase.getReference("noTriggerRegistered");    // get reference to noTriggerRegistered


        // set variable if a new Trigger is registered or unregistered
        noTrigRegRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    noTriggerRegistered = snapshot.getValue(boolean.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_Trigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (true) {
                    // a new trigger is registered
                    noTrigRegRef.setValue(false);

                    Intent intent = new Intent(v.getContext(), ShakeSensor.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(v.getContext(), "Es ist bereits ein Trigger registriert!", Toast.LENGTH_LONG).show();
                }

            }
        });


        // Inflate the layout for this fragment
        return v;
    }
}
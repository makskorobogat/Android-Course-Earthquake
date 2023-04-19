package com.example.detektor;

import android.location.Location;

import java.util.Date;

public class ShockwaveData {
    private Date date;
    private Location location;
    private int amplitude;

    public ShockwaveData(Date date, Location location, int amplitude) {
        this.date = date;
        this.location = location;
        this.amplitude = amplitude;
    }
}

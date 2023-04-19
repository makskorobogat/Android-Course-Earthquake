package com.example.schockwellennetz;

import java.time.LocalDateTime;

public class SensorData {
    public DateTime dateTime;
    public String longitude;
    public String latitude;
    public int amplitude;

    public SensorData() {
    }

    public SensorData(DateTime dateTime, String longitude, String latitude, int amplitude) {
        this.dateTime = dateTime;
        this.longitude = longitude;
        this.latitude = latitude;
        this.amplitude = amplitude;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public int getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(int amplitude) {
        this.amplitude = amplitude;
    }
}

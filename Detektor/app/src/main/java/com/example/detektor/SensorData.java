package com.example.detektor;

import java.time.LocalDateTime;



public class SensorData {


    private String longitude;
    private String latitude;
    private String emptyString="";
    private String amplitude;
    private String dateTime;

    public String getLongitude() {
        if (longitude!=null)
            return longitude;
        else
            return emptyString;
    }

    public String getLatitude() {
        if (latitude!=null)
            return latitude;
        else
            return emptyString;
    }

    public String getAmplitude() {
        return amplitude;
    }

    public String getDateTime() {
        return dateTime;
    }



    public SensorData(){

    }
    public SensorData(String dateTime, String longitude, String latitude, String amplitude) {
        this.dateTime = dateTime;
        this.longitude = longitude;
        this.latitude = latitude;
        this.amplitude = amplitude;
    }


}

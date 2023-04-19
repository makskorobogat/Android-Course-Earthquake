package com.example.schockwellennetz;

public class DateTime {
    public int day;
    public int month;
    public int year;
    public int hour;
    public int minute;
    public int second;
    public int nano;

    public DateTime() {
    }

    public DateTime(int day, int month, int year, int hour, int minute, int second, int nano) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.nano = nano;
    }
}

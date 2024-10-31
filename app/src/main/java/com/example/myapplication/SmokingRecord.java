package com.example.myapplication;

public class SmokingRecord {
    private String date;
    private int count;

    public SmokingRecord(String date, int count) {
        this.date = date;
        this.count = count;
    }

    public String getDate() {
        return date;
    }

    public int getCount() {
        return count;
    }
}

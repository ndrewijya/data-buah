package com.example.projekakhir_andrewijaya;

public class TokoBuah {
    private String nama;
    private double latitude;
    private double longitude;

    public TokoBuah(String nama, double latitude, double longitude) {
        this.nama = nama;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getNama() {
        return nama;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
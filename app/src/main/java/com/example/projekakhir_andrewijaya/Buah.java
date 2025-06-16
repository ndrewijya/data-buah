package com.example.projekakhir_andrewijaya;

public class Buah {
    private String id;
    private String nama;
    private String jenis;

    public Buah(String id, String nama, String jenis) {
        this.id = id;
        this.nama = nama;
        this.jenis = jenis;
    }

    // Getter methods
    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getJenis() {
        return jenis;
    }

    // Override toString() untuk ditampilkan di TextView
    @Override
    public String toString() {
        return this.nama + " (" + this.jenis + ")";
    }
}
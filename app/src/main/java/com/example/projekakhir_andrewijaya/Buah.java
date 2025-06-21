package com.example.projekakhir_andrewijaya;

public class Buah {
    private int id;
    private String nama;
    private String jenis;

    public Buah(String nama, String jenis) {
        this.nama = nama;
        this.jenis = jenis;
    }

    public Buah(int id, String nama, String jenis) {
        this.id = id;
        this.nama = nama;
        this.jenis = jenis;
    }

    public int getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getJenis() {
        return jenis;
    }

    @Override
    public String toString() {
        return "Nama: " + nama + "\nJenis: " + jenis;
    }
}
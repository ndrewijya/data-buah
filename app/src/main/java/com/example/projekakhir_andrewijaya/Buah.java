package com.example.projekakhir_andrewijaya;

/*Anggota Kelompok:
* 1. Andre Wijaya (221011400791)
* 2. Iqbal Isya Fathurrohman (221011401657)
* 3. Novandra Anugrah (221011400778)
*/

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
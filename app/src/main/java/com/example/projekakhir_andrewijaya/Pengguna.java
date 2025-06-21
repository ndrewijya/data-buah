package com.example.projekakhir_andrewijaya;

/*Anggota Kelompok:
 * 1. Andre Wijaya (221011400791)
 * 2. Iqbal Isya Fathurrohman (221011401657)
 * 3. Novandra Anugrah (221011400778)
 */

public class Pengguna {
    private int id;
    private String username;
    private String password;

    // Constructor untuk membaca data dari database
    public Pengguna(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    // Getter untuk setiap field
    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
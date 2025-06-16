package com.example.projekakhir_andrewijaya;

public class Pengguna {
    private String id;
    private String username;

    public Pengguna(String id, String username) {
        this.id = id;
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
}
package com.example.projekakhir_andrewijaya;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

public class DashboardActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ListView navList;
    private ImageView menuButton;

    // Deklarasi semua tombol fitur dari GridLayout
    private LinearLayout sensorButton;
    private LinearLayout settingsButton;
    private LinearLayout lihatDataBuahButton; // Diubah namanya agar lebih spesifik
    private LinearLayout mapsButton;
    private LinearLayout lihatPenggunaButton; // Variabel baru untuk tombol pengguna

    private String[] drawerItems = {
            "Dashboard", "Sensor", "Lihat Data Buah", "Lihat Pengguna", "Peta Lokasi", "Logout"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // --- Inisialisasi semua komponen UI ---
        drawerLayout = findViewById(R.id.drawer_layout);
        navList = findViewById(R.id.nav_list);
        menuButton = findViewById(R.id.menu_button);

        // PERBAIKAN: Setiap tombol dihubungkan ke variabelnya masing-masing
        sensorButton = findViewById(R.id.sensor_button);
        settingsButton = findViewById(R.id.settings_button);
        lihatDataBuahButton = findViewById(R.id.lihat_data_button); // Pastikan ID ini benar di XML
        mapsButton = findViewById(R.id.maps_button);
        lihatPenggunaButton = findViewById(R.id.lihat_pengguna_button); // Hubungkan ke ID tombol baru

        setupNavDrawer();
        setupFeatureButtons();
    }

    private void setupNavDrawer() {
        navList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, drawerItems));
        navList.setOnItemClickListener((parent, view, position, id) -> {
            switch (position) {
                case 0: break;
                case 1: startActivity(new Intent(DashboardActivity.this, SensorActivity.class)); break;
                case 2: startActivity(new Intent(DashboardActivity.this, LihatDataActivity.class)); break;
                case 3: startActivity(new Intent(DashboardActivity.this, LihatPenggunaActivity.class)); break;
                case 4: startActivity(new Intent(DashboardActivity.this, MapsActivity.class)); break;
                case 5: logoutUser();
                break;
            }
            drawerLayout.closeDrawer(navList);
        });
        menuButton.setOnClickListener(v -> drawerLayout.openDrawer(navList));
    }

    // Metode baru untuk menangani proses logout
    private void logoutUser() {
        // Pindah ke halaman MainActivity (login)
        Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
        // Hapus semua activity sebelumnya dari back stack
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        // Tampilkan pesan
        Toast.makeText(this, "Anda telah logout", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void setupFeatureButtons() {
        // PERBAIKAN: Setiap tombol memiliki listener-nya sendiri
        sensorButton.setOnClickListener(v -> startActivity(new Intent(DashboardActivity.this, SensorActivity.class)));
        settingsButton.setOnClickListener(v -> Toast.makeText(DashboardActivity.this, "Fitur Pengaturan diklik", Toast.LENGTH_SHORT).show());
        lihatDataBuahButton.setOnClickListener(v -> startActivity(new Intent(DashboardActivity.this, LihatDataActivity.class)));
        mapsButton.setOnClickListener(v -> startActivity(new Intent(DashboardActivity.this, MapsActivity.class)));
        lihatPenggunaButton.setOnClickListener(v -> startActivity(new Intent(DashboardActivity.this, LihatPenggunaActivity.class)));
    }
}
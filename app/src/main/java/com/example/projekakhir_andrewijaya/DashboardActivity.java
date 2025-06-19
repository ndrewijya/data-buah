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

    // Tombol di GridLayout yang tersisa
    private LinearLayout sensorButton;
    private LinearLayout settingsButton;
    private LinearLayout mapsButton;
    // Variabel untuk lihatDataBuahButton dan lihatPenggunaButton telah dihapus

    // Item untuk menu samping (Navigation Drawer) tetap lengkap
    private String[] drawerItems = {
            "Dashboard", "Sensor", "Lihat Data Buah", "Lihat Pengguna", "Peta Lokasi", "Logout"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // --- Inisialisasi komponen UI ---
        drawerLayout = findViewById(R.id.drawer_layout);
        navList = findViewById(R.id.nav_list);
        menuButton = findViewById(R.id.menu_button);
        sensorButton = findViewById(R.id.sensor_button);
        settingsButton = findViewById(R.id.settings_button);
        mapsButton = findViewById(R.id.maps_button);
        // Referensi ke tombol yang dihapus sudah dihilangkan

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
                case 5: logoutUser(); break;
            }
            drawerLayout.closeDrawer(navList);
        });
        menuButton.setOnClickListener(v -> drawerLayout.openDrawer(navList));
    }

    private void logoutUser() {
        Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        Toast.makeText(this, "Anda telah logout", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void setupFeatureButtons() {
        sensorButton.setOnClickListener(v -> startActivity(new Intent(DashboardActivity.this, SensorActivity.class)));
        settingsButton.setOnClickListener(v -> Toast.makeText(DashboardActivity.this, "Fitur Pengaturan diklik", Toast.LENGTH_SHORT).show());
        mapsButton.setOnClickListener(v -> startActivity(new Intent(DashboardActivity.this, MapsActivity.class)));
        // Listener untuk tombol yang dihapus sudah dihilangkan
    }
}
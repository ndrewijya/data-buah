package com.example.projekakhir_andrewijaya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

/*Anggota Kelompok:
 * 1. Andre Wijaya (221011400791)
 * 2. Iqbal Isya Fathurrohman (221011401657)
 * 3. Novandra Anugrah (221011400778)
 */

public class DashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    private LinearLayout sensorButton, mapsButton, voiceToTextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Dashboard");
        }

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Panggil metode untuk setup tombol di dashboard
        setupFeatureButtons();

        // Mengambil header view dari navigation view
        View headerView = navigationView.getHeaderView(0);
        // Mencari tombol close di dalam header view
        ImageButton buttonClose = headerView.findViewById(R.id.button_close_sidebar);

        // Menambahkan listener klik pada tombol close
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perintah untuk menutup navigation drawer
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
    }

    private void setupFeatureButtons() {
        sensorButton = findViewById(R.id.sensor_button);
        mapsButton = findViewById(R.id.maps_button);
        voiceToTextButton = findViewById(R.id.voice_to_text_button);

        sensorButton.setOnClickListener(v -> startActivity(new Intent(this, SensorActivity.class)));
        mapsButton.setOnClickListener(v -> startActivity(new Intent(this, MapsActivity.class)));
        voiceToTextButton.setOnClickListener(v -> startActivity(new Intent(this, VoiceToTextActivity.class)));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_dashboard) {
        } else if (itemId == R.id.nav_sensor) {
            startActivity(new Intent(this, SensorActivity.class));
        } else if (itemId == R.id.nav_lihat_data_buah) {
            startActivity(new Intent(this, LihatDataActivity.class));
        } else if (itemId == R.id.nav_lihat_pengguna) {
            startActivity(new Intent(this, LihatPenggunaActivity.class));
        } else if (itemId == R.id.nav_peta) {
            startActivity(new Intent(this, MapsActivity.class));
        } else if (itemId == R.id.nav_voice) {
            startActivity(new Intent(this, VoiceToTextActivity.class));
        } else if (itemId == R.id.nav_logout) {
            logoutUser();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void logoutUser() {

        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Hapus semua data di SharedPreferences (isLoggedIn dan username)
        editor.apply();

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        Toast.makeText(this, "Anda telah logout", Toast.LENGTH_SHORT).show();
        finish();
    }
}
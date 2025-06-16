package com.example.projekakhir_andrewijaya;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity {

    private MapView mapView;
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Konfigurasi penting untuk osmdroid
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        setContentView(R.layout.activity_maps);

        mapView = findViewById(R.id.mapView);
        mapView.setTileSource(TileSourceFactory.MAPNIK); // Mengatur sumber peta

        // Meminta izin lokasi
        requestPermissionsIfNecessary(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });

        // Mengatur posisi awal dan zoom peta
        mapView.getController().setZoom(15.0);
        // Contoh: Mengatur titik awal di Monumen Nasional (Monas), Jakarta
        GeoPoint startPoint = new GeoPoint(-6.175392, 106.827153);
        mapView.getController().setCenter(startPoint);

        // Menambahkan penanda (marker) di lokasi awal
        addMarker(startPoint, "Kebun Buah Tropis", "Contoh lokasi kebun buah di Jakarta");
    }

    // Metode untuk menambahkan marker ke peta
    private void addMarker(GeoPoint center, String title, String description) {
        Marker marker = new Marker(mapView);
        marker.setPosition(center);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setTitle(title);
        marker.setSnippet(description);
        mapView.getOverlays().add(marker);
        mapView.invalidate(); // Refresh peta untuk menampilkan marker
    }

    @Override
    public void onResume() {
        super.onResume();
        // Penting untuk me-refresh peta saat activity kembali dibuka
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        // Penting untuk menjeda peta saat activity tidak aktif untuk hemat resource
        mapView.onPause();
    }

    // --- Bagian untuk mengelola Izin Lokasi ---

    private void requestPermissionsIfNecessary(String[] permissions) {
        List<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }
        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Izin diberikan, tidak perlu aksi tambahan karena peta sudah di-setup
                Toast.makeText(this, "Izin lokasi diberikan!", Toast.LENGTH_SHORT).show();
            } else {
                // Izin ditolak
                Toast.makeText(this, "Izin lokasi ditolak, beberapa fitur mungkin tidak bekerja.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
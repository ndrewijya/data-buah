package com.example.projekakhir_andrewijaya;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity {

    private MapView mapView;
    private SearchView searchView;
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private List<TokoBuah> daftarTokoBuah = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        setContentView(R.layout.activity_maps);

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_maps);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Peta");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        isiDataToko();

        mapView = findViewById(R.id.mapView);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);

        requestPermissionsIfNecessary(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });

        tampilkanSemuaToko();
        setupSearchView();
    }

    private void isiDataToko() {
        daftarTokoBuah.add(new TokoBuah("Toko Buah Total Segar - Jakarta", -6.2088, 106.8456));
        daftarTokoBuah.add(new TokoBuah("Istana Buah - Bandung", -6.8994, 107.6111));
        daftarTokoBuah.add(new TokoBuah("Hokky Buah - Surabaya", -7.2886, 112.7341));
        daftarTokoBuah.add(new TokoBuah("Laris Buah - Depok", -6.4025, 106.7942));
    }

    private void tampilkanSemuaToko() {
        if (daftarTokoBuah.isEmpty()) return;

        ArrayList<GeoPoint> points = new ArrayList<>();
        for (TokoBuah toko : daftarTokoBuah) {
            GeoPoint tokoPoint = new GeoPoint(toko.getLatitude(), toko.getLongitude());
            points.add(tokoPoint);
            addMarker(tokoPoint, toko.getNama(), "Toko Buah");
        }
        BoundingBox boundingBox = BoundingBox.fromGeoPoints(points);

        // Lakukan zoom setelah layout peta siap
        mapView.post(() -> {
            mapView.zoomToBoundingBox(boundingBox, true, 100);
        });
    }

    private void setupSearchView() {
        searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                cariLokasi(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnClickListener(v -> {
            searchView.setIconified(false);
        });
    }

    private void cariLokasi(String namaLokasi) {
        Geocoder geocoder = new Geocoder(this);
        List<Address> addressList;

        try {
            addressList = geocoder.getFromLocationName(namaLokasi, 1);
            if (addressList != null && !addressList.isEmpty()) {
                Address address = addressList.get(0);
                GeoPoint geoPoint = new GeoPoint(address.getLatitude(), address.getLongitude());

                mapView.getController().animateTo(geoPoint);
                mapView.getController().setZoom(18.0);

                addMarker(geoPoint, namaLokasi, address.getAddressLine(0));

                Toast.makeText(this, "Lokasi ditemukan: " + address.getAddressLine(0), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Lokasi tidak ditemukan.", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Gagal mencari lokasi, periksa koneksi internet.", Toast.LENGTH_SHORT).show();
        }
    }


    private void addMarker(GeoPoint center, String title, String description) {
        Marker marker = new Marker(mapView);
        marker.setPosition(center);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setTitle(title);
        marker.setSnippet(description);
        marker.setIcon(getResources().getDrawable(R.drawable.ic_peta_lokasi, getTheme()));
        mapView.getOverlays().add(marker);
        mapView.invalidate();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

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
            } else {
                Toast.makeText(this, "Izin lokasi ditolak, beberapa fitur mungkin tidak bekerja.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
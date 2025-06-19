package com.example.projekakhir_andrewijaya;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem; // Import yang diperlukan untuk item menu
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull; // Import yang diperlukan untuk @NonNull
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar; // Import yang diperlukan untuk Toolbar

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class LihatDataActivity extends AppCompatActivity {

    private ListView listView;
    private FloatingActionButton fabTambah;
    private DatabaseHelper dbHelper;
    private List<Buah> buahList;
    private BuahAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_data);

        // Setup Toolbar kustom
        Toolbar toolbar = findViewById(R.id.toolbar_lihat_data);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Daftar Buah");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Inisialisasi komponen lain
        listView = findViewById(R.id.listView);
        fabTambah = findViewById(R.id.fab_tambah);
        dbHelper = new DatabaseHelper(this);
        buahList = new ArrayList<>();

        // Setup Listener untuk Tombol Tambah
        fabTambah.setOnClickListener(v ->
                startActivity(new Intent(LihatDataActivity.this, TambahDataActivity.class))
        );

        // Memuat data awal
        loadDataBuah();
    }

    // Metode ini untuk menangani klik pada tombol kembali di Toolbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Menutup activity ini dan kembali ke halaman sebelumnya
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadDataBuah() {
        Cursor cursor = dbHelper.getAllData();
        if (cursor.getCount() == 0 && buahList.isEmpty()) {
            Toast.makeText(this, "Tidak ada data buah. Klik tombol + untuk menambah.", Toast.LENGTH_LONG).show();
        }

        buahList.clear();
        while (cursor.moveToNext()) {
            String id = cursor.getString(0);
            String nama = cursor.getString(1);
            String jenis = cursor.getString(2);
            buahList.add(new Buah(id, nama, jenis));
        }
        cursor.close();

        if (adapter == null) {
            adapter = new BuahAdapter(this, buahList);
            listView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Muat ulang data setiap kali activity kembali dibuka
        loadDataBuah();
    }
}
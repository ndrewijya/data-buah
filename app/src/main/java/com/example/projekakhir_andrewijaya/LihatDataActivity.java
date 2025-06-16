package com.example.projekakhir_andrewijaya;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class LihatDataActivity extends AppCompatActivity {

    private ListView listView;
    private TextView tvJudul;
    private FloatingActionButton fabTambah; // Deklarasi FAB dipindah ke atas
    private DatabaseHelper dbHelper;
    private List<Buah> buahList;
    private BuahAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // HANYA ADA SATU METODE ONCREATE
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_data);

        // --- Inisialisasi semua komponen UI di satu tempat ---
        listView = findViewById(R.id.listView);
        tvJudul = findViewById(R.id.tvJudul);
        fabTambah = findViewById(R.id.fab_tambah); // Inisialisasi FAB

        tvJudul.setText("Daftar Buah");

        dbHelper = new DatabaseHelper(this);
        buahList = new ArrayList<>();

        // --- Setup Listener untuk Tombol Tambah ---
        fabTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Buka halaman TambahDataActivity
                startActivity(new Intent(LihatDataActivity.this, TambahDataActivity.class));
            }
        });

        // Memuat data awal saat activity dibuat
        loadDataBuah();
    }

    private void loadDataBuah() {
        Cursor cursor = dbHelper.getAllData();
        if (cursor.getCount() == 0 && buahList.isEmpty()) { // Kondisi diperbaiki agar Toast tidak muncul terus
            Toast.makeText(this, "Tidak ada data buah. Klik tombol + untuk menambah.", Toast.LENGTH_LONG).show();
        }

        // Bersihkan list sebelum memuat data baru
        buahList.clear();

        while (cursor.moveToNext()) {
            String id = cursor.getString(0);
            String nama = cursor.getString(1);
            String jenis = cursor.getString(2);
            Buah buah = new Buah(id, nama, jenis);
            buahList.add(buah);
        }
        cursor.close();

        // Inisialisasi adapter jika belum ada, atau perbarui data jika sudah ada
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
        // Muat ulang data setiap kali activity kembali dibuka,
        // agar daftar selalu update setelah menambah data baru
        loadDataBuah();
    }
}
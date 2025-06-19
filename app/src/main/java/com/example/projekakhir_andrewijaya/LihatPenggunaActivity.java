package com.example.projekakhir_andrewijaya;

import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class LihatPenggunaActivity extends AppCompatActivity {

    private ListView listView;
    private DatabaseHelper dbHelper;
    private List<Pengguna> penggunaList;
    private PenggunaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_pengguna);

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_lihat_pengguna);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Daftar Pengguna");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Inisialisasi komponen lain
        listView = findViewById(R.id.listViewPengguna);
        dbHelper = new DatabaseHelper(this);
        penggunaList = new ArrayList<>();

        loadUsersData();
    }

    // Metode untuk menangani klik pada tombol kembali di Toolbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Kembali ke halaman Dashboard
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadUsersData() {
        Cursor cursor = dbHelper.getAllUsers();
        if (cursor.getCount() == 0 && penggunaList.isEmpty()) {
            Toast.makeText(this, "Belum ada pengguna yang terdaftar", Toast.LENGTH_SHORT).show();
        }

        penggunaList.clear();
        while (cursor.moveToNext()) {
            String id = cursor.getString(0);
            String username = cursor.getString(1);
            penggunaList.add(new Pengguna(id, username));
        }
        cursor.close();

        if (adapter == null) {
            adapter = new PenggunaAdapter(this, penggunaList);
            listView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUsersData();
    }
}
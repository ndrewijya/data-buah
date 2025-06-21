package com.example.projekakhir_andrewijaya;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

/*Anggota Kelompok:
 * 1. Andre Wijaya (221011400791)
 * 2. Iqbal Isya Fathurrohman (221011401657)
 * 3. Novandra Anugrah (221011400778)
 */

public class TambahDataActivity extends AppCompatActivity {

    private EditText etNamaBuah, etJenisBuah;
    private Button btnSimpan;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_data);

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_tambah_data);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Tambah Data Buah Baru");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        dbHelper = new DatabaseHelper(this);
        etNamaBuah = findViewById(R.id.et_nama_buah);
        etJenisBuah = findViewById(R.id.et_jenis_buah);
        btnSimpan = findViewById(R.id.btn_simpan);

        btnSimpan.setOnClickListener(v -> {
            simpanData();
        });
    }

    private void simpanData() {
        String nama = etNamaBuah.getText().toString().trim();
        String jenis = etJenisBuah.getText().toString().trim();

        if (TextUtils.isEmpty(nama) || TextUtils.isEmpty(jenis)) {
            Toast.makeText(this, "Semua kolom harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }
        if (dbHelper.checkBuahExists(nama, jenis)) {
            Toast.makeText(this, "Buah dengan nama dan jenis yang sama sudah ada!", Toast.LENGTH_LONG).show();
        } else {
            boolean isInserted = dbHelper.insertBuah(nama, jenis);
            if (isInserted) {
                Toast.makeText(this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Gagal menambahkan data", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
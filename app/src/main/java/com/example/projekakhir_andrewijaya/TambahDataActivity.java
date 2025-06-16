package com.example.projekakhir_andrewijaya;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class TambahDataActivity extends AppCompatActivity {

    private EditText etNamaBuah, etJenisBuah;
    private Button btnSimpan;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_data);

        dbHelper = new DatabaseHelper(this);

        etNamaBuah = findViewById(R.id.et_nama_buah);
        etJenisBuah = findViewById(R.id.et_jenis_buah);
        btnSimpan = findViewById(R.id.btn_simpan);

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpanData();
            }
        });
    }

    private void simpanData() {
        String nama = etNamaBuah.getText().toString().trim();
        String jenis = etJenisBuah.getText().toString().trim();

        if (TextUtils.isEmpty(nama) || TextUtils.isEmpty(jenis)) {
            Toast.makeText(this, "Semua kolom harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isInserted = dbHelper.insertData(nama, jenis);

        if (isInserted) {
            Toast.makeText(this, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show();
            finish(); // Kembali ke halaman daftar buah
        } else {
            Toast.makeText(this, "Gagal menambahkan data", Toast.LENGTH_SHORT).show();
        }
    }
}
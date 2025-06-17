package com.example.projekakhir_andrewijaya;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditDataActivity extends AppCompatActivity {

    private EditText etEditNamaBuah, etEditJenisBuah;
    private Button btnUpdate;
    private DatabaseHelper dbHelper;
    private String buahId; // Untuk menyimpan ID buah yang akan diedit

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);

        dbHelper = new DatabaseHelper(this);

        etEditNamaBuah = findViewById(R.id.et_edit_nama_buah);
        etEditJenisBuah = findViewById(R.id.et_edit_jenis_buah);
        btnUpdate = findViewById(R.id.btn_update);

        // Mengambil data yang dikirim dari BuahAdapter
        Intent intent = getIntent();
        buahId = intent.getStringExtra("buah_id");
        String namaLama = intent.getStringExtra("buah_nama");
        String jenisLama = intent.getStringExtra("buah_jenis");

        // Menampilkan data lama di form
        etEditNamaBuah.setText(namaLama);
        etEditJenisBuah.setText(jenisLama);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });
    }

    private void updateData() {
        String namaBaru = etEditNamaBuah.getText().toString().trim();
        String jenisBaru = etEditJenisBuah.getText().toString().trim();

        if (TextUtils.isEmpty(namaBaru) || TextUtils.isEmpty(jenisBaru)) {
            Toast.makeText(this, "Semua kolom harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isUpdated = dbHelper.updateData(buahId, namaBaru, jenisBaru);

        if (isUpdated) {
            Toast.makeText(this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show();
            finish(); // Kembali ke halaman daftar buah
        } else {
            Toast.makeText(this, "Gagal memperbarui data", Toast.LENGTH_SHORT).show();
        }
    }
}
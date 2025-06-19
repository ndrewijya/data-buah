package com.example.projekakhir_andrewijaya;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditPenggunaActivity extends AppCompatActivity {

    private TextView tvUsernameInfo;
    private EditText etEditPassword;
    private Button btnUpdate;
    private DatabaseHelper dbHelper;
    private String userId; // Untuk menyimpan ID pengguna yang akan diedit

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pengguna);

        dbHelper = new DatabaseHelper(this);

        tvUsernameInfo = findViewById(R.id.tv_username_info);
        etEditPassword = findViewById(R.id.et_edit_password_pengguna);
        btnUpdate = findViewById(R.id.btn_update_pengguna);

        // Mengambil data yang dikirim dari PenggunaAdapter
        Intent intent = getIntent();
        userId = intent.getStringExtra("user_id");
        String username = intent.getStringExtra("user_username");

        // Menampilkan username yang sedang diedit
        tvUsernameInfo.setText("Username: " + username);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePassword();
            }
        });
    }

    private void updatePassword() {
        String passwordBaru = etEditPassword.getText().toString().trim();

        if (TextUtils.isEmpty(passwordBaru)) {
            Toast.makeText(this, "Password baru tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isUpdated = dbHelper.updateUser(userId, passwordBaru);

        if (isUpdated) {
            Toast.makeText(this, "Password berhasil diperbarui", Toast.LENGTH_SHORT).show();
            finish(); // Kembali ke halaman daftar pengguna
        } else {
            Toast.makeText(this, "Gagal memperbarui password", Toast.LENGTH_SHORT).show();
        }
    }
}
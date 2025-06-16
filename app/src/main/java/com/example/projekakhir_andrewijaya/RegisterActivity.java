package com.example.projekakhir_andrewijaya;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText regUsername, regPassword;
    private Button regButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DatabaseHelper(this);

        regUsername = findViewById(R.id.reg_username);
        regPassword = findViewById(R.id.reg_password);
        regButton = findViewById(R.id.reg_button);

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = regUsername.getText().toString().trim();
                String password = regPassword.getText().toString().trim();

                // Validasi input tidak boleh kosong
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    Toast.makeText(RegisterActivity.this, "Username dan Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Memanggil metode addUser dari DatabaseHelper
                boolean isInserted = dbHelper.addUser(username, password);

                if (isInserted) {
                    Toast.makeText(RegisterActivity.this, "Registrasi Berhasil!", Toast.LENGTH_SHORT).show();
                    // Pindah ke halaman login setelah berhasil registrasi
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // Tutup activity ini
                } else {
                    Toast.makeText(RegisterActivity.this, "Registrasi Gagal, coba lagi.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
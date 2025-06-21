package com.example.projekakhir_andrewijaya;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class EditPenggunaActivity extends AppCompatActivity {

    private TextView tvUsernameInfo;
    private EditText etEditPassword;
    private Button btnUpdate;
    private DatabaseHelper dbHelper;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pengguna);

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_edit_pengguna);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Edit Password");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        dbHelper = new DatabaseHelper(this);
        tvUsernameInfo = findViewById(R.id.tv_username_info);
        etEditPassword = findViewById(R.id.et_edit_password_pengguna);
        btnUpdate = findViewById(R.id.btn_update_pengguna);

        Intent intent = getIntent();
        userId = intent.getStringExtra("user_id");
        String username = intent.getStringExtra("user_username");

        tvUsernameInfo.setText("Username: " + username);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePassword();
            }
        });
    }

    // Metode untuk menangani klik pada tombol kembali di Toolbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
            finish();
        } else {
            Toast.makeText(this, "Gagal memperbarui password", Toast.LENGTH_SHORT).show();
        }
    }
}
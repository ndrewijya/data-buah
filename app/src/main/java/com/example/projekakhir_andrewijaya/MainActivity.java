package com.example.projekakhir_andrewijaya;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button loginButton, registerButton;
    private DatabaseHelper dbHelper; // 1. Deklarasi DatabaseHelper

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // EdgeToEdge.enable(this); // Dihapus untuk konsistensi dengan activity lain
        setContentView(R.layout.activity_main);

        // Inisialisasi DatabaseHelper
        dbHelper = new DatabaseHelper(this); // 2. Inisialisasi

        // Menghubungkan variabel dengan komponen di layout
        // Pastikan ID di XML adalah username dan password
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        registerButton = findViewById(R.id.register);

        // Meminta izin notifikasi untuk Android 13+
        requestNotificationPermission();

        // Listener untuk tombol login
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                // Validasi input tidak boleh kosong
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    Toast.makeText(MainActivity.this, "Username dan Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 3. PERUBAHAN UTAMA: Memeriksa pengguna ke database
                boolean isUserValid = dbHelper.checkUser(username, password);

                if (isUserValid) {
                    Toast.makeText(getApplicationContext(), "Login Berhasil", Toast.LENGTH_SHORT).show();
                    showLoginNotification(username); // Mengirim username ke notifikasi
                    startActivity(new Intent(MainActivity.this, DashboardActivity.class));
                    finish(); // Tutup halaman login agar tidak bisa kembali
                } else {
                    Toast.makeText(getApplicationContext(), "Username atau password salah", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Listener untuk tombol register
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Langsung pindah ke halaman Register
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }
    }

    public void showLoginNotification(String username) {
        String channelId = "login_channel";
        String channelName = "Login Notification";

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        Notification.Builder builder = new Notification.Builder(this, channelId)
                .setSmallIcon(R.drawable.baseline_person_24)
                .setContentTitle("Login Berhasil")
                .setContentText("Selamat datang, " + username + "!") // Menampilkan username yang login
                .setAutoCancel(true);

        notificationManager.notify(1, builder.build());
    }
}
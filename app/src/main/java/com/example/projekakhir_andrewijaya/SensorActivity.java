package com.example.projekakhir_andrewijaya;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import androidx.annotation.NonNull;

public class SensorActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor lightSensor;
    private Sensor accelerometerSensor;

    private TextView tvLightValue, tvLightStatus, tvAccelerometerStatus;
    private View mainLayout;

    // Variabel untuk deteksi goyangan (shake)
    private long lastUpdateTime;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 800;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        Toolbar toolbar = findViewById(R.id.toolbar_sensor);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Inisialisasi View
        mainLayout = findViewById(R.id.sensor_layout);
        tvLightValue = findViewById(R.id.light_sensor_value);
        tvLightStatus = findViewById(R.id.light_sensor_status);
        tvAccelerometerStatus = findViewById(R.id.accelerometer_status);

        // Inisialisasi Sensor Manager
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // Cek dan inisialisasi Sensor Cahaya
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (lightSensor == null) {
            tvLightStatus.setText("Sensor Cahaya tidak tersedia di perangkat ini.");
        }

        // Cek dan inisialisasi Sensor Akselerometer
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometerSensor == null) {
            tvAccelerometerStatus.setText("Sensor Gerak tidak tersedia di perangkat ini.");
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

    @Override
    protected void onResume() {
        super.onResume();
        // Daftarkan listener saat activity kembali aktif
        if (lightSensor != null) {
            sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (accelerometerSensor != null) {
            sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Lepaskan listener saat activity dijeda untuk menghemat baterai
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Cek tipe sensor mana yang memberikan data
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            handleLightSensor(event);
        } else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            handleAccelerometerSensor(event);
        }
    }

    private void handleLightSensor(SensorEvent event) {
        float luxValue = event.values[0];
        tvLightValue.setText("Nilai Lux: " + luxValue + " lx");

        // Interpretasikan nilai Lux
        if (luxValue < 50) {
            tvLightStatus.setText("Kondisi: Sangat Gelap");
            mainLayout.setBackgroundColor(Color.DKGRAY); // Warna latar abu-abu tua
        } else if (luxValue < 200) {
            tvLightStatus.setText("Kondisi: Redup");
            mainLayout.setBackgroundColor(Color.LTGRAY); // Warna latar abu-abu muda
        } else if (luxValue < 5000) {
            tvLightStatus.setText("Kondisi: Normal");
            mainLayout.setBackgroundColor(Color.WHITE); // Warna latar putih
        } else {
            tvLightStatus.setText("Kondisi: Sangat Terang");
            mainLayout.setBackgroundColor(Color.YELLOW); // Warna latar kuning
        }
    }

    private void handleAccelerometerSensor(SensorEvent event) {
        long currentTime = System.currentTimeMillis();
        // Hanya cek setiap 100 milidetik
        if ((currentTime - lastUpdateTime) > 100) {
            long diffTime = (currentTime - lastUpdateTime);
            lastUpdateTime = currentTime;

            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            // Hitung kecepatan perubahan akselerasi
            float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;

            if (speed > SHAKE_THRESHOLD) {
                // Goyangan terdeteksi!
                tvAccelerometerStatus.setText("Ponsel digoyangkan!");
                Toast.makeText(this, "Shake Detected!", Toast.LENGTH_SHORT).show();
            }

            last_x = x;
            last_y = y;
            last_z = z;
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Tidak perlu diubah untuk contoh ini
    }
}
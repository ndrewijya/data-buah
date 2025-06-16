package com.example.projekakhir_andrewijaya;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.hardware.SensorEventListener;
import android.widget.TextView;

import androidx.annotation.NonNull; // Di beberapa versi Android Studio, ini mungkin tetap dibutuhkan

import java.io.IOException;

// Implementasikan SurfaceHolder.Callback untuk mengelola kamera
public class SensorActivity extends Activity implements SurfaceHolder.Callback, SensorEventListener {

    private SurfaceView cameraPreview;
    private SurfaceHolder surfaceHolder;
    private Camera camera; // Menggunakan API Camera yang lama
    private SensorManager sensorManager;
    private Sensor accelerometer, proximity, gyroscope;
    private TextView accelText, proxText, gyroText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        // Inisialisasi komponen UI
        cameraPreview = findViewById(R.id.cameraPreview);
        accelText = findViewById(R.id.accelText);
        proxText = findViewById(R.id.proxText);
        gyroText = findViewById(R.id.gyroText);

        // Menyiapkan SurfaceHolder untuk pratinjau kamera
        surfaceHolder = cameraPreview.getHolder();
        surfaceHolder.addCallback(this);

        // Inisialisasi Sensor Manager dan sensor-sensor yang akan digunakan
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        proximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Mendaftarkan listener untuk sensor saat activity aktif
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (proximity != null) {
            sensorManager.registerListener(this, proximity, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (gyroscope != null) {
            sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Melepas listener sensor dan kamera saat activity dijeda untuk menghemat baterai
        sensorManager.unregisterListener(this);
        releaseCamera();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onSensorChanged(SensorEvent event) {
        // Menampilkan data sensor ke TextView
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelText.setText("Accelerometer:\nX: " + event.values[0]
                    + "\nY: " + event.values[1]
                    + "\nZ: " + event.values[2]);
        } else if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            proxText.setText("Proximity: " + event.values[0]);
        } else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            gyroText.setText("Gyroscope:\nX: " + event.values[0]
                    + "\nY: " + event.values[1]
                    + "\nZ: " + event.values[2]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Tidak perlu implementasi untuk saat ini, sesuai modul
    }

    // --- CAMERA HANDLER SESUAI MODUL ---

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        // Membuka kamera saat surface (area tampilan) dibuat
        try {
            camera = Camera.open();
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        // Menangani perubahan pada surface, misalnya saat rotasi layar
        if (camera != null) {
            camera.stopPreview();
            try {
                camera.setPreviewDisplay(holder);
                camera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        // Melepas kamera saat surface dihancurkan
        releaseCamera();
    }

    private void releaseCamera() {
        // Metode untuk menghentikan pratinjau dan melepas resource kamera
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }
}
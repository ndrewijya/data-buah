package com.example.projekakhir_andrewijaya;

import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity; // DIUBAH: dari Activity menjadi AppCompatActivity
import androidx.appcompat.widget.Toolbar;   // DITAMBAHKAN: import untuk Toolbar

import java.io.IOException;

public class SensorActivity extends AppCompatActivity implements SurfaceHolder.Callback, SensorEventListener {

    private SurfaceView cameraPreview;
    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private SensorManager sensorManager;
    private Sensor accelerometer, proximity, gyroscope;
    private TextView accelText, proxText, gyroText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_sensor);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Fitur Sensor");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Inisialisasi komponen lain (tidak berubah)
        cameraPreview = findViewById(R.id.cameraPreview);
        accelText = findViewById(R.id.accelText);
        proxText = findViewById(R.id.proxText);
        gyroText = findViewById(R.id.gyroText);
        surfaceHolder = cameraPreview.getHolder();
        surfaceHolder.addCallback(this);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        proximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }

    // DITAMBAHKAN: Metode untuk menangani klik pada tombol kembali di Toolbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Menutup activity dan kembali ke Dashboard
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // ... sisa kode Anda (onResume, onPause, onSensorChanged, dll) tetap sama ...
    @Override
    protected void onResume() {
        super.onResume();
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
        sensorManager.unregisterListener(this);
        releaseCamera();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
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
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
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
        releaseCamera();
    }

    private void releaseCamera() {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }
}
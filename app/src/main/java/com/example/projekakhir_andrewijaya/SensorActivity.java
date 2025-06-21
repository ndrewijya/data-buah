package com.example.projekakhir_andrewijaya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class SensorActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor lightSensor;
    private Sensor accelerometerSensor;
    private TextView tvLightTitle, tvLightValue, tvLightStatus;
    private TextView tvAccelerometerTitle, tvAccelerometerStatus;
    private View mainLayout;
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

        mainLayout = findViewById(R.id.sensor_layout);
        tvLightTitle = findViewById(R.id.light_sensor_title);
        tvLightValue = findViewById(R.id.light_sensor_value);
        tvLightStatus = findViewById(R.id.light_sensor_status);
        tvAccelerometerTitle = findViewById(R.id.accelerometer_title);
        tvAccelerometerStatus = findViewById(R.id.accelerometer_status);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (lightSensor == null) {
            tvLightStatus.setText("Sensor Cahaya tidak tersedia di perangkat ini.");
        }

        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometerSensor == null) {
            tvAccelerometerStatus.setText("Sensor Gerak tidak tersedia di perangkat ini.");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            handleLightSensor(event);
        } else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            handleAccelerometerSensor(event);
        }
    }

    private void handleLightSensor(SensorEvent event) {
        float luxValue = event.values[0];
        tvLightValue.setText("Nilai Lux: " + luxValue + " lx");

        if (luxValue < 50) {
            tvLightStatus.setText("Kondisi: Sangat Gelap");
            mainLayout.setBackgroundColor(Color.DKGRAY);
            updateTextColors(Color.WHITE);
        } else if (luxValue < 200) {
            tvLightStatus.setText("Kondisi: Redup");
            mainLayout.setBackgroundColor(Color.LTGRAY);
            updateTextColors(Color.BLACK);
        } else if (luxValue < 5000) {
            tvLightStatus.setText("Kondisi: Normal");
            mainLayout.setBackgroundColor(Color.WHITE);
            updateTextColors(Color.BLACK);
        } else {
            tvLightStatus.setText("Kondisi: Sangat Terang");
            mainLayout.setBackgroundColor(Color.YELLOW);
            updateTextColors(Color.BLACK);
        }
    }

    private void updateTextColors(int color) {
        tvLightTitle.setTextColor(color);
        tvLightValue.setTextColor(color);
        tvLightStatus.setTextColor(color);
        tvAccelerometerTitle.setTextColor(color);
        tvAccelerometerStatus.setTextColor(color);
    }

    private void handleAccelerometerSensor(SensorEvent event) {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - lastUpdateTime) > 100) {
            long diffTime = (currentTime - lastUpdateTime);
            lastUpdateTime = currentTime;

            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;

            if (speed > SHAKE_THRESHOLD) {
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
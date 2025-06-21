package com.example.projekakhir_andrewijaya;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class VoiceToTextActivity extends AppCompatActivity {

    private ImageButton micButton, btnCopyText;
    private TextView resultTextView, tvHasilTitle;
    private View cardResult; // Menggunakan View untuk mereferensikan CardView

    private static final int RECOGNIZER_RESULT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_to_text);

        // Setup Toolbar dengan tombol kembali
        Toolbar toolbar = findViewById(R.id.toolbar_voice);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Inisialisasi semua komponen UI
        micButton = findViewById(R.id.btn_bicara);
        resultTextView = findViewById(R.id.tv_hasil_suara);
        btnCopyText = findViewById(R.id.btn_copy_text);
        tvHasilTitle = findViewById(R.id.tv_hasil_title);
        cardResult = findViewById(R.id.card_result);

        micButton.setOnClickListener(v -> {
            Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Mulai berbicara...");

            try {
                startActivityForResult(speechIntent, RECOGNIZER_RESULT);
            } catch (Exception e) {
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Menambahkan fungsi untuk tombol copy
        btnCopyText.setOnClickListener(v -> {
            copyToClipboard(resultTextView.getText().toString());
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RECOGNIZER_RESULT && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                String recognizedText = result.get(0);
                resultTextView.setText(recognizedText);

                // Tampilkan card hasil dan judulnya setelah ada hasil
                tvHasilTitle.setVisibility(View.VISIBLE);
                cardResult.setVisibility(View.VISIBLE);
            }
        } else {
            // Beri feedback jika tidak ada suara yang dikenali
            Toast.makeText(this, "Tidak ada suara yang dikenali, silakan coba lagi.", Toast.LENGTH_SHORT).show();
        }
    }

    // Method baru untuk menyalin teks ke clipboard
    private void copyToClipboard(String text) {
        if (text.isEmpty() || text.equals("Hasil akan muncul di sini...")) {
            Toast.makeText(this, "Tidak ada teks untuk disalin", Toast.LENGTH_SHORT).show();
            return;
        }
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied Text", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "Teks berhasil disalin!", Toast.LENGTH_SHORT).show();
    }

    // Fungsi untuk tombol kembali di Toolbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
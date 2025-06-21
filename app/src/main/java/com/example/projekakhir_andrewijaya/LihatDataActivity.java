package com.example.projekakhir_andrewijaya;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.Locale;

/*Anggota Kelompok:
 * 1. Andre Wijaya (221011400791)
 * 2. Iqbal Isya Fathurrohman (221011401657)
 * 3. Novandra Anugrah (221011400778)
 */

public class LihatDataActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BuahAdapter buahAdapter;
    private DatabaseHelper databaseHelper;
    private SearchView searchView;
    private ImageButton btnVoiceSearch;
    private FloatingActionButton fabTambah;

    private static final int VOICE_SEARCH_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_data);

        Toolbar toolbar = findViewById(R.id.toolbar_lihat_data);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        databaseHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recycler_view_buah);
        searchView = findViewById(R.id.search_view_buah);
        btnVoiceSearch = findViewById(R.id.btn_voice_search);
        fabTambah = findViewById(R.id.fab_tambah);

        setupRecyclerView();
        setupListeners();
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        buahAdapter = new BuahAdapter(this, databaseHelper.getAllBuah());
        recyclerView.setAdapter(buahAdapter);
    }

    private void setupListeners() {
        fabTambah.setOnClickListener(v -> {
            startActivity(new Intent(LihatDataActivity.this, TambahDataActivity.class));
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (buahAdapter != null) {
                    buahAdapter.getFilter().filter(newText);
                }
                return true;
            }
        });

        btnVoiceSearch.setOnClickListener(v -> startVoiceRecognition());
    }

    private void startVoiceRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Ucapkan nama buah...");

        try {
            startActivityForResult(intent, VOICE_SEARCH_REQUEST_CODE);
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == VOICE_SEARCH_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                String spokenText = result.get(0);
                searchView.setQuery(spokenText, true);

            }
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
        if (buahAdapter != null) {
            buahAdapter.updateData(databaseHelper.getAllBuah());
        }
    }
}
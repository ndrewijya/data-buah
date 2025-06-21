package com.example.projekakhir_andrewijaya;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

/*Anggota Kelompok:
 * 1. Andre Wijaya (221011400791)
 * 2. Iqbal Isya Fathurrohman (221011401657)
 * 3. Novandra Anugrah (221011400778)
 */

public class LihatPenggunaActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PenggunaAdapter penggunaAdapter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_pengguna);

        Toolbar toolbar = findViewById(R.id.toolbar_lihat_pengguna);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        databaseHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recycler_view_pengguna);

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<Pengguna> listPengguna = databaseHelper.getAllUsers();
        if(listPengguna.isEmpty()){
            Toast.makeText(this, "Belum ada pengguna yang terdaftar", Toast.LENGTH_SHORT).show();
        }

        penggunaAdapter = new PenggunaAdapter(this, listPengguna);
        recyclerView.setAdapter(penggunaAdapter);
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
        if (penggunaAdapter != null) {
            penggunaAdapter.updateData(databaseHelper.getAllUsers());
        }
    }
}
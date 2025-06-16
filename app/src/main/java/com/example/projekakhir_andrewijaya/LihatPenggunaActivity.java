package com.example.projekakhir_andrewijaya;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class LihatPenggunaActivity extends AppCompatActivity {

    private ListView listView;
    private DatabaseHelper dbHelper;
    private ArrayList<String> userList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_pengguna);

        listView = findViewById(R.id.listViewPengguna);
        dbHelper = new DatabaseHelper(this);
        userList = new ArrayList<>();

        loadUsersData();
    }

    private void loadUsersData() {
        Cursor cursor = dbHelper.getAllUsers();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Belum ada pengguna yang terdaftar", Toast.LENGTH_SHORT).show();
            return;
        }

        while (cursor.moveToNext()) {
            // Indeks kolom: 0 untuk ID, 1 untuk USERNAME
            String username = cursor.getString(1);
            userList.add(username);
        }
        cursor.close();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userList);
        listView.setAdapter(adapter);
    }
}
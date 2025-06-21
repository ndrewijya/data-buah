package com.example.projekakhir_andrewijaya;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "AplikasiData.db";
    public static final int DATABASE_VERSION = 1;

    // Definisi Tabel PENGGUNA (USERS)
    public static final String TABLE_USERS = "users";
    public static final String COL_USER_ID = "ID";
    public static final String COL_USERNAME = "username";
    public static final String COL_PASSWORD = "password";

    // Definisi Tabel BUAH
    public static final String TABLE_BUAH = "tabel_buah";
    public static final String COL_BUAH_ID = "ID_BUAH";
    public static final String COL_BUAH_NAMA = "NAMA_BUAH";
    public static final String COL_BUAH_JENIS = "JENIS_BUAH";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableUsers = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USERNAME + " TEXT, " +
                COL_PASSWORD + " TEXT)";
        db.execSQL(createTableUsers);

        String createTableBuah = "CREATE TABLE " + TABLE_BUAH + " (" +
                COL_BUAH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_BUAH_NAMA + " TEXT, " +
                COL_BUAH_JENIS + " TEXT)";
        db.execSQL(createTableBuah);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUAH);
        onCreate(db);
    }

    // =================================================================
    // --- SEMUA METODE UNTUK PENGGUNA (USERS) ---
    // =================================================================

    public boolean addUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, username);
        values.put(COL_PASSWORD, password);
        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COL_USER_ID},
                COL_USERNAME + " = ? AND " + COL_PASSWORD + " = ?",
                new String[]{username, password}, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count > 0;
    }

    // METHOD YANG HILANG SEBELUMNYA, SEKARANG DIKEMBALIKAN
    public boolean checkUsernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COL_USERNAME + " = ?", new String[]{username});
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count > 0;
    }

    public ArrayList<Pengguna> getAllUsers() {
        ArrayList<Pengguna> listPengguna = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER_ID));
                String username = cursor.getString(cursor.getColumnIndexOrThrow(COL_USERNAME));
                String password = cursor.getString(cursor.getColumnIndexOrThrow(COL_PASSWORD));

                listPengguna.add(new Pengguna(id, username, password));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return listPengguna;
    }

    public boolean updateUser(String id, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PASSWORD, newPassword);
        int result = db.update(TABLE_USERS, values, COL_USER_ID + " = ?", new String[]{id});
        db.close();
        return result > 0;
    }

    public Integer deleteUser(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_USERS, COL_USER_ID + " = ?", new String[]{id});
        db.close();
        return result;
    }

    // =================================================================
    // --- SEMUA METODE UNTUK BUAH (CRUD) ---
    // =================================================================

    public boolean insertBuah(String namaBuah, String jenisBuah) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_BUAH_NAMA, namaBuah);
        values.put(COL_BUAH_JENIS, jenisBuah);
        long result = db.insert(TABLE_BUAH, null, values);
        db.close();
        return result != -1;
    }

    public ArrayList<Buah> getAllBuah() {
        ArrayList<Buah> listBuah = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BUAH, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_BUAH_ID));
                String nama = cursor.getString(cursor.getColumnIndexOrThrow(COL_BUAH_NAMA));
                String jenis = cursor.getString(cursor.getColumnIndexOrThrow(COL_BUAH_JENIS));
                listBuah.add(new Buah(id, nama, jenis));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return listBuah;
    }

    public boolean updateBuah(String id, String namaBuah, String jenisBuah) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_BUAH_NAMA, namaBuah);
        values.put(COL_BUAH_JENIS, jenisBuah);
        int result = db.update(TABLE_BUAH, values, COL_BUAH_ID + " = ?", new String[]{id});
        db.close();
        return result > 0;
    }

    public Integer deleteBuah(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_BUAH, COL_BUAH_ID + " = ?", new String[]{id});
        db.close();
        return result;
    }
}
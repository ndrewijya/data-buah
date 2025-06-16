package com.example.projekakhir_andrewijaya;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Nama database umum untuk seluruh aplikasi
    public static final String DATABASE_NAME = "AplikasiData.db";
    public static final int DATABASE_VERSION = 1;

    // --- Definisi Tabel untuk PENGGUNA (USERS) ---
    public static final String TABLE_USERS = "users";
    public static final String COL_USER_ID = "ID";
    public static final String COL_USERNAME = "username";
    public static final String COL_PASSWORD = "password";

    // --- Definisi Tabel untuk BUAH ---
    public static final String TABLE_BUAH = "tabel_buah";
    public static final String COL_BUAH_ID = "ID_BUAH";
    public static final String COL_BUAH_NAMA = "NAMA_BUAH";
    public static final String COL_BUAH_JENIS = "JENIS_BUAH";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 1. Query untuk membuat tabel pengguna (untuk Register & Login)
        String createTableUsers = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USERNAME + " TEXT, " +
                COL_PASSWORD + " TEXT)";
        db.execSQL(createTableUsers);

        // 2. Query untuk membuat tabel buah
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

    // --- METODE-METODE UNTUK PENGGUNA (USERS) ---

    /**
     * Menambahkan user baru untuk registrasi
     */
    public boolean addUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, username);
        values.put(COL_PASSWORD, password); // Dalam aplikasi nyata, password harus di-enkripsi (hashing)
        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    /**
     * Memeriksa keberadaan user untuk login
     */
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COL_USER_ID};
        String selection = COL_USERNAME + " = ?" + " AND " + COL_PASSWORD + " = ?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }

    /**
     * Mengambil semua data pengguna dari tabel users
     */
    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS, null);
    }

    // --- METODE-METODE UNTUK BUAH (INI TETAP SAMA, TIDAK PERLU DIUBAH) ---

    public boolean insertData(String namaBuah, String jenisBuah) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_BUAH_NAMA, namaBuah);
        values.put(COL_BUAH_JENIS, jenisBuah);
        long result = db.insert(TABLE_BUAH, null, values);
        return result != -1;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_BUAH, null);
    }

    public boolean updateData(String id, String namaBuah, String jenisBuah) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_BUAH_NAMA, namaBuah);
        values.put(COL_BUAH_JENIS, jenisBuah);
        int result = db.update(TABLE_BUAH, values, COL_BUAH_ID + " = ?", new String[]{id});
        return result > 0;
    }

    public Integer deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_BUAH, COL_BUAH_ID + " = ?", new String[]{id});
    }
}
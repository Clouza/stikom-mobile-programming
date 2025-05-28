package com.example.rabu21mei;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MHS.db";
    private static final String TABLE_NAME = "Mahasiswa";
    private static final String COLUMN_NIM = "nim";
    private static final String COLUMN_NAMA = "nama";
    private static final String COLUMN_UMUR = "umur";
    private static final String COLUMN_PATH = "path";
    private static final String COLUMN_TEST = "test";

    private static final String TABLE_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
            + "("
            + COLUMN_NIM + " TEXT PRIMARY KEY, "
            + COLUMN_NAMA + " TEXT, "
            + COLUMN_UMUR + " INTEGER, "
            + COLUMN_PATH + " TEXT "
            + ")";

    SQLiteDatabase db;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        this.db = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = null;
        if (newVersion == 2) {
            query = "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_TEST + " TEXT;";
            db.execSQL(query);
            this.db = db;
        }
    }

    public int getCountData() {
        int result = 0;
        db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_NIM}, null, null, null, null, COLUMN_NIM + " ASC ");
        result = cursor.getCount();

        db.close();
        return result;
    }

    public Mahasiswa getExistData(Context context, String key) {
        Mahasiswa mhs = null;
        try {
            String query = "SELECT * FROM " + TABLE_NAME + " WHERE upper(" + COLUMN_NIM + ") = '" + key.toUpperCase() + "'";
            db = getReadableDatabase();
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                mhs = new Mahasiswa(
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NIM)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAMA)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PATH)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_UMUR))
                );
            }
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        db.close();
        return mhs;
    }

    public ArrayList<Mahasiswa> transferToArrayList(Context context) {
        ArrayList<Mahasiswa> arrayMahasiswa = null;
        try {
            db = getReadableDatabase();
            Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_NIM, COLUMN_NAMA, COLUMN_UMUR, COLUMN_PATH}, null, null, null, null, COLUMN_NIM + " ASC");

            if (cursor.getCount() > 0) {
                arrayMahasiswa = new ArrayList<Mahasiswa>();
                cursor.moveToFirst();

                do {
                    arrayMahasiswa.add(new Mahasiswa(
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NIM)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAMA)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PATH)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_UMUR))
                    ));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        db.close();
        return arrayMahasiswa;
    }

    public boolean insertData(Context context, Mahasiswa mahasiswa) {
        boolean isSuccess = false;
        try {
            db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_NIM, mahasiswa.getNim());
            values.put(COLUMN_NAMA, mahasiswa.getNama());
            values.put(COLUMN_UMUR, mahasiswa.getUmur());
            values.put(COLUMN_PATH, mahasiswa.getPath());

            long result = db.insert(TABLE_NAME, null, values);
            if (result > -1) {
                isSuccess = true;
            }
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage().toString(), Toast.LENGTH_LONG).show();
        }

        db.close();
        return isSuccess;
    }

    public boolean updateData(Context context, Mahasiswa mahasiswa) {
        boolean isSuccess = false;
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAMA, mahasiswa.getNama());
            values.put(COLUMN_UMUR, mahasiswa.getUmur());
            values.put(COLUMN_PATH, mahasiswa.getPath());
            db = getWritableDatabase();
            db.update(TABLE_NAME, values, COLUMN_NIM + "=?", new String[]{mahasiswa.getNim()});
            db.close();
            isSuccess = true;
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage().toString(), Toast.LENGTH_LONG).show();
        }

        return isSuccess;
    }

    public int deleteData(Context context, String nim) {
        int deleteCount = -1;
        try {
            db = getWritableDatabase();
            deleteCount = db.delete(TABLE_NAME, COLUMN_NIM + "=?", new String[]{nim});
            db.close();
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }

        return deleteCount;
    }
}

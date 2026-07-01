package com.eni.chanteurapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.eni.chanteurapp.model.Chanteur;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME    = "chanteurs.db";
    private static final int    DB_VERSION = 1;

    private static final String TABLE_CHANTEUR = "chanteur";
    private static final String COL_ID         = "idchant";
    private static final String COL_NOM        = "nom";
    private static final String COL_DATENAIS   = "datenais";
    private static final String COL_PHOTO      = "photo";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable =
            "CREATE TABLE IF NOT EXISTS " + TABLE_CHANTEUR + " (" +
            COL_ID       + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_NOM      + " TEXT NOT NULL, " +
            COL_DATENAIS + " TEXT, " +
            COL_PHOTO    + " TEXT" +
            ");";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHANTEUR);
        onCreate(db);
    }

    // CREATE
    public void addChanteur(Chanteur chanteur) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NOM,      chanteur.getNom());
        values.put(COL_DATENAIS, chanteur.getDatenais());
        values.put(COL_PHOTO,    chanteur.getPhoto());
        db.insert(TABLE_CHANTEUR, null, values);
        db.close();
    }

    // READ ALL
    public List<Chanteur> getAllChanteurs() {
        List<Chanteur> liste = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
            "SELECT * FROM " + TABLE_CHANTEUR + " ORDER BY " + COL_NOM + " ASC", null
        );
        if (cursor.moveToFirst()) {
            do {
                liste.add(new Chanteur(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_NOM)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_DATENAIS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_PHOTO))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return liste;
    }

    // READ ONE
    public Chanteur getChanteurById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
            "SELECT * FROM " + TABLE_CHANTEUR + " WHERE " + COL_ID + " = ?",
            new String[]{String.valueOf(id)}
        );
        Chanteur chanteur = null;
        if (cursor.moveToFirst()) {
            chanteur = new Chanteur(
                cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_NOM)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_DATENAIS)),
                cursor.getString(cursor.getColumnIndexOrThrow(COL_PHOTO))
            );
        }
        cursor.close();
        db.close();
        return chanteur;
    }

    // UPDATE
    public void updateChanteur(Chanteur chanteur) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NOM,      chanteur.getNom());
        values.put(COL_DATENAIS, chanteur.getDatenais());
        values.put(COL_PHOTO,    chanteur.getPhoto());
        db.update(TABLE_CHANTEUR, values,
            COL_ID + " = ?",
            new String[]{String.valueOf(chanteur.getIdchant())}
        );
        db.close();
    }

    // DELETE
    public void deleteChanteur(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CHANTEUR,
            COL_ID + " = ?",
            new String[]{String.valueOf(id)}
        );
        db.close();
    }

    // SEARCH
    public List<Chanteur> searchChanteurs(String keyword) {
        List<Chanteur> liste = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
            "SELECT * FROM " + TABLE_CHANTEUR +
            " WHERE " + COL_NOM + " LIKE ?" +
            " ORDER BY " + COL_NOM + " ASC",
            new String[]{"%" + keyword + "%"}
        );
        if (cursor.moveToFirst()) {
            do {
                liste.add(new Chanteur(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_NOM)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_DATENAIS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_PHOTO))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return liste;
    }
}
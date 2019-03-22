package com.santos.dev.Utils.SQLiteFukes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.santos.firebasecomponents.Models.Semana;

import java.util.ArrayList;

import static com.santos.dev.Utils.SQLiteFukes.Campos.CATEDRATICO;
import static com.santos.dev.Utils.SQLiteFukes.Campos.HORA_A;
import static com.santos.dev.Utils.SQLiteFukes.Campos.HORA_DE;
import static com.santos.dev.Utils.SQLiteFukes.Campos.DIA;
import static com.santos.dev.Utils.SQLiteFukes.Campos.CURSO;
import static com.santos.dev.Utils.SQLiteFukes.Campos.ID_SEMANA;
import static com.santos.dev.Utils.SQLiteFukes.Campos.SALON;
import static com.santos.dev.Utils.SQLiteFukes.Campos.TABLE_NAME;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";

    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                ID_SEMANA + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DIA + " TEXT, " +
                CURSO + " TEXT, " +
                CATEDRATICO + " TEXT, " +
                SALON + " TEXT, " +
                HORA_DE + " TEXT, " +
                HORA_A + " TEXT) ";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String delete = "DROP IF TABLE EXISTS ";
        db.execSQL(delete + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(Semana semana) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DIA, semana.getDia());
        contentValues.put(CURSO, semana.getCurso());
        contentValues.put(CATEDRATICO, semana.getCatedratico());
        contentValues.put(SALON, semana.getSalon());
        contentValues.put(HORA_DE, semana.getHora_de());
        contentValues.put(HORA_A, semana.getHora_a());

        Log.d(TAG, "addData: Adding " + semana + " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, contentValues);
        //db.update(TABLE_NAME, contentValues, DIA, null);
        db.close();

        // db.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }

    public ArrayList<Semana> getSemana(String fragment) {
        SQLiteDatabase db = this.getWritableDatabase();

        ArrayList<Semana> weeklist = new ArrayList<>();
        Semana semana;
        Cursor cursor = db.rawQuery("SELECT * FROM ( SELECT * FROM " + TABLE_NAME + " ORDER BY " + HORA_DE + " ) WHERE " + DIA + " LIKE '" + fragment + "%'", null);
        while (cursor.moveToNext()) {
            semana = new Semana();
            semana.setId(cursor.getInt(cursor.getColumnIndex(ID_SEMANA)));
            semana.setCurso(cursor.getString(cursor.getColumnIndex(CURSO)));
            semana.setCatedratico(cursor.getString(cursor.getColumnIndex(CATEDRATICO)));
            semana.setSalon(cursor.getString(cursor.getColumnIndex(SALON)));
            semana.setHora_de(cursor.getString(cursor.getColumnIndex(HORA_DE)));
            semana.setHora_a(cursor.getString(cursor.getColumnIndex(HORA_A)));
            weeklist.add(semana);
        }
        return weeklist;
    }

    public void deleteWeekById(Semana semana) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ID_SEMANA + " = ? ", new String[]{String.valueOf(semana.getId())});
        db.close();
    }
}

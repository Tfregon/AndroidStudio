package com.example.grocerymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "grocery.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_GROCERY = "grocery_items";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_CATEGORY = "category";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_GROCERY + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_CATEGORY + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROCERY);
        onCreate(db);
    }

    // Método para adicionar ou atualizar um item
    public boolean saveItem(String name, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_CATEGORY, category);

        long result = db.insert(TABLE_GROCERY, null, values);
        return result != -1;
    }

    // Método para deletar um item
    public boolean deleteItem(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_GROCERY, COLUMN_NAME + "=?", new String[]{name}) > 0;
    }

    // Método para obter todos os itens
    public ArrayList<String[]> getAllItems() {
        ArrayList<String[]> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_GROCERY, null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(1);
                String category = cursor.getString(2);
                list.add(new String[]{name, category});
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
}

package com.example.myapplication.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.example.myapplication.helpers.SQLiteHelper;

public abstract class BaseDAO {
    protected SQLiteHelper dbHelper;
    protected Context context;

    public BaseDAO(Context context) {
        this.context = context;
        this.dbHelper = new SQLiteHelper(context);
    }

    protected SQLiteDatabase getWritableDatabase() {
        return dbHelper.getWritableDatabase();
    }

    protected SQLiteDatabase getReadableDatabase() {
        return dbHelper.getReadableDatabase();
    }

    protected void closeDatabase(SQLiteDatabase db) {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    public void close() {
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}
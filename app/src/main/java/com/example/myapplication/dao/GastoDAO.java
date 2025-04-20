package com.example.myapplication.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.helpers.SQLiteHelper;
import com.example.myapplication.models.Gasto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GastoDAO extends BaseDAO {
    
    public GastoDAO(Context context) {
        super(context);
    }

    public long insert(Gasto gasto) {
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.COLUMN_DESCRICAO, gasto.getDescricao());
        values.put(SQLiteHelper.COLUMN_VALOR, gasto.getValor());
        values.put(SQLiteHelper.COLUMN_DATA, gasto.getData().getTime());
        values.put(SQLiteHelper.COLUMN_CATEGORIA_ID, gasto.getCategoriaId());
        values.put(SQLiteHelper.COLUMN_USUARIO_ID, gasto.getUsuarioId());

        return getWritableDatabase().insert(SQLiteHelper.TABLE_GASTO, null, values);
    }

    public boolean update(Gasto gasto) {
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.COLUMN_DESCRICAO, gasto.getDescricao());
        values.put(SQLiteHelper.COLUMN_VALOR, gasto.getValor());
        values.put(SQLiteHelper.COLUMN_DATA, gasto.getData().getTime());
        values.put(SQLiteHelper.COLUMN_CATEGORIA_ID, gasto.getCategoriaId());
        values.put(SQLiteHelper.COLUMN_USUARIO_ID, gasto.getUsuarioId());

        return getWritableDatabase().update(SQLiteHelper.TABLE_GASTO, values,
                SQLiteHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(gasto.getId())}) > 0;
    }

    public boolean delete(int id) {
        return getWritableDatabase().delete(SQLiteHelper.TABLE_GASTO,
                SQLiteHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)}) > 0;
    }

    public List<Gasto> list(long usuarioId) {
        List<Gasto> gastos = new ArrayList<>();
        
        Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT g.*, c." + SQLiteHelper.COLUMN_NOME_CATEGORIA + " as nome_categoria " +
                "FROM " + SQLiteHelper.TABLE_GASTO + " g " +
                "LEFT JOIN " + SQLiteHelper.TABLE_CATEGORIAS + " c ON g." + SQLiteHelper.COLUMN_CATEGORIA_ID + " = c." + SQLiteHelper.COLUMN_ID_CATEGORIA + " " +
                "WHERE g." + SQLiteHelper.COLUMN_USUARIO_ID + " = ? " +
                "ORDER BY g." + SQLiteHelper.COLUMN_DATA + " DESC", 
                new String[]{String.valueOf(usuarioId)});

        while (cursor.moveToNext()) {
            Gasto gasto = new Gasto();
            gasto.setId(cursor.getInt(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMN_ID)));
            gasto.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMN_DESCRICAO)));
            gasto.setValor(cursor.getDouble(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMN_VALOR)));
            long timestamp = cursor.getLong(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMN_DATA));
            gasto.setData(new Date(timestamp));
            gasto.setCategoriaId(cursor.getInt(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMN_CATEGORIA_ID)));
            gasto.setUsuarioId(cursor.getInt(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMN_USUARIO_ID)));
            gasto.setNomeCategoria(cursor.getString(cursor.getColumnIndexOrThrow("nome_categoria")));
            
            gastos.add(gasto);
        }
        cursor.close();

        return gastos;
    }

    public Gasto get(int id) {
        Gasto gasto = null;
        
        Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT g.*, c." + SQLiteHelper.COLUMN_NOME_CATEGORIA + " as nome_categoria " +
                "FROM " + SQLiteHelper.TABLE_GASTO + " g " +
                "LEFT JOIN " + SQLiteHelper.TABLE_CATEGORIAS + " c ON g." + SQLiteHelper.COLUMN_CATEGORIA_ID + " = c." + SQLiteHelper.COLUMN_ID_CATEGORIA + " " +
                "WHERE g." + SQLiteHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {
            gasto = new Gasto();
            gasto.setId(cursor.getInt(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMN_ID)));
            gasto.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMN_DESCRICAO)));
            gasto.setValor(cursor.getDouble(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMN_VALOR)));
            long timestamp = cursor.getLong(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMN_DATA));
            gasto.setData(new Date(timestamp));
            gasto.setCategoriaId(cursor.getInt(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMN_CATEGORIA_ID)));
            gasto.setUsuarioId(cursor.getInt(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMN_USUARIO_ID)));
            gasto.setNomeCategoria(cursor.getString(cursor.getColumnIndexOrThrow("nome_categoria")));
        }
        cursor.close();

        return gasto;
    }
} 
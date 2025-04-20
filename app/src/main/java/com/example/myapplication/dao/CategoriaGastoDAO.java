package com.example.myapplication.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.myapplication.helpers.SQLiteHelper;
import com.example.myapplication.models.CategoriaGasto;

import java.util.ArrayList;
import java.util.List;

public class CategoriaGastoDAO {
    private static final String TAG = "CategoriaGastoDAO";
    private SQLiteHelper dbHelper;
    private Context context;

    public CategoriaGastoDAO(Context context) {
        this.context = context;
        this.dbHelper = new SQLiteHelper(context);
    }

    public long insert(CategoriaGasto categoria) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id = -1;

        try {
            ContentValues values = new ContentValues();
            values.put(SQLiteHelper.COLUMN_NOME_CATEGORIA, categoria.getDescricao());
            values.put(SQLiteHelper.COLUMN_USUARIO_ID, categoria.getUsuarioId());

            id = db.insert(SQLiteHelper.TABLE_CATEGORIAS, null, values);
            Log.d(TAG, "Categoria inserida com ID: " + id);
        } catch (Exception e) {
            Log.e(TAG, "Erro ao inserir categoria", e);
        } finally {
            db.close();
        }

        return id;
    }

    public boolean update(CategoriaGasto categoria) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        boolean success = false;

        try {
            ContentValues values = new ContentValues();
            values.put(SQLiteHelper.COLUMN_NOME_CATEGORIA, categoria.getDescricao());

            String selection = SQLiteHelper.COLUMN_ID_CATEGORIA + " = ? AND " + 
                             SQLiteHelper.COLUMN_USUARIO_ID + " = ?";
            String[] selectionArgs = {
                String.valueOf(categoria.getIdCategoria()),
                String.valueOf(categoria.getUsuarioId())
            };

            int count = db.update(SQLiteHelper.TABLE_CATEGORIAS, values, selection, selectionArgs);
            success = count > 0;
            Log.d(TAG, "Categorias atualizadas: " + count);
        } catch (Exception e) {
            Log.e(TAG, "Erro ao atualizar categoria", e);
        } finally {
            db.close();
        }

        return success;
    }

    public boolean delete(int idCategoria, int usuarioId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        boolean success = false;

        try {
            String selection = SQLiteHelper.COLUMN_ID_CATEGORIA + " = ? AND " + 
                             SQLiteHelper.COLUMN_USUARIO_ID + " = ?";
            String[] selectionArgs = {
                String.valueOf(idCategoria),
                String.valueOf(usuarioId)
            };

            int count = db.delete(SQLiteHelper.TABLE_CATEGORIAS, selection, selectionArgs);
            success = count > 0;
            Log.d(TAG, "Categorias deletadas: " + count);
        } catch (Exception e) {
            Log.e(TAG, "Erro ao deletar categoria", e);
        } finally {
            db.close();
        }

        return success;
    }

    public CategoriaGasto findById(int idCategoria, int usuarioId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        CategoriaGasto categoria = null;
        Cursor cursor = null;

        try {
            String selection = SQLiteHelper.COLUMN_ID_CATEGORIA + " = ? AND " + 
                             SQLiteHelper.COLUMN_USUARIO_ID + " = ?";
            String[] selectionArgs = {
                String.valueOf(idCategoria),
                String.valueOf(usuarioId)
            };

            cursor = db.query(SQLiteHelper.TABLE_CATEGORIAS, null, selection, selectionArgs, 
                            null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                categoria = cursorToCategoria(cursor);
            }
        } catch (Exception e) {
            Log.e(TAG, "Erro ao buscar categoria por ID", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return categoria;
    }

    public List<CategoriaGasto> findAll(int usuarioId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<CategoriaGasto> categorias = new ArrayList<>();
        Cursor cursor = null;

        try {
            String selection = SQLiteHelper.COLUMN_USUARIO_ID + " = ?";
            String[] selectionArgs = { String.valueOf(usuarioId) };
            String orderBy = SQLiteHelper.COLUMN_NOME_CATEGORIA + " ASC";

            cursor = db.query(SQLiteHelper.TABLE_CATEGORIAS, null, selection, selectionArgs, 
                            null, null, orderBy);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    CategoriaGasto categoria = cursorToCategoria(cursor);
                    categorias.add(categoria);
                } while (cursor.moveToNext());
            }
            
            Log.d(TAG, "Categorias encontradas: " + categorias.size());
        } catch (Exception e) {
            Log.e(TAG, "Erro ao buscar todas as categorias", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return categorias;
    }

    private CategoriaGasto cursorToCategoria(Cursor cursor) {
        CategoriaGasto categoria = new CategoriaGasto();
        categoria.setIdCategoria(cursor.getInt(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMN_ID_CATEGORIA)));
        categoria.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMN_NOME_CATEGORIA)));
        categoria.setUsuarioId(cursor.getInt(cursor.getColumnIndexOrThrow(SQLiteHelper.COLUMN_USUARIO_ID)));
        return categoria;
    }

    public void close() {
        dbHelper.close();
    }
} 
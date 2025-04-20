package com.example.myapplication.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.myapplication.helpers.SQLiteHelper;
import com.example.myapplication.models.Disciplina;

import java.util.ArrayList;
import java.util.List;

public class DisciplinaDAO extends BaseDAO {
    private static final String TAG = "DisciplinaDAO";

    public DisciplinaDAO(Context context) {
        super(context);
    }

    public long insert(Disciplina disciplina) {
        ContentValues values = new ContentValues();
        values.put("nome", disciplina.getNome());
        values.put("professor", disciplina.getProfessor());
        values.put("usuario_id", disciplina.getUsuarioId());
        values.put("periodo", disciplina.getPeriodo());
        values.put("status", disciplina.getStatus());

        return getWritableDatabase().insert("disciplinas", null, values);
    }

    public boolean update(Disciplina disciplina) {
        ContentValues values = new ContentValues();
        values.put("nome", disciplina.getNome());
        values.put("professor", disciplina.getProfessor());
        values.put("periodo", disciplina.getPeriodo());
        values.put("status", disciplina.getStatus());

        return getWritableDatabase().update("disciplinas", values,
                "id = ? AND usuario_id = ?",
                new String[]{String.valueOf(disciplina.getId()), String.valueOf(disciplina.getUsuarioId())}) > 0;
    }

    public boolean delete(int id, int usuarioId) {
        return getWritableDatabase().delete("disciplinas",
                "id = ? AND usuario_id = ?",
                new String[]{String.valueOf(id), String.valueOf(usuarioId)}) > 0;
    }

    public List<Disciplina> list(long usuarioId) {
        List<Disciplina> disciplinas = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = getReadableDatabase().rawQuery(
                    "SELECT * FROM disciplinas WHERE usuario_id = ? ORDER BY nome ASC",
                    new String[]{String.valueOf(usuarioId)});

            while (cursor.moveToNext()) {
                Disciplina disciplina = new Disciplina();
                disciplina.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                disciplina.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
                disciplina.setProfessor(cursor.getString(cursor.getColumnIndexOrThrow("professor")));
                disciplina.setUsuarioId(cursor.getInt(cursor.getColumnIndexOrThrow("usuario_id")));
                disciplina.setPeriodo(cursor.getString(cursor.getColumnIndexOrThrow("periodo")));
                disciplina.setStatus(cursor.getString(cursor.getColumnIndexOrThrow("status")));
                
                disciplinas.add(disciplina);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return disciplinas;
    }

    public Disciplina get(int id) {
        Disciplina disciplina = null;
        Cursor cursor = null;

        try {
            cursor = getReadableDatabase().rawQuery(
                    "SELECT * FROM disciplinas WHERE id = ?",
                    new String[]{String.valueOf(id)});

            if (cursor.moveToFirst()) {
                disciplina = new Disciplina();
                disciplina.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                disciplina.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
                disciplina.setProfessor(cursor.getString(cursor.getColumnIndexOrThrow("professor")));
                disciplina.setUsuarioId(cursor.getInt(cursor.getColumnIndexOrThrow("usuario_id")));
                disciplina.setPeriodo(cursor.getString(cursor.getColumnIndexOrThrow("periodo")));
                disciplina.setStatus(cursor.getString(cursor.getColumnIndexOrThrow("status")));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return disciplina;
    }
} 
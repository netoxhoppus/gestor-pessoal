package com.example.myapplication.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.myapplication.helpers.SQLiteHelper;
import com.example.myapplication.models.Entrega;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EntregaDAO extends BaseDAO {
    private static final String TAG = "EntregaDAO";

    public EntregaDAO(Context context) {
        super(context);
    }

    public long insert(Entrega entrega) {
        ContentValues values = new ContentValues();
        values.put("titulo", entrega.getTitulo());
        values.put("descricao", entrega.getDescricao());
        values.put("data_entrega", entrega.getDataEntrega().getTime());
        values.put("disciplina_id", entrega.getDisciplinaId());
        values.put("usuario_id", entrega.getUsuarioId());
        values.put("status", entrega.getStatus());
        values.put("nota", entrega.getNota());

        return getWritableDatabase().insert("entregas", null, values);
    }

    public boolean update(Entrega entrega) {
        ContentValues values = new ContentValues();
        values.put("titulo", entrega.getTitulo());
        values.put("descricao", entrega.getDescricao());
        values.put("data_entrega", entrega.getDataEntrega().getTime());
        values.put("disciplina_id", entrega.getDisciplinaId());
        values.put("status", entrega.getStatus());
        values.put("nota", entrega.getNota());

        String whereClause = "id = ? AND usuario_id = ?";
        String[] whereArgs = {String.valueOf(entrega.getId()), String.valueOf(entrega.getUsuarioId())};

        return getWritableDatabase().update("entregas", values, whereClause, whereArgs) > 0;
    }

    public boolean delete(int id, int usuarioId) {
        String whereClause = "id = ? AND usuario_id = ?";
        String[] whereArgs = {String.valueOf(id), String.valueOf(usuarioId)};

        return getWritableDatabase().delete("entregas", whereClause, whereArgs) > 0;
    }

    public List<Entrega> list(long usuarioId) {
        List<Entrega> entregas = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT e.*, d.nome as disciplina_nome " +
                          "FROM entregas e " +
                          "LEFT JOIN disciplinas d ON e.disciplina_id = d.id " +
                          "WHERE e.usuario_id = ? " +
                          "ORDER BY e.data_entrega ASC";

            cursor = db.rawQuery(query, new String[]{String.valueOf(usuarioId)});

            while (cursor.moveToNext()) {
                Entrega entrega = new Entrega();
                entrega.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                entrega.setTitulo(cursor.getString(cursor.getColumnIndexOrThrow("titulo")));
                entrega.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
                entrega.setDataEntrega(new Date(cursor.getLong(cursor.getColumnIndexOrThrow("data_entrega"))));
                entrega.setDisciplinaId(cursor.getInt(cursor.getColumnIndexOrThrow("disciplina_id")));
                entrega.setDisciplinaNome(cursor.getString(cursor.getColumnIndexOrThrow("disciplina_nome")));
                entrega.setUsuarioId(cursor.getInt(cursor.getColumnIndexOrThrow("usuario_id")));
                entrega.setStatus(cursor.getString(cursor.getColumnIndexOrThrow("status")));
                entrega.setNota(cursor.getFloat(cursor.getColumnIndexOrThrow("nota")));
                entrega.setConcluida(entrega.getStatus().equals("Concluída"));

                entregas.add(entrega);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return entregas;
    }

    public List<Entrega> listByDisciplina(long disciplinaId, long usuarioId) {
        List<Entrega> entregas = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = getReadableDatabase().rawQuery(
                    "SELECT e.*, COALESCE(d.nome, 'Disciplina removida') as disciplina_nome " +
                    "FROM entregas e " +
                    "LEFT JOIN disciplinas d ON e.disciplina_id = d.id " +
                    "WHERE e.disciplina_id = ? AND e.usuario_id = ? " +
                    "ORDER BY e.data_entrega ASC",
                    new String[]{String.valueOf(disciplinaId), String.valueOf(usuarioId)});

            while (cursor.moveToNext()) {
                try {
                    Entrega entrega = new Entrega();
                    entrega.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                    entrega.setTitulo(cursor.getString(cursor.getColumnIndexOrThrow("titulo")));
                    entrega.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
                    long timestamp = cursor.getLong(cursor.getColumnIndexOrThrow("data_entrega"));
                    entrega.setDataEntrega(new Date(timestamp));
                    entrega.setDisciplinaId(cursor.getInt(cursor.getColumnIndexOrThrow("disciplina_id")));
                    entrega.setUsuarioId(cursor.getInt(cursor.getColumnIndexOrThrow("usuario_id")));
                    entrega.setStatus(cursor.getString(cursor.getColumnIndexOrThrow("status")));
                    
                    // Tratamento para possíveis valores nulos
                    try {
                        entrega.setNota(cursor.getFloat(cursor.getColumnIndexOrThrow("nota")));
                    } catch (Exception e) {
                        entrega.setNota(0.0f);
                    }
                    
                    String disciplinaNome = cursor.getString(cursor.getColumnIndexOrThrow("disciplina_nome"));
                    entrega.setDisciplinaNome(disciplinaNome != null ? disciplinaNome : "Disciplina removida");
                    
                    entregas.add(entrega);
                } catch (Exception e) {
                    Log.e(TAG, "Erro ao processar entrega do cursor", e);
                    // Continua para a próxima entrega em caso de erro
                    continue;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Erro ao listar entregas por disciplina", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return entregas;
    }

    public Entrega get(int id) {
        Entrega entrega = null;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT e.*, d.nome as disciplina_nome " +
                          "FROM entregas e " +
                          "LEFT JOIN disciplinas d ON e.disciplina_id = d.id " +
                          "WHERE e.id = ?";

            cursor = db.rawQuery(query, new String[]{String.valueOf(id)});

            if (cursor.moveToFirst()) {
                entrega = new Entrega();
                entrega.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                entrega.setTitulo(cursor.getString(cursor.getColumnIndexOrThrow("titulo")));
                entrega.setDescricao(cursor.getString(cursor.getColumnIndexOrThrow("descricao")));
                entrega.setDataEntrega(new Date(cursor.getLong(cursor.getColumnIndexOrThrow("data_entrega"))));
                entrega.setDisciplinaId(cursor.getInt(cursor.getColumnIndexOrThrow("disciplina_id")));
                entrega.setDisciplinaNome(cursor.getString(cursor.getColumnIndexOrThrow("disciplina_nome")));
                entrega.setUsuarioId(cursor.getInt(cursor.getColumnIndexOrThrow("usuario_id")));
                entrega.setStatus(cursor.getString(cursor.getColumnIndexOrThrow("status")));
                entrega.setNota(cursor.getFloat(cursor.getColumnIndexOrThrow("nota")));
                entrega.setConcluida(entrega.getStatus().equals("Concluída"));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return entrega;
    }
} 
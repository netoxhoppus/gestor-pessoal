package com.example.myapplication.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.myapplication.models.Horario;

import java.util.ArrayList;
import java.util.List;

public class HorarioDAO extends BaseDAO {

    public HorarioDAO(Context context) {
        super(context);
    }

    public long insert(Horario horario) {
        ContentValues values = new ContentValues();
        values.put("dia_semana", horario.getDiaSemana());
        values.put("hora_inicio", horario.getHoraInicio());
        values.put("hora_fim", horario.getHoraFim());
        values.put("observacoes", horario.getObservacoes());
        if (horario.getIdAula() > 0) {
            values.put("id_aula", horario.getIdAula());
        }
        values.put("usuario_id", horario.getUsuarioId());

        return getWritableDatabase().insert("horarios", null, values);
    }

    public boolean update(Horario horario) {
        ContentValues values = new ContentValues();
        values.put("dia_semana", horario.getDiaSemana());
        values.put("hora_inicio", horario.getHoraInicio());
        values.put("hora_fim", horario.getHoraFim());
        values.put("observacoes", horario.getObservacoes());
        values.put("id_aula", horario.getIdAula());

        String whereClause = "id_horario = ?";
        String[] whereArgs = {String.valueOf(horario.getIdHorario())};

        int rowsAffected = getWritableDatabase().update("horarios", values, whereClause, whereArgs);
        return rowsAffected > 0;
    }

    public boolean delete(int idHorario) {
        String whereClause = "id_horario = ?";
        String[] whereArgs = {String.valueOf(idHorario)};

        int rowsAffected = getWritableDatabase().delete("horarios", whereClause, whereArgs);
        return rowsAffected > 0;
    }

    public List<Horario> list(long usuarioId) {
        List<Horario> horarios = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = getReadableDatabase().rawQuery(
                    "SELECT h.*, COALESCE(d.nome, 'Disciplina não definida') as disciplina_nome, " +
                    "CASE LOWER(REPLACE(REPLACE(h.dia_semana, 'ç', 'c'), 'á', 'a')) " +
                    "WHEN 'segunda' THEN 1 " +
                    "WHEN 'segunda-feira' THEN 1 " +
                    "WHEN 'terca' THEN 2 " +
                    "WHEN 'terça' THEN 2 " +
                    "WHEN 'terca-feira' THEN 2 " +
                    "WHEN 'terça-feira' THEN 2 " +
                    "WHEN 'quarta' THEN 3 " +
                    "WHEN 'quarta-feira' THEN 3 " +
                    "WHEN 'quinta' THEN 4 " +
                    "WHEN 'quinta-feira' THEN 4 " +
                    "WHEN 'sexta' THEN 5 " +
                    "WHEN 'sexta-feira' THEN 5 " +
                    "WHEN 'sabado' THEN 6 " +
                    "WHEN 'sábado' THEN 6 " +
                    "WHEN 'domingo' THEN 7 " +
                    "ELSE 8 END as ordem_dia " +
                    "FROM horarios h " +
                    "LEFT JOIN disciplinas d ON h.id_aula = d.id " +
                    "WHERE h.usuario_id = ? " +
                    "ORDER BY ordem_dia ASC, h.hora_inicio ASC",
                    new String[]{String.valueOf(usuarioId)});

            while (cursor.moveToNext()) {
                try {
                    Horario horario = new Horario();
                    horario.setIdHorario(cursor.getInt(cursor.getColumnIndexOrThrow("id_horario")));
                    horario.setDiaSemana(cursor.getString(cursor.getColumnIndexOrThrow("dia_semana")));
                    horario.setHoraInicio(cursor.getString(cursor.getColumnIndexOrThrow("hora_inicio")));
                    horario.setHoraFim(cursor.getString(cursor.getColumnIndexOrThrow("hora_fim")));
                    
                    // Tratamento para observações nulas
                    int observacoesIndex = cursor.getColumnIndex("observacoes");
                    if (!cursor.isNull(observacoesIndex)) {
                        horario.setObservacoes(cursor.getString(observacoesIndex));
                    } else {
                        horario.setObservacoes("");
                    }
                    
                    horario.setIdAula(cursor.getInt(cursor.getColumnIndexOrThrow("id_aula")));
                    horario.setUsuarioId(cursor.getInt(cursor.getColumnIndexOrThrow("usuario_id")));
                    
                    // Tratamento para nome da disciplina nulo
                    String disciplinaNome = cursor.getString(cursor.getColumnIndexOrThrow("disciplina_nome"));
                    horario.setDisciplinaNome(disciplinaNome != null ? disciplinaNome : "Disciplina não definida");
                    
                    horarios.add(horario);
                } catch (Exception e) {
                    android.util.Log.e("HorarioDAO", "Erro ao processar horário do cursor", e);
                    continue;
                }
            }
        } catch (Exception e) {
            android.util.Log.e("HorarioDAO", "Erro ao listar horários", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return horarios;
    }

    public Horario get(int idHorario) {
        Horario horario = null;
        Cursor cursor = null;

        try {
            cursor = getReadableDatabase().rawQuery(
                    "SELECT h.*, d.nome as disciplina_nome " +
                    "FROM horarios h " +
                    "INNER JOIN disciplinas d ON h.id_aula = d.id " +
                    "WHERE h.id_horario = ?",
                    new String[]{String.valueOf(idHorario)});

            if (cursor.moveToFirst()) {
                horario = new Horario();
                horario.setIdHorario(cursor.getInt(cursor.getColumnIndexOrThrow("id_horario")));
                horario.setDiaSemana(cursor.getString(cursor.getColumnIndexOrThrow("dia_semana")));
                horario.setHoraInicio(cursor.getString(cursor.getColumnIndexOrThrow("hora_inicio")));
                horario.setHoraFim(cursor.getString(cursor.getColumnIndexOrThrow("hora_fim")));
                horario.setObservacoes(cursor.getString(cursor.getColumnIndexOrThrow("observacoes")));
                horario.setIdAula(cursor.getInt(cursor.getColumnIndexOrThrow("id_aula")));
                horario.setUsuarioId(cursor.getInt(cursor.getColumnIndexOrThrow("usuario_id")));
                horario.setDisciplinaNome(cursor.getString(cursor.getColumnIndexOrThrow("disciplina_nome")));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return horario;
    }
} 
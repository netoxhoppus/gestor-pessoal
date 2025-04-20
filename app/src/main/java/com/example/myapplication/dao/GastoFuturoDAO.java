package com.example.myapplication.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.myapplication.models.GastoFuturo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GastoFuturoDAO extends BaseDAO {
    private static final String TAG = "GastoFuturoDAO";
    
    public GastoFuturoDAO(Context context) {
        super(context);
    }

    public long insert(GastoFuturo gastoFuturo) {
        ContentValues values = new ContentValues();
        values.put("descricao", gastoFuturo.getDescricao());
        values.put("valor", gastoFuturo.getValor());
        values.put("data_vencimento", gastoFuturo.getDataVencimento().getTime());
        values.put("categoria_id", gastoFuturo.getCategoriaId());
        values.put("usuario_id", gastoFuturo.getUsuarioId());
        values.put("status", gastoFuturo.getStatus());
        values.put("recorrente", gastoFuturo.isRecorrente() ? 1 : 0);
        values.put("periodo_recorrencia", gastoFuturo.getPeriodoRecorrencia());

        long id = getWritableDatabase().insert("gastos_futuros", null, values);
        Log.d(TAG, "insert: ID gerado = " + id + ", dados = " + gastoFuturo.toString());
        return id;
    }

    public boolean update(GastoFuturo gastoFuturo) {
        ContentValues values = new ContentValues();
        values.put("descricao", gastoFuturo.getDescricao());
        values.put("valor", gastoFuturo.getValor());
        values.put("data_vencimento", gastoFuturo.getDataVencimento().getTime());
        values.put("categoria_id", gastoFuturo.getCategoriaId());
        values.put("usuario_id", gastoFuturo.getUsuarioId());
        values.put("status", gastoFuturo.getStatus());
        values.put("recorrente", gastoFuturo.isRecorrente() ? 1 : 0);
        values.put("periodo_recorrencia", gastoFuturo.getPeriodoRecorrencia());

        return getWritableDatabase().update("gastos_futuros", values,
                "id = ?", new String[]{String.valueOf(gastoFuturo.getId())}) > 0;
    }

    public boolean delete(int id) {
        return getWritableDatabase().delete("gastos_futuros",
                "id = ?", new String[]{String.valueOf(id)}) > 0;
    }

    public List<GastoFuturo> list(long usuarioId) {
        List<GastoFuturo> gastosFuturos = new ArrayList<>();
        Cursor cursor = null;
        SQLiteDatabase db = null;
        
        try {
            db = getReadableDatabase();
            Log.d(TAG, "list: Iniciando busca para usuarioId = " + usuarioId);
            
            // Primeiro, vamos verificar se a tabela existe e tem registros
            Cursor checkTable = db.rawQuery(
                "SELECT COUNT(*) FROM gastos_futuros WHERE usuario_id = ?",
                new String[]{String.valueOf(usuarioId)});
            
            if (checkTable.moveToFirst()) {
                int count = checkTable.getInt(0);
                Log.d(TAG, "list: Total de registros encontrados: " + count);
            }
            checkTable.close();
            
            cursor = db.rawQuery(
                    "SELECT g.*, c.nome_categoria as nome_categoria " +
                    "FROM gastos_futuros g " +
                    "LEFT JOIN categoriagastos c ON g.categoria_id = c.id_categoria " +
                    "WHERE g.usuario_id = ? " +
                    "ORDER BY g.data_vencimento ASC", 
                    new String[]{String.valueOf(usuarioId)});

            Log.d(TAG, "list: Query executada, cursor tem " + 
                  (cursor != null ? cursor.getCount() : 0) + " registros");

            if (cursor != null) {
                int idIndex = cursor.getColumnIndex("id");
                int descricaoIndex = cursor.getColumnIndex("descricao");
                int valorIndex = cursor.getColumnIndex("valor");
                int dataVencimentoIndex = cursor.getColumnIndex("data_vencimento");
                int categoriaIdIndex = cursor.getColumnIndex("categoria_id");
                int usuarioIdIndex = cursor.getColumnIndex("usuario_id");
                int nomeCategoriaIndex = cursor.getColumnIndex("nome_categoria");
                int statusIndex = cursor.getColumnIndex("status");
                int recorrenteIndex = cursor.getColumnIndex("recorrente");
                int periodoRecorrenciaIndex = cursor.getColumnIndex("periodo_recorrencia");

                Log.d(TAG, "list: Índices das colunas: id=" + idIndex + 
                      ", descricao=" + descricaoIndex + 
                      ", valor=" + valorIndex);

                while (cursor.moveToNext()) {
                    GastoFuturo gastoFuturo = new GastoFuturo();
                    
                    if (idIndex >= 0) gastoFuturo.setId(cursor.getInt(idIndex));
                    if (descricaoIndex >= 0) gastoFuturo.setDescricao(cursor.getString(descricaoIndex));
                    if (valorIndex >= 0) gastoFuturo.setValor(cursor.getDouble(valorIndex));
                    if (dataVencimentoIndex >= 0) {
                        long timestamp = cursor.getLong(dataVencimentoIndex);
                        gastoFuturo.setDataVencimento(new Date(timestamp));
                    }
                    if (categoriaIdIndex >= 0) gastoFuturo.setCategoriaId(cursor.getInt(categoriaIdIndex));
                    if (usuarioIdIndex >= 0) gastoFuturo.setUsuarioId(cursor.getInt(usuarioIdIndex));
                    if (nomeCategoriaIndex >= 0) gastoFuturo.setNomeCategoria(cursor.getString(nomeCategoriaIndex));
                    if (statusIndex >= 0) gastoFuturo.setStatus(cursor.getString(statusIndex));
                    if (recorrenteIndex >= 0) gastoFuturo.setRecorrente(cursor.getInt(recorrenteIndex) == 1);
                    if (periodoRecorrenciaIndex >= 0) gastoFuturo.setPeriodoRecorrencia(cursor.getString(periodoRecorrenciaIndex));
                    
                    gastosFuturos.add(gastoFuturo);
                    Log.d(TAG, "list: Gasto encontrado: " + gastoFuturo.toString());
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "list: Erro ao buscar gastos futuros", e);
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        Log.d(TAG, "list: Total de gastos retornados: " + gastosFuturos.size());
        return gastosFuturos;
    }

    public GastoFuturo get(int id) {
        GastoFuturo gastoFuturo = null;
        Cursor cursor = null;
        
        try {
            cursor = getReadableDatabase().rawQuery(
                    "SELECT g.*, c.nome_categoria as nome_categoria " +
                    "FROM gastos_futuros g " +
                    "LEFT JOIN categoriagastos c ON g.categoria_id = c.id_categoria " +
                    "WHERE g.id = ?",
                    new String[]{String.valueOf(id)});

            if (cursor != null && cursor.moveToFirst()) {
                gastoFuturo = new GastoFuturo();
                
                int idIndex = cursor.getColumnIndex("id");
                int descricaoIndex = cursor.getColumnIndex("descricao");
                int valorIndex = cursor.getColumnIndex("valor");
                int dataVencimentoIndex = cursor.getColumnIndex("data_vencimento");
                int categoriaIdIndex = cursor.getColumnIndex("categoria_id");
                int usuarioIdIndex = cursor.getColumnIndex("usuario_id");
                int nomeCategoriaIndex = cursor.getColumnIndex("nome_categoria");
                int statusIndex = cursor.getColumnIndex("status");
                int recorrenteIndex = cursor.getColumnIndex("recorrente");
                int periodoRecorrenciaIndex = cursor.getColumnIndex("periodo_recorrencia");

                if (idIndex >= 0) gastoFuturo.setId(cursor.getInt(idIndex));
                if (descricaoIndex >= 0) gastoFuturo.setDescricao(cursor.getString(descricaoIndex));
                if (valorIndex >= 0) gastoFuturo.setValor(cursor.getDouble(valorIndex));
                if (dataVencimentoIndex >= 0) {
                    long timestamp = cursor.getLong(dataVencimentoIndex);
                    gastoFuturo.setDataVencimento(new Date(timestamp));
                }
                if (categoriaIdIndex >= 0) gastoFuturo.setCategoriaId(cursor.getInt(categoriaIdIndex));
                if (usuarioIdIndex >= 0) gastoFuturo.setUsuarioId(cursor.getInt(usuarioIdIndex));
                if (nomeCategoriaIndex >= 0) gastoFuturo.setNomeCategoria(cursor.getString(nomeCategoriaIndex));
                if (statusIndex >= 0) gastoFuturo.setStatus(cursor.getString(statusIndex));
                if (recorrenteIndex >= 0) gastoFuturo.setRecorrente(cursor.getInt(recorrenteIndex) == 1);
                if (periodoRecorrenciaIndex >= 0) gastoFuturo.setPeriodoRecorrencia(cursor.getString(periodoRecorrenciaIndex));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return gastoFuturo;
    }

    public List<GastoFuturo> listByStatus(long usuarioId, String status) {
        List<GastoFuturo> gastosFuturos = new ArrayList<>();
        Cursor cursor = null;
        
        try {
            cursor = getReadableDatabase().rawQuery(
                    "SELECT g.*, c.nome_categoria as nome_categoria " +
                    "FROM gastos_futuros g " +
                    "LEFT JOIN categoriagastos c ON g.categoria_id = c.id_categoria " +
                    "WHERE usuario_id = ? AND g.status = ? " +
                    "ORDER BY g.data_vencimento ASC", 
                    new String[]{String.valueOf(usuarioId), status});

            if (cursor != null) {
                int idIndex = cursor.getColumnIndex("id");
                int descricaoIndex = cursor.getColumnIndex("descricao");
                int valorIndex = cursor.getColumnIndex("valor");
                int dataVencimentoIndex = cursor.getColumnIndex("data_vencimento");
                int categoriaIdIndex = cursor.getColumnIndex("categoria_id");
                int usuarioIdIndex = cursor.getColumnIndex("usuario_id");
                int nomeCategoriaIndex = cursor.getColumnIndex("nome_categoria");
                int statusIndex = cursor.getColumnIndex("status");
                int recorrenteIndex = cursor.getColumnIndex("recorrente");
                int periodoRecorrenciaIndex = cursor.getColumnIndex("periodo_recorrencia");

                while (cursor.moveToNext()) {
                    GastoFuturo gastoFuturo = new GastoFuturo();
                    
                    if (idIndex >= 0) gastoFuturo.setId(cursor.getInt(idIndex));
                    if (descricaoIndex >= 0) gastoFuturo.setDescricao(cursor.getString(descricaoIndex));
                    if (valorIndex >= 0) gastoFuturo.setValor(cursor.getDouble(valorIndex));
                    if (dataVencimentoIndex >= 0) {
                        long timestamp = cursor.getLong(dataVencimentoIndex);
                        gastoFuturo.setDataVencimento(new Date(timestamp));
                    }
                    if (categoriaIdIndex >= 0) gastoFuturo.setCategoriaId(cursor.getInt(categoriaIdIndex));
                    if (usuarioIdIndex >= 0) gastoFuturo.setUsuarioId(cursor.getInt(usuarioIdIndex));
                    if (nomeCategoriaIndex >= 0) gastoFuturo.setNomeCategoria(cursor.getString(nomeCategoriaIndex));
                    if (statusIndex >= 0) gastoFuturo.setStatus(cursor.getString(statusIndex));
                    if (recorrenteIndex >= 0) gastoFuturo.setRecorrente(cursor.getInt(recorrenteIndex) == 1);
                    if (periodoRecorrenciaIndex >= 0) gastoFuturo.setPeriodoRecorrencia(cursor.getString(periodoRecorrenciaIndex));
                    
                    gastosFuturos.add(gastoFuturo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return gastosFuturos;
    }
} 
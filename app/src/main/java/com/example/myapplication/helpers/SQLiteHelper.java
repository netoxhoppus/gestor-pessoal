package com.example.myapplication.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String TAG = "SQLiteHelper";
    private static final String DATABASE_NAME = "gestor_pessoal.db";
    private static final int DATABASE_VERSION = 6;  // Incrementado para recriar a tabela entregas
    private static int currentUserId = 1; // Default user ID

    // Tabela de usuários
    public static final String TABLE_USUARIOS = "usuarios";

    // Tabela de categorias
    public static final String TABLE_CATEGORIAS = "categoriagastos";
    public static final String COLUMN_ID_CATEGORIA = "id_categoria";
    public static final String COLUMN_NOME_CATEGORIA = "nome_categoria";
    public static final String COLUMN_USUARIO_ID = "usuario_id";

    // Tabela de gastos
    public static final String TABLE_GASTO = "gastos";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DESCRICAO = "descricao";
    public static final String COLUMN_VALOR = "valor";
    public static final String COLUMN_DATA = "data";
    public static final String COLUMN_CATEGORIA_ID = "categoria_id";

    // Tabela de gastos futuros
    public static final String TABLE_GASTOS_FUTUROS = "gastos_futuros";
    public static final String COLUMN_DATA_VENCIMENTO = "data_vencimento";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_RECORRENTE = "recorrente";
    public static final String COLUMN_PERIODO_RECORRENCIA = "periodo_recorrencia";

    // Tabela de disciplinas
    public static final String TABLE_DISCIPLINAS = "disciplinas";
    public static final String COLUMN_PROFESSOR = "professor";
    public static final String COLUMN_NOME = "nome";
    public static final String COLUMN_PERIODO = "periodo";

    // Tabela de horários
    public static final String TABLE_HORARIOS = "horarios";
    public static final String COLUMN_ID_HORARIO = "id_horario";
    public static final String COLUMN_DIA_SEMANA = "dia_semana";
    public static final String COLUMN_HORA_INICIO = "hora_inicio";
    public static final String COLUMN_HORA_FIM = "hora_fim";
    public static final String COLUMN_OBSERVACOES = "observacoes";
    public static final String COLUMN_ID_AULA = "id_aula";

    // Tabela de entregas
    public static final String TABLE_ENTREGAS = "entregas";
    public static final String COLUMN_TITULO = "titulo";
    public static final String COLUMN_DISCIPLINA_ID = "disciplina_id";
    public static final String COLUMN_DATA_ENTREGA = "data_entrega";
    public static final String COLUMN_CONCLUIDA = "concluida";

    // SQL de criação da tabela de usuários
    private static final String SQL_CREATE_USUARIOS = "CREATE TABLE " + TABLE_USUARIOS + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "nome TEXT NOT NULL, " +
            "email TEXT NOT NULL UNIQUE, " +
            "senha TEXT NOT NULL)";

    // SQL de criação da tabela de categorias
    private static final String SQL_CREATE_CATEGORIAS =
            "CREATE TABLE " + TABLE_CATEGORIAS + " (" +
                    COLUMN_ID_CATEGORIA + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NOME_CATEGORIA + " TEXT NOT NULL, " +
                    COLUMN_USUARIO_ID + " INTEGER NOT NULL, " +
                    "FOREIGN KEY(" + COLUMN_USUARIO_ID + ") REFERENCES " + 
                    TABLE_USUARIOS + "(" + COLUMN_ID + "))";

    // SQL de criação da tabela de gastos
    private static final String SQL_CREATE_GASTO =
            "CREATE TABLE " + TABLE_GASTO + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_DESCRICAO + " TEXT NOT NULL, " +
                    COLUMN_VALOR + " REAL NOT NULL, " +
                    COLUMN_DATA + " TEXT NOT NULL, " +
                    COLUMN_CATEGORIA_ID + " INTEGER NOT NULL, " +
                    COLUMN_USUARIO_ID + " INTEGER NOT NULL, " +
                    "FOREIGN KEY(" + COLUMN_CATEGORIA_ID + ") REFERENCES " + 
                    TABLE_CATEGORIAS + "(" + COLUMN_ID_CATEGORIA + "), " +
                    "FOREIGN KEY(" + COLUMN_USUARIO_ID + ") REFERENCES " + 
                    TABLE_USUARIOS + "(" + COLUMN_ID + "))";

    // SQL de criação da tabela de gastos futuros
    private static final String SQL_CREATE_GASTOS_FUTUROS =
            "CREATE TABLE " + TABLE_GASTOS_FUTUROS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_DESCRICAO + " TEXT NOT NULL, " +
                    COLUMN_VALOR + " REAL NOT NULL, " +
                    COLUMN_DATA_VENCIMENTO + " INTEGER NOT NULL, " +
                    COLUMN_CATEGORIA_ID + " INTEGER NOT NULL, " +
                    COLUMN_USUARIO_ID + " INTEGER NOT NULL, " +
                    COLUMN_STATUS + " TEXT NOT NULL, " +
                    COLUMN_RECORRENTE + " INTEGER NOT NULL DEFAULT 0, " +
                    COLUMN_PERIODO_RECORRENCIA + " TEXT, " +
                    "FOREIGN KEY(" + COLUMN_CATEGORIA_ID + ") REFERENCES " + 
                    TABLE_CATEGORIAS + "(" + COLUMN_ID_CATEGORIA + "), " +
                    "FOREIGN KEY(" + COLUMN_USUARIO_ID + ") REFERENCES " + 
                    TABLE_USUARIOS + "(" + COLUMN_ID + "))";

    // SQL de criação da tabela de disciplinas
    private static final String SQL_CREATE_DISCIPLINAS = "CREATE TABLE " + TABLE_DISCIPLINAS + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NOME + " TEXT NOT NULL, " +
            COLUMN_PROFESSOR + " TEXT NOT NULL, " +
            COLUMN_PERIODO + " TEXT NOT NULL, " +
            COLUMN_STATUS + " TEXT NOT NULL DEFAULT 'Ativa', " +
            COLUMN_USUARIO_ID + " INTEGER NOT NULL, " +
            "FOREIGN KEY(" + COLUMN_USUARIO_ID + ") REFERENCES " + 
            TABLE_USUARIOS + "(" + COLUMN_ID + "))";

    // SQL de criação da tabela de horários
    private static final String SQL_CREATE_HORARIOS = "CREATE TABLE " + TABLE_HORARIOS + " (" +
            COLUMN_ID_HORARIO + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_DIA_SEMANA + " TEXT NOT NULL, " +
            COLUMN_HORA_INICIO + " TEXT NOT NULL, " +
            COLUMN_HORA_FIM + " TEXT NOT NULL, " +
            COLUMN_OBSERVACOES + " TEXT, " +
            COLUMN_ID_AULA + " INTEGER, " +
            COLUMN_USUARIO_ID + " INTEGER NOT NULL, " +
            "FOREIGN KEY(" + COLUMN_ID_AULA + ") REFERENCES " + 
            TABLE_DISCIPLINAS + "(" + COLUMN_ID + "), " +
            "FOREIGN KEY(" + COLUMN_USUARIO_ID + ") REFERENCES " + 
            TABLE_USUARIOS + "(" + COLUMN_ID + "))";

    // SQL de criação da tabela de entregas
    private static final String SQL_CREATE_ENTREGAS = "CREATE TABLE " + TABLE_ENTREGAS + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TITULO + " TEXT NOT NULL, " +
            COLUMN_DESCRICAO + " TEXT NOT NULL, " +
            COLUMN_DATA_ENTREGA + " INTEGER NOT NULL, " +
            COLUMN_DISCIPLINA_ID + " INTEGER NOT NULL, " +
            COLUMN_USUARIO_ID + " INTEGER NOT NULL, " +
            COLUMN_STATUS + " TEXT NOT NULL DEFAULT 'Pendente', " +
            "nota REAL DEFAULT 0, " +
            "FOREIGN KEY(" + COLUMN_DISCIPLINA_ID + ") REFERENCES " + 
            TABLE_DISCIPLINAS + "(" + COLUMN_ID + "), " +
            "FOREIGN KEY(" + COLUMN_USUARIO_ID + ") REFERENCES " + 
            TABLE_USUARIOS + "(" + COLUMN_ID + "))";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(SQL_CREATE_USUARIOS);
            Log.d(TAG, "Tabela de usuários criada com sucesso");
            
            db.execSQL(SQL_CREATE_CATEGORIAS);
            Log.d(TAG, "Tabela de categorias criada com sucesso");
            
            db.execSQL(SQL_CREATE_GASTO);
            Log.d(TAG, "Tabela de gastos criada com sucesso");

            db.execSQL(SQL_CREATE_GASTOS_FUTUROS);
            Log.d(TAG, "Tabela de gastos futuros criada com sucesso");

            db.execSQL(SQL_CREATE_DISCIPLINAS);
            Log.d(TAG, "Tabela de disciplinas criada com sucesso");

            db.execSQL(SQL_CREATE_HORARIOS);
            Log.d(TAG, "Tabela de horários criada com sucesso");

            db.execSQL(SQL_CREATE_ENTREGAS);
            Log.d(TAG, "Tabela de entregas criada com sucesso");
        } catch (Exception e) {
            Log.e(TAG, "Erro ao criar tabelas", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            Log.d(TAG, "Atualizando banco de dados da versão " + oldVersion + " para " + newVersion);
            
            if (oldVersion < 2) {
                // Adiciona a tabela de entregas
                db.execSQL(SQL_CREATE_ENTREGAS);
            }

            if (oldVersion < 3) {
                // Recria a tabela de disciplinas sem as colunas sala e horario
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_DISCIPLINAS);
                db.execSQL(SQL_CREATE_DISCIPLINAS);
            }

            if (oldVersion < 4) {
                // Adiciona a tabela de horários
                db.execSQL(SQL_CREATE_HORARIOS);
            }

            if (oldVersion < 5) {
                // Recria a tabela de horários para garantir que está atualizada
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_HORARIOS);
                db.execSQL(SQL_CREATE_HORARIOS);
            }

            if (oldVersion < 6) {
                // Recria a tabela de entregas com as novas colunas
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTREGAS);
                db.execSQL(SQL_CREATE_ENTREGAS);
            }
            
            Log.d(TAG, "Atualização do banco de dados concluída com sucesso");
        } catch (Exception e) {
            Log.e(TAG, "Erro ao atualizar banco de dados", e);
        }
    }

    public static int getCurrentUserId() {
        return currentUserId;
    }

    public static void setCurrentUserId(int userId) {
        currentUserId = userId;
    }
} 
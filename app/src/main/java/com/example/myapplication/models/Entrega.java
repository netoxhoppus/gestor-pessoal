package com.example.myapplication.models;

import java.io.Serializable;
import java.util.Date;

public class Entrega implements Serializable {
    private int id;
    private String titulo;
    private String descricao;
    private Date dataEntrega;
    private int disciplinaId;
    private String disciplinaNome;
    private int usuarioId;
    private String status;
    private boolean concluida;
    private float nota;

    public Entrega() {
        this.status = "Pendente";
        this.concluida = false;
        this.nota = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(Date dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    public String getDataEntregaFormatada() {
        if (dataEntrega == null) return "";
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy", new java.util.Locale("pt", "BR"));
        return sdf.format(dataEntrega);
    }

    public void setDataEntrega(String dataStr) {
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy", new java.util.Locale("pt", "BR"));
            this.dataEntrega = sdf.parse(dataStr);
        } catch (Exception e) {
            this.dataEntrega = new Date();
        }
    }

    public int getDisciplinaId() {
        return disciplinaId;
    }

    public void setDisciplinaId(int disciplinaId) {
        this.disciplinaId = disciplinaId;
    }

    public String getDisciplinaNome() {
        return disciplinaNome;
    }

    public void setDisciplinaNome(String disciplinaNome) {
        this.disciplinaNome = disciplinaNome;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isConcluida() {
        return concluida;
    }

    public void setConcluida(boolean concluida) {
        this.concluida = concluida;
        this.status = concluida ? "Concluída" : "Pendente";
    }

    public float getNota() {
        return nota;
    }

    public void setNota(float nota) {
        this.nota = nota;
    }

    @Override
    public String toString() {
        return titulo;
    }
} 
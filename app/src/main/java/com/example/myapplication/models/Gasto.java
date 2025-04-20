package com.example.myapplication.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Gasto implements Serializable {
    private int id;
    private String descricao;
    private double valor;
    private Date data;
    private int categoriaId;
    private int usuarioId;
    private String nomeCategoria;

    // Construtores
    public Gasto() {}

    public Gasto(int id, String descricao, double valor, Date data, int categoriaId, int usuarioId) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
        this.data = data;
        this.categoriaId = categoriaId;
        this.usuarioId = usuarioId;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    // Sobrecarga dos métodos de data para aceitar String
    public String getDataFormatada() {
        if (data == null) return "";
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy", new java.util.Locale("pt", "BR"));
        return sdf.format(data);
    }

    public void setData(String dataStr) {
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy", new java.util.Locale("pt", "BR"));
            this.data = sdf.parse(dataStr);
        } catch (Exception e) {
            this.data = new Date(); // Em caso de erro, usa a data atual
        }
    }

    public int getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(int categoriaId) {
        this.categoriaId = categoriaId;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }
} 
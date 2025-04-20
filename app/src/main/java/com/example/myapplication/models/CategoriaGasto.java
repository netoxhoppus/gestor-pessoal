package com.example.myapplication.models;

import java.io.Serializable;

public class CategoriaGasto implements Serializable {
    private int idCategoria;
    private String nomeCategoria;
    private int usuarioId;

    public CategoriaGasto() {
    }

    public CategoriaGasto(int idCategoria, String nomeCategoria, int usuarioId) {
        this.idCategoria = idCategoria;
        this.nomeCategoria = nomeCategoria;
        this.usuarioId = usuarioId;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    // Adicionando getters/setters alternativos para manter compatibilidade
    public String getDescricao() {
        return getNomeCategoria();
    }

    public void setDescricao(String descricao) {
        setNomeCategoria(descricao);
    }

    @Override
    public String toString() {
        return nomeCategoria;
    }
} 
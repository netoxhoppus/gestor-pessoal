package com.example.myapplication.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class GastoFuturo implements Serializable {
    private int id;
    private String descricao;
    private double valor;
    private Date dataVencimento;
    private int categoriaId;
    private int usuarioId;
    private String nomeCategoria;
    private String status;
    private boolean recorrente;
    private String periodoRecorrencia;

    public GastoFuturo() {
    }

    public GastoFuturo(int id, String descricao, double valor, Date dataVencimento, int categoriaId, int usuarioId, String status) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
        this.dataVencimento = dataVencimento;
        this.categoriaId = categoriaId;
        this.usuarioId = usuarioId;
        this.status = status;
    }

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

    public Date getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    // Sobrecarga dos métodos de data para aceitar String
    public String getDataVencimentoFormatada() {
        if (dataVencimento == null) return "";
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy", new java.util.Locale("pt", "BR"));
        return sdf.format(dataVencimento);
    }

    public void setDataVencimento(String dataStr) {
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy", new java.util.Locale("pt", "BR"));
            this.dataVencimento = sdf.parse(dataStr);
        } catch (Exception e) {
            this.dataVencimento = new Date(); // Em caso de erro, usa a data atual
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isRecorrente() {
        return recorrente;
    }

    public void setRecorrente(boolean recorrente) {
        this.recorrente = recorrente;
    }

    public String getPeriodoRecorrencia() {
        return periodoRecorrencia;
    }

    public void setPeriodoRecorrencia(String periodoRecorrencia) {
        this.periodoRecorrencia = periodoRecorrencia;
    }

    @Override
    public String toString() {
        return "GastoFuturo{" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                ", valor=" + valor +
                ", dataVencimento=" + dataVencimento +
                ", categoriaId=" + categoriaId +
                ", usuarioId=" + usuarioId +
                ", nomeCategoria='" + nomeCategoria + '\'' +
                ", status='" + status + '\'' +
                ", recorrente=" + recorrente +
                ", periodoRecorrencia='" + periodoRecorrencia + '\'' +
                '}';
    }
} 
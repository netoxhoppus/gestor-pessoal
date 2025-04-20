package com.example.myapplication.models;

import java.math.BigDecimal;
import java.util.Date;

public class ContaPagar {
    private int idConta;
    private Date dataVencimento;
    private String descricao;
    private BigDecimal valor;
    private boolean pago;
    private String observacoes;
    private int idCategoria;
    private int usuarioId;

    // Construtores
    public ContaPagar() {}

    public ContaPagar(int idConta, Date dataVencimento, String descricao, BigDecimal valor,
                     boolean pago, String observacoes, int idCategoria, int usuarioId) {
        this.idConta = idConta;
        this.dataVencimento = dataVencimento;
        this.descricao = descricao;
        this.valor = valor;
        this.pago = pago;
        this.observacoes = observacoes;
        this.idCategoria = idCategoria;
        this.usuarioId = usuarioId;
    }

    // Getters e Setters
    public int getIdConta() {
        return idConta;
    }

    public void setIdConta(int idConta) {
        this.idConta = idConta;
    }

    public Date getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public boolean isPago() {
        return pago;
    }

    public void setPago(boolean pago) {
        this.pago = pago;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }
} 
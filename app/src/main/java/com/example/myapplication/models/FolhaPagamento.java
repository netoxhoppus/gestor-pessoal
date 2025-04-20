package com.example.myapplication.models;

import java.math.BigDecimal;
import java.util.Date;

public class FolhaPagamento {
    private int id;
    private Date mesReferencia;
    private BigDecimal salarioBruto;
    private BigDecimal faltas;
    private BigDecimal val;
    private BigDecimal descontos;
    private BigDecimal salarioLiquido;
    private int usuarioId;

    // Construtores
    public FolhaPagamento() {}

    public FolhaPagamento(int id, Date mesReferencia, BigDecimal salarioBruto, BigDecimal faltas,
                         BigDecimal val, BigDecimal descontos, BigDecimal salarioLiquido, int usuarioId) {
        this.id = id;
        this.mesReferencia = mesReferencia;
        this.salarioBruto = salarioBruto;
        this.faltas = faltas;
        this.val = val;
        this.descontos = descontos;
        this.salarioLiquido = salarioLiquido;
        this.usuarioId = usuarioId;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getMesReferencia() {
        return mesReferencia;
    }

    public void setMesReferencia(Date mesReferencia) {
        this.mesReferencia = mesReferencia;
    }

    public BigDecimal getSalarioBruto() {
        return salarioBruto;
    }

    public void setSalarioBruto(BigDecimal salarioBruto) {
        this.salarioBruto = salarioBruto;
    }

    public BigDecimal getFaltas() {
        return faltas;
    }

    public void setFaltas(BigDecimal faltas) {
        this.faltas = faltas;
    }

    public BigDecimal getVal() {
        return val;
    }

    public void setVal(BigDecimal val) {
        this.val = val;
    }

    public BigDecimal getDescontos() {
        return descontos;
    }

    public void setDescontos(BigDecimal descontos) {
        this.descontos = descontos;
    }

    public BigDecimal getSalarioLiquido() {
        return salarioLiquido;
    }

    public void setSalarioLiquido(BigDecimal salarioLiquido) {
        this.salarioLiquido = salarioLiquido;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }
} 
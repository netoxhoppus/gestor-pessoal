package com.example.myapplication.models;

import java.io.Serializable;

public class Horario implements Serializable {
    private int idHorario;
    private String diaSemana;
    private String horaInicio;
    private String horaFim;
    private String observacoes;
    private int idAula;
    private int usuarioId;
    private String disciplinaNome; // Nome da disciplina (transient)

    // Construtores
    public Horario() {}

    public Horario(int idHorario, String diaSemana, String horaInicio, String horaFim, 
                  String observacoes, int idAula, int usuarioId) {
        this.idHorario = idHorario;
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.observacoes = observacoes;
        this.idAula = idAula;
        this.usuarioId = usuarioId;
    }

    // Getters e Setters
    public int getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(int idHorario) {
        this.idHorario = idHorario;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(String horaFim) {
        this.horaFim = horaFim;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public int getIdAula() {
        return idAula;
    }

    public void setIdAula(int idAula) {
        this.idAula = idAula;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getDisciplinaNome() {
        return disciplinaNome;
    }

    public void setDisciplinaNome(String disciplinaNome) {
        this.disciplinaNome = disciplinaNome;
    }
} 
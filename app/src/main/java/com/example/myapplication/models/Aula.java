package com.example.myapplication.models;

public class Aula {
    private int idAula;
    private String nomeDisciplina;
    private String professor;
    private String observacoes;
    private int usuarioId;

    // Construtores
    public Aula() {}

    public Aula(int idAula, String nomeDisciplina, String professor, String observacoes, int usuarioId) {
        this.idAula = idAula;
        this.nomeDisciplina = nomeDisciplina;
        this.professor = professor;
        this.observacoes = observacoes;
        this.usuarioId = usuarioId;
    }

    // Getters e Setters
    public int getIdAula() {
        return idAula;
    }

    public void setIdAula(int idAula) {
        this.idAula = idAula;
    }

    public String getNomeDisciplina() {
        return nomeDisciplina;
    }

    public void setNomeDisciplina(String nomeDisciplina) {
        this.nomeDisciplina = nomeDisciplina;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }
} 
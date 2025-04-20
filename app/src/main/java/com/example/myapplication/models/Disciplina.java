package com.example.myapplication.models;

import java.io.Serializable;
import java.util.Date;

public class Disciplina implements Serializable {
    private int id;
    private String nome;
    private String professor;
    private int usuarioId;
    private String periodo;
    private String status;

    public Disciplina() {
    }

    public Disciplina(String nome, String professor, String periodo, long usuarioId) {
        this.nome = nome;
        this.professor = professor;
        this.periodo = periodo;
        this.usuarioId = (int) usuarioId;
        this.status = "Ativa"; // Default status for new disciplines
    }

    public Disciplina(int id, String nome, String professor, int usuarioId, String periodo, String status) {
        this.id = id;
        this.nome = nome;
        this.professor = professor;
        this.usuarioId = usuarioId;
        this.periodo = periodo;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return nome;
    }
} 
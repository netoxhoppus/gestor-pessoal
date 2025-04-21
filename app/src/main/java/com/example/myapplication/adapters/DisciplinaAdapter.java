package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.Disciplina;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class DisciplinaAdapter extends RecyclerView.Adapter<DisciplinaAdapter.ViewHolder> {

    private List<Disciplina> disciplinas;
    private OnDisciplinaClickListener listener;

    public interface OnDisciplinaClickListener {
        void onDisciplinaClick(Disciplina disciplina);
        void onDisciplinaLongClick(Disciplina disciplina);
    }

    public DisciplinaAdapter(List<Disciplina> disciplinas) {
        this.disciplinas = disciplinas;
    }

    public void setDisciplinas(List<Disciplina> disciplinas) {
        this.disciplinas = disciplinas;
        notifyDataSetChanged();
    }

    public void setOnDisciplinaClickListener(OnDisciplinaClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_disciplina, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Disciplina disciplina = disciplinas.get(position);
        holder.textViewNome.setText(disciplina.getNome());
        holder.textViewProfessor.setText(disciplina.getProfessor());
        holder.textViewNota.setText(String.format("%sº Período", disciplina.getPeriodo()));

        holder.cardView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDisciplinaClick(disciplina);
            }
        });

        holder.cardView.setOnLongClickListener(v -> {
            if (listener != null) {
                listener.onDisciplinaLongClick(disciplina);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return disciplinas.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardView;
        TextView textViewNome;
        TextView textViewProfessor;
        TextView textViewNota;

        ViewHolder(View itemView) {
            super(itemView);
            cardView = (MaterialCardView) itemView;
            textViewNome = itemView.findViewById(R.id.textViewNome);
            textViewProfessor = itemView.findViewById(R.id.textViewProfessor);
            textViewNota = itemView.findViewById(R.id.textViewNota);
        }
    }
} 
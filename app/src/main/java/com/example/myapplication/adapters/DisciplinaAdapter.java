package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.Disciplina;

import java.util.List;

public class DisciplinaAdapter extends RecyclerView.Adapter<DisciplinaAdapter.ViewHolder> {

    private List<Disciplina> disciplinas;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Disciplina disciplina);
        void onItemLongClick(Disciplina disciplina);
    }

    public DisciplinaAdapter(List<Disciplina> disciplinas, OnItemClickListener listener) {
        this.disciplinas = disciplinas;
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
        holder.bind(disciplina, listener);
    }

    @Override
    public int getItemCount() {
        return disciplinas.size();
    }

    public void updateList(List<Disciplina> newList) {
        this.disciplinas = newList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewNomeDisciplina;
        private TextView textViewProfessor;
        private TextView textViewPeriodo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNomeDisciplina = itemView.findViewById(R.id.textViewNomeDisciplina);
            textViewProfessor = itemView.findViewById(R.id.textViewProfessor);
            textViewPeriodo = itemView.findViewById(R.id.textViewPeriodo);
        }

        public void bind(final Disciplina disciplina, final OnItemClickListener listener) {
            textViewNomeDisciplina.setText(disciplina.getNome());
            textViewProfessor.setText(disciplina.getProfessor());
            textViewPeriodo.setText(disciplina.getPeriodo());
            
            itemView.setOnClickListener(v -> listener.onItemClick(disciplina));
            itemView.setOnLongClickListener(v -> {
                listener.onItemLongClick(disciplina);
                return true;
            });
        }
    }
} 
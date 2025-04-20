package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.Horario;

import java.util.ArrayList;
import java.util.List;

public class HorarioAdapter extends RecyclerView.Adapter<HorarioAdapter.ViewHolder> {

    private List<Horario> horarios;
    private OnHorarioClickListener listener;

    public interface OnHorarioClickListener {
        void onItemClick(Horario horario);
        void onItemLongClick(Horario horario);
    }

    public HorarioAdapter() {
        this.horarios = new ArrayList<>();
    }

    public void setHorarios(List<Horario> horarios) {
        this.horarios = horarios;
        notifyDataSetChanged();
    }

    public void setOnHorarioClickListener(OnHorarioClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_horario, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Horario horario = horarios.get(position);
        holder.bind(horario);
    }

    @Override
    public int getItemCount() {
        return horarios.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewDiaSemana;
        private final TextView textViewHorario;
        private final TextView textViewDisciplina;
        private final TextView textViewSala;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDiaSemana = itemView.findViewById(R.id.textViewDiaSemana);
            textViewHorario = itemView.findViewById(R.id.textViewHorario);
            textViewDisciplina = itemView.findViewById(R.id.textViewDisciplina);
            textViewSala = itemView.findViewById(R.id.textViewSala);
        }

        public void bind(final Horario horario) {
            textViewDiaSemana.setText(horario.getDiaSemana());
            textViewHorario.setText(String.format("%s - %s", horario.getHoraInicio(), horario.getHoraFim()));
            textViewDisciplina.setText(horario.getDisciplinaNome());
            textViewSala.setText(horario.getObservacoes());

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(horario);
                }
            });

            itemView.setOnLongClickListener(v -> {
                if (listener != null) {
                    listener.onItemLongClick(horario);
                }
                return true;
            });
        }
    }
} 
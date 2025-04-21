package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.Entrega;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EntregaAdapter extends RecyclerView.Adapter<EntregaAdapter.EntregaViewHolder> {
    private List<Entrega> entregas;
    private OnEntregaClickListener listener;
    private OnEntregaCheckListener checkListener;
    private final SimpleDateFormat dateFormat;

    public EntregaAdapter() {
        this.entregas = new ArrayList<>();
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    }

    @NonNull
    @Override
    public EntregaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_entrega, parent, false);
        return new EntregaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EntregaViewHolder holder, int position) {
        Entrega entrega = entregas.get(position);
        holder.bind(entrega);
    }

    @Override
    public int getItemCount() {
        return entregas.size();
    }

    public void setEntregas(List<Entrega> entregas) {
        this.entregas = entregas;
        notifyDataSetChanged();
    }

    public void setOnEntregaClickListener(OnEntregaClickListener listener) {
        this.listener = listener;
    }

    public void setOnEntregaCheckListener(OnEntregaCheckListener listener) {
        this.checkListener = listener;
    }

    public interface OnEntregaClickListener {
        void onEntregaClick(Entrega entrega);
        void onEntregaLongClick(Entrega entrega);
    }

    public interface OnEntregaCheckListener {
        void onEntregaChecked(Entrega entrega, boolean isChecked);
    }

    class EntregaViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewTitulo;
        private final TextView textViewDisciplina;
        private final TextView textViewDescricao;
        private final TextView textViewDataEntrega;
        private final CheckBox checkBoxConcluida;

        public EntregaViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitulo = itemView.findViewById(R.id.textViewTitulo);
            textViewDisciplina = itemView.findViewById(R.id.textViewDisciplina);
            textViewDescricao = itemView.findViewById(R.id.textViewDescricao);
            textViewDataEntrega = itemView.findViewById(R.id.textViewDataEntrega);
            checkBoxConcluida = itemView.findViewById(R.id.checkBoxConcluida);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onEntregaClick(entregas.get(position));
                }
            });

            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onEntregaLongClick(entregas.get(position));
                    return true;
                }
                return false;
            });

            checkBoxConcluida.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && checkListener != null) {
                    checkListener.onEntregaChecked(entregas.get(position), checkBoxConcluida.isChecked());
                }
            });
        }

        public void bind(Entrega entrega) {
            textViewTitulo.setText(entrega.getTitulo());
            textViewDisciplina.setText(entrega.getDisciplinaNome());
            textViewDescricao.setText(entrega.getDescricao());
            textViewDataEntrega.setText(dateFormat.format(entrega.getDataEntrega()));
            checkBoxConcluida.setChecked(entrega.isConcluida());
        }
    }
} 
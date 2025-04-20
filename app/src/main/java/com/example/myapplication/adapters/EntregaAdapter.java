package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.Entrega;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class EntregaAdapter extends RecyclerView.Adapter<EntregaAdapter.ViewHolder> {

    private final List<Entrega> entregas;

    public EntregaAdapter(List<Entrega> entregas) {
        this.entregas = entregas;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_entrega, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Entrega entrega = entregas.get(position);
        holder.bind(entrega);
    }

    @Override
    public int getItemCount() {
        return entregas.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewTitulo;
        private final TextView textViewDescricao;
        private final TextView textViewDisciplina;
        private final TextView textViewData;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitulo = itemView.findViewById(R.id.textViewTitulo);
            textViewDescricao = itemView.findViewById(R.id.textViewDescricao);
            textViewDisciplina = itemView.findViewById(R.id.textViewDisciplina);
            textViewData = itemView.findViewById(R.id.textViewData);
        }

        public void bind(Entrega entrega) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            
            textViewTitulo.setText(entrega.getTitulo() != null ? entrega.getTitulo() : "");
            textViewDescricao.setText(entrega.getDescricao() != null ? entrega.getDescricao() : "");
            textViewDisciplina.setText(entrega.getDisciplinaNome() != null ? entrega.getDisciplinaNome() : "Disciplina não definida");
            
            if (entrega.getDataEntrega() != null) {
                textViewData.setText(dateFormat.format(entrega.getDataEntrega()));
            } else {
                textViewData.setText("");
            }
        }
    }
} 
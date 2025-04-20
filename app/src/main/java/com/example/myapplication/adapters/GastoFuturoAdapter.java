package com.example.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.GastoFuturo;
import com.google.android.material.chip.Chip;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GastoFuturoAdapter extends RecyclerView.Adapter<GastoFuturoAdapter.ViewHolder> {

    private final List<GastoFuturo> gastosFuturos;
    private final Context context;
    private final OnItemClickListener listener;
    private final SimpleDateFormat dateFormat;
    private final NumberFormat currencyFormat;

    public interface OnItemClickListener {
        void onItemClick(GastoFuturo gastoFuturo);
        void onItemLongClick(GastoFuturo gastoFuturo);
        void onStatusClick(GastoFuturo gastoFuturo);
    }

    public GastoFuturoAdapter(Context context, List<GastoFuturo> gastosFuturos, OnItemClickListener listener) {
        this.context = context;
        this.gastosFuturos = gastosFuturos;
        this.listener = listener;
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("pt", "BR"));
        this.currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_gasto_futuro, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GastoFuturo gastoFuturo = gastosFuturos.get(position);
        
        holder.textViewDescricao.setText(gastoFuturo.getDescricao());
        holder.textViewValor.setText(currencyFormat.format(gastoFuturo.getValor()));
        holder.textViewCategoria.setText(gastoFuturo.getNomeCategoria());

        // Configura o texto e estilo do vencimento
        Date hoje = new Date();
        String dataFormatada = dateFormat.format(gastoFuturo.getDataVencimento());
        if (gastoFuturo.getDataVencimento().before(hoje) && !gastoFuturo.getStatus().equals("PAGO")) {
            holder.textViewVencimento.setText(context.getString(R.string.vencido_em, dataFormatada));
            holder.textViewVencimento.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
        } else {
            holder.textViewVencimento.setText(context.getString(R.string.vence_em, dataFormatada));
            holder.textViewVencimento.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
        }

        // Configura o chip de status
        switch (gastoFuturo.getStatus()) {
            case "PENDENTE":
                holder.chipStatus.setText(R.string.status_pendente);
                holder.chipStatus.setChipBackgroundColorResource(R.color.colorPrimary);
                break;
            case "PAGO":
                holder.chipStatus.setText(R.string.status_pago);
                holder.chipStatus.setChipBackgroundColorResource(android.R.color.holo_green_light);
                break;
            case "VENCIDO":
                holder.chipStatus.setText(R.string.status_vencido);
                holder.chipStatus.setChipBackgroundColorResource(android.R.color.holo_red_light);
                break;
        }

        // Configura o chip de recorrência
        if (gastoFuturo.isRecorrente()) {
            holder.chipRecorrencia.setVisibility(View.VISIBLE);
            holder.chipRecorrencia.setText(gastoFuturo.getPeriodoRecorrencia().equals("MENSAL") 
                    ? R.string.recorrencia_mensal 
                    : R.string.recorrencia_anual);
        } else {
            holder.chipRecorrencia.setVisibility(View.GONE);
        }

        // Configura os cliques
        holder.itemView.setOnClickListener(v -> listener.onItemClick(gastoFuturo));
        holder.itemView.setOnLongClickListener(v -> {
            listener.onItemLongClick(gastoFuturo);
            return true;
        });
        holder.chipStatus.setOnClickListener(v -> listener.onStatusClick(gastoFuturo));
    }

    @Override
    public int getItemCount() {
        return gastosFuturos.size();
    }

    public void updateList(List<GastoFuturo> newList) {
        gastosFuturos.clear();
        gastosFuturos.addAll(newList);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDescricao;
        TextView textViewValor;
        TextView textViewCategoria;
        TextView textViewVencimento;
        Chip chipStatus;
        Chip chipRecorrencia;

        ViewHolder(View itemView) {
            super(itemView);
            textViewDescricao = itemView.findViewById(R.id.textViewDescricao);
            textViewValor = itemView.findViewById(R.id.textViewValor);
            textViewCategoria = itemView.findViewById(R.id.textViewCategoria);
            textViewVencimento = itemView.findViewById(R.id.textViewVencimento);
            chipStatus = itemView.findViewById(R.id.chipStatus);
            chipRecorrencia = itemView.findViewById(R.id.chipRecorrencia);
        }
    }
} 
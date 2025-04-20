package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.Gasto;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GastoAdapter extends RecyclerView.Adapter<GastoAdapter.GastoViewHolder> {
    private List<Gasto> gastos;
    private OnGastoClickListener listener;
    private final NumberFormat currencyFormat;
    private final SimpleDateFormat dateFormat;
    private static final Locale LOCALE_BR = new Locale("pt", "BR");

    public interface OnGastoClickListener {
        void onEditClick(Gasto gasto);
        void onDeleteClick(Gasto gasto);
    }

    public GastoAdapter() {
        this.gastos = new ArrayList<>();
        this.currencyFormat = NumberFormat.getCurrencyInstance(LOCALE_BR);
        this.dateFormat = new SimpleDateFormat("dd/MM/yyyy", LOCALE_BR);
    }

    public void setOnGastoClickListener(OnGastoClickListener listener) {
        this.listener = listener;
    }

    public void setGastos(List<Gasto> gastos) {
        this.gastos = gastos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GastoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gasto, parent, false);
        return new GastoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GastoViewHolder holder, int position) {
        Gasto gasto = gastos.get(position);
        holder.bind(gasto);
    }

    @Override
    public int getItemCount() {
        return gastos.size();
    }

    class GastoViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtDescricao;
        private final TextView txtValor;
        private final TextView txtData;
        private final TextView txtCategoria;
        private final ImageButton btnEdit;
        private final ImageButton btnDelete;

        public GastoViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDescricao = itemView.findViewById(R.id.txtDescricao);
            txtValor = itemView.findViewById(R.id.txtValor);
            txtData = itemView.findViewById(R.id.txtData);
            txtCategoria = itemView.findViewById(R.id.txtCategoria);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }

        public void bind(final Gasto gasto) {
            txtDescricao.setText(gasto.getDescricao());
            txtValor.setText(currencyFormat.format(gasto.getValor()));
            txtData.setText(dateFormat.format(gasto.getData()));
            txtCategoria.setText(gasto.getNomeCategoria());

            btnEdit.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEditClick(gasto);
                }
            });

            btnDelete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteClick(gasto);
                }
            });
        }
    }
} 
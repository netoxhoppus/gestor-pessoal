package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.CategoriaGasto;

import java.util.ArrayList;
import java.util.List;

public class CategoriaGastoAdapter extends RecyclerView.Adapter<CategoriaGastoAdapter.CategoriaViewHolder> {

    private List<CategoriaGasto> categorias;
    private OnCategoriaClickListener listener;

    public CategoriaGastoAdapter() {
        this.categorias = new ArrayList<>();
    }

    public void setCategorias(List<CategoriaGasto> categorias) {
        this.categorias = categorias;
        notifyDataSetChanged();
    }

    public void setOnCategoriaClickListener(OnCategoriaClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoriaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_categoria_gasto, parent, false);
        return new CategoriaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriaViewHolder holder, int position) {
        CategoriaGasto categoria = categorias.get(position);
        holder.bind(categoria);
    }

    @Override
    public int getItemCount() {
        return categorias.size();
    }

    class CategoriaViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNome;
        private ImageButton btnEdit;
        private ImageButton btnDelete;

        public CategoriaViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNome = itemView.findViewById(R.id.txtNome);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }

        public void bind(CategoriaGasto categoria) {
            txtNome.setText(categoria.getNomeCategoria());

            btnEdit.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEditClick(categoria);
                }
            });

            btnDelete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteClick(categoria);
                }
            });
        }
    }

    public interface OnCategoriaClickListener {
        void onEditClick(CategoriaGasto categoria);
        void onDeleteClick(CategoriaGasto categoria);
    }
} 
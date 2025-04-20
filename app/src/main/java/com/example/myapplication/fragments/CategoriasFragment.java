package com.example.myapplication.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.CategoriaGastoAdapter;
import com.example.myapplication.dao.CategoriaGastoDAO;
import com.example.myapplication.models.CategoriaGasto;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class CategoriasFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView textViewEmpty;
    private CategoriaGastoAdapter adapter;
    private CategoriaGastoDAO categoriaDAO;
    private List<CategoriaGasto> categorias;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categorias, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewCategorias);
        textViewEmpty = view.findViewById(R.id.textViewEmpty);

        categoriaDAO = new CategoriaGastoDAO(requireContext());
        categorias = new ArrayList<>();
        
        setupRecyclerView();
        carregarCategorias();

        return view;
    }

    private void setupRecyclerView() {
        adapter = new CategoriaGastoAdapter();
        adapter.setOnCategoriaClickListener(new CategoriaGastoAdapter.OnCategoriaClickListener() {
            @Override
            public void onEditClick(CategoriaGasto categoria) {
                // TODO: Implementar edição da categoria
            }

            @Override
            public void onDeleteClick(CategoriaGasto categoria) {
                new MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.excluir_categoria)
                    .setMessage("Deseja excluir esta categoria?")
                    .setPositiveButton("Sim", (dialog, which) -> {
                        // TODO: Pegar usuário logado
                        int usuarioId = 1; // Temporário
                        categoriaDAO.delete(categoria.getIdCategoria(), usuarioId);
                        carregarCategorias();
                    })
                    .setNegativeButton("Não", null)
                    .show();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
    }

    private void carregarCategorias() {
        // TODO: Pegar usuário logado
        long usuarioId = 1; // Temporário
        categorias.clear();
        categorias.addAll(categoriaDAO.findAll((int) usuarioId));
        adapter.notifyDataSetChanged();
        
        updateEmptyView();
    }

    private void updateEmptyView() {
        if (categorias.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            textViewEmpty.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            textViewEmpty.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        carregarCategorias();
    }
} 
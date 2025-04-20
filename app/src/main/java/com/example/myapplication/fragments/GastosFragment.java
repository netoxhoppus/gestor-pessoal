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
import com.example.myapplication.adapters.GastoAdapter;
import com.example.myapplication.dao.GastoDAO;
import com.example.myapplication.models.Gasto;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class GastosFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView textViewEmpty;
    private GastoAdapter adapter;
    private GastoDAO gastoDAO;
    private List<Gasto> gastos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gastos, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewGastos);
        textViewEmpty = view.findViewById(R.id.textViewEmpty);

        gastoDAO = new GastoDAO(requireContext());
        gastos = new ArrayList<>();
        
        setupRecyclerView();
        carregarGastos();

        return view;
    }

    private void setupRecyclerView() {
        adapter = new GastoAdapter();
        adapter.setOnGastoClickListener(new GastoAdapter.OnGastoClickListener() {
            @Override
            public void onEditClick(Gasto gasto) {
                // TODO: Implementar edição do gasto
            }

            @Override
            public void onDeleteClick(Gasto gasto) {
                new MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.excluir_gasto)
                    .setMessage("Deseja excluir este gasto?")
                    .setPositiveButton("Sim", (dialog, which) -> {
                        gastoDAO.delete(gasto.getId());
                        carregarGastos();
                    })
                    .setNegativeButton("Não", null)
                    .show();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
    }

    private void carregarGastos() {
        // TODO: Pegar usuário logado
        long usuarioId = 1; // Temporário
        gastos.clear();
        gastos.addAll(gastoDAO.list(usuarioId));
        adapter.notifyDataSetChanged();
        
        updateEmptyView();
    }

    private void updateEmptyView() {
        if (gastos.isEmpty()) {
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
        carregarGastos();
    }
} 
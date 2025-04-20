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
import com.example.myapplication.adapters.GastoFuturoAdapter;
import com.example.myapplication.dao.GastoFuturoDAO;
import com.example.myapplication.models.GastoFuturo;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class GastosFuturosFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView textViewEmpty;
    private GastoFuturoAdapter adapter;
    private GastoFuturoDAO gastoFuturoDAO;
    private List<GastoFuturo> gastosFuturos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gastos_futuros, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewGastosFuturos);
        textViewEmpty = view.findViewById(R.id.textViewEmpty);

        gastoFuturoDAO = new GastoFuturoDAO(requireContext());
        gastosFuturos = new ArrayList<>();
        
        setupRecyclerView();
        carregarGastosFuturos();

        return view;
    }

    private void setupRecyclerView() {
        adapter = new GastoFuturoAdapter(requireContext(), gastosFuturos, new GastoFuturoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(GastoFuturo gastoFuturo) {
                // TODO: Implementar edição do gasto futuro
            }

            @Override
            public void onItemLongClick(GastoFuturo gastoFuturo) {
                new MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Excluir Gasto Futuro")
                    .setMessage("Deseja excluir este gasto futuro?")
                    .setPositiveButton("Sim", (dialog, which) -> {
                        gastoFuturoDAO.delete(gastoFuturo.getId());
                        carregarGastosFuturos();
                    })
                    .setNegativeButton("Não", null)
                    .show();
            }

            @Override
            public void onStatusClick(GastoFuturo gastoFuturo) {
                String[] statusOptions = {"Pendente", "Pago", "Vencido"};
                new MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Alterar Status")
                    .setItems(statusOptions, (dialog, which) -> {
                        String newStatus;
                        switch (which) {
                            case 0:
                                newStatus = "PENDENTE";
                                break;
                            case 1:
                                newStatus = "PAGO";
                                break;
                            case 2:
                                newStatus = "VENCIDO";
                                break;
                            default:
                                return;
                        }
                        gastoFuturo.setStatus(newStatus);
                        gastoFuturoDAO.update(gastoFuturo);
                        carregarGastosFuturos();
                    })
                    .show();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
    }

    private void carregarGastosFuturos() {
        // TODO: Pegar usuário logado
        long usuarioId = 1; // Temporário
        gastosFuturos.clear();
        gastosFuturos.addAll(gastoFuturoDAO.list(usuarioId));
        adapter.notifyDataSetChanged();
        
        updateEmptyView();
    }

    private void updateEmptyView() {
        if (gastosFuturos.isEmpty()) {
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
        carregarGastosFuturos();
    }
} 
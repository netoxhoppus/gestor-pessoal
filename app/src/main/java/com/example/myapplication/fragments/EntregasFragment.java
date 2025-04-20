package com.example.myapplication.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.EntregaAdapter;
import com.example.myapplication.dao.EntregaDAO;
import com.example.myapplication.models.Entrega;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class EntregasFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView textViewEmpty;
    private EntregaAdapter adapter;
    private EntregaDAO entregaDAO;
    private List<Entrega> entregas;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_entregas, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewEntregas);
        textViewEmpty = view.findViewById(R.id.textViewEmpty);
        
        entregaDAO = new EntregaDAO(requireContext());
        entregas = new ArrayList<>();
        
        setupRecyclerView();
        carregarEntregas();
        
        return view;
    }

    private void setupRecyclerView() {
        adapter = new EntregaAdapter(entregas);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
    }

    private void carregarEntregas() {
        try {
            // TODO: Pegar usuário logado
            long usuarioId = 1; // Temporário
            entregas.clear();
            entregas.addAll(entregaDAO.list(usuarioId));
            adapter.notifyDataSetChanged();
            updateEmptyView();
        } catch (Exception e) {
            android.util.Log.e("EntregasFragment", "Erro ao carregar entregas", e);
            Toast.makeText(requireContext(), "Erro ao carregar entregas", Toast.LENGTH_SHORT).show();
            updateEmptyView();
        }
    }

    private void updateEmptyView() {
        if (entregas.isEmpty()) {
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
        carregarEntregas();
    }
} 
package com.example.myapplication.fragments;

import android.content.Intent;
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
import com.example.myapplication.activities.AdicionarHorarioActivity;
import com.example.myapplication.adapters.HorarioAdapter;
import com.example.myapplication.dao.HorarioDAO;
import com.example.myapplication.models.Horario;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class HorariosFragment extends Fragment implements HorarioAdapter.OnHorarioClickListener {

    private RecyclerView recyclerView;
    private TextView textViewEmptyHorarios;
    private FloatingActionButton fabAddHorario;
    private HorarioAdapter adapter;
    private HorarioDAO horarioDAO;
    private List<Horario> horarios;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_horarios, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewHorarios);
        textViewEmptyHorarios = view.findViewById(R.id.textViewEmptyHorarios);
        fabAddHorario = view.findViewById(R.id.fabAddHorario);

        horarioDAO = new HorarioDAO(requireContext());
        horarios = new ArrayList<>();
        
        setupRecyclerView();
        setupFAB();
        carregarHorarios();

        return view;
    }

    private void setupRecyclerView() {
        adapter = new HorarioAdapter();
        adapter.setOnHorarioClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
    }

    private void setupFAB() {
        fabAddHorario.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), AdicionarHorarioActivity.class);
            startActivity(intent);
        });
    }

    private void carregarHorarios() {
        // TODO: Pegar usuário logado
        long usuarioId = 1; // Temporário
        horarios.clear();
        horarios.addAll(horarioDAO.list(usuarioId));
        adapter.setHorarios(horarios);
        
        updateEmptyView();
    }

    private void updateEmptyView() {
        if (horarios.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            textViewEmptyHorarios.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            textViewEmptyHorarios.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        carregarHorarios();
    }

    @Override
    public void onItemClick(Horario horario) {
        Intent intent = new Intent(requireContext(), AdicionarHorarioActivity.class);
        intent.putExtra("horario", horario);
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(Horario horario) {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.excluir_horario)
                .setMessage(R.string.confirmar_exclusao_horario)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    if (horarioDAO.delete(horario.getIdHorario())) {
                        carregarHorarios();
                        Toast.makeText(requireContext(), "Horário excluído com sucesso", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }
} 
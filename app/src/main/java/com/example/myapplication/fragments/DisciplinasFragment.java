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
import com.example.myapplication.activities.AdicionarDisciplinaActivity;
import com.example.myapplication.adapters.DisciplinaAdapter;
import com.example.myapplication.dao.DisciplinaDAO;
import com.example.myapplication.models.Disciplina;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class DisciplinasFragment extends Fragment implements DisciplinaAdapter.OnDisciplinaClickListener {

    private RecyclerView recyclerView;
    private TextView textViewEmpty;
    private FloatingActionButton fabAdd;
    private DisciplinaAdapter adapter;
    private DisciplinaDAO disciplinaDAO;
    private List<Disciplina> disciplinas;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_disciplinas, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewDisciplinas);
        textViewEmpty = view.findViewById(R.id.textViewEmpty);
        fabAdd = view.findViewById(R.id.fabAddDisciplina);

        disciplinaDAO = new DisciplinaDAO(requireContext());
        disciplinas = new ArrayList<>();
        
        setupRecyclerView();
        setupFAB();
        carregarDisciplinas();

        return view;
    }

    private void setupRecyclerView() {
        adapter = new DisciplinaAdapter(disciplinas);
        adapter.setOnDisciplinaClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
    }

    private void setupFAB() {
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), AdicionarDisciplinaActivity.class);
            startActivity(intent);
        });
    }

    private void carregarDisciplinas() {
        try {
            // TODO: Pegar usuário logado
            long usuarioId = 1; // Temporário
            disciplinas.clear();
            disciplinas.addAll(disciplinaDAO.list(usuarioId));
            adapter.notifyDataSetChanged();
            updateEmptyView();
        } catch (Exception e) {
            android.util.Log.e("DisciplinasFragment", "Erro ao carregar disciplinas", e);
            Toast.makeText(requireContext(), "Erro ao carregar disciplinas", Toast.LENGTH_SHORT).show();
            updateEmptyView();
        }
    }

    private void updateEmptyView() {
        if (disciplinas.isEmpty()) {
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
        carregarDisciplinas();
    }

    @Override
    public void onDisciplinaClick(Disciplina disciplina) {
        Intent intent = new Intent(requireContext(), AdicionarDisciplinaActivity.class);
        intent.putExtra("disciplina", disciplina);
        startActivity(intent);
    }

    @Override
    public void onDisciplinaLongClick(Disciplina disciplina) {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.excluir_disciplina)
                .setMessage(R.string.confirmar_exclusao_disciplina)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    if (disciplinaDAO.delete(disciplina.getId(), disciplina.getUsuarioId())) {
                        carregarDisciplinas();
                        Toast.makeText(requireContext(), "Disciplina excluída com sucesso", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }
} 
package com.example.myapplication.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

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
        android.util.Log.d("DisciplinasFragment", "onCreateView called");
        View view = inflater.inflate(R.layout.fragment_disciplinas, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewDisciplinas);
        textViewEmpty = view.findViewById(R.id.textViewEmpty);
        fabAdd = view.findViewById(R.id.fabAddDisciplina);

        android.util.Log.d("DisciplinasFragment", "Views initialized - FAB is " + (fabAdd != null ? "not null" : "null"));

        setupRecyclerView();
        setupFAB();

        return view;
    }

    private void setupRecyclerView() {
        disciplinas = new ArrayList<>();
        adapter = new DisciplinaAdapter(disciplinas);
        adapter.setOnDisciplinaClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
        
        disciplinaDAO = new DisciplinaDAO(requireContext());
        loadDisciplinas();
    }

    private void setupFAB() {
        android.util.Log.d("DisciplinasFragment", "setupFAB called - FAB is " + (fabAdd != null ? "not null" : "null"));
        if (fabAdd != null) {
            fabAdd.setOnClickListener(v -> {
                android.util.Log.d("DisciplinasFragment", "FAB clicked");
                showAddDisciplinaDialog();
            });
        } else {
            android.util.Log.e("DisciplinasFragment", "FAB is null");
        }
    }

    public void showAddDisciplinaDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_disciplina, null);
        EditText editTextNome = dialogView.findViewById(R.id.editTextNomeDisciplina);
        EditText editTextProfessor = dialogView.findViewById(R.id.editTextProfessor);
        EditText editTextPeriodo = dialogView.findViewById(R.id.editTextPeriodo);

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.adicionar_disciplina)
                .setView(dialogView)
                .setPositiveButton(R.string.btn_salvar, (dialog, which) -> {
                    String nome = editTextNome.getText().toString().trim();
                    String professor = editTextProfessor.getText().toString().trim();
                    String periodo = editTextPeriodo.getText().toString().trim();

                    if (nome.isEmpty() || professor.isEmpty() || periodo.isEmpty()) {
                        Toast.makeText(requireContext(), R.string.erro_campos_vazios, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Disciplina disciplina = new Disciplina();
                    disciplina.setNome(nome);
                    disciplina.setProfessor(professor);
                    disciplina.setPeriodo(periodo);
                    disciplina.setUsuarioId(1); // Temporário até implementar autenticação
                    disciplina.setStatus("Ativa"); // Definindo o status padrão

                    long id = disciplinaDAO.insert(disciplina);
                    if (id != -1) {
                        loadDisciplinas(); // Recarrega todas as disciplinas do banco
                        Toast.makeText(requireContext(), R.string.disciplina_adicionada, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(), R.string.erro_adicionar_disciplina, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.btn_cancelar, null);

        builder.show();
    }

    private void loadDisciplinas() {
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
        loadDisciplinas();
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
                        int position = disciplinas.indexOf(disciplina);
                        disciplinas.remove(position);
                        adapter.notifyItemRemoved(position);
                        updateEmptyView();
                        Toast.makeText(requireContext(), R.string.disciplina_excluida, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(), R.string.erro_excluir_disciplina, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }
} 
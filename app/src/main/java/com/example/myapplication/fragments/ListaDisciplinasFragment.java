package com.example.myapplication.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.DisciplinaAdapter;
import com.example.myapplication.dao.DisciplinaDAO;
import com.example.myapplication.models.Disciplina;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class ListaDisciplinasFragment extends Fragment implements DisciplinaAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private TextView textViewEmpty;
    private DisciplinaAdapter adapter;
    private DisciplinaDAO disciplinaDAO;
    private List<Disciplina> disciplinas;
    private FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_disciplinas, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewDisciplinas);
        textViewEmpty = view.findViewById(R.id.textViewEmpty);
        fab = view.findViewById(R.id.fabAddDisciplina);

        disciplinaDAO = new DisciplinaDAO(requireContext());
        disciplinas = new ArrayList<>();
        
        setupRecyclerView();
        carregarDisciplinas();

        fab.setOnClickListener(v -> showAddEditDialog(null));

        return view;
    }

    private void setupRecyclerView() {
        adapter = new DisciplinaAdapter(disciplinas, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
    }

    private void carregarDisciplinas() {
        // TODO: Pegar usuário logado
        long usuarioId = 1; // Temporário
        disciplinas.clear();
        disciplinas.addAll(disciplinaDAO.list(usuarioId));
        adapter.notifyDataSetChanged();
        
        updateEmptyView();
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
    public void onItemClick(Disciplina disciplina) {
        showAddEditDialog(disciplina);
    }

    @Override
    public void onItemLongClick(Disciplina disciplina) {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.excluir_disciplina)
                .setMessage(R.string.confirmar_exclusao_disciplina)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    disciplinaDAO.delete(disciplina.getId(), disciplina.getUsuarioId());
                    carregarDisciplinas();
                    Toast.makeText(requireContext(), "Disciplina excluída", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    private void showAddEditDialog(@Nullable Disciplina disciplina) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_edit_disciplina, null);
        
        TextInputLayout tilNome = dialogView.findViewById(R.id.tilNome);
        TextInputLayout tilProfessor = dialogView.findViewById(R.id.tilProfessor);
        TextInputLayout tilPeriodo = dialogView.findViewById(R.id.tilPeriodo);

        TextInputEditText edtNome = dialogView.findViewById(R.id.edtNome);
        TextInputEditText edtProfessor = dialogView.findViewById(R.id.edtProfessor);
        TextInputEditText edtPeriodo = dialogView.findViewById(R.id.edtPeriodo);

        if (disciplina != null) {
            edtNome.setText(disciplina.getNome());
            edtProfessor.setText(disciplina.getProfessor());
            edtPeriodo.setText(disciplina.getPeriodo());
        }

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext())
                .setTitle(disciplina == null ? R.string.adicionar_disciplina : R.string.editar_disciplina)
                .setView(dialogView)
                .setPositiveButton(R.string.btn_salvar, null)
                .setNegativeButton(android.R.string.cancel, null);

        AlertDialog dialog = builder.create();

        dialog.setOnShowListener(dialogInterface -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
                String nome = edtNome.getText().toString().trim();
                String professor = edtProfessor.getText().toString().trim();
                String periodo = edtPeriodo.getText().toString().trim();

                if (nome.isEmpty()) {
                    tilNome.setError(getString(R.string.campo_obrigatorio));
                    return;
                }
                
                if (professor.isEmpty()) {
                    tilProfessor.setError(getString(R.string.campo_obrigatorio));
                    return;
                }

                if (periodo.isEmpty()) {
                    tilPeriodo.setError(getString(R.string.campo_obrigatorio));
                    return;
                }

                // TODO: Pegar usuário logado
                long usuarioId = 1; // Temporário

                if (disciplina == null) {
                    Disciplina novaDisciplina = new Disciplina(nome, professor, periodo, usuarioId);
                    disciplinaDAO.insert(novaDisciplina);
                    Toast.makeText(requireContext(), R.string.disciplina_salva, Toast.LENGTH_SHORT).show();
                } else {
                    disciplina.setNome(nome);
                    disciplina.setProfessor(professor);
                    disciplina.setPeriodo(periodo);
                    disciplinaDAO.update(disciplina);
                    Toast.makeText(requireContext(), R.string.disciplina_salva, Toast.LENGTH_SHORT).show();
                }

                carregarDisciplinas();
                dialog.dismiss();
            });
        });

        dialog.show();
    }
} 
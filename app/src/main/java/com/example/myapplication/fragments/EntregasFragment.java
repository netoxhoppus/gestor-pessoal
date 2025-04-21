package com.example.myapplication.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.EntregaAdapter;
import com.example.myapplication.dao.DisciplinaDAO;
import com.example.myapplication.dao.EntregaDAO;
import com.example.myapplication.models.Disciplina;
import com.example.myapplication.models.Entrega;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EntregasFragment extends Fragment implements EntregaAdapter.OnEntregaClickListener, EntregaAdapter.OnEntregaCheckListener {

    private RecyclerView recyclerView;
    private TextView textViewEmpty;
    private FloatingActionButton fabAddEntrega;
    private EntregaAdapter adapter;
    private EntregaDAO entregaDAO;
    private List<Entrega> entregas;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_entregas, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewEntregas);
        textViewEmpty = view.findViewById(R.id.textViewEmpty);
        fabAddEntrega = view.findViewById(R.id.fabAddEntrega);

        setupRecyclerView();
        setupFAB();
        carregarEntregas();

        return view;
    }

    private void setupRecyclerView() {
        adapter = new EntregaAdapter();
        adapter.setOnEntregaClickListener(this);
        adapter.setOnEntregaCheckListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
        
        entregaDAO = new EntregaDAO(requireContext());
        entregas = new ArrayList<>();
    }

    private void setupFAB() {
        fabAddEntrega.setOnClickListener(v -> showAddEntregaDialog());
    }

    public void showAddEntregaDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_entrega, null);
        
        TextInputEditText editTextTitulo = dialogView.findViewById(R.id.editTextTitulo);
        TextInputEditText editTextDescricao = dialogView.findViewById(R.id.editTextDescricao);
        TextInputEditText editTextDataEntrega = dialogView.findViewById(R.id.editTextDataEntrega);
        AutoCompleteTextView autoCompleteDisciplina = dialogView.findViewById(R.id.autoCompleteDisciplina);

        // Setup disciplinas dropdown
        DisciplinaDAO disciplinaDAO = new DisciplinaDAO(requireContext());
        List<Disciplina> disciplinas = disciplinaDAO.list(1); // TODO: Get real user ID
        List<String> nomesDisciplinas = new ArrayList<>();
        for (Disciplina disciplina : disciplinas) {
            nomesDisciplinas.add(disciplina.getNome());
        }
        ArrayAdapter<String> disciplinasAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_dropdown_item_1line, nomesDisciplinas);
        autoCompleteDisciplina.setAdapter(disciplinasAdapter);

        // Setup date picker
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        
        editTextDataEntrega.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    requireContext(),
                    (view, year, month, dayOfMonth) -> {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        editTextDataEntrega.setText(dateFormat.format(calendar.getTime()));
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.adicionar_entrega)
                .setView(dialogView)
                .setPositiveButton(R.string.btn_salvar, (dialog, which) -> {
                    String titulo = editTextTitulo.getText().toString().trim();
                    String descricao = editTextDescricao.getText().toString().trim();
                    String dataEntregaStr = editTextDataEntrega.getText().toString().trim();
                    String disciplinaNome = autoCompleteDisciplina.getText().toString().trim();

                    if (titulo.isEmpty() || descricao.isEmpty() || dataEntregaStr.isEmpty() || disciplinaNome.isEmpty()) {
                        Toast.makeText(requireContext(), R.string.erro_campos_obrigatorios_entrega, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Find disciplina ID
                    int disciplinaId = -1;
                    for (Disciplina disciplina : disciplinas) {
                        if (disciplina.getNome().equals(disciplinaNome)) {
                            disciplinaId = disciplina.getId();
                            break;
                        }
                    }

                    if (disciplinaId == -1) {
                        Toast.makeText(requireContext(), R.string.erro_disciplina_nao_encontrada, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try {
                        Date dataEntrega = dateFormat.parse(dataEntregaStr);
                        if (dataEntrega == null) throw new Exception("Data inválida");

                        // Create and save entrega
                        Entrega entrega = new Entrega();
                        entrega.setTitulo(titulo);
                        entrega.setDescricao(descricao);
                        entrega.setDataEntrega(dataEntrega);
                        entrega.setDisciplinaId(disciplinaId);
                        entrega.setDisciplinaNome(disciplinaNome);
                        entrega.setUsuarioId(1); // TODO: Get real user ID

                        long id = entregaDAO.insert(entrega);
                        if (id != -1) {
                            carregarEntregas();
                            Toast.makeText(requireContext(), R.string.entrega_adicionada, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireContext(), R.string.erro_adicionar_entrega, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(requireContext(), R.string.erro_adicionar_entrega, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.btn_cancelar, null);

        builder.show();
    }

    private void carregarEntregas() {
        try {
            // TODO: Pegar usuário logado
            long usuarioId = 1; // Temporário
            entregas.clear();
            entregas.addAll(entregaDAO.list(usuarioId));
            adapter.setEntregas(entregas);
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

    @Override
    public void onEntregaClick(Entrega entrega) {
        // TODO: Implementar edição de entrega
    }

    @Override
    public void onEntregaLongClick(Entrega entrega) {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.excluir_entrega)
                .setMessage(R.string.confirmar_exclusao_entrega)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    if (entregaDAO.delete(entrega.getId(), entrega.getUsuarioId())) {
                        int position = entregas.indexOf(entrega);
                        entregas.remove(position);
                        adapter.notifyItemRemoved(position);
                        updateEmptyView();
                        Toast.makeText(requireContext(), R.string.entrega_excluida, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(), R.string.erro_excluir_entrega, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    @Override
    public void onEntregaChecked(Entrega entrega, boolean isChecked) {
        entrega.setConcluida(isChecked);
        if (entregaDAO.update(entrega)) {
            Toast.makeText(requireContext(), R.string.entrega_atualizada, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), R.string.erro_atualizar_entrega, Toast.LENGTH_SHORT).show();
            entrega.setConcluida(!isChecked); // Reverte a mudança em caso de erro
            adapter.notifyItemChanged(entregas.indexOf(entrega));
        }
    }
} 
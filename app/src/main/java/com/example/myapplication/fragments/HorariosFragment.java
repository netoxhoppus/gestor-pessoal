package com.example.myapplication.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AutoCompleteTextView;
import android.widget.ArrayAdapter;
import android.app.TimePickerDialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.activities.AdicionarHorarioActivity;
import com.example.myapplication.adapters.HorarioAdapter;
import com.example.myapplication.dao.HorarioDAO;
import com.example.myapplication.dao.DisciplinaDAO;
import com.example.myapplication.models.Horario;
import com.example.myapplication.models.Disciplina;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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
        textViewEmptyHorarios = view.findViewById(R.id.textViewEmpty);
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
            showAddHorarioDialog();
        });
    }

    public void showAddHorarioDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_horario, null);
        
        // Initialize views
        AutoCompleteTextView autoCompleteDiaSemana = dialogView.findViewById(R.id.autoCompleteTextViewDiaSemana);
        TextInputEditText editTextHoraInicio = dialogView.findViewById(R.id.editTextHoraInicio);
        TextInputEditText editTextHoraFim = dialogView.findViewById(R.id.editTextHoraFim);
        AutoCompleteTextView autoCompleteDisciplina = dialogView.findViewById(R.id.autoCompleteTextViewDisciplina);
        TextInputEditText editTextSala = dialogView.findViewById(R.id.editTextSala);

        // Setup dias da semana dropdown
        String[] diasSemana = getResources().getStringArray(R.array.dias_semana);
        ArrayAdapter<String> diasAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_dropdown_item_1line, diasSemana);
        autoCompleteDiaSemana.setAdapter(diasAdapter);

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

        // Setup time pickers
        editTextHoraInicio.setOnClickListener(v -> showTimePickerDialog(editTextHoraInicio));
        editTextHoraFim.setOnClickListener(v -> showTimePickerDialog(editTextHoraFim));

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.adicionar_horario)
                .setView(dialogView)
                .setPositiveButton(R.string.btn_salvar, (dialog, which) -> {
                    String diaSemana = autoCompleteDiaSemana.getText().toString();
                    String horaInicio = editTextHoraInicio.getText().toString();
                    String horaFim = editTextHoraFim.getText().toString();
                    String disciplinaNome = autoCompleteDisciplina.getText().toString();
                    String sala = editTextSala.getText().toString();

                    if (diaSemana.isEmpty() || horaInicio.isEmpty() || horaFim.isEmpty() || disciplinaNome.isEmpty()) {
                        Toast.makeText(requireContext(), R.string.erro_campos_obrigatorios, Toast.LENGTH_SHORT).show();
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

                    // Create and save horario
                    Horario horario = new Horario();
                    horario.setDiaSemana(diaSemana);
                    horario.setHoraInicio(horaInicio);
                    horario.setHoraFim(horaFim);
                    horario.setObservacoes(sala);
                    horario.setIdAula(disciplinaId);
                    horario.setUsuarioId(1); // TODO: Get real user ID

                    long id = horarioDAO.insert(horario);
                    if (id != -1) {
                        carregarHorarios();
                        Toast.makeText(requireContext(), R.string.horario_adicionado, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(), R.string.erro_adicionar_horario, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.btn_cancelar, null);

        builder.show();
    }

    private void showTimePickerDialog(TextInputEditText editText) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
                (view, hourOfDay, selectedMinute) -> {
                    String time = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, selectedMinute);
                    editText.setText(time);
                },
                hour,
                minute,
                true);

        timePickerDialog.show();
    }

    private void carregarHorarios() {
        try {
            // TODO: Pegar usuário logado
            long usuarioId = 1; // Temporário
            horarios.clear();
            horarios.addAll(horarioDAO.list(usuarioId));
            adapter.setHorarios(horarios);
            updateEmptyView();
        } catch (Exception e) {
            android.util.Log.e("HorariosFragment", "Erro ao carregar horários", e);
            Toast.makeText(requireContext(), "Erro ao carregar horários", Toast.LENGTH_SHORT).show();
            updateEmptyView();
        }
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
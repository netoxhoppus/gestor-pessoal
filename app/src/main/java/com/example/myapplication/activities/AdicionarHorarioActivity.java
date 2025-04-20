package com.example.myapplication.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.dao.DisciplinaDAO;
import com.example.myapplication.dao.HorarioDAO;
import com.example.myapplication.helpers.SQLiteHelper;
import com.example.myapplication.models.Disciplina;
import com.example.myapplication.models.Horario;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AdicionarHorarioActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private AutoCompleteTextView autoCompleteTextViewDiaSemana;
    private TextInputEditText editTextHoraInicio;
    private TextInputEditText editTextHoraFim;
    private AutoCompleteTextView autoCompleteTextViewDisciplina;
    private TextInputEditText editTextSala;
    private MaterialButton buttonSalvar;

    private HorarioDAO horarioDAO;
    private DisciplinaDAO disciplinaDAO;
    private Horario horario;
    private boolean isEditMode = false;
    private final Calendar calendar = Calendar.getInstance();
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_horario);

        // Inicializa os DAOs
        horarioDAO = new HorarioDAO(this);
        disciplinaDAO = new DisciplinaDAO(this);

        // Inicializa as views
        initializeViews();
        setupToolbar();
        setupDiaSemanaDropdown();
        setupDisciplinaDropdown();
        setupTimePickers();
        setupSalvarButton();

        // Verifica se é modo de edição
        if (getIntent().hasExtra("horario")) {
            isEditMode = true;
            horario = (Horario) getIntent().getSerializableExtra("horario");
            preencherCampos();
        }
    }

    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        autoCompleteTextViewDiaSemana = findViewById(R.id.autoCompleteTextViewDiaSemana);
        editTextHoraInicio = findViewById(R.id.editTextHoraInicio);
        editTextHoraFim = findViewById(R.id.editTextHoraFim);
        autoCompleteTextViewDisciplina = findViewById(R.id.autoCompleteTextViewDisciplina);
        editTextSala = findViewById(R.id.editTextSala);
        buttonSalvar = findViewById(R.id.buttonSalvar);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(isEditMode ? R.string.editar_horario : R.string.adicionar_horario);
        }
    }

    private void setupDiaSemanaDropdown() {
        String[] diasSemana = getResources().getStringArray(R.array.dias_semana);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, diasSemana);
        autoCompleteTextViewDiaSemana.setAdapter(adapter);
    }

    private void setupDisciplinaDropdown() {
        List<Disciplina> disciplinas = disciplinaDAO.list(SQLiteHelper.getCurrentUserId());
        List<String> nomesDisciplinas = new ArrayList<>();
        for (Disciplina disciplina : disciplinas) {
            nomesDisciplinas.add(disciplina.getNome());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, nomesDisciplinas);
        autoCompleteTextViewDisciplina.setAdapter(adapter);
    }

    private void setupTimePickers() {
        editTextHoraInicio.setOnClickListener(v -> showTimePickerDialog(editTextHoraInicio));
        editTextHoraFim.setOnClickListener(v -> showTimePickerDialog(editTextHoraFim));
    }

    private void showTimePickerDialog(TextInputEditText editText) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
            this,
            (view, hourOfDay, minute) -> {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                editText.setText(timeFormat.format(calendar.getTime()));
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        );
        timePickerDialog.show();
    }

    private void setupSalvarButton() {
        buttonSalvar.setOnClickListener(v -> salvarHorario());
    }

    private void preencherCampos() {
        autoCompleteTextViewDiaSemana.setText(horario.getDiaSemana());
        editTextHoraInicio.setText(horario.getHoraInicio());
        editTextHoraFim.setText(horario.getHoraFim());
        autoCompleteTextViewDisciplina.setText(horario.getDisciplinaNome());
        editTextSala.setText(horario.getObservacoes());
    }

    private void salvarHorario() {
        String diaSemana = autoCompleteTextViewDiaSemana.getText().toString();
        String horaInicio = editTextHoraInicio.getText().toString();
        String horaFim = editTextHoraFim.getText().toString();
        String nomeDisciplina = autoCompleteTextViewDisciplina.getText().toString();
        String sala = editTextSala.getText().toString();

        if (diaSemana.isEmpty() || horaInicio.isEmpty() || horaFim.isEmpty() || nomeDisciplina.isEmpty()) {
            Toast.makeText(this, R.string.erro_campos_obrigatorios, Toast.LENGTH_SHORT).show();
            return;
        }

        if (horario == null) {
            horario = new Horario();
        }

        horario.setDiaSemana(diaSemana);
        horario.setHoraInicio(horaInicio);
        horario.setHoraFim(horaFim);
        horario.setObservacoes(sala);
        horario.setDisciplinaNome(nomeDisciplina);

        // Busca a disciplina pelo nome para obter o ID
        List<Disciplina> disciplinas = disciplinaDAO.list(SQLiteHelper.getCurrentUserId());
        for (Disciplina disciplina : disciplinas) {
            if (disciplina.getNome().equals(nomeDisciplina)) {
                horario.setIdAula(disciplina.getId());
                break;
            }
        }

        boolean success;
        if (isEditMode) {
            success = horarioDAO.update(horario);
        } else {
            success = horarioDAO.insert(horario) > 0;
        }

        if (success) {
            Toast.makeText(this, R.string.horario_salvo, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, R.string.erro_salvar_horario, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
} 
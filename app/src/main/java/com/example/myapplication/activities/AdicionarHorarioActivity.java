package com.example.myapplication.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.dao.DisciplinaDAO;
import com.example.myapplication.dao.HorarioDAO;
import com.example.myapplication.models.Disciplina;
import com.example.myapplication.models.Horario;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class AdicionarHorarioActivity extends AppCompatActivity {

    private TextInputEditText edtDiaSemana;
    private TextInputEditText edtHoraInicio;
    private TextInputEditText edtHoraFim;
    private TextInputEditText edtObservacoes;
    private AutoCompleteTextView actvDisciplina;
    private Button btnSalvar;

    private HorarioDAO horarioDAO;
    private DisciplinaDAO disciplinaDAO;
    private List<Disciplina> disciplinas;
    private Horario horarioParaEditar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_horario);

        // Configurar a ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.adicionar_horario));
        }

        // Inicializar DAOs
        horarioDAO = new HorarioDAO(this);
        disciplinaDAO = new DisciplinaDAO(this);

        // Inicializar views
        edtDiaSemana = findViewById(R.id.edtDiaSemana);
        edtHoraInicio = findViewById(R.id.edtHoraInicio);
        edtHoraFim = findViewById(R.id.edtHoraFim);
        edtObservacoes = findViewById(R.id.edtObservacoes);
        actvDisciplina = findViewById(R.id.actvDisciplina);
        btnSalvar = findViewById(R.id.btnSalvar);

        // Carregar disciplinas para o AutoCompleteTextView
        carregarDisciplinas();

        // Verificar se é edição
        horarioParaEditar = (Horario) getIntent().getSerializableExtra("horario");
        if (horarioParaEditar != null) {
            preencherCamposParaEdicao();
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(getString(R.string.editar_horario));
            }
        }

        // Configurar botão salvar
        btnSalvar.setOnClickListener(v -> salvarHorario());
    }

    private void carregarDisciplinas() {
        // TODO: Pegar usuário logado
        long usuarioId = 1; // Temporário
        disciplinas = disciplinaDAO.list(usuarioId);
        ArrayAdapter<Disciplina> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, disciplinas);
        actvDisciplina.setAdapter(adapter);
    }

    private void preencherCamposParaEdicao() {
        edtDiaSemana.setText(horarioParaEditar.getDiaSemana());
        edtHoraInicio.setText(horarioParaEditar.getHoraInicio());
        edtHoraFim.setText(horarioParaEditar.getHoraFim());
        edtObservacoes.setText(horarioParaEditar.getObservacoes());

        // Encontrar e selecionar a disciplina correta
        for (Disciplina disciplina : disciplinas) {
            if (disciplina.getId() == horarioParaEditar.getIdAula()) {
                actvDisciplina.setText(disciplina.getNome(), false);
                break;
            }
        }
    }

    private void salvarHorario() {
        if (!validarCampos()) return;

        String diaSemana = edtDiaSemana.getText().toString().trim();
        String horaInicio = edtHoraInicio.getText().toString().trim();
        String horaFim = edtHoraFim.getText().toString().trim();
        String observacoes = edtObservacoes.getText().toString().trim();
        String nomeDisciplina = actvDisciplina.getText().toString().trim();

        // Encontrar o ID da disciplina selecionada
        int idAula = -1;
        for (Disciplina disciplina : disciplinas) {
            if (disciplina.getNome().equals(nomeDisciplina)) {
                idAula = disciplina.getId();
                break;
            }
        }

        if (idAula == -1) {
            Toast.makeText(this, "Selecione uma disciplina válida", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO: Pegar usuário logado
        long usuarioId = 1; // Temporário

        boolean sucesso;
        if (horarioParaEditar == null) {
            // Criar novo horário
            Horario novoHorario = new Horario();
            novoHorario.setDiaSemana(diaSemana);
            novoHorario.setHoraInicio(horaInicio);
            novoHorario.setHoraFim(horaFim);
            novoHorario.setObservacoes(observacoes);
            novoHorario.setIdAula(idAula);
            novoHorario.setUsuarioId((int) usuarioId);

            sucesso = horarioDAO.insert(novoHorario) > 0;
        } else {
            // Atualizar horário existente
            horarioParaEditar.setDiaSemana(diaSemana);
            horarioParaEditar.setHoraInicio(horaInicio);
            horarioParaEditar.setHoraFim(horaFim);
            horarioParaEditar.setObservacoes(observacoes);
            horarioParaEditar.setIdAula(idAula);

            sucesso = horarioDAO.update(horarioParaEditar);
        }

        if (sucesso) {
            Toast.makeText(this, R.string.horario_salvo, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, R.string.erro_salvar_horario, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validarCampos() {
        if (edtDiaSemana.getText().toString().trim().isEmpty()) {
            edtDiaSemana.setError(getString(R.string.campo_obrigatorio));
            return false;
        }
        if (edtHoraInicio.getText().toString().trim().isEmpty()) {
            edtHoraInicio.setError(getString(R.string.campo_obrigatorio));
            return false;
        }
        if (edtHoraFim.getText().toString().trim().isEmpty()) {
            edtHoraFim.setError(getString(R.string.campo_obrigatorio));
            return false;
        }
        if (actvDisciplina.getText().toString().trim().isEmpty()) {
            actvDisciplina.setError(getString(R.string.campo_obrigatorio));
            return false;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
} 
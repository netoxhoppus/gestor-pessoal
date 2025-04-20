package com.example.myapplication.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.R;
import com.example.myapplication.dao.DisciplinaDAO;
import com.example.myapplication.models.Disciplina;

public class AdicionarDisciplinaActivity extends AppCompatActivity {
    private EditText edtNome;
    private EditText edtProfessor;
    private EditText edtPeriodo;
    private Button btnSalvar;
    private DisciplinaDAO disciplinaDAO;
    private Disciplina disciplinaParaEditar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_disciplina);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize views and DAO
        initViews();
        disciplinaDAO = new DisciplinaDAO(this);

        // Check if we're editing an existing disciplina
        if (getIntent().hasExtra("disciplina")) {
            disciplinaParaEditar = (Disciplina) getIntent().getSerializableExtra("disciplina");
            preencherCampos();
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(R.string.editar_disciplina);
            }
        } else {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(R.string.adicionar_disciplina);
            }
        }

        // Setup save button
        btnSalvar.setOnClickListener(v -> salvarDisciplina());
    }

    private void initViews() {
        edtNome = findViewById(R.id.edtNome);
        edtProfessor = findViewById(R.id.edtProfessor);
        edtPeriodo = findViewById(R.id.edtPeriodo);
        btnSalvar = findViewById(R.id.btnSalvar);
    }

    private void preencherCampos() {
        if (disciplinaParaEditar != null) {
            edtNome.setText(disciplinaParaEditar.getNome());
            edtProfessor.setText(disciplinaParaEditar.getProfessor());
            edtPeriodo.setText(disciplinaParaEditar.getPeriodo());
        }
    }

    private void salvarDisciplina() {
        if (!validarCampos()) {
            return;
        }

        String nome = edtNome.getText().toString();
        String professor = edtProfessor.getText().toString();
        String periodo = edtPeriodo.getText().toString();

        if (disciplinaParaEditar == null) {
            // Creating new disciplina
            Disciplina novaDisciplina = new Disciplina(nome, professor, periodo, 1L); // TODO: Get real user ID
            long id = disciplinaDAO.insert(novaDisciplina);
            if (id > 0) {
                Toast.makeText(this, R.string.disciplina_salva, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, R.string.erro_salvar_disciplina, Toast.LENGTH_SHORT).show();
            }
        } else {
            // Updating existing disciplina
            disciplinaParaEditar.setNome(nome);
            disciplinaParaEditar.setProfessor(professor);
            disciplinaParaEditar.setPeriodo(periodo);
            
            if (disciplinaDAO.update(disciplinaParaEditar)) {
                Toast.makeText(this, R.string.disciplina_salva, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, R.string.erro_salvar_disciplina, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validarCampos() {
        if (edtNome.getText().toString().trim().isEmpty()) {
            edtNome.setError(getString(R.string.campo_obrigatorio));
            return false;
        }
        if (edtProfessor.getText().toString().trim().isEmpty()) {
            edtProfessor.setError(getString(R.string.campo_obrigatorio));
            return false;
        }
        if (edtPeriodo.getText().toString().trim().isEmpty()) {
            edtPeriodo.setError(getString(R.string.campo_obrigatorio));
            return false;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
} 
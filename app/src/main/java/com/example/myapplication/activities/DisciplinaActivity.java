package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.DisciplinaAdapter;
import com.example.myapplication.dao.DisciplinaDAO;
import com.example.myapplication.models.Disciplina;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class DisciplinaActivity extends AppCompatActivity implements DisciplinaAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private TextView txtEmpty;
    private DisciplinaAdapter adapter;
    private DisciplinaDAO disciplinaDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disciplina);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.disciplinas);
        }

        // Initialize views and DAO
        initViews();
        disciplinaDAO = new DisciplinaDAO(this);

        // Setup RecyclerView
        setupRecyclerView();

        // Setup FAB
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(this, AdicionarDisciplinaActivity.class);
            startActivity(intent);
        });
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        txtEmpty = findViewById(R.id.txtEmpty);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DisciplinaAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDisciplinas();
    }

    private void loadDisciplinas() {
        List<Disciplina> disciplinas = disciplinaDAO.list(1L); // TODO: Get real user ID
        adapter.updateList(disciplinas);
        
        // Show/hide empty view
        if (disciplinas.isEmpty()) {
            txtEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            txtEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemClick(Disciplina disciplina) {
        Intent intent = new Intent(this, AdicionarDisciplinaActivity.class);
        intent.putExtra("disciplina", disciplina);
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(Disciplina disciplina) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.excluir_disciplina)
                .setMessage(R.string.confirmar_exclusao_disciplina)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    disciplinaDAO.delete(disciplina.getId(), disciplina.getUsuarioId());
                    loadDisciplinas();
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
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
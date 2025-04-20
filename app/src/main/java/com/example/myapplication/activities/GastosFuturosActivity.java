package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.GastoFuturoAdapter;
import com.example.myapplication.dao.GastoFuturoDAO;
import com.example.myapplication.models.GastoFuturo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class GastosFuturosActivity extends AppCompatActivity implements GastoFuturoAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private TextView textViewEmpty;
    private GastoFuturoAdapter adapter;
    private GastoFuturoDAO gastoFuturoDAO;
    private List<GastoFuturo> gastosFuturos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gastos_futuros);

        // Configura a Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.gastos_futuros);
        }

        // Inicializa as views
        recyclerView = findViewById(R.id.recyclerViewGastosFuturos);
        textViewEmpty = findViewById(R.id.textViewEmpty);
        FloatingActionButton fab = findViewById(R.id.fabAdicionarGastoFuturo);

        // Configura o RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        gastosFuturos = new ArrayList<>();
        adapter = new GastoFuturoAdapter(this, gastosFuturos, this);
        recyclerView.setAdapter(adapter);

        // Inicializa o DAO
        gastoFuturoDAO = new GastoFuturoDAO(this);

        // Configura o FAB
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(this, AdicionarGastoFuturoActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarGastosFuturos();
    }

    private void carregarGastosFuturos() {
        // TODO: Pegar o ID do usuário logado
        int usuarioId = 1; // Temporário
        
        List<GastoFuturo> lista = gastoFuturoDAO.list(usuarioId);
        adapter.updateList(lista);
        
        if (lista.isEmpty()) {
            textViewEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            textViewEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemClick(GastoFuturo gastoFuturo) {
        Intent intent = new Intent(this, AdicionarGastoFuturoActivity.class);
        intent.putExtra("gasto_futuro_id", gastoFuturo.getId());
        startActivity(intent);
    }

    @Override
    public void onStatusClick(GastoFuturo gastoFuturo) {
        // Alterna o status entre PENDENTE e PAGO
        String novoStatus = gastoFuturo.getStatus().equals("PAGO") ? "PENDENTE" : "PAGO";
        gastoFuturo.setStatus(novoStatus);
        
        if (gastoFuturoDAO.update(gastoFuturo)) {
            carregarGastosFuturos();
        }
    }

    @Override
    public void onItemLongClick(GastoFuturo gastoFuturo) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle(R.string.excluir_gasto_futuro)
                .setMessage(R.string.confirmar_exclusao_gasto_futuro)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    if (gastoFuturoDAO.delete(gastoFuturo.getId())) {
                        carregarGastosFuturos();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
} 
package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.GastoAdapter;
import com.example.myapplication.dao.GastoDAO;
import com.example.myapplication.models.Gasto;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GastoActivity extends AppCompatActivity {

    private static final String TAG = "GastoActivity";
    private RecyclerView recyclerView;
    private GastoAdapter adapter;
    private GastoDAO gastoDAO;
    private ProgressBar progressBar;
    private TextView txtEmpty;
    private FloatingActionButton fabAdd;
    private int usuarioId = 1; // Temporário, deve vir do login
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());
    private static final int REQUEST_ADD_GASTO = 1;
    private static final int REQUEST_EDIT_GASTO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gasto);

        try {
            // Configurar Toolbar
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Gastos");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            // Inicializar views
            initViews();

            // Configurar RecyclerView
            setupRecyclerView();

            // Inicializar DAO
            gastoDAO = new GastoDAO(this);

            // Carregar dados
            loadGastos();

            // Configurar FAB
            fabAdd.setOnClickListener(v -> {
                Intent intent = new Intent(this, AdicionarGastoActivity.class);
                startActivityForResult(intent, REQUEST_ADD_GASTO);
            });
        } catch (Exception e) {
            Log.e(TAG, "Erro ao criar activity", e);
            Toast.makeText(this, "Erro ao inicializar a tela", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_ADD_GASTO || requestCode == REQUEST_EDIT_GASTO) 
            && resultCode == RESULT_OK) {
            // Recarregar lista de gastos
            loadGastos();
        }
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        txtEmpty = findViewById(R.id.txtEmpty);
        fabAdd = findViewById(R.id.fabAdd);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GastoAdapter();
        recyclerView.setAdapter(adapter);
        
        // Configurar listeners do adapter
        adapter.setOnGastoClickListener(new GastoAdapter.OnGastoClickListener() {
            @Override
            public void onEditClick(Gasto gasto) {
                editarGasto(gasto);
            }

            @Override
            public void onDeleteClick(Gasto gasto) {
                confirmarExclusao(gasto);
            }
        });
    }

    private void editarGasto(Gasto gasto) {
        Intent intent = new Intent(this, AdicionarGastoActivity.class);
        intent.putExtra("gasto", gasto);
        startActivityForResult(intent, REQUEST_EDIT_GASTO);
    }

    private void confirmarExclusao(Gasto gasto) {
        new AlertDialog.Builder(this)
            .setTitle("Excluir Gasto")
            .setMessage("Tem certeza que deseja excluir este gasto?")
            .setPositiveButton("Sim", (dialog, which) -> excluirGasto(gasto))
            .setNegativeButton("Não", null)
            .show();
    }

    private void excluirGasto(Gasto gasto) {
        executor.execute(() -> {
            try {
                boolean sucesso = gastoDAO.delete(gasto.getId());
                handler.post(() -> {
                    if (sucesso) {
                        Toast.makeText(this, "Gasto excluído com sucesso", Toast.LENGTH_SHORT).show();
                        loadGastos();
                    } else {
                        Toast.makeText(this, "Erro ao excluir gasto", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "Erro ao excluir gasto", e);
                handler.post(() -> 
                    Toast.makeText(this, "Erro ao excluir gasto: " + e.getMessage(), 
                        Toast.LENGTH_SHORT).show()
                );
            }
        });
    }

    private void loadGastos() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        txtEmpty.setVisibility(View.GONE);

        executor.execute(() -> {
            try {
                // Carregar gastos do banco em background
                List<Gasto> gastos = gastoDAO.list(usuarioId);

                // Atualizar UI na thread principal
                handler.post(() -> {
                    progressBar.setVisibility(View.GONE);

                    if (gastos == null) {
                        showError("Erro ao carregar gastos");
                        return;
                    }

                    if (gastos.isEmpty()) {
                        txtEmpty.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        txtEmpty.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        adapter.setGastos(gastos);
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "Erro ao carregar gastos", e);
                handler.post(() -> showError("Erro ao carregar gastos: " + e.getMessage()));
            }
        });
    }

    private void showError(String message) {
        progressBar.setVisibility(View.GONE);
        txtEmpty.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
        if (gastoDAO != null) {
            gastoDAO.close();
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
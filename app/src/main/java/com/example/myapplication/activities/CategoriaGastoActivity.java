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
import com.example.myapplication.adapters.CategoriaGastoAdapter;
import com.example.myapplication.dao.CategoriaGastoDAO;
import com.example.myapplication.models.CategoriaGasto;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CategoriaGastoActivity extends AppCompatActivity {

    private static final String TAG = "CategoriaGastoActivity";
    private RecyclerView recyclerView;
    private CategoriaGastoAdapter adapter;
    private CategoriaGastoDAO categoriaGastoDAO;
    private ProgressBar progressBar;
    private TextView txtEmpty;
    private FloatingActionButton fabAdd;
    private int usuarioId = 1; // Temporário, deve vir do login
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());
    private static final int REQUEST_ADD_CATEGORIA = 1;
    private static final int REQUEST_EDIT_CATEGORIA = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria_gasto);

        try {
            // Configurar Toolbar
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Categorias de Gasto");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            // Inicializar views
            initViews();

            // Configurar RecyclerView
            setupRecyclerView();

            // Inicializar DAO
            categoriaGastoDAO = new CategoriaGastoDAO(this);

            // Carregar dados
            loadCategorias();

            // Configurar FAB
            fabAdd.setOnClickListener(v -> {
                Intent intent = new Intent(this, AdicionarCategoriaActivity.class);
                startActivityForResult(intent, REQUEST_ADD_CATEGORIA);
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
        if ((requestCode == REQUEST_ADD_CATEGORIA || requestCode == REQUEST_EDIT_CATEGORIA) 
            && resultCode == RESULT_OK) {
            // Recarregar lista de categorias
            loadCategorias();
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
        adapter = new CategoriaGastoAdapter();
        recyclerView.setAdapter(adapter);
        
        // Configurar listeners do adapter
        adapter.setOnCategoriaClickListener(new CategoriaGastoAdapter.OnCategoriaClickListener() {
            @Override
            public void onEditClick(CategoriaGasto categoria) {
                editarCategoria(categoria);
            }

            @Override
            public void onDeleteClick(CategoriaGasto categoria) {
                confirmarExclusao(categoria);
            }
        });
    }

    private void editarCategoria(CategoriaGasto categoria) {
        Intent intent = new Intent(this, AdicionarCategoriaActivity.class);
        intent.putExtra("categoria", categoria);
        startActivityForResult(intent, REQUEST_EDIT_CATEGORIA);
    }

    private void confirmarExclusao(CategoriaGasto categoria) {
        new AlertDialog.Builder(this)
            .setTitle("Excluir Categoria")
            .setMessage("Tem certeza que deseja excluir a categoria '" + categoria.getNomeCategoria() + "'?")
            .setPositiveButton("Sim", (dialog, which) -> excluirCategoria(categoria))
            .setNegativeButton("Não", null)
            .show();
    }

    private void excluirCategoria(CategoriaGasto categoria) {
        executor.execute(() -> {
            try {
                boolean sucesso = categoriaGastoDAO.delete(categoria.getIdCategoria(), usuarioId);
                handler.post(() -> {
                    if (sucesso) {
                        Toast.makeText(this, "Categoria excluída com sucesso", Toast.LENGTH_SHORT).show();
                        loadCategorias();
                    } else {
                        Toast.makeText(this, "Erro ao excluir categoria", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "Erro ao excluir categoria", e);
                handler.post(() -> 
                    Toast.makeText(this, "Erro ao excluir categoria: " + e.getMessage(), 
                        Toast.LENGTH_SHORT).show()
                );
            }
        });
    }

    private void loadCategorias() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        txtEmpty.setVisibility(View.GONE);

        executor.execute(() -> {
            try {
                // Carregar categorias do banco em background
                List<CategoriaGasto> categorias = categoriaGastoDAO.findAll(usuarioId);

                // Atualizar UI na thread principal
                handler.post(() -> {
                    progressBar.setVisibility(View.GONE);

                    if (categorias == null) {
                        showError("Erro ao carregar categorias");
                        return;
                    }

                    if (categorias.isEmpty()) {
                        txtEmpty.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        txtEmpty.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        adapter.setCategorias(categorias);
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, "Erro ao carregar categorias", e);
                handler.post(() -> showError("Erro ao carregar categorias: " + e.getMessage()));
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
        if (categoriaGastoDAO != null) {
            categoriaGastoDAO.close();
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
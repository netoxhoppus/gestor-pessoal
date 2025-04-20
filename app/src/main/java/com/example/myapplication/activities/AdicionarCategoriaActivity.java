package com.example.myapplication.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.R;
import com.example.myapplication.dao.CategoriaGastoDAO;
import com.example.myapplication.models.CategoriaGasto;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AdicionarCategoriaActivity extends AppCompatActivity {

    private TextInputLayout inputLayoutNome;
    private TextInputEditText edtNome;
    private MaterialButton btnSalvar;
    private CategoriaGastoDAO categoriaGastoDAO;
    private int usuarioId = 1; // Temporário, deve vir do login
    private CategoriaGasto categoriaParaEditar;
    private boolean modoEdicao = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_categoria);

        // Verificar se é modo de edição
        if (getIntent().hasExtra("categoria")) {
            modoEdicao = true;
            categoriaParaEditar = (CategoriaGasto) getIntent().getSerializableExtra("categoria");
        }

        // Configurar Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(modoEdicao ? "Editar Categoria" : "Nova Categoria");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Inicializar views
        inputLayoutNome = findViewById(R.id.inputLayoutNome);
        edtNome = findViewById(R.id.edtNome);
        btnSalvar = findViewById(R.id.btnSalvar);

        // Inicializar DAO
        categoriaGastoDAO = new CategoriaGastoDAO(this);

        // Se for edição, preencher campos
        if (modoEdicao && categoriaParaEditar != null) {
            edtNome.setText(categoriaParaEditar.getDescricao());
        }

        // Configurar clique do botão salvar
        btnSalvar.setOnClickListener(v -> salvarCategoria());
    }

    private void salvarCategoria() {
        String nome = edtNome.getText().toString().trim();

        if (nome.isEmpty()) {
            inputLayoutNome.setError("Digite o nome da categoria");
            return;
        }

        boolean sucesso;
        
        if (modoEdicao) {
            categoriaParaEditar.setDescricao(nome);
            sucesso = categoriaGastoDAO.update(categoriaParaEditar);
        } else {
            CategoriaGasto categoria = new CategoriaGasto();
            categoria.setDescricao(nome);
            categoria.setUsuarioId(usuarioId);
            sucesso = categoriaGastoDAO.insert(categoria) > 0;
        }

        if (sucesso) {
            Toast.makeText(this, 
                modoEdicao ? "Categoria atualizada com sucesso" : "Categoria salva com sucesso", 
                Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, 
                modoEdicao ? "Erro ao atualizar categoria" : "Erro ao salvar categoria", 
                Toast.LENGTH_SHORT).show();
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
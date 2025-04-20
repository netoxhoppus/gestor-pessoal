package com.example.myapplication.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.R;
import com.example.myapplication.dao.CategoriaGastoDAO;
import com.example.myapplication.dao.GastoDAO;
import com.example.myapplication.models.CategoriaGasto;
import com.example.myapplication.models.Gasto;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AdicionarGastoActivity extends AppCompatActivity {

    private TextInputLayout inputLayoutDescricao;
    private TextInputLayout inputLayoutValor;
    private TextInputLayout inputLayoutData;
    private TextInputLayout inputLayoutCategoria;
    private TextInputEditText edtDescricao;
    private TextInputEditText edtValor;
    private TextInputEditText edtData;
    private AutoCompleteTextView spinnerCategoria;
    private MaterialButton btnSalvar;
    private GastoDAO gastoDAO;
    private CategoriaGastoDAO categoriaGastoDAO;
    private Gasto gastoParaEditar;
    private boolean modoEdicao = false;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_gasto);

        // Verificar se é modo de edição
        if (getIntent().hasExtra("gasto")) {
            modoEdicao = true;
            gastoParaEditar = (Gasto) getIntent().getSerializableExtra("gasto");
        }

        // Configurar Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(modoEdicao ? "Editar Gasto" : "Novo Gasto");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Inicializar componentes
        initViews();
        initDatePicker();
        
        // Inicializar DAOs
        gastoDAO = new GastoDAO(this);
        categoriaGastoDAO = new CategoriaGastoDAO(this);

        // Carregar categorias
        carregarCategorias();

        // Se for edição, preencher campos
        if (modoEdicao && gastoParaEditar != null) {
            preencherCampos();
        }

        // Configurar clique do botão salvar
        btnSalvar.setOnClickListener(v -> salvarGasto());
    }

    private void initViews() {
        inputLayoutDescricao = findViewById(R.id.inputLayoutDescricao);
        inputLayoutValor = findViewById(R.id.inputLayoutValor);
        inputLayoutData = findViewById(R.id.inputLayoutData);
        inputLayoutCategoria = findViewById(R.id.inputLayoutCategoria);
        edtDescricao = findViewById(R.id.edtDescricao);
        edtValor = findViewById(R.id.edtValor);
        edtData = findViewById(R.id.edtData);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        btnSalvar = findViewById(R.id.btnSalvar);
    }

    private void initDatePicker() {
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("pt", "BR"));

        edtData.setOnClickListener(v -> {
            new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                edtData.setText(dateFormat.format(calendar.getTime()));
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void carregarCategorias() {
        List<CategoriaGasto> categorias = categoriaGastoDAO.findAll(1); // TODO: Usar ID do usuário logado
        if (categorias != null && !categorias.isEmpty()) {
            ArrayAdapter<CategoriaGasto> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_dropdown_item_1line, categorias);
            spinnerCategoria.setAdapter(adapter);
        } else {
            Toast.makeText(this, "Erro ao carregar categorias", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void preencherCampos() {
        edtDescricao.setText(gastoParaEditar.getDescricao());
        edtValor.setText(String.valueOf(gastoParaEditar.getValor()));
        edtData.setText(gastoParaEditar.getDataFormatada());
        
        // Selecionar categoria correta no spinner
        for (int i = 0; i < spinnerCategoria.getAdapter().getCount(); i++) {
            CategoriaGasto categoria = (CategoriaGasto) spinnerCategoria.getAdapter().getItem(i);
            if (categoria.getIdCategoria() == gastoParaEditar.getCategoriaId()) {
                spinnerCategoria.setText(categoria.toString(), false);
                break;
            }
        }
    }

    private void salvarGasto() {
        // Validar campos
        if (!validarCampos()) {
            return;
        }

        // Obter valores dos campos
        String descricao = edtDescricao.getText().toString().trim();
        double valor = Double.parseDouble(edtValor.getText().toString().trim());
        String data = edtData.getText().toString().trim();
        
        // Encontrar a categoria selecionada
        CategoriaGasto categoriaSelecionada = null;
        String categoriaTexto = spinnerCategoria.getText().toString().trim();
        for (int i = 0; i < spinnerCategoria.getAdapter().getCount(); i++) {
            CategoriaGasto categoria = (CategoriaGasto) spinnerCategoria.getAdapter().getItem(i);
            if (categoria.toString().equals(categoriaTexto)) {
                categoriaSelecionada = categoria;
                break;
            }
        }
        
        if (categoriaSelecionada == null) {
            Toast.makeText(this, "Erro: categoria não encontrada", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean sucesso;
        
        if (modoEdicao) {
            gastoParaEditar.setDescricao(descricao);
            gastoParaEditar.setValor(valor);
            gastoParaEditar.setData(data);
            gastoParaEditar.setCategoriaId(categoriaSelecionada.getIdCategoria());
            sucesso = gastoDAO.update(gastoParaEditar);
        } else {
            Gasto gasto = new Gasto();
            gasto.setDescricao(descricao);
            gasto.setValor(valor);
            gasto.setData(data);
            gasto.setCategoriaId(categoriaSelecionada.getIdCategoria());
            gasto.setUsuarioId(1); // TODO: Usar ID do usuário logado
            sucesso = gastoDAO.insert(gasto) > 0;
        }

        if (sucesso) {
            Toast.makeText(this, 
                modoEdicao ? "Gasto atualizado com sucesso" : "Gasto salvo com sucesso", 
                Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, 
                modoEdicao ? "Erro ao atualizar gasto" : "Erro ao salvar gasto", 
                Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validarCampos() {
        boolean isValid = true;

        String descricao = edtDescricao.getText().toString().trim();
        String valor = edtValor.getText().toString().trim();
        String data = edtData.getText().toString().trim();
        String categoria = spinnerCategoria.getText().toString().trim();

        if (descricao.isEmpty()) {
            inputLayoutDescricao.setError("Digite a descrição");
            isValid = false;
        } else {
            inputLayoutDescricao.setError(null);
        }

        if (valor.isEmpty()) {
            inputLayoutValor.setError("Digite o valor");
            isValid = false;
        } else {
            try {
                Double.parseDouble(valor);
                inputLayoutValor.setError(null);
            } catch (NumberFormatException e) {
                inputLayoutValor.setError("Valor inválido");
                isValid = false;
            }
        }

        if (data.isEmpty()) {
            inputLayoutData.setError("Selecione a data");
            isValid = false;
        } else {
            inputLayoutData.setError(null);
        }

        if (categoria.isEmpty()) {
            inputLayoutCategoria.setError("Selecione a categoria");
            isValid = false;
        } else {
            inputLayoutCategoria.setError(null);
        }

        return isValid;
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
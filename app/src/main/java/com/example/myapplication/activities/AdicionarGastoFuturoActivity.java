package com.example.myapplication.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.R;
import com.example.myapplication.dao.CategoriaGastoDAO;
import com.example.myapplication.dao.GastoFuturoDAO;
import com.example.myapplication.models.CategoriaGasto;
import com.example.myapplication.models.GastoFuturo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdicionarGastoFuturoActivity extends AppCompatActivity {

    private EditText editTextDescricao;
    private EditText editTextValor;
    private EditText editTextData;
    private Spinner spinnerCategoria;
    private Switch switchRecorrente;
    private Spinner spinnerPeriodoRecorrencia;
    private GastoFuturoDAO gastoFuturoDAO;
    private CategoriaGastoDAO categoriaGastoDAO;
    private SimpleDateFormat dateFormat;
    private int gastoFuturoId = -1;
    private GastoFuturo gastoFuturoAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_gasto_futuro);

        // Configura a Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.adicionar_gasto_futuro);
        }

        // Inicializa os componentes
        initViews();
        initDatePicker();
        
        // Inicializa os DAOs
        gastoFuturoDAO = new GastoFuturoDAO(this);
        categoriaGastoDAO = new CategoriaGastoDAO(this);
        
        // Carrega as categorias
        carregarCategorias();

        // Configura o modo de edição se necessário
        gastoFuturoId = getIntent().getIntExtra("gasto_futuro_id", -1);
        if (gastoFuturoId != -1) {
            try {
                gastoFuturoAtual = gastoFuturoDAO.get(gastoFuturoId);
                if (gastoFuturoAtual != null) {
                    preencherCampos(gastoFuturoAtual);
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().setTitle(R.string.editar_gasto);
                    }
                } else {
                    Toast.makeText(this, R.string.gasto_futuro_nao_encontrado, Toast.LENGTH_SHORT).show();
                    finish();
                }
            } catch (Exception e) {
                Toast.makeText(this, R.string.erro_carregar_gasto_futuro, Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        // Configura o switch de recorrência
        switchRecorrente.setOnCheckedChangeListener((buttonView, isChecked) -> {
            spinnerPeriodoRecorrencia.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });

        // Configura o botão de salvar
        findViewById(R.id.buttonSalvar).setOnClickListener(v -> salvarGasto());
    }

    private void initViews() {
        editTextDescricao = findViewById(R.id.editTextDescricao);
        editTextValor = findViewById(R.id.editTextValor);
        editTextData = findViewById(R.id.editTextData);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        switchRecorrente = findViewById(R.id.switchRecorrente);
        spinnerPeriodoRecorrencia = findViewById(R.id.spinnerPeriodoRecorrencia);

        // Configura o spinner de período de recorrência
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.periodos_recorrencia, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPeriodoRecorrencia.setAdapter(adapter);
        spinnerPeriodoRecorrencia.setVisibility(View.GONE);
    }

    private void initDatePicker() {
        dateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("pt", "BR"));
        editTextData.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            if (!editTextData.getText().toString().isEmpty()) {
                try {
                    Date date = dateFormat.parse(editTextData.getText().toString());
                    calendar.setTime(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, year, month, dayOfMonth) -> {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, month, dayOfMonth);
                        editTextData.setText(dateFormat.format(selectedDate.getTime()));
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });
    }

    private void carregarCategorias() {
        // TODO: Pegar o ID do usuário logado
        int usuarioId = 1; // Temporário
        
        List<CategoriaGasto> categorias = categoriaGastoDAO.findAll(usuarioId);
        ArrayAdapter<CategoriaGasto> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categorias);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapter);
    }

    private void preencherCampos(GastoFuturo gastoFuturo) {
        editTextDescricao.setText(gastoFuturo.getDescricao());
        editTextValor.setText(String.valueOf(gastoFuturo.getValor()));
        editTextData.setText(dateFormat.format(gastoFuturo.getDataVencimento()));
        
        // Seleciona a categoria correta no spinner
        ArrayAdapter<CategoriaGasto> adapter = (ArrayAdapter<CategoriaGasto>) spinnerCategoria.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).getIdCategoria() == gastoFuturo.getCategoriaId()) {
                spinnerCategoria.setSelection(i);
                break;
            }
        }

        // Configura a recorrência
        switchRecorrente.setChecked(gastoFuturo.isRecorrente());
        if (gastoFuturo.isRecorrente()) {
            spinnerPeriodoRecorrencia.setVisibility(View.VISIBLE);
            spinnerPeriodoRecorrencia.setSelection(
                    gastoFuturo.getPeriodoRecorrencia().equals("MENSAL") ? 0 : 1
            );
        }
    }

    private void salvarGasto() {
        if (!validarCampos()) {
            return;
        }

        try {
            GastoFuturo gastoFuturo = new GastoFuturo();
            if (gastoFuturoId != -1) {
                gastoFuturo.setId(gastoFuturoId);
            }

            gastoFuturo.setDescricao(editTextDescricao.getText().toString());
            gastoFuturo.setValor(Double.parseDouble(editTextValor.getText().toString().replace(",", ".")));
            gastoFuturo.setDataVencimento(dateFormat.parse(editTextData.getText().toString()));
            
            CategoriaGasto categoriaSelecionada = (CategoriaGasto) spinnerCategoria.getSelectedItem();
            gastoFuturo.setCategoriaId(categoriaSelecionada.getIdCategoria());
            
            // TODO: Pegar o ID do usuário logado
            gastoFuturo.setUsuarioId(1); // Temporário
            
            gastoFuturo.setStatus("PENDENTE");
            gastoFuturo.setRecorrente(switchRecorrente.isChecked());
            if (switchRecorrente.isChecked()) {
                gastoFuturo.setPeriodoRecorrencia(
                        spinnerPeriodoRecorrencia.getSelectedItemPosition() == 0 ? "MENSAL" : "ANUAL"
                );
            }

            boolean sucesso;
            if (gastoFuturoId != -1) {
                sucesso = gastoFuturoDAO.update(gastoFuturo);
            } else {
                sucesso = gastoFuturoDAO.insert(gastoFuturo) > 0;
            }

            if (sucesso) {
                Toast.makeText(this, "Gasto futuro salvo com sucesso!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Erro ao salvar gasto futuro", Toast.LENGTH_SHORT).show();
            }

        } catch (ParseException e) {
            Toast.makeText(this, "Data inválida", Toast.LENGTH_SHORT).show();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Valor inválido", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validarCampos() {
        if (editTextDescricao.getText().toString().trim().isEmpty()) {
            editTextDescricao.setError("Descrição é obrigatória");
            return false;
        }

        if (editTextValor.getText().toString().trim().isEmpty()) {
            editTextValor.setError("Valor é obrigatório");
            return false;
        }

        if (editTextData.getText().toString().trim().isEmpty()) {
            editTextData.setError("Data é obrigatória");
            return false;
        }

        if (spinnerCategoria.getSelectedItem() == null) {
            Toast.makeText(this, "Selecione uma categoria", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
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
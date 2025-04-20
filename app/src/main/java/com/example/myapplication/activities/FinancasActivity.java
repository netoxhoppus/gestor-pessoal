package com.example.myapplication.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myapplication.R;
import com.example.myapplication.adapters.FinancasViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class FinancasActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financas);

        // Configurar Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Finanças");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Inicializar views
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        // Configurar ViewPager e TabLayout
        setupViewPager();
    }

    private void setupViewPager() {
        FinancasViewPagerAdapter adapter = new FinancasViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // Configurar as tabs
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Gastos");
                    break;
                case 1:
                    tab.setText("Futuros");
                    break;
                case 2:
                    tab.setText("Categorias");
                    break;
                case 3:
                    tab.setText("Contas");
                    break;
                case 4:
                    tab.setText("Folha");
                    break;
            }
        }).attach();
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
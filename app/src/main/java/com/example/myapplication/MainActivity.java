package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.example.myapplication.activities.GastoActivity;
import com.example.myapplication.activities.DisciplinaActivity;

public class MainActivity extends AppCompatActivity {

    private CardView cardFinancas;
    private CardView cardFaculdade;
    private TextView txtGreeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize views
        initViews();
        
        // Setup click listeners
        setupClickListeners();
        
        // Update greeting
        updateGreeting();
    }

    private void initViews() {
        cardFinancas = findViewById(R.id.cardFinancas);
        cardFaculdade = findViewById(R.id.cardFaculdade);
        txtGreeting = findViewById(R.id.txtGreeting);
    }

    private void setupClickListeners() {
        cardFinancas.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GastoActivity.class);
            startActivity(intent);
        });

        cardFaculdade.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DisciplinaActivity.class);
            startActivity(intent);
        });
    }

    private void updateGreeting() {
        // TODO: Get actual username from preferences or login
        String username = "netox";
        txtGreeting.setText(getString(R.string.greeting_message, username));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            // TODO: Implement logout
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
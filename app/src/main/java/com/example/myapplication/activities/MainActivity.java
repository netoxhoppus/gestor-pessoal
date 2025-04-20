package com.example.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.myapplication.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configurar saudação
        TextView txtGreeting = findViewById(R.id.txtGreeting);
        txtGreeting.setText("Olá, netox");

        // Configurar cards
        CardView cardFinancas = findViewById(R.id.cardFinancas);
        CardView cardFaculdade = findViewById(R.id.cardFaculdade);

        cardFinancas.setOnClickListener(v -> {
            Intent intent = new Intent(this, FinancasActivity.class);
            startActivity(intent);
        });

        cardFaculdade.setOnClickListener(v -> {
            Intent intent = new Intent(this, FaculdadeActivity.class);
            startActivity(intent);
        });
    }
} 
package com.example.cuentabolas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.cuentabolas.Juego;
import com.example.cuentabolas.R;

public class MainActivity extends AppCompatActivity {

    private Button btEmpezar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btEmpezar = findViewById(R.id.btEmpezar);
        btEmpezar.setOnClickListener(view -> {
            startActivity(new Intent(this, Juego.class));
        });
    }
}
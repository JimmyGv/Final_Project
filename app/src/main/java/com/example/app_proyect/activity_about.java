package com.example.app_proyect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class activity_about extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Button btnReg = findViewById(R.id.btnAboutRegresar);

        btnReg.setOnClickListener(v -> {
            Intent intento = new Intent(v.getContext(), activity_login.class);
            startActivity(intento);
        });
    }
}
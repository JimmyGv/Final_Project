package com.example.app_proyect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class activity_materias_student extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materias_student);

        Button buttonReg = findViewById(R.id.buttonMateriasStudentRegresar);

        buttonReg.setOnClickListener(v -> {
            Intent intento = new Intent(v.getContext(), activity_students_view.class);
            startActivity(intento);
        });
    }
}
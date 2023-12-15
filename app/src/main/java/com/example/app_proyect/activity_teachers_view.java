package com.example.app_proyect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.app_proyect.model.userActive;

public class activity_teachers_view extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachers_view);

        Button btnClose = findViewById(R.id.btnTeacherCloseSesion);

        btnClose.setOnClickListener(v -> {
            Intent intento = new Intent(v.getContext(), activity_login.class);
            startActivity(intento);
        });


        Button btnCalif = findViewById(R.id.btnTeacherCalif);

        btnCalif.setOnClickListener(v -> {
            Intent intento = new Intent(v.getContext(), activity_califi_docente.class);
            startActivity(intento);
        });

        Button btnAlumnos = findViewById(R.id.btnTeachersStudents);

        btnAlumnos.setOnClickListener(v -> {
            Intent intento = new Intent(v.getContext(), activity_alumnos_inscritos.class);
            startActivity(intento);
        });

        Button btnRubro = findViewById(R.id.btnTeacherRubros);

        btnRubro.setOnClickListener(v -> {
            Intent intento = new Intent(v.getContext(), activity_modify_rubro.class);
            startActivity(intento);
        });
    }
}
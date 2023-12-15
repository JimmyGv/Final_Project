package com.example.app_proyect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.app_proyect.model.userActive;

import org.w3c.dom.Text;

public class activity_admin_view extends AppCompatActivity {
    TextView usuar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view);
        usuar =findViewById(R.id.tvTextUserAdminView);
        userActive u = new userActive();
        usuar.setText(u.getNombre());
        Button btnAdmin = findViewById(R.id.btnAdminViewCloseSesion);

        btnAdmin.setOnClickListener(v -> {
            Intent intento = new Intent(v.getContext(), activity_login.class);
            startActivity(intento);
        });

        Button btnDocentes = findViewById(R.id.btnAdminDocentes);

        btnDocentes.setOnClickListener(v -> {
            Intent intento = new Intent(v.getContext(), activity_modify_teachers.class);
            startActivity(intento);
        });

        Button btnUsers = findViewById(R.id.btnAdminUsuarios);

        btnUsers.setOnClickListener(v -> {
            Intent intento = new Intent(v.getContext(), activity_see_users.class);
            startActivity(intento);
        });

        Button btnAlum = findViewById(R.id.btnAdminAlumnos);

        btnAlum.setOnClickListener(v -> {
            Intent intento = new Intent(v.getContext(), activity_modify_students.class);
            startActivity(intento);
        });

        Button btnAulas = findViewById(R.id.btnAdminAulas);

        btnAulas.setOnClickListener(v -> {
            Intent intento = new Intent(v.getContext(), activity_aulas.class);
            startActivity(intento);
        });

        Button btnMaterias = findViewById(R.id.btnAdminMaterias);

        btnMaterias.setOnClickListener(v -> {
            Intent intento = new Intent(v.getContext(), activity_modify_materias.class);
            startActivity(intento);
        });

        Button btnGroup = findViewById(R.id.btnAdminGrupos);

        btnGroup.setOnClickListener(v -> {
            Intent intento = new Intent(v.getContext(), activity_modify_group.class);
            startActivity(intento);
        });

        Button btnUsersMod = findViewById(R.id.buttonAdminVerU);

        btnUsersMod.setOnClickListener(v -> {
            Intent intento = new Intent(v.getContext(), activity_modify_users.class);
            startActivity(intento);
        });
    }
}
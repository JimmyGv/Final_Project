package com.example.app_proyect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.app_proyect.model.userActive;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;

public class activity_students_view extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseDatabase bd;
    DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_view);
        iniciarBd();
        auth = FirebaseAuth.getInstance();

        //auth.getCurrentUser();

        Button btnCalif = findViewById(R.id.btnStudentsCalif);

        btnCalif.setOnClickListener(v -> {
            Intent intento = new Intent(v.getContext(), activity_students_calif.class);
            startActivity(intento);
        });

        Button btnMateria = findViewById(R.id.btnStudentsMaterias);

        btnMateria.setOnClickListener(v -> {
            Intent intento = new Intent(v.getContext(), activity_materias_student.class);
            startActivity(intento);
        });


        Button btnClose = findViewById(R.id.btnStudentsCloseSesion);

        btnClose.setOnClickListener(v -> {
            Intent intento = new Intent(v.getContext(), activity_login.class);
            auth.signOut();
            finish();
            startActivity(intento);
        });
    }

    private void iniciarBd() {
        FirebaseApp.initializeApp(this);
        bd = FirebaseDatabase.getInstance();
        ref = bd.getReference();

    }

}
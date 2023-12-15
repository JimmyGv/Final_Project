package com.example.app_proyect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.app_proyect.model.aula;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class activity_crear_aulas extends AppCompatActivity {
    DatabaseReference ref;
    FirebaseDatabase bd;
    private EditText nombre,descripcion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_aulas);
        iniciarBd();
        nombre = findViewById(R.id.etModifyAulaNombre);
        descripcion = findViewById(R.id.etModifyAuDesc);

        Button buttonReg = findViewById(R.id.btnModiAulaRegresar);

        buttonReg.setOnClickListener(v -> {
            Intent intento = new Intent(v.getContext(), activity_aulas.class);
            startActivity(intento);
        });

        Button buttonCreate = findViewById(R.id.btnModifyAuSave);

        buttonCreate.setOnClickListener(v -> {
            String nmb = nombre.getText().toString();
            String desc = descripcion.getText().toString();

            if(nmb.equals("")||desc.equals("")){
                Toast.makeText(this,"Por favor llena los campos", Toast.LENGTH_SHORT).show();
            }
            else{
                guardar();
            }
        });

    }

    private void guardar() {
        aula a = new aula();
        String nombreG = nombre.getText().toString().trim();
        String desc = descripcion.getText().toString().trim();
        try {
            a.setIdAula(UUID.randomUUID().toString());
            a.setDescripcion(desc);
            a.setNombre(nombreG);
            ref.child("aula").child(a.getIdAula()).setValue(a);
            Toast.makeText(this,"Aula Creada", Toast.LENGTH_SHORT).show();
            limpiar();
        }catch (Exception e){
            Toast.makeText(this,"Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void limpiar() {
        nombre.setText("");
        descripcion.setText("");

    }

    private void iniciarBd() {
        FirebaseApp.initializeApp(this);
        bd = FirebaseDatabase.getInstance();
        ref = bd.getReference();

    }
}
package com.example.app_proyect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.app_proyect.model.aceptados;
import com.example.app_proyect.model.aula;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class activity_aulas extends AppCompatActivity {
    DatabaseReference ref;
    FirebaseDatabase bd;
    private EditText nombre,descripcion;
    private ListView listaA;

    aula aulaSelected;
    private List<aula> arrAulas = new ArrayList<>();
    ArrayAdapter<aula> arrayAdapterAulas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aulas);
        iniciarBd();
        nombre = findViewById(R.id.etAulaNombre);
        descripcion = findViewById(R.id.etAulaDescripcion);
        listaA = findViewById(R.id.listVerAulas);

        listar();


        listaA.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                aulaSelected = (aula) parent.getItemAtPosition(position);
                nombre.setText(aulaSelected.getNombre());
                descripcion.setText(aulaSelected.getDescripcion());
            }
        });

        Button buttonReg = findViewById(R.id.buttonSeeAulasReg);

        buttonReg.setOnClickListener(v -> {
            Intent intento = new Intent(v.getContext(), activity_admin_view.class);
            startActivity(intento);
        });

        Button buttonCrear = findViewById(R.id.buttonSeeAulasCrear);

        buttonCrear.setOnClickListener(v -> {
            Intent intento = new Intent(v.getContext(), activity_crear_aulas.class);
            startActivity(intento);
        });

        Button buttonEliminar = findViewById(R.id.btnSeeAulasElim);

        buttonEliminar.setOnClickListener(v -> {
            eliminar();
        });

        Button btnSave = findViewById(R.id.btnSeeAulasModif);

        btnSave.setOnClickListener(v -> {
            String nmb = nombre.getText().toString();
            String des = descripcion.getText().toString();
            if(nmb.equals("")||des.equals("")){
                Toast.makeText(this, "Por favor llene todos los campos", Toast.LENGTH_SHORT).show();
            }else {
                guardar();
            }
        });
    }

    private void listar() {
        ref.child("aula").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrAulas.clear();
                for(DataSnapshot obj:snapshot.getChildren()){
                    aula au = obj.getValue(aula.class);
                    arrAulas.add(au);
                    arrayAdapterAulas=new ArrayAdapter<aula>(activity_aulas.this,android.R.layout.simple_list_item_1,arrAulas);
                    listaA.setAdapter(arrayAdapterAulas);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void guardar() {
        aula a = new aula();
        try {
            a.setIdAula(aulaSelected.getIdAula());
            a.setDescripcion(aulaSelected.getDescripcion().toString().trim());
            a.setNombre(aulaSelected.getNombre().toString().trim());
            ref.child("aula").child(a.getIdAula()).setValue(a);
            Toast.makeText(this,"Aula Actualizada", Toast.LENGTH_SHORT).show();
            limpiar();
        }catch (Exception e){
            Toast.makeText(this,"Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void eliminar() {
        aula u = new aula();
        try {
            u.setIdAula(aulaSelected.getIdAula());
            ref.child("aula").child(u.getIdAula()).removeValue();
            limpiar();
            Toast.makeText(this, "Registro Eliminado", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(this, "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
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
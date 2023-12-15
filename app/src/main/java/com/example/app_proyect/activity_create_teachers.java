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
import android.widget.Spinner;
import android.widget.Toast;

import com.example.app_proyect.model.aceptados;
import com.example.app_proyect.model.docentes;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class activity_create_teachers extends AppCompatActivity {

    DatabaseReference ref;
    FirebaseDatabase bd;
    Spinner user;
    EditText especialidad, estudios;
    private List<aceptados> lista = new ArrayList<aceptados>();
    ArrayAdapter<aceptados> arrayAdapterUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_teachers);
        iniciarBd();

        especialidad = findViewById(R.id.etSpeciallity);
        estudios = findViewById(R.id.etStudies);
        user = findViewById(R.id.spinner);
        listar();


        Button buttonReg = findViewById(R.id.btnCreateTeacherRegresar);
        buttonReg.setOnClickListener(v -> {
            Intent intento = new Intent(v.getContext(), activity_modify_teachers.class);
            startActivity(intento);
        });

        Button buttonCr = findViewById(R.id.btnCreateTeacher);
        buttonCr.setOnClickListener(v -> {
            String val, val1;
            val = especialidad.getText().toString();
            val1 = estudios.getText().toString();
            if(val.equals("")||val1.equals("")){
                Toast.makeText(this,"Por favor llene todos los campos", Toast.LENGTH_SHORT).show();
            }else{
                crear();
            }
        });
    }

    private void listar() {
        ref.child("aceptados").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lista.clear();
                for(DataSnapshot obj : snapshot.getChildren()){
                    aceptados a = obj.getValue(aceptados.class);
                    if(a.getUserType().equals("Docente")) {
                        lista.add(a);
                        arrayAdapterUs = new ArrayAdapter<aceptados>(activity_create_teachers.this, android.R.layout.simple_spinner_dropdown_item, lista);
                        user.setAdapter(arrayAdapterUs);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void crear() {
        docentes d = new docentes();
        aceptados acp = (aceptados) user.getSelectedItem();
        String idUser = acp.getId();
        try {
            ref.child("docentes").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean bandera = false;
                    for(DataSnapshot obj : snapshot.getChildren()) {
                        docentes a = obj.getValue(docentes.class);
                        if(a.getIdUsuario().equals(idUser)){
                            bandera = true;
                            break;
                        }
                    }
                    if(bandera){
                        Toast.makeText(activity_create_teachers.this,"No se puede crear un docente 2 veces", Toast.LENGTH_SHORT).show();
                    }else{
                        d.setIdDocente(UUID.randomUUID().toString());
                        d.setEstudios(estudios.getText().toString());
                        d.setEspecialidad(especialidad.getText().toString());
                        d.setIdUsuario(idUser);
                        ref.child("docentes").child(d.getIdUsuario()).setValue(d);
                        Toast.makeText(activity_create_teachers.this,"Docente agregado con exito", Toast.LENGTH_SHORT).show();
                        limpiar();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }catch (Exception e){
            Toast.makeText(this,"Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void limpiar() {
        estudios.setText("");
        especialidad.setText("");
    }

    private void iniciarBd() {
        FirebaseApp.initializeApp(this);
        bd = FirebaseDatabase.getInstance();
        ref = bd.getReference();

    }
}
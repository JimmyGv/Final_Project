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

public class activity_modify_teachers extends AppCompatActivity {
    DatabaseReference ref;
    FirebaseDatabase bd;
    Spinner usuarios;
    EditText especialidad, estudios;
    private List<aceptados> lista = new ArrayList<aceptados>();
    ArrayAdapter<aceptados> arrayAdapterUs;
    aceptados userSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_teachers);
        iniciarBd();

        usuarios = findViewById(R.id.spinnerModi);
        especialidad = findViewById(R.id.etSpeciallityModi);
        estudios = findViewById(R.id.etStudiesModi);

        listar();

        usuarios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userSelect = (aceptados) parent.getItemAtPosition(position);
                ref.child("docentes").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot obj: snapshot.getChildren()){
                            docentes doc = obj.getValue(docentes.class);
                            String val = doc.getIdUsuario();
                            if(val.equals(userSelect.getId())){
                                especialidad.setText(doc.getEspecialidad());
                                estudios.setText(doc.getEstudios());
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//--------------------------------------------------------------------------------------------------
        Button buttonReg = findViewById(R.id.btnModiTeacherRegresar);

        buttonReg.setOnClickListener(v -> {
            Intent intento = new Intent(v.getContext(), activity_admin_view.class);
            startActivity(intento);
        });

        Button buttonCreate = findViewById(R.id.btnModiTeacherCrear);

        buttonCreate.setOnClickListener(v -> {
            Intent intento = new Intent(v.getContext(), activity_create_teachers.class);
            startActivity(intento);
        });

        Button buttonElim = findViewById(R.id.btnModifyTeacherDelete);

        buttonElim.setOnClickListener(v -> {
            eliminar();
        });

        Button buttonCrear = findViewById(R.id.btnModifyTeacher);

        buttonCrear.setOnClickListener(v -> {
            String estud, esp;
            estud = estudios.getText().toString();
            esp = especialidad.getText().toString();

            if(estud.equals("")){
                estudios.setError("Required");
            }else{
                if(esp.equals("")){
                    especialidad.setError("Required");
                }
                else{
                    guardar();
                }
            }

        });
    }

    private void eliminar() {
        aceptados a = (aceptados)usuarios.getSelectedItem();
        String id = a.getId();
        ref.child("docentes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot obj : snapshot.getChildren()) {
                    docentes d = obj.getValue(docentes.class);
                    if (id.equals(d.getIdUsuario())) {
                        d.setIdDocente(d.getIdUsuario());
                        ref.child("docentes").child(d.getIdDocente()).removeValue();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void guardar() {
        aceptados a = (aceptados)usuarios.getSelectedItem();
        String id = a.getId();
        try {
            ref.child("docentes").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot obj : snapshot.getChildren()) {
                        docentes d = obj.getValue(docentes.class);
                        if (id.equals(d.getIdUsuario())) {
                            d.setIdDocente(d.getIdDocente());
                            d.setEspecialidad(especialidad.getText().toString().trim());
                            d.setEstudios(estudios.getText().toString().trim());
                            ref.child("docentes").child(d.getIdDocente()).setValue(d);
                            Toast.makeText(activity_modify_teachers.this, "Docente actualizado", Toast.LENGTH_SHORT).show();
                            estudios.setText("");
                            especialidad.setText("");
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch (Exception e){
            Toast.makeText(activity_modify_teachers.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void listar() {
        ref.child("docentes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lista.clear();
                for(DataSnapshot obj : snapshot.getChildren()){
                    docentes d = obj.getValue(docentes.class);
                    String id = d.getIdUsuario();
                    ref.child("aceptados").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot obj : snapshot.getChildren()){
                                aceptados a = obj.getValue(aceptados.class);
                                if(id.equals(a.getId())){
                                    lista.add(a);
                                    arrayAdapterUs = new ArrayAdapter<aceptados>(activity_modify_teachers.this, android.R.layout.simple_spinner_dropdown_item,lista);
                                    usuarios.setAdapter(arrayAdapterUs);
                                    break;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void iniciarBd() {
        FirebaseApp.initializeApp(this);
        bd = FirebaseDatabase.getInstance();
        ref = bd.getReference();

    }
}
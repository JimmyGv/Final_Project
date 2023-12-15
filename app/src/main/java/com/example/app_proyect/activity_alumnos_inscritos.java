package com.example.app_proyect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.app_proyect.model.aceptados;
import com.example.app_proyect.model.alumnos;
import com.example.app_proyect.model.inscritos;
import com.example.app_proyect.model.usuario;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class activity_alumnos_inscritos extends AppCompatActivity {

    FirebaseDatabase bd;
    DatabaseReference ref;
    ListView listV;
    private List<aceptados> listaInscritos = new ArrayList<aceptados>();
    ArrayAdapter<aceptados> arrayAdapterInscritos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumnos_inscritos);
        iniciarBd();
        listV = findViewById(R.id.listVerAlumnos);
        listar();

        Button buttonReg = findViewById(R.id.buttonAlumnosInsRegresar);

        buttonReg.setOnClickListener(v -> {
            Intent intento = new Intent(v.getContext(), activity_teachers_view.class);
            startActivity(intento);
        });
    }

    private void listar() {
        ref.child("inscritos").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaInscritos.clear();
                for (DataSnapshot obj : snapshot.getChildren()){
                    inscritos i = obj.getValue(inscritos.class);
                    String val = i.getIdAlumno();
                    ref.child("alumnos").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot obj : snapshot.getChildren()){
                                alumnos a = obj.getValue(alumnos.class);
                                if(a.getId().equals(val)){
                                    String id = a.getIdUsuario();
                                    ref.child("aceptados").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot obj : snapshot.getChildren()){
                                                aceptados b = obj.getValue(aceptados.class);
                                                if(b.getId().equals(id)){
                                                    listaInscritos.add(b);

                                                    arrayAdapterInscritos = new ArrayAdapter<aceptados>(activity_alumnos_inscritos.this,android.R.layout.simple_list_item_1,listaInscritos);
                                                    listV.setAdapter(arrayAdapterInscritos);
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
package com.example.app_proyect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.app_proyect.model.aceptados;
import com.example.app_proyect.model.alumnos;
import com.example.app_proyect.model.calificaciones;
import com.example.app_proyect.model.materias;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class activity_students_calif extends AppCompatActivity {
    FirebaseDatabase bd;
    DatabaseReference ref;
    ListView calificacion;
    List<String> conjuncion = new ArrayList<>();
    Spinner trimestre;

    ArrayAdapter<String> array;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_calif);
        iniciarBd();

        calificacion = findViewById(R.id.listVerCalif);
        trimestre = findViewById(R.id.spinnerStuCalifTr);
        ArrayAdapter<CharSequence> arr = ArrayAdapter.createFromResource(this,R.array.trimestre_grado, android.R.layout.simple_spinner_dropdown_item);
        arr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        trimestre.setAdapter(arr);
        listar();

        Button btnReg = findViewById(R.id.btnStudentsCalifRegresar);

        btnReg.setOnClickListener(v -> {
            Intent intento = new Intent(v.getContext(), activity_califi_docente.class);
            startActivity(intento);
        });
    }

    private void listar(){
        ref.child("calificaciones").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                conjuncion.clear();
                for(DataSnapshot obj: snapshot.getChildren()){
                    calificaciones cal = obj.getValue(calificaciones.class);
                    cal.getIdAlumno();
                    ref.child("alumnos").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot ob:snapshot.getChildren()){
                                alumnos al = ob.getValue(alumnos.class);
                                if(al.getId().equals(cal.getIdAlumno())){
                                    ref.child("aceptados").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for(DataSnapshot obj3:snapshot.getChildren()){
                                                aceptados a = obj3.getValue(aceptados.class);
                                                if(a.getId().equals(al.getIdUsuario())){
                                                    ref.child("materias").addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            for(DataSnapshot obj2:snapshot.getChildren()){
                                                                materias mat = obj2.getValue(materias.class);
                                                                if(cal.getMateria().equals(mat.getIdMateria())){
                                                                    conjuncion.add(a.getUsuario()+"     "+mat.getNombre()+"     "+cal.getCalificacion());
                                                                    array = new ArrayAdapter<>(activity_students_calif.this, android.R.layout.simple_list_item_1,conjuncion);
                                                                    calificacion.setAdapter(array);
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                                    break;
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
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
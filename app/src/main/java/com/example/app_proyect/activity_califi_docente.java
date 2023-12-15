package com.example.app_proyect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.app_proyect.model.aceptados;
import com.example.app_proyect.model.alumnos;
import com.example.app_proyect.model.calificaciones;
import com.example.app_proyect.model.materias;
import com.example.app_proyect.model.rubros;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class activity_califi_docente extends AppCompatActivity {
    private List<materias> listaAulas = new ArrayList<materias>();
    FirebaseDatabase bd;
    DatabaseReference ref;
    Spinner trimestre, materia, alum,rubro;
    ArrayAdapter<materias> arrayAdapterAu;
    ArrayAdapter<aceptados> arrayAdapterAc;
    private List<aceptados> aceptadosList = new ArrayList<>();
    ArrayAdapter<rubros> arrAdap;
    List<rubros> lista = new ArrayList<>();
    EditText asist,val,trab,exam;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        iniciarBd();
        setContentView(R.layout.activity_califi_docente);
        trimestre = findViewById(R.id.spinnerCalifDocTrim);
        materia = findViewById(R.id.spinnerCalifDocMat);
        alum = findViewById(R.id.spinnerCalifDocAlumno);
        rubro = findViewById(R.id.spinnerCalifDocRubro);
        asist = findViewById(R.id.etCalifDocAsist);
        val = findViewById(R.id.etCalifDocValores);
        trab = findViewById(R.id.etCalifDocTrabajos);
        exam = findViewById(R.id.etCalifDocExamen);


        ArrayAdapter<CharSequence> arr = ArrayAdapter.createFromResource(this,R.array.trimestre_grado, android.R.layout.simple_spinner_dropdown_item);
        arr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        trimestre.setAdapter(arr);
        listar();
        listarRub();
        listarAl();
//--------------------------------------------------------------------------------------
        Button btnClose = findViewById(R.id.buttonCalifDocRegresar);

        btnClose.setOnClickListener(v -> {
            Intent intento = new Intent(v.getContext(), activity_teachers_view.class);
            startActivity(intento);
        });

        Button btnCalif = findViewById(R.id.buttonCalifDocCalif);

        btnCalif.setOnClickListener(v -> {
            Intent intento = new Intent(v.getContext(), activity_students_calif.class);
            startActivity(intento);
        });

        Button btnSave = findViewById(R.id.buttonCalifDocSave);

        btnSave.setOnClickListener(v -> {
            if(exam.getText().toString().equals("")||trab.getText().toString().equals("")||asist.getText().toString().equals("")||val.getText().toString().equals("")){
                Toast.makeText(this, "LLene todos los campos", Toast.LENGTH_SHORT).show();
            }else {
                int varE = Integer.parseInt(exam.getText().toString());
                int varT = Integer.parseInt(trab.getText().toString());
                int varA = Integer.parseInt(asist.getText().toString());
                int val2 = Integer.parseInt(val.getText().toString());
                if(varE > 10){
                    exam.setError("No puede ser un numero mayor a 10");
                }else {
                    if(varT > 10){
                        trab.setError("No puede ser un numero mayor a 10");
                    }else {
                        if(varA > 10){
                            asist.setError("No puede ser un numero mayor a 10");
                        }else {
                            if(val2 > 10){
                                exam.setError("No puede ser un numero mayor a 10");
                            }else {
                                save();
                            }
                        }
                    }
                }
            }
        });

    }

    private void save() {
        aceptados ace = (aceptados)alum.getSelectedItem();
        String idUser = ace.getId();
        materias mat = (materias)materia.getSelectedItem();
        String idMat = mat.getIdMateria();
        rubros r = (rubros)rubro.getSelectedItem();
        int ast = Integer.parseInt(r.getAsistencia());
        int trbjs = Integer.parseInt(r.getTrabajos());
        int exmn = Integer.parseInt(r.getExamen());
        int vlrs = Integer.parseInt(r.getValores());

        int varE = Integer.parseInt(exam.getText().toString());
        int varT = Integer.parseInt(trab.getText().toString());
        int varA = Integer.parseInt(asist.getText().toString());
        int val2 = Integer.parseInt(val.getText().toString());

        int astn, trbj, xmn, vlr, calif_final;

        if(varE == 0 || varE<0){
            xmn = 0;
        }else{
            xmn = (exmn * varE)/10;
        }

        if(varT == 0 || varT<0){
            trbj = 0;
        }else{
            trbj = (trbjs * varT)/10;
        }

        if(varA == 0 || varA<0){
            astn = 0;
        }else{
            astn = (ast * varA)/10;
        }

        if(val2 == 0 || val2<0){
            vlr = 0;
        }else{
            vlr = (vlrs * val2)/10;
        }

        calif_final = astn + vlr + trbj+xmn;

        ref.child("alumnos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot obj: snapshot.getChildren()){
                    alumnos al = obj.getValue(alumnos.class);
                    if(al.getIdUsuario().equals(idUser)){
                        ref.child("calificaciones").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                boolean flag = false;
                                for(DataSnapshot obj1 : snapshot.getChildren()){
                                    calificaciones cal = obj1.getValue(calificaciones.class);
                                    if(al.getId().equals(cal.getIdAlumno())) {
                                        if (cal.getMateria().equals(idMat)) {
                                            cal.setId(cal.getId());
                                            cal.setAsistencia(astn+"");
                                            cal.setExamen(xmn+"");
                                            cal.setTrabajos(trbj+"");
                                            cal.setCalificacion(calif_final+"");
                                            cal.setValores(vlr+"");
                                            ref.child("calificaciones").child(cal.getId()).setValue(cal);
                                            limpiar();
                                            flag = true;
                                            break;
                                        }
                                        else{
                                            cal.setId(UUID.randomUUID().toString());
                                            cal.setAsistencia(astn+"");
                                            cal.setExamen(xmn+"");
                                            cal.setTrabajos(trbj+"");
                                            cal.setCalificacion(calif_final+"");
                                            cal.setValores(vlr+"");
                                            cal.setMateria(idMat);
                                            ref.child("calificaciones").child(cal.getId()).setValue(cal);
                                            limpiar();
                                            flag = true;
                                            break;
                                        }

                                    }
                                }
                                if(flag == false){
                                    calificaciones cal = new calificaciones();
                                    cal.setId(UUID.randomUUID().toString());
                                    cal.setAsistencia(astn+"");
                                    cal.setExamen(xmn+"");
                                    cal.setTrabajos(trbj+"");
                                    cal.setCalificacion(calif_final+"");
                                    cal.setValores(vlr+"");
                                    cal.setMateria(idMat);
                                    ref.child("calificaciones").child(cal.getId()).setValue(cal);
                                    limpiar();
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

    private void listar() {
        ref.child("materias").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaAulas.clear();
                for(DataSnapshot obj : snapshot.getChildren()){
                    materias a = obj.getValue(materias.class);
                    listaAulas.add(a);
                    arrayAdapterAu = new ArrayAdapter<materias>(activity_califi_docente.this,android.R.layout.simple_spinner_dropdown_item,listaAulas);
                    materia.setAdapter(arrayAdapterAu);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void limpiar(){
        asist.setText("");
        trab.setText("");
        val.setText("");
        exam.setText("");
    }
    private void listarRub() {
        ref.child("rubros").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lista.clear();
                for(DataSnapshot obj : snapshot.getChildren()){
                    rubros r = obj.getValue(rubros.class);
                    lista.add(r);
                    arrAdap =  new ArrayAdapter<>(activity_califi_docente.this, android.R.layout.simple_spinner_dropdown_item,lista);
                    rubro.setAdapter(arrAdap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void listarAl() {
        ref.child("alumnos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                aceptadosList.clear();
                for(DataSnapshot obj : snapshot.getChildren()){
                    alumnos a = obj.getValue(alumnos.class);
                    ref.child("aceptados").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot obj:snapshot.getChildren()){
                                aceptados acep = obj.getValue(aceptados.class);
                                if(a.getIdUsuario().equals(acep.getId())){
                                    aceptadosList.add(acep);
                                    arrayAdapterAc = new ArrayAdapter<>(activity_califi_docente.this, android.R.layout.simple_spinner_dropdown_item,aceptadosList);
                                    alum.setAdapter(arrayAdapterAc);
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
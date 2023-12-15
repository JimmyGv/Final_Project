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
import com.example.app_proyect.model.alumnos;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class activity_modify_students extends AppCompatActivity {

    EditText padre, madre, tel1,tel2, curp;
    Spinner users;
    DatabaseReference ref;
    FirebaseDatabase bd;
    aceptados usuarioSelect;
    alumnos alumnoSelect;
    private List<aceptados> lista = new ArrayList<aceptados>();
    ArrayAdapter<aceptados> arrayAdapterUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_students);
        iniciarBd();

        padre = findViewById(R.id.etModifyStParent2);
        madre = findViewById(R.id.etModifyStParent1);
        tel1 = findViewById(R.id.etModifyStPhone);
        tel2 = findViewById(R.id.etModifyStPhone2);
        users = findViewById(R.id.spinnerModifyStUser);
        curp = findViewById(R.id.etModifyStCurp);

        listar();

        users.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                usuarioSelect = (aceptados) parent.getItemAtPosition(position);
                ref.child("alumnos").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot obj: snapshot.getChildren()){
                            alumnos al = obj.getValue(alumnos.class);
                            if(usuarioSelect.getId().equals(al.getIdUsuario())){
                                padre.setText(al.getPadre());
                                madre.setText(al.getMadre());
                                curp.setText(al.getCurp());
                                tel1.setText(al.getNoTel1());
                                tel2.setText(al.getNoTel2());
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

        Button buttonReg = findViewById(R.id.btnModifyStudentsRegresar);

        buttonReg.setOnClickListener(v -> {
            Intent intento = new Intent(v.getContext(), activity_admin_view.class);
            startActivity(intento);
        });

        Button buttonCr = findViewById(R.id.btnModifyStudentsCreate);

        buttonCr.setOnClickListener(v -> {
            Intent intento = new Intent(v.getContext(), activity_create_students.class);
            startActivity(intento);
        });

        Button buttonDelete = findViewById(R.id.btnModifyStudentsElim);

        buttonDelete.setOnClickListener(v -> {
            eliminar();
        });

        Button buttonSave = findViewById(R.id.btnModifyStudents);

        buttonSave.setOnClickListener(v -> {
            String md,pd,tel,crp;
            md=madre.getText().toString();
            pd = padre.getText().toString();
            tel = tel1.getText().toString();
            crp = curp.getText().toString();
            if(md.equals("")){
                madre.setError("Required");
            } else {
                if(pd.equals("")){
                    padre.setError("Required");
                }else{
                    if(tel.equals("")){
                        tel1.setError("Required");
                    }else{
                        if(crp.equals("")){
                            curp.setError("Required");
                        }else{
                            save();
                        }
                    }
                }
            }
        });
    }

    private void listar() {
        ref.child("alumnos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lista.clear();
                for(DataSnapshot obj: snapshot.getChildren()){
                    alumnos alum = obj.getValue(alumnos.class);
                    ref.child("aceptados").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot obj : snapshot.getChildren()){
                                aceptados a = obj.getValue(aceptados.class);
                                if(a.getId().equals(alum.getIdUsuario())){
                                    lista.add(a);
                                    arrayAdapterUs = new ArrayAdapter<aceptados>(activity_modify_students.this, android.R.layout.simple_spinner_dropdown_item,lista);
                                    users.setAdapter(arrayAdapterUs);
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

    private void save() {
        aceptados acep = (aceptados) users.getSelectedItem();
        String idU = acep.getId();
        try{
            ref.child("alumnos").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot obj: snapshot.getChildren()){
                        alumnos a = obj.getValue(alumnos.class);
                        if (idU.equals(a.getIdUsuario())){
                            a.setId(a.getId());
                            a.setPadre(padre.getText().toString().trim());
                            a.setMadre(madre.getText().toString().trim());
                            a.setCurp(curp.getText().toString().trim());
                            a.setNoTel1(tel1.getText().toString().trim());
                            if(tel2.getText().toString().equals("")){
                                a.setNoTel2("123-123-1233");
                            }else {
                                a.setNoTel2(tel2.getText().toString().trim());
                            }
                            ref.child("alumnos").child(a.getId()).setValue(a);
                            Toast.makeText(activity_modify_students.this, "Alumno actualizado", Toast.LENGTH_SHORT).show();
                            break;

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch (Exception e){
            Toast.makeText(this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    private void eliminar() {
        aceptados acep = (aceptados) users.getSelectedItem();
        String idU = acep.getId();
        try {
            ref.child("alumnos").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot obj : snapshot.getChildren()) {
                        alumnos a = obj.getValue(alumnos.class);
                        if (idU.equals(a.getIdUsuario())) {
                            a.setId(a.getId());
                            ref.child("alumnos").child(a.getId()).removeValue();
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch (Exception e){
            Toast.makeText(this,"Error: "+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
}
package com.example.app_proyect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.app_proyect.model.aceptados;
import com.example.app_proyect.model.docentes;
import com.example.app_proyect.model.grupo;
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

public class activity_create_group extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    DatabaseReference ref;
    FirebaseDatabase bd;

    Spinner grade, docente, au;
    EditText nombre, cupo;
    private List<aceptados> lista = new ArrayList<aceptados>();
    private List<aula> listaAulas = new ArrayList<aula>();
    ArrayAdapter<aceptados> arrayAdapterDoc;
    ArrayAdapter<aula> arrayAdapterAu;

    aula aulaSelect;
    docentes docSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        iniciarBd();

        nombre = findViewById(R.id.etNameGroup);
        cupo = findViewById(R.id.etCupoGroup);
        grade = findViewById(R.id.spinnerCrGrGrade);
        docente = findViewById(R.id.spinnerCrGrDoc);
        au = findViewById(R.id.spinnerCrGrAula);
        listarDoc();
        listar();
        ArrayAdapter<CharSequence> arr = ArrayAdapter.createFromResource(this,R.array.trimestre_grado, android.R.layout.simple_spinner_dropdown_item);
        arr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        grade.setAdapter(arr);

        grade.setOnItemSelectedListener(this);



        Button buttonReg = findViewById(R.id.btnAdminViewCreateGroupReg);

        buttonReg.setOnClickListener(v -> {
            Intent intento = new Intent(v.getContext(), activity_modify_group.class);
            startActivity(intento);
        });

        Button buttonSave = findViewById(R.id.btnAdminViewCreateGroup);

        buttonSave.setOnClickListener(v -> {
            String nomb = nombre.getText().toString();
            String num = cupo.getText().toString();
            if(nomb.equals("")||num.equals("")){
                Toast.makeText(this,"Faltan campos por llenar", Toast.LENGTH_SHORT).show();
            }
            else {
                crear();
            }
        });
    }

    private void listar() {
        ref.child("aula").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaAulas.clear();
                for(DataSnapshot obj : snapshot.getChildren()){
                    aula a = obj.getValue(aula.class);
                    listaAulas.add(a);
                    arrayAdapterAu = new ArrayAdapter<aula>(activity_create_group.this,android.R.layout.simple_spinner_dropdown_item,listaAulas);
                    au.setAdapter(arrayAdapterAu);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void listarDoc() {
        ref.child("aceptados").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lista.clear();
                for(DataSnapshot obj : snapshot.getChildren()){
                    aceptados a = obj.getValue(aceptados.class);
                    if(a.getUserType().equals("Docente")){
                        lista.add(a);
                        arrayAdapterDoc = new ArrayAdapter<aceptados>(activity_create_group.this,android.R.layout.simple_spinner_dropdown_item,lista);
                        docente.setAdapter(arrayAdapterDoc);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void crear() {
        grupo g = new grupo();
        aceptados ac = (aceptados) docente.getSelectedItem();
        String idUsr = ac.getId();
        String id_aula;
        aula aul = (aula) au.getSelectedItem();
        id_aula = aul.getIdAula();
        try {
            ref.child("docentes").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot obj : snapshot.getChildren()) {
                        String idDoc;
                        docentes a = obj.getValue(docentes.class);
                        if (a.getIdUsuario().equals(idUsr)) {
                            idDoc = a.getIdDocente();
                            ref.child("grupo").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    boolean flag = false;
                                    for (DataSnapshot obj : snapshot.getChildren()) {
                                        grupo g = obj.getValue(grupo.class);
                                        if(g.getIdDocente().equals(a.getIdDocente())){
                                            flag = true;
                                            break;
                                        }
                                    }
                                    if (flag) {
                                        Toast.makeText(activity_create_group.this, "No se puede asignar un docente a 2 grupos", Toast.LENGTH_SHORT).show();
                                    } else {
                                        g.setId(UUID.randomUUID().toString());
                                        g.setNombre(nombre.getText().toString());
                                        g.setCupo(cupo.getText().toString());
                                        g.setContador(cupo.getText().toString());
                                        g.setIdAula(id_aula);
                                        g.setGrado(grade.getSelectedItem().toString());
                                        g.setIdDocente(idDoc);
                                        ref.child("grupo").child(g.getId()).setValue(g);
                                        Toast.makeText(activity_create_group.this, "Grupo Creado", Toast.LENGTH_SHORT).show();
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

        }catch (Exception e){
            Toast.makeText(this,"Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void limpiar() {
        nombre.setText("");
        cupo.setText("");
    }

    private void iniciarBd() {
        FirebaseApp.initializeApp(this);
        bd = FirebaseDatabase.getInstance();
        ref = bd.getReference();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String tipo = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(),tipo,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
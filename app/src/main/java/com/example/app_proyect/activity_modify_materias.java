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
import android.widget.Spinner;
import android.widget.Toast;

import com.example.app_proyect.model.materias;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class activity_modify_materias extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    DatabaseReference ref;
    FirebaseDatabase bd;
    Spinner grade;
    EditText nombre, desc;
    ListView lista;
    materias matSelected;
    private List<materias> arr_lista = new ArrayList<materias>();
    ArrayAdapter<materias> arrayAdapterMat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_materias);
        iniciarBd();

        grade = findViewById(R.id.spinnerModiMateriaGrade);
        nombre = findViewById(R.id.etModifyMateriaNombre);
        desc = findViewById(R.id.etModifyMateriaDesc);
        lista = findViewById(R.id.listVermaterias);

        ArrayAdapter<CharSequence> arr = ArrayAdapter.createFromResource(this,R.array.trimestre_grado, android.R.layout.simple_spinner_dropdown_item);
        arr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        grade.setAdapter(arr);

        grade.setOnItemSelectedListener(this);
        listar();
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    matSelected = (materias) parent.getItemAtPosition(position);
                    nombre.setText(matSelected.getNombre());
                    desc.setText(matSelected.getDescripcion());

                    int num = grade.getCount();
                    String val = matSelected.getGrado();
                    for (int i = 0; i < num; i++) {
                        String item = (String) grade.getItemAtPosition(i);
                        if (val.equals(item)) {
                            grade.setSelection(i);
                            break;
                        }
                    }
                }catch (Exception e){
                    Toast.makeText(activity_modify_materias.this,"Error: "+ e.getMessage(),Toast.LENGTH_SHORT).show();
                }

            }
        });

        Button buttonReg = findViewById(R.id.buttonMateriasModifyReg);
        buttonReg.setOnClickListener(v -> {
            Intent intento = new Intent(v.getContext(), activity_admin_view.class);
            startActivity(intento);
        });

        Button buttonSave = findViewById(R.id.buttonModifyMatSave);
        buttonSave.setOnClickListener(v -> {
            if(desc.getText().toString().equals("")||nombre.getText().toString().equals("")){
                Toast.makeText(this,"Por favor llene todos los campos",Toast.LENGTH_SHORT).show();
            }else {
                guardar();
            }
        });

        Button buttonElim = findViewById(R.id.buttonModifyMatElim);
        buttonElim.setOnClickListener(v -> {
            eliminar();
        });

        Button buttonCreate = findViewById(R.id.buttonMateriasModifyCr);
        buttonCreate.setOnClickListener(v -> {
            Intent intento = new Intent(v.getContext(), activity_create_materia.class);
            startActivity(intento);
        });
    }

    private void listar() {
        ref.child("materias").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arr_lista.clear();
                for(DataSnapshot obj : snapshot.getChildren()){
                    materias m = obj.getValue(materias.class);
                    arr_lista.add(m);

                    arrayAdapterMat = new ArrayAdapter<materias>(activity_modify_materias.this,android.R.layout.simple_list_item_1,arr_lista);
                    lista.setAdapter(arrayAdapterMat);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void guardar() {
        materias m = new materias();
        try {
            m.setIdMateria(matSelected.getIdMateria());
            m.setNombre(nombre.getText().toString().trim());
            m.setGrado(grade.getSelectedItem().toString().trim());
            m.setDescripcion(desc.getText().toString().trim());
            ref.child("materias").child(m.getIdMateria()).setValue(m);
            nombre.setText("");
            desc.setText("");
        }catch (Exception e){
            Toast.makeText(this,"Error: "+e.getMessage(),Toast.LENGTH_SHORT).show();
        }

    }
    private void eliminar() {
        materias m = new materias();
        try {
            m.setIdMateria(matSelected.getIdMateria());
            ref.child("materias").child(m.getIdMateria()).removeValue();
            nombre.setText("");
            desc.setText("");
            Toast.makeText(this,"Materia eliminada",Toast.LENGTH_SHORT).show();
            listar();
        }catch (Exception e){
            Toast.makeText(this,"Error: "+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
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
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

import com.example.app_proyect.model.rubros;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class activity_modify_rubro extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    DatabaseReference ref;
    FirebaseDatabase bd;
    Spinner rubroSpinner, trimestre;
    ArrayAdapter<rubros> arrAdap;
    List<rubros> lista = new ArrayList<>();
    rubros rubroSelect;
    EditText nombre, asistencia, trabajos, examen, valores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_rubro);
        iniciarBd();

        rubroSpinner = findViewById(R.id.spinnerModifyRRubros);
        trimestre = findViewById(R.id.spinnerModifyRMateria);
        nombre = findViewById(R.id.etRubroNuevoName);
        asistencia = findViewById(R.id.etModifyRAsist);
        trabajos =findViewById(R.id.etModifyRClassWork);
        examen = findViewById(R.id.etModifyRTest);
        valores = findViewById(R.id.etModifyRValores);

        ArrayAdapter<CharSequence> arr = ArrayAdapter.createFromResource(this,R.array.trimestre_grado, android.R.layout.simple_spinner_dropdown_item);
        arr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        trimestre.setAdapter(arr);

        trimestre.setOnItemSelectedListener(this);

        listar();

        rubroSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                rubroSelect = (rubros) parent.getItemAtPosition(position);
                nombre.setText(rubroSelect.getNombre());
                asistencia.setText(rubroSelect.getAsistencia());
                trabajos.setText(rubroSelect.getTrabajos());
                examen.setText(rubroSelect.getExamen());
                valores.setText(rubroSelect.getValores());
                int cont = trimestre.getCount();
                String trim = rubroSelect.getTrimestre();
                for(int i = 0;i<cont;i++){
                    String item = (String) trimestre.getItemAtPosition(i);
                    if(item.equals(trim)){
                        trimestre.setSelection(i);
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//------------------------------------------------------------------------------------------
        Button btnRegresar = findViewById(R.id.btnModifyReg);

        btnRegresar.setOnClickListener(v -> {
            Intent intento = new Intent(v.getContext(), activity_teachers_view.class);
            startActivity(intento);
        });

        Button btnCrear = findViewById(R.id.btnModifyRCreate);

        btnCrear.setOnClickListener(v -> {
            Intent intento = new Intent(v.getContext(), activity_rubros.class);
            startActivity(intento);
        });

        Button btnDelete = findViewById(R.id.btnModifyRDelete);

        btnDelete.setOnClickListener(v -> {
            eliminar();
        });

        Button btnSave = findViewById(R.id.btnModifyRSave);

        btnSave.setOnClickListener(v -> {
            if(nombre.getText().toString().equals("")||trabajos.getText().toString().equals("")||examen.getText().toString().equals("")||asistencia.getText().toString().equals("")||valores.getText().toString().equals("")){
                Toast.makeText(this,"Faltan campos por llenar",Toast.LENGTH_SHORT).show();
            }
            {
                save();
            }
        });
    }

    private void listar() {
        ref.child("rubros").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lista.clear();
                for(DataSnapshot obj : snapshot.getChildren()){
                    rubros r = obj.getValue(rubros.class);
                    lista.add(r);
                    arrAdap =  new ArrayAdapter<>(activity_modify_rubro.this, android.R.layout.simple_spinner_dropdown_item,lista);
                    rubroSpinner.setAdapter(arrAdap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void save() {
        rubros r = new rubros();
        try {
            r.setId(rubroSelect.getId());
            r.setAsistencia(asistencia.getText().toString().trim());
            r.setExamen(examen.getText().toString().trim());
            r.setValores(valores.getText().toString().trim());
            r.setTrabajos(trabajos.getText().toString().trim());
            r.setTrimestre(trimestre.getSelectedItem().toString().trim());
            r.setNombre(nombre.getText().toString().trim());
            ref.child("rubros").child(r.getId()).setValue(r);
            limpiar();
            Toast.makeText(this,"Rubro Actualizado",Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(this,"Error: "+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    private void eliminar() {
        rubros r = new rubros();
        try {
            r.setId(rubroSelect.getId());
            ref.child("rubros").child(r.getId()).removeValue();
            Toast.makeText(this,"Rubro Eliminado",Toast.LENGTH_SHORT).show();
            limpiar();
        }catch (Exception e){
            Toast.makeText(this,"Error: "+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    private void limpiar() {
        examen.setText("");
        trabajos.setText("");
        valores.setText("");
        asistencia.setText("");
        nombre.setText("");
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
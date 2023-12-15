package com.example.app_proyect;

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

import com.example.app_proyect.model.materias;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class activity_create_materia extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    DatabaseReference ref;
    FirebaseDatabase bd;
    EditText nombre,desc;
    Spinner grade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_materia);
        iniciarBd();

        nombre = findViewById(R.id.etCreateMateriaNombre);
        desc = findViewById(R.id.etCreateMateriaDesc);
        grade = findViewById(R.id.spinnerCreateMateria);

        ArrayAdapter<CharSequence> arr = ArrayAdapter.createFromResource(this,R.array.trimestre_grado, android.R.layout.simple_spinner_dropdown_item);
        arr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        grade.setAdapter(arr);

        grade.setOnItemSelectedListener(this);

        Button buttonReg = findViewById(R.id.buttonCreateMatRegresar);
        buttonReg.setOnClickListener(v -> {
            Intent intento = new Intent(v.getContext(), activity_modify_materias.class);
            startActivity(intento);
        });

        Button buttonCreate = findViewById(R.id.buttonCreateMatCr);
        buttonCreate.setOnClickListener(v -> {
            if(nombre.getText().toString().equals("")||desc.getText().toString().equals("")){
                Toast.makeText(this,"Faltan campos por llenar", Toast.LENGTH_SHORT).show();
            }else{
                crear();
            }
        });
    }

    private void crear() {
        materias m = new materias();
        try {
            m.setIdMateria(UUID.randomUUID().toString());
            m.setDescripcion(desc.getText().toString());
            m.setGrado(grade.getSelectedItem().toString());
            m.setNombre(nombre.getText().toString());
            ref.child("materias").child(m.getIdMateria()).setValue(m);
            Toast.makeText(this,"Materia agregada con exito", Toast.LENGTH_SHORT).show();
            limpiar();
        }catch (Exception e){
            Toast.makeText(this,"Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void limpiar() {
        nombre.setText("");
        desc.setText("");
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
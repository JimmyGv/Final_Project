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

import com.example.app_proyect.model.rubros;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class activity_rubros extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    DatabaseReference ref;
    FirebaseDatabase bd;
    EditText nombre, asistencia, trabajos, examen, valores;
    Spinner trimestre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rubros);
        iniciarBd();

        nombre = findViewById(R.id.etRubroName);
        asistencia = findViewById(R.id.etAsist);
        trabajos= findViewById(R.id.etClassWork);
        examen= findViewById(R.id.etTest);
        valores= findViewById(R.id.etValores);
        trimestre= findViewById(R.id.spinnerRubros);

        ArrayAdapter<CharSequence> arr = ArrayAdapter.createFromResource(this,R.array.trimestre_grado, android.R.layout.simple_spinner_dropdown_item);
        arr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        trimestre.setAdapter(arr);

        trimestre.setOnItemSelectedListener(this);

//-----------------------------------------------------------------------------------------------
        Button btnRegresar = findViewById(R.id.btnRubroRegresar);

        btnRegresar.setOnClickListener(v -> {
            Intent intento = new Intent(v.getContext(), activity_modify_rubro.class);
            startActivity(intento);
        });

        Button btnCrear = findViewById(R.id.btnRubroSave);

        btnCrear.setOnClickListener(v -> {
            if(nombre.getText().toString().equals("")||trabajos.getText().toString().equals("")||examen.getText().toString().equals("")||asistencia.getText().toString().equals("")||valores.getText().toString().equals("")){
                Toast.makeText(this,"Faltan campos por llenar",Toast.LENGTH_SHORT).show();
            }
            {
                crear();
            }
        });


    }

    private void crear() {
        try {
            rubros r = new rubros();
            r.setId(UUID.randomUUID().toString());
            r.setNombre(nombre.getText().toString());
            r.setTrimestre(trimestre.getSelectedItem().toString());
            r.setTrabajos(trabajos.getText().toString());
            r.setValores(valores.getText().toString());
            r.setExamen(examen.getText().toString());
            r.setAsistencia(asistencia.getText().toString());
            ref.child("rubros").child(r.getId()).setValue(r);
            limpiar();
            Toast.makeText(this,"Rubro creado con exito",Toast.LENGTH_SHORT).show();
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
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
import java.util.UUID;

public class activity_create_students extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    DatabaseReference ref;
    FirebaseDatabase bd;
    Spinner users,grade;
    EditText madre, padre, tel1, tel2, curp;

    aceptados ac;
    String idUser;
    private List<aceptados> lista = new ArrayList<aceptados>();
    ArrayAdapter<aceptados> arrayAdapterUs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_students);
        iniciarBd();
        users = findViewById(R.id.spinnerUser);
        madre = findViewById(R.id.etParent1);
        padre = findViewById(R.id.etParent2);
        tel1 = findViewById(R.id.etPhone);
        tel2 = findViewById(R.id.etPhone2);
        curp = findViewById(R.id.etCurp);
        grade = findViewById(R.id.spinnerGr);

        ArrayAdapter<CharSequence> arr = ArrayAdapter.createFromResource(this,R.array.trimestre_grado, android.R.layout.simple_spinner_dropdown_item);
        arr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        grade.setAdapter(arr);

        grade.setOnItemSelectedListener(this);

        //listas
        listar();

        users.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ac = (aceptados) parent.getItemAtPosition(position);
                idUser = ac.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //botones
        Button buttonReg = findViewById(R.id.btnCreateStudentsRegresar);
        buttonReg.setOnClickListener(v -> {
            Intent intento = new Intent(v.getContext(), activity_modify_students.class);
            startActivity(intento);
        });

        Button buttonCre = findViewById(R.id.btnAgregarStudents);
        buttonCre.setOnClickListener(v -> {
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
                            crear();
                        }
                    }
                }
            }
        });
    }

    private void listar() {
        ref.child("aceptados").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lista.clear();
                for(DataSnapshot obj : snapshot.getChildren()){
                    aceptados a = obj.getValue(aceptados.class);
                    if(a.getUserType().equals("Alumno")){
                        lista.add(a);
                        arrayAdapterUs = new ArrayAdapter<aceptados>(activity_create_students.this,android.R.layout.simple_spinner_dropdown_item,lista);
                        users.setAdapter(arrayAdapterUs);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void crear() {
        alumnos a = new alumnos();
        try {
            ref.child("alumnos").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean bandera = false;
                    for (DataSnapshot obj : snapshot.getChildren()) {
                        alumnos b = obj.getValue(alumnos.class);
                        if (b.getIdUsuario().equals(idUser)) {
                            bandera = true;
                            break;
                        }
                    }
                    if (bandera) {
                        Toast.makeText(activity_create_students.this, "No se puede crear un alumno 2 veces", Toast.LENGTH_SHORT).show();
                    } else {
                        a.setId(UUID.randomUUID().toString());
                        a.setCurp(curp.getText().toString());
                        a.setGrado(grade.getSelectedItem().toString());
                        a.setNoTel1(tel1.getText().toString());
                        if(tel2.getText().toString().equals("")){
                            a.setNoTel2("123-123-123");
                        }else{
                            a.setNoTel2(tel2.getText().toString());
                        }
                        a.setMadre(madre.getText().toString());
                        a.setPadre(padre.getText().toString());
                        a.setPromedio("0");
                        a.setIdUsuario(idUser);
                        ref.child("alumnos").child(a.getId()).setValue(a);
                        Toast.makeText(activity_create_students.this,"Alumno agregado con exito", Toast.LENGTH_SHORT).show();
                        limpiar();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch (Exception e){
            Toast.makeText(this,"Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void limpiar() {
        padre.setText("");
        madre.setText("");
        tel2.setText("");
        tel1.setText("");
        curp.setText("");

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
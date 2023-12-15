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

import com.example.app_proyect.model.aceptados;
import com.example.app_proyect.model.usuario;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class activity_modify_users extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    FirebaseDatabase bd;
    DatabaseReference ref;
    aceptados userSelect;
    private EditText nombre,correo,contrasena;

    private Spinner estatus, type;
    ListView listaU;

    private List<aceptados> listaUsers = new ArrayList<>();

    ArrayAdapter<aceptados> arrayAdapterUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_users);
        iniciarBd();

        listaU = findViewById(R.id.listVerModU);
        correo = findViewById(R.id.etVerModiUsermail);
        contrasena = findViewById(R.id.etVerModiUserPwrd);
        estatus = findViewById(R.id.spinnerEstatus);
        type = findViewById(R.id.spinnerType);
        nombre = findViewById(R.id.etVerModiUserName);

        listar();

        ArrayAdapter<CharSequence> arr = ArrayAdapter.createFromResource(this,R.array.user_estatus, android.R.layout.simple_spinner_dropdown_item);
        arr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        estatus.setAdapter(arr);

        ArrayAdapter<CharSequence> arr2 = ArrayAdapter.createFromResource(this,R.array.user_type, android.R.layout.simple_spinner_dropdown_item);
        arr2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(arr2);

        listaU.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                userSelect = (aceptados) parent.getItemAtPosition(position);
                nombre.setText(userSelect.getUsuario());
                correo.setText(userSelect.getCorreo());
                contrasena.setText(userSelect.getContrasena());

                int items = estatus.getCount();
                String valor = userSelect.getEstatus();
                for(int i=0;i<items;i++){
                    String itemValue = (String) estatus.getItemAtPosition(i);
                    if (valor.equals(itemValue)){
                        estatus.setSelection(i);
                        break;
                    }
                }

                int num = type.getCount();
                String tipoUser = userSelect.getUserType();
                for(int i=0;i<num;i++){
                    String item = (String) type.getItemAtPosition(i);
                    if (tipoUser.equals(item)){
                        type.setSelection(i);
                        break;
                    }
                }

            }
        });

        estatus.setOnItemSelectedListener(this);


        Button btnEliminar = findViewById(R.id.buttonVerModiUElim);

        btnEliminar.setOnClickListener(v -> {
            eliminar();
        });

        Button btnActualizar = findViewById(R.id.buttonAcceptVerModiU);

        btnActualizar.setOnClickListener(v -> {
            String nmb, crr, cnts;
            nmb = nombre.getText().toString();
            crr = correo.getText().toString();
            cnts= contrasena.getText().toString();
            if(nmb.equals("") || crr.equals("") || cnts.equals("") ){
                Toast.makeText(this, "Por favor llene todos los campos", Toast.LENGTH_SHORT).show();
            }else {
                actualizar();
            }
        });

        Button buttonReg = findViewById(R.id.buttonVerModiURegresar);

        buttonReg.setOnClickListener(v -> {
            Intent intento = new Intent(v.getContext(), activity_admin_view.class);
            startActivity(intento);
        });
    }

    private void listar() {
        ref.child("aceptados").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaUsers.clear();
                for(DataSnapshot obj:snapshot.getChildren()){
                    aceptados u = obj.getValue(aceptados.class);
                    listaUsers.add(u);
                    arrayAdapterUser=new ArrayAdapter<aceptados>(activity_modify_users.this, android.R.layout.simple_list_item_1, listaUsers);
                    listaU.setAdapter(arrayAdapterUser);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void actualizar() {
        aceptados a = new aceptados();
        try {
            a.setId(userSelect.getId());
            a.setEstatus(estatus.getSelectedItem().toString().trim());
            a.setCorreo(correo.getText().toString().trim());
            a.setContrasena(contrasena.getText().toString().trim());
            a.setUsuario(nombre.getText().toString().trim());
            a.setUserType(type.getSelectedItem().toString().trim());
            ref.child("aceptados").child(a.getId()).setValue(a);
            limpiar();
            Toast.makeText(this, "Usuario Actualizado", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(this, "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void iniciarBd() {
        FirebaseApp.initializeApp(this);
        bd = FirebaseDatabase.getInstance();
        ref = bd.getReference();

    }

    private void eliminar() {
        aceptados u = new aceptados();
        try {
            u.setId(userSelect.getId());
            ref.child("aceptados").child(u.getId()).removeValue();
            limpiar();
            Toast.makeText(this, "Registro Eliminado", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(this, "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void limpiar() {
        correo.setText("");
        contrasena.setText("");
        nombre.setText("");
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
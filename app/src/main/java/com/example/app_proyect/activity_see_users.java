package com.example.app_proyect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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
import java.util.UUID;

public class activity_see_users extends AppCompatActivity {
    FirebaseDatabase bd;
    ListView listV;
    DatabaseReference ref;
    private TextView nombreReg,mail,tipo;
    usuario userSelected;
    private List<usuario> listaUsers = new ArrayList<usuario>();
    ArrayAdapter<usuario> arrayAdapterUsuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_users);

        listV = findViewById(R.id.listVUsers);
        nombreReg = findViewById(R.id.tvTextAcceptName);
        tipo = findViewById(R.id.tvTextAcceptType);
        mail = findViewById(R.id.tvTextAcceptMail);
        iniciarBd();
        listar();

        listV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                userSelected = (usuario) parent.getItemAtPosition(position);
                nombreReg.setText(userSelected.getNombreUser());
                tipo.setText(userSelected.getTipo());
                mail.setText(userSelected.getMail());
            }
        });

        //Regresar
        Button btnReg = findViewById(R.id.buttonSeeStudentRegresar);

        btnReg.setOnClickListener(v -> {
            Intent intento = new Intent(v.getContext(), activity_admin_view.class);
            startActivity(intento);
        });

        //Aceptar
        Button btnAccept = findViewById(R.id.buttonAcceptSee);

        btnAccept.setOnClickListener(v -> {
            aceptar();
            eliminar();
        });

        //Eliminar
        Button btnElim = findViewById(R.id.buttonAcceptElim);

        btnElim.setOnClickListener(v -> {
            eliminar();
        });
    }

    private void eliminar() {
        usuario u = new usuario();
        try {
            u.setUid(userSelected.getUid());
            ref.child("usuario").child(u.getUid()).removeValue();
        }catch (Exception e){
            Toast.makeText(this,"Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void aceptar() {
        aceptados u = new aceptados();
        try {
            u.setId(UUID.randomUUID().toString());
            u.setUsuario(userSelected.getNombreUser());
            u.setCorreo(userSelected.getMail());
            u.setEstatus("A");
            u.setContrasena(userSelected.getPswrd());
            u.setUserType(userSelected.getTipo());
            ref.child("aceptados").child(u.getId()).setValue(u);

        }catch (Exception e){
            Toast.makeText(this,"Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void listar() {
        ref.child("usuario").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaUsers.clear();
                for (DataSnapshot obj : snapshot.getChildren()){
                    usuario u = obj.getValue(usuario.class);
                    listaUsers.add(u);

                    arrayAdapterUsuario=new ArrayAdapter<usuario>(activity_see_users.this, android.R.layout.simple_list_item_1, listaUsers);
                    listV.setAdapter(arrayAdapterUsuario);
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
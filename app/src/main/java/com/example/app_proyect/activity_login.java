package com.example.app_proyect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.app_proyect.model.aceptados;
import com.example.app_proyect.model.userActive;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class activity_login extends AppCompatActivity {

    FirebaseDatabase bd;
    DatabaseReference ref;
    FirebaseAuth a;
    aceptados user;
    private EditText correo,contrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        correo = findViewById(R.id.etLoginUsername);
        contrasena=findViewById(R.id.etLoginPassword);
        iniciarBd();
        a = FirebaseAuth.getInstance();
        Button btnCrearUser = findViewById(R.id.btnCreateUser);

        btnCrearUser.setOnClickListener(v -> {
            Intent intento = new Intent(v.getContext(), activity_solicitar_registro.class);
            startActivity(intento);
        });

        Button btnLoginDocentes = findViewById(R.id.btnAcercade);

        btnLoginDocentes.setOnClickListener(v -> {
            Intent intento = new Intent(v.getContext(), activity_about.class);
            startActivity(intento);
        });

        Button btnLoginAdmin = findViewById(R.id.btnOferta);

        btnLoginAdmin.setOnClickListener(v -> {
            Intent intento = new Intent(v.getContext(), activity_oferta.class);
            startActivity(intento);
        });

        Button btnLogin = findViewById(R.id.btnLoginIncioSesion);

        btnLogin.setOnClickListener(v -> {
            String var,var2;
            var = correo.getText().toString();
            var2 = contrasena.getText().toString();
            if(var.equals("")||var2.equals("")){
                Toast.makeText(this,"Por favor llene los campos" , Toast.LENGTH_SHORT).show();
            }else{
                listar();
                //inicioSesion(var, var2);
            }

        });
    }

    private void inicioSesion(String corr, String contra) {
        a.signInWithEmailAndPassword(corr,contra).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    finish();
                    FirebaseUser p = a.getCurrentUser();
                    Toast.makeText(activity_login.this,"User " + p.getUid(),Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(activity_login.this,"Error al iniciar Sesion",Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity_login.this,"Error al iniciar Sesion",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void listar() {
        String mail = correo.getText().toString();
        String contra = contrasena.getText().toString();
        ref.child("aceptados").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String correoVal,contraVal;
                for (DataSnapshot obj : snapshot.getChildren()){
                    aceptados a = obj.getValue(aceptados.class);
                    correoVal= a.getCorreo();
                    contraVal = a.getContrasena();
                    if( correoVal.equals(mail)){    
                        if(contraVal.equals(contra)){
                            if(a.getUserType().equals("Docente")){
                                userActive u = new userActive();
                                u.setId(a.getId());
                                u.setNombre(a.getUsuario());
                                startActivity(new Intent(activity_login.this, activity_teachers_view.class));
                                Toast.makeText(activity_login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            } else if (a.getUserType().equals("Alumno")) {
                                userActive u = new userActive();
                                u.setId(a.getId());
                                u.setNombre(a.getUsuario());
                                startActivity(new Intent(activity_login.this, activity_students_view.class));
                                Toast.makeText(activity_login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            }else {
                                userActive u = new userActive();
                                u.setId(a.getId());
                                u.setNombre(a.getUsuario());
                                startActivity(new Intent(activity_login.this, activity_admin_view.class));
                                Toast.makeText(activity_login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(activity_login.this,"La contraseña no coinicide",Toast.LENGTH_SHORT).show();
                        }
                    }
                    Toast.makeText(activity_login.this, "Login Failed", Toast.LENGTH_SHORT).show();
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
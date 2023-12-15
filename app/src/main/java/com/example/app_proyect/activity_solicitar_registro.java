package com.example.app_proyect;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import  android.widget.Button;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.app_proyect.model.usuario;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;

import java.util.UUID;

public class activity_solicitar_registro extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private EditText nombre, email, pwrd;
    Spinner userType;

    FirebaseDatabase bd;
    DatabaseReference ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitar_registro);

        nombre = findViewById(R.id.etUserRegistroName);
        email = findViewById(R.id.etUsermail);
        pwrd = findViewById(R.id.etUserPassword);
        userType = findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> arr = ArrayAdapter.createFromResource(this,R.array.user_type, android.R.layout.simple_spinner_dropdown_item);
        arr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userType.setAdapter(arr);

        userType.setOnItemSelectedListener(this);

        iniciarBd();


        Button registro = findViewById(R.id.btnUserSolicitud);

        registro.setOnClickListener(v -> {
                validacion();
        });

        Button buttonReg = findViewById(R.id.buttonRegistroRegresar);

        buttonReg.setOnClickListener(v -> {
            Intent intento = new Intent(v.getContext(), activity_login.class);
            startActivity(intento);
        });


    }


    private void iniciarBd() {
        FirebaseApp.initializeApp(this);
        bd = FirebaseDatabase.getInstance();
        ref = bd.getReference();

    }

    private void validacion(){
        String nmb = nombre.getText().toString();
        String passwd = pwrd.getText().toString();
        String mail = email.getText().toString();
        String typeUser = userType.getSelectedItem().toString();

        if (nmb.equals("")){
            nombre.setError("Required");
        }else{
            if (passwd.equals("")) {
                pwrd.setError("Required");
            }
            else {
                if (mail.equals("")) {
                    email.setError("Required");
                }
                else{
                    usuario u = new usuario();
                    u.setUid(UUID.randomUUID().toString());
                    u.setNombreUser(nmb);
                    u.setMail(mail);
                    u.setPswrd(passwd);
                    u.setTipo(typeUser);
                    ref.child("usuario").child(u.getUid()).setValue(u);
                    limpiar();
                }
            }
        }

    }

    private void limpiar() {
        nombre.setText("");
        email.setText("");
        pwrd.setText("");
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
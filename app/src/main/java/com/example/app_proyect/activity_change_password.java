package com.example.app_proyect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.app_proyect.model.aceptados;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class activity_change_password extends AppCompatActivity {
    FirebaseDatabase bd;
    DatabaseReference ref;
    EditText currentP, newPwrd, confirmP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        iniciarBd();

        currentP = findViewById(R.id.etPwrdActual);
        newPwrd = findViewById(R.id.etNewPassword);
        confirmP = findViewById(R.id.etNewPasswordConfirm);


        Button btnReg = findViewById(R.id.btnChangeReg);

        btnReg.setOnClickListener(v -> {
            Intent intento = new Intent(v.getContext(), activity_teachers_view.class);
            startActivity(intento);
        });

        Button btnSave = findViewById(R.id.btnSaveNewPwrd);

        btnSave.setOnClickListener(v -> {
            String curr = currentP.getText().toString();
            String np = newPwrd.getText().toString();
            String conf = confirmP.getText().toString();
            if(curr.equals("")||np.equals("")||conf.equals("")){
                Toast.makeText(this,"Por favor llene los campos" , Toast.LENGTH_SHORT).show();
            }else {
                if(np.equals(conf)){
                    guardar();
                }else{
                    confirmP.setError("No coinciden las contraseñas");
                }
            }

        });
    }

    private void guardar() {
        //aceptados a = new aceptados();
        //try {
            //a.setContrasena(confirm.getText().toString().trim());
            //ref.child("aceptados").child(a.getId()).setValue(a);
        //}catch (Exception e ){
            //Toast.makeText(this,"Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        //}

    }

    private void iniciarBd() {
        FirebaseApp.initializeApp(this);
        bd = FirebaseDatabase.getInstance();
        ref = bd.getReference();

    }
}
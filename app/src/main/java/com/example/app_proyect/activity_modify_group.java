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
import com.example.app_proyect.model.aula;
import com.example.app_proyect.model.docentes;
import com.example.app_proyect.model.grupo;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class activity_modify_group extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    FirebaseDatabase bd;
    DatabaseReference ref;
    Spinner grade, docente, au;
    EditText nombre,cupo;
    grupo groupSelect;
    ListView listaG;
    private List<aceptados> lista = new ArrayList<aceptados>();
    private List<grupo> lista2 = new ArrayList<grupo>();
    private List<aula> listaAulas = new ArrayList<aula>();
    ArrayAdapter<aceptados> arrayAdapterDoc;
    ArrayAdapter<aula> arrayAdapterAu;
    ArrayAdapter<grupo> arrayAdapterGr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_group);
        iniciarBd();

        nombre = findViewById(R.id.etNameModifyGroup);
        cupo = findViewById(R.id.etCupoModifyGroup);
        listaG = findViewById(R.id.listVerGroups);
        grade = findViewById(R.id.spinnerModifyGrGrade);
        docente = findViewById(R.id.spinnerModifyGrDoc);
        au = findViewById(R.id.spinnerModifyGrAula);
        ArrayAdapter<CharSequence> arr = ArrayAdapter.createFromResource(this,R.array.trimestre_grado, android.R.layout.simple_spinner_dropdown_item);
        arr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        grade.setAdapter(arr);

        grade.setOnItemSelectedListener(this);

        //listas
        listarDoc();
        listar();
        listarGroups();



        listaG.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    groupSelect = (grupo) parent.getItemAtPosition(position);
                    nombre.setText(groupSelect.getNombre());
                    cupo.setText(groupSelect.getCupo());

                    int contador = grade.getCount();
                    String val = groupSelect.getGrado();
                    for (int i = 0; i < contador; i++) {
                        String val2 = (String) grade.getItemAtPosition(i);
                        if (val.equals(val2)) {
                            grade.setSelection(i);
                            break;
                        }
                    }

                    int counter = au.getCount();
                    String var = groupSelect.getIdAula();
                    for (int i = 0; i < counter; i++) {
                        aula val2 = (aula) au.getItemAtPosition(i);
                        if (var.equals(val2.getIdAula())) {
                            au.setSelection(i);
                            break;
                        }
                    }

                    int cont = docente.getCount();
                    String v = groupSelect.getIdDocente();
                    for (int i = 0; i < cont; i++) {
                        aceptados val2 = (aceptados) docente.getItemAtPosition(i);
                        if (v.equals(val2.getId())) {
                            docente.setSelection(i);
                            break;
                        }
                    }
                }catch (Exception e){
                    Toast.makeText(activity_modify_group.this,"Error: "+ e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button buttonReg = findViewById(R.id.btnAdminViewModifyGroupReg);
        buttonReg.setOnClickListener(v -> {
            Intent intento = new Intent(v.getContext(), activity_admin_view.class);
            startActivity(intento);
        });

        Button buttonCreate = findViewById(R.id.btnAdminViewModifyGroupCr);

        buttonCreate.setOnClickListener(v -> {
            Intent intento = new Intent(v.getContext(), activity_create_group.class);
            startActivity(intento);
        });

        Button buttonSave = findViewById(R.id.btnModifyGroupSave);

        buttonSave.setOnClickListener(v -> {
            if(nombre.getText().toString().equals("")||cupo.getText().toString().equals("")){
                Toast.makeText(this,"Por favor llene todos los campos",Toast.LENGTH_SHORT).show();
            }else {
                guardar();
            }
        });

        Button buttonElim = findViewById(R.id.btnModifyGroupDelete);

        buttonElim.setOnClickListener(v -> {
            eliminar();
        });
    }

    private void listarGroups() {
        ref.child("grupo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lista2.clear();
                for(DataSnapshot obj : snapshot.getChildren()){
                    grupo g = obj.getValue(grupo.class);
                    lista2.add(g);
                    arrayAdapterGr =  new ArrayAdapter<grupo>(activity_modify_group.this,android.R.layout.simple_list_item_1,lista2);
                    listaG.setAdapter(arrayAdapterGr);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void guardar() {
        grupo g = new grupo();
        aceptados ac = (aceptados) docente.getSelectedItem();
        String idUs = ac.getId();
        aula aul = (aula) au.getSelectedItem();
        String idAula = aul.getIdAula();
        try{
            ref.child("docentes").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot obj:snapshot.getChildren()){
                        docentes a = obj.getValue(docentes.class);
                        if(a.getIdUsuario().equals(idUs)){
                            ref.child("grupo").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    int contador = 0;
                                    for(DataSnapshot obj:snapshot.getChildren()){
                                        grupo gr = obj.getValue(grupo.class);
                                        if(gr.getIdDocente().equals(a.getIdDocente())){
                                            if(contador<1){
                                                contador ++;
                                            }
                                            else {
                                                break;
                                            }
                                        }
                                    }
                                    if(contador>1){
                                        Toast.makeText(activity_modify_group.this,"No se puede asignar un docente a 2 grupos",Toast.LENGTH_SHORT).show();
                                    }else {
                                        String idDoc = a.getIdDocente();
                                        g.setId(groupSelect.getId());
                                        g.setGrado(grade.getSelectedItem().toString().trim());
                                        g.setCupo(cupo.getText().toString().trim());
                                        g.setIdDocente(idDoc);
                                        g.setIdAula(idAula);
                                        g.setContador(cupo.getText().toString().trim());
                                        g.setNombre(nombre.getText().toString().trim());
                                        ref.child("grupo").child(g.getId()).setValue(g);
                                        Toast.makeText(activity_modify_group.this, "Grupo Actualizado", Toast.LENGTH_SHORT).show();
                                        limpiar();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            break;
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }catch (Exception e){
            Toast.makeText(this, "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void limpiar() {
        cupo.setText("");
        nombre.setText("");
    }

    private void eliminar() {
        grupo g = new grupo();
        try {
            g.setId(groupSelect.getId());
            ref.child("grupo").child(g.getId()).removeValue();
            Toast.makeText(this, "Grupo Eliminado", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(this, "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void iniciarBd() {
        FirebaseApp.initializeApp(this);
        bd = FirebaseDatabase.getInstance();
        ref = bd.getReference();

    }

    private void listar() {
        ref.child("aula").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaAulas.clear();
                for(DataSnapshot obj : snapshot.getChildren()){
                    aula a = obj.getValue(aula.class);
                    listaAulas.add(a);
                    arrayAdapterAu = new ArrayAdapter<aula>(activity_modify_group.this,android.R.layout.simple_spinner_dropdown_item,listaAulas);
                    au.setAdapter(arrayAdapterAu);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void listarDoc() {
        ref.child("aceptados").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lista.clear();
                for(DataSnapshot obj : snapshot.getChildren()){
                    aceptados a = obj.getValue(aceptados.class);
                    if(a.getUserType().equals("Docente")){
                        lista.add(a);
                        arrayAdapterDoc = new ArrayAdapter<aceptados>(activity_modify_group.this,android.R.layout.simple_spinner_dropdown_item,lista);
                        docente.setAdapter(arrayAdapterDoc);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
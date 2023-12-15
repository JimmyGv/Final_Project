package com.example.app_proyect;

import com.google.firebase.database.FirebaseDatabase;

public class firebase extends android.app.Application{
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}

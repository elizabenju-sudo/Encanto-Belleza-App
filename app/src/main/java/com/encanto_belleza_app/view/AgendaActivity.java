package com.encanto_belleza_app.view;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.encanto_belleza_app.R;

public class AgendaActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);
        
        // Esta actividad sería para ver la agenda de citas
        Toast.makeText(this, "Módulo de agenda", Toast.LENGTH_SHORT).show();
    }
}

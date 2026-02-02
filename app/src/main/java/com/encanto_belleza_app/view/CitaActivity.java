package com.encanto_belleza_app.view;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.encanto_belleza_app.R;

public class CitaActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cita);
        
        // Esta actividad sería para agendar citas específicas
        Toast.makeText(this, "Módulo de agendar citas", Toast.LENGTH_SHORT).show();
    }
}

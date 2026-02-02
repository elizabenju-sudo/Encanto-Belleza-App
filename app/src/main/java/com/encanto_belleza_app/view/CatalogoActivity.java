package com.encanto_belleza_app.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.encanto_belleza_app.R;
import com.encanto_belleza_app.adapters.ServicioAdapter;
import com.encanto_belleza_app.controller.ServicioController;
import com.encanto_belleza_app.model.Servicio;
import java.util.ArrayList;
import java.util.List;

public class CatalogoActivity extends AppCompatActivity implements ServicioAdapter.OnAgendarClickListener {
    private TextView tvBienvenida;
    private Button btnLogout, btnAgenda;
    private RecyclerView rvServicios;
    private ServicioAdapter servicioAdapter;
    private ServicioController servicioController;
    private String usuarioEmail, usuarioNombre, usuarioTipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo);

        // Obtener datos del usuario
        Intent intent = getIntent();
        usuarioEmail = intent.getStringExtra("usuario_email");
        usuarioNombre = intent.getStringExtra("usuario_nombre");
        usuarioTipo = intent.getStringExtra("usuario_tipo");

        tvBienvenida = findViewById(R.id.tvBienvenida);
        btnLogout = findViewById(R.id.btnLogout);
        btnAgenda = findViewById(R.id.btnAgenda);
        rvServicios = findViewById(R.id.rvServicios);

        // Configurar RecyclerView
        rvServicios.setLayoutManager(new LinearLayoutManager(this));
        servicioAdapter = new ServicioAdapter(new ArrayList<>(), this);
        rvServicios.setAdapter(servicioAdapter);

        // Mostrar mensaje de bienvenida
        String mensajeBienvenida = String.format(getString(R.string.bienvenido), usuarioNombre);
        tvBienvenida.setText(mensajeBienvenida);

        // Inicializar controlador
        servicioController = new ServicioController(this);

        // Cargar servicios
        cargarServicios();

        // Configurar botones
        btnLogout.setOnClickListener(v -> {
            Intent loginIntent = new Intent(CatalogoActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        });

        btnAgenda.setOnClickListener(v -> {
            Intent agendaIntent = new Intent(CatalogoActivity.this, AgendaActivity.class);
            agendaIntent.putExtra("usuario_email", usuarioEmail);
            agendaIntent.putExtra("usuario_nombre", usuarioNombre);
            agendaIntent.putExtra("usuario_tipo", usuarioTipo);
            startActivity(agendaIntent);
        });
    }

    private void cargarServicios() {
        List<Servicio> servicios = servicioController.obtenerTodosServicios();
        if (servicios.isEmpty()) {
            Toast.makeText(this, "No hay servicios disponibles", Toast.LENGTH_SHORT).show();
        } else {
            servicioAdapter.updateServicios(servicios);
        }
    }

    @Override
    public void onAgendarClick(Servicio servicio) {
        Toast.makeText(this, "Agendando: " + servicio.getNombre(), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, CitaActivity.class);
        intent.putExtra("servicio_id", servicio.getId());
        intent.putExtra("servicio_nombre", servicio.getNombre());
        intent.putExtra("usuario_email", usuarioEmail);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        // No hacer nada para evitar volver al login
    }
}

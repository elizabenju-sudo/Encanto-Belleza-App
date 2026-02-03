package com.encanto_belleza_app.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.encanto_belleza_app.R;
import com.encanto_belleza_app.controller.CitaController;

public class ConfirmacionCitaActivity extends AppCompatActivity {

    private TextView tvClienteConfirmacion, tvServicioConfirmacion, tvEmpleadoConfirmacion;
    private TextView tvFechaConfirmacion, tvHoraConfirmacion, tvDuracionConfirmacion, tvPrecioConfirmacion;
    private Button btnVolverCatalogo, btnVerAgenda;

    private CitaController citaController;
    private String usuarioEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmacion_cita);

        // Inicializar controller
        citaController = new CitaController(this);

        // Inicializar views
        initViews();

        // Obtener datos del intent
        Intent intent = getIntent();
        cargarDatosConfirmacion(intent);

        // Configurar listeners
        setupListeners();
    }

    private void initViews() {
        tvClienteConfirmacion = findViewById(R.id.tvClienteConfirmacion);
        tvServicioConfirmacion = findViewById(R.id.tvServicioConfirmacion);
        tvEmpleadoConfirmacion = findViewById(R.id.tvEmpleadoConfirmacion);
        tvFechaConfirmacion = findViewById(R.id.tvFechaConfirmacion);
        tvHoraConfirmacion = findViewById(R.id.tvHoraConfirmacion);
        tvDuracionConfirmacion = findViewById(R.id.tvDuracionConfirmacion);
        tvPrecioConfirmacion = findViewById(R.id.tvPrecioConfirmacion);
        btnVolverCatalogo = findViewById(R.id.btnVolverCatalogo);
        btnVerAgenda = findViewById(R.id.btnVerAgenda);
    }

    private void cargarDatosConfirmacion(Intent intent) {
        String clienteNombre = intent.getStringExtra("cliente_nombre");
        String servicioNombre = intent.getStringExtra("servicio_nombre");
        String empleadoNombre = intent.getStringExtra("empleado_nombre");
        String fecha = intent.getStringExtra("fecha");
        String hora = intent.getStringExtra("hora");
        int duracion = intent.getIntExtra("duracion", 0);
        double precio = intent.getDoubleExtra("precio", 0.0);
        usuarioEmail = intent.getStringExtra("usuario_email");

        // Formatear fecha y hora
        String fechaFormateada = citaController.formatearFechaParaMostrar(fecha);
        String horaFormateada = citaController.formatearHoraParaMostrar(hora);

        // Mostrar datos
        tvClienteConfirmacion.setText("Cliente: " + clienteNombre);
        tvServicioConfirmacion.setText("Servicio: " + servicioNombre);
        tvEmpleadoConfirmacion.setText("Empleado: " + empleadoNombre);
        tvFechaConfirmacion.setText("Fecha: " + fechaFormateada);
        tvHoraConfirmacion.setText("Hora: " + horaFormateada);
        tvDuracionConfirmacion.setText("Duración: " + duracion + " min");
        tvPrecioConfirmacion.setText(String.format("Precio: $%.2f", precio));
    }

    private void setupListeners() {
        btnVolverCatalogo.setOnClickListener(v -> {
            Intent intent = new Intent(this, CatalogoActivity.class);
            intent.putExtra("usuario_email", usuarioEmail);
            startActivity(intent);
            finish();
        });

        btnVerAgenda.setOnClickListener(v -> {
            Intent intent = new Intent(this, AgendaActivity.class);
            intent.putExtra("usuario_email", usuarioEmail);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        // Redirigir al catálogo en lugar de volver atrás
        Intent intent = new Intent(this, CatalogoActivity.class);
        intent.putExtra("usuario_email", usuarioEmail);
        startActivity(intent);
        finish();
    }
}

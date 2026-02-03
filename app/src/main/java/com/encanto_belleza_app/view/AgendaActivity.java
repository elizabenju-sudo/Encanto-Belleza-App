package com.encanto_belleza_app.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.encanto_belleza_app.R;
import com.encanto_belleza_app.adapters.CitaAdapter;
import com.encanto_belleza_app.controller.CitaController;
import com.encanto_belleza_app.controller.ServicioController;
import com.encanto_belleza_app.controller.UsuarioController;
import com.encanto_belleza_app.model.Cita;
import com.encanto_belleza_app.model.Servicio;
import com.encanto_belleza_app.model.Usuario;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgendaActivity extends AppCompatActivity implements CitaAdapter.OnCitaActionListener {

    // Views
    private TextView tvTituloAgenda, tvTotalCitas, tvCitasPendientes, tvCitasConfirmadas, tvMensajeVacio;
    private AutoCompleteTextView spinnerEstado;
    private Button btnVolver, btnFiltrar, btnAgendarNueva, btnRefrescar;
    private RecyclerView rvCitas;

    // Controllers
    private CitaController citaController;
    private UsuarioController usuarioController;
    private ServicioController servicioController;

    // Adapter
    private CitaAdapter citaAdapter;

    // Datos
    private String usuarioEmail, usuarioNombre, usuarioTipo;
    private int usuarioId;
    private List<Cita> todasLasCitas;
    private List<Cita> citasFiltradas;

    // Mapa para almacenar servicios por ID (para evitar consultas repetidas)
    private Map<Integer, Servicio> mapaServicios = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);

        // Obtener datos del usuario con validación
        Intent intent = getIntent();
        usuarioEmail = intent.getStringExtra("usuario_email");
        usuarioNombre = intent.getStringExtra("usuario_nombre");
        usuarioTipo = intent.getStringExtra("usuario_tipo");

        // Validar que los datos no sean nulos
        if (usuarioEmail == null || usuarioNombre == null || usuarioTipo == null) {
            Toast.makeText(this, "Error: Datos de usuario no disponibles", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Inicializar views
        initViews();

        // Inicializar controllers
        citaController = new CitaController(this);
        usuarioController = new UsuarioController(this);
        servicioController = new ServicioController(this);

        // Obtener ID del usuario
        usuarioId = obtenerIdUsuario();
        if (usuarioId == -1) {
            Toast.makeText(this, "Error: Usuario no encontrado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Configurar RecyclerView
        rvCitas.setLayoutManager(new LinearLayoutManager(this));
        citaAdapter = new CitaAdapter(this, new ArrayList<>(), this);
        rvCitas.setAdapter(citaAdapter);

        // Configurar spinner de estado
        configurarSpinnerEstado();

        // Configurar listeners
        setupListeners();

        // Cargar citas iniciales
        cargarCitas();

        // Pre-cargar servicios en cache
        cargarServiciosEnCache();
    }

    private void initViews() {
        tvTituloAgenda = findViewById(R.id.tvTituloAgenda);
        tvTotalCitas = findViewById(R.id.tvTotalCitas);
        tvCitasPendientes = findViewById(R.id.tvCitasPendientes);
        tvCitasConfirmadas = findViewById(R.id.tvCitasConfirmadas);
        tvMensajeVacio = findViewById(R.id.tvMensajeVacio);
        spinnerEstado = findViewById(R.id.spinnerEstado);
        btnVolver = findViewById(R.id.btnVolver);
        btnFiltrar = findViewById(R.id.btnFiltrar);
        btnAgendarNueva = findViewById(R.id.btnAgendarNueva);
        btnRefrescar = findViewById(R.id.btnRefrescar);
        rvCitas = findViewById(R.id.rvCitas);

        // Personalizar título según tipo de usuario con validación
        String titulo;
        if (usuarioTipo != null && usuarioTipo.equals("empleado")) {
            titulo = "Agenda de Trabajo";
        } else {
            titulo = "Mi Agenda";
        }
        tvTituloAgenda.setText(titulo);
    }

    private int obtenerIdUsuario() {
        if (usuarioEmail == null) {
            return -1;
        }
        Usuario usuario = usuarioController.obtenerUsuario(usuarioEmail);
        return usuario != null ? usuario.getId() : -1;
    }

    private void configurarSpinnerEstado() {
        String[] estados = {"Todas", "Confirmadas", "Pendientes", "Canceladas", "Pagadas", "No Pagadas"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                estados
        );
        spinnerEstado.setAdapter(adapter);
        spinnerEstado.setText("Todas", false);
    }

    private void setupListeners() {
        btnVolver.setOnClickListener(v -> {
            Intent intent = new Intent(this, CatalogoActivity.class);
            intent.putExtra("usuario_email", usuarioEmail);
            intent.putExtra("usuario_nombre", usuarioNombre);
            intent.putExtra("usuario_tipo", usuarioTipo);
            startActivity(intent);
            finish();
        });

        btnFiltrar.setOnClickListener(v -> {
            aplicarFiltro();
        });

        btnAgendarNueva.setOnClickListener(v -> {
            Intent intent = new Intent(this, CatalogoActivity.class);
            intent.putExtra("usuario_email", usuarioEmail);
            intent.putExtra("usuario_nombre", usuarioNombre);
            intent.putExtra("usuario_tipo", usuarioTipo);
            startActivity(intent);
            finish();
        });

        btnRefrescar.setOnClickListener(v -> {
            cargarCitas();
            Toast.makeText(this, "Lista actualizada", Toast.LENGTH_SHORT).show();
        });
    }

    private void cargarCitas() {
        // Verificar datos del usuario
        if (usuarioId == -1) {
            mostrarMensajeVacio(true);
            return;
        }

        // Obtener citas según tipo de usuario con validación
        if (usuarioTipo != null && usuarioTipo.equals("empleado")) {
            // Para empleados: mostrar todas las citas asignadas
            todasLasCitas = citaController.obtenerCitasPorEmpleado(usuarioId);
        } else {
            // Para clientes: mostrar solo sus citas
            todasLasCitas = citaController.obtenerCitasPorCliente(usuarioId);
        }

        if (todasLasCitas == null || todasLasCitas.isEmpty()) {
            mostrarMensajeVacio(true);
            Toast.makeText(this, "No tienes citas programadas", Toast.LENGTH_SHORT).show();
        } else {
            mostrarMensajeVacio(false);
            // Enriquecer citas con datos completos
            todasLasCitas = enriquecerCitasConDatos(todasLasCitas);
            aplicarFiltro();
            actualizarContadores();
        }
    }

    private List<Cita> enriquecerCitasConDatos(List<Cita> citas) {
        List<Cita> citasEnriquecidas = new ArrayList<>();

        for (Cita cita : citas) {
            // Obtener servicio
            Servicio servicio = mapaServicios.get(cita.getIdServicio());
            if (servicio == null) {
                servicio = servicioController.obtenerServicioPorId(cita.getIdServicio());
                if (servicio != null) {
                    mapaServicios.put(servicio.getId(), servicio);
                }
            }

            // Aquí podrías también obtener datos del empleado si es necesario
            // Por ahora solo retornamos las citas originales
            citasEnriquecidas.add(cita);
        }

        return citasEnriquecidas;
    }

    private void cargarServiciosEnCache() {
        List<Servicio> servicios = servicioController.obtenerTodosServicios();
        for (Servicio servicio : servicios) {
            if (servicio != null) {
                mapaServicios.put(servicio.getId(), servicio);
            }
        }
    }

    private void aplicarFiltro() {
        if (todasLasCitas == null) {
            citasFiltradas = new ArrayList<>();
            citaAdapter.updateCitas(citasFiltradas);
            return;
        }

        String filtroEstado = spinnerEstado.getText().toString();
        citasFiltradas = new ArrayList<>();

        for (Cita cita : todasLasCitas) {
            boolean cumpleFiltro = true;

            switch (filtroEstado) {
                case "Confirmadas":
                    cumpleFiltro = cita.getEstado() != null &&
                            cita.getEstado().equalsIgnoreCase("confirmada");
                    break;
                case "Pendientes":
                    cumpleFiltro = cita.getEstado() != null &&
                            cita.getEstado().equalsIgnoreCase("pendiente");
                    break;
                case "Canceladas":
                    cumpleFiltro = cita.getEstado() != null &&
                            cita.getEstado().equalsIgnoreCase("cancelada");
                    break;
                case "Pagadas":
                    cumpleFiltro = cita.isPagada();
                    break;
                case "No Pagadas":
                    cumpleFiltro = !cita.isPagada();
                    break;
                // "Todas" no aplica filtro
            }

            if (cumpleFiltro) {
                citasFiltradas.add(cita);
            }
        }

        // Actualizar adapter
        citaAdapter.updateCitas(citasFiltradas);

        // Actualizar mensaje de vacío
        mostrarMensajeVacio(citasFiltradas.isEmpty());
    }

    private void actualizarContadores() {
        if (todasLasCitas == null) {
            tvTotalCitas.setText("Total: 0");
            tvCitasPendientes.setText("Pendientes: 0");
            tvCitasConfirmadas.setText("Confirmadas: 0");
            return;
        }

        int total = todasLasCitas.size();
        int pendientes = 0;
        int confirmadas = 0;

        for (Cita cita : todasLasCitas) {
            if (cita.getEstado() != null) {
                if (cita.getEstado().equalsIgnoreCase("pendiente")) {
                    pendientes++;
                } else if (cita.getEstado().equalsIgnoreCase("confirmada")) {
                    confirmadas++;
                }
            }
        }

        tvTotalCitas.setText("Total: " + total);
        tvCitasPendientes.setText("Pendientes: " + pendientes);
        tvCitasConfirmadas.setText("Confirmadas: " + confirmadas);
    }

    private void mostrarMensajeVacio(boolean mostrar) {
        if (mostrar) {
            tvMensajeVacio.setVisibility(View.VISIBLE);
            rvCitas.setVisibility(View.GONE);
        } else {
            tvMensajeVacio.setVisibility(View.GONE);
            rvCitas.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCitaUpdated() {
        // Recargar citas cuando se actualiza una
        cargarCitas();
    }

    @Override
    public void onCitaDeleted() {
        // Recargar citas cuando se elimina una
        cargarCitas();
    }
}
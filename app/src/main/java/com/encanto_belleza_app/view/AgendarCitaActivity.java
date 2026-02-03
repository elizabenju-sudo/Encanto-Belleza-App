package com.encanto_belleza_app.view;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.encanto_belleza_app.R;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.encanto_belleza_app.controller.CitaController;
import com.encanto_belleza_app.controller.EmpleadoController;
import com.encanto_belleza_app.controller.ServicioController;
import com.encanto_belleza_app.controller.UsuarioController;
import com.encanto_belleza_app.model.Cita;
import com.encanto_belleza_app.model.Empleado;
import com.encanto_belleza_app.model.Servicio;
import com.encanto_belleza_app.model.Usuario;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AgendarCitaActivity extends AppCompatActivity {

    // Views
    private TextView tvServicioNombre, tvServicioDescripcion, tvDuracion, tvPrecio;
    private AutoCompleteTextView autoCompleteEmpleados;
    private EditText etFecha, etHora;
    private Button btnHoy, btnManana, btnPasadoManana, btnAgendarCita;

    // Controllers
    private ServicioController servicioController;
    private EmpleadoController empleadoController;
    private CitaController citaController;
    private UsuarioController usuarioController;

    // Datos
    private Servicio servicioSeleccionado;
    private Empleado empleadoSeleccionado;
    private String fechaSeleccionada = "";
    private String horaSeleccionada = "";
    private String usuarioEmail;
    private Usuario usuarioActual;

    // Fecha y hora
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private SimpleDateFormat timeFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendar_cita);

        // Obtener datos del intent
        Intent intent = getIntent();
        int servicioId = intent.getIntExtra("servicio_id", -1);
        usuarioEmail = intent.getStringExtra("usuario_email");

        // Inicializar controllers
        servicioController = new ServicioController(this);
        empleadoController = new EmpleadoController(this);
        citaController = new CitaController(this);
        usuarioController = new UsuarioController(this);

        // Inicializar formatos de fecha
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        // Inicializar views
        initViews();

        // Cargar servicio
        if (servicioId != -1) {
            cargarServicio(servicioId);
        } else {
            Toast.makeText(this, "Error: Servicio no especificado", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Cargar empleados
        cargarEmpleados();

        // Obtener usuario actual
        usuarioActual = usuarioController.obtenerUsuario(usuarioEmail);

        // Configurar listeners
        setupListeners();
    }

    private void initViews() {
        tvServicioNombre = findViewById(R.id.tvServicioNombre);
        tvServicioDescripcion = findViewById(R.id.tvServicioDescripcion);
        tvDuracion = findViewById(R.id.tvDuracion);
        tvPrecio = findViewById(R.id.tvPrecio);
        autoCompleteEmpleados = findViewById(R.id.autoCompleteEmpleados);
        etFecha = findViewById(R.id.etFecha);
        etHora = findViewById(R.id.etHora);
        btnHoy = findViewById(R.id.btnHoy);
        btnManana = findViewById(R.id.btnManana);
        btnPasadoManana = findViewById(R.id.btnPasadoManana);
        btnAgendarCita = findViewById(R.id.btnAgendarCita);
    }

    private void cargarServicio(int servicioId) {
        servicioSeleccionado = servicioController.obtenerServicioPorId(servicioId);
        if (servicioSeleccionado != null) {
            tvServicioNombre.setText(servicioSeleccionado.getNombre());
            tvServicioDescripcion.setText(servicioSeleccionado.getDescripcion());
            tvDuracion.setText(servicioSeleccionado.getDuracion() + " min");
            tvPrecio.setText(String.format("$%.2f", servicioSeleccionado.getPrecio()));
        }
    }

    private void cargarEmpleados() {
        List<Empleado> empleados = empleadoController.obtenerTodosEmpleados();
        List<String> nombresEmpleados = new ArrayList<>();

        for (Empleado empleado : empleados) {
            nombresEmpleados.add(empleado.getNombre() + " - " + empleado.getEspecialidad());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                nombresEmpleados
        );

        autoCompleteEmpleados.setAdapter(adapter);
        autoCompleteEmpleados.setOnItemClickListener((parent, view, position, id) -> {
            empleadoSeleccionado = empleados.get(position);
        });
    }

    private void setupListeners() {
        // Seleccionar fecha
        etFecha.setOnClickListener(v -> mostrarDialogoFecha());

        // Seleccionar hora
        etHora.setOnClickListener(v -> {
            if (fechaSeleccionada.isEmpty()) {
                Toast.makeText(this, "Primero seleccione una fecha", Toast.LENGTH_SHORT).show();
                return;
            }
            if (empleadoSeleccionado == null) {
                Toast.makeText(this, "Primero seleccione un empleado", Toast.LENGTH_SHORT).show();
                return;
            }
            mostrarDialogoHora();
        });

        // Botones de fecha rápida
        btnHoy.setOnClickListener(v -> seleccionarFechaRapida("hoy"));
        btnManana.setOnClickListener(v -> seleccionarFechaRapida("manana"));
        btnPasadoManana.setOnClickListener(v -> seleccionarFechaRapida("pasado_manana"));

        // Botón agendar cita
        btnAgendarCita.setOnClickListener(v -> agendarCita());
    }

    private void mostrarDialogoFecha() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_calendario_dialog, null);

        DatePicker datePicker = dialogView.findViewById(R.id.datePicker);
        Button btnHoyDialog = dialogView.findViewById(R.id.btnHoyDialog);
        Button btnMananaDialog = dialogView.findViewById(R.id.btnMananaDialog);
        Button btnPasadoMananaDialog = dialogView.findViewById(R.id.btnPasadoMananaDialog);
        Button btnCancelar = dialogView.findViewById(R.id.btnCancelarFecha);
        Button btnConfirmar = dialogView.findViewById(R.id.btnConfirmarFecha);

        // Configurar fecha mínima (hoy)
        Calendar minDate = Calendar.getInstance();
        datePicker.setMinDate(minDate.getTimeInMillis());

        // Configurar fecha máxima (3 meses)
        Calendar maxDate = Calendar.getInstance();
        maxDate.add(Calendar.MONTH, 3);
        datePicker.setMaxDate(maxDate.getTimeInMillis());

        AlertDialog dialog = builder.setView(dialogView).create();

        // Botones de fecha rápida
        btnHoyDialog.setOnClickListener(v -> {
            calendar.setTimeInMillis(System.currentTimeMillis());
            datePicker.updateDate(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
        });

        btnMananaDialog.setOnClickListener(v -> {
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            datePicker.updateDate(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
        });

        btnPasadoMananaDialog.setOnClickListener(v -> {
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.add(Calendar.DAY_OF_YEAR, 2);
            datePicker.updateDate(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
        });

        btnCancelar.setOnClickListener(v -> dialog.dismiss());

        btnConfirmar.setOnClickListener(v -> {
            int year = datePicker.getYear();
            int month = datePicker.getMonth();
            int day = datePicker.getDayOfMonth();

            calendar.set(year, month, day);
            fechaSeleccionada = dateFormat.format(calendar.getTime());

            String fechaFormateada = citaController.formatearFechaParaMostrar(fechaSeleccionada);
            etFecha.setText(fechaFormateada);

            // Limpiar hora si cambia la fecha
            etHora.setText("");
            horaSeleccionada = "";

            dialog.dismiss();
        });

        dialog.show();
    }

    private void mostrarDialogoHora() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.layout_hora_dialog, null);

        TimePicker timePicker = dialogView.findViewById(R.id.timePicker);
        TextView tvFechaSeleccionada = dialogView.findViewById(R.id.tvFechaSeleccionada);
        Button btnCancelar = dialogView.findViewById(R.id.btnCancelarHora);
        Button btnConfirmar = dialogView.findViewById(R.id.btnConfirmarHora);

        // Mostrar fecha seleccionada
        String fechaFormateada = citaController.formatearFechaParaMostrar(fechaSeleccionada);
        tvFechaSeleccionada.setText("Fecha: " + fechaFormateada);

        // Configurar time picker en formato 24h
        timePicker.setIs24HourView(true);

        AlertDialog dialog = builder.setView(dialogView).create();

        btnCancelar.setOnClickListener(v -> dialog.dismiss());

        btnConfirmar.setOnClickListener(v -> {
            int hour = timePicker.getHour();
            int minute = timePicker.getMinute();

            // Formatear hora en HH:mm
            horaSeleccionada = String.format("%02d:%02d", hour, minute);

            // Verificar disponibilidad
            if (empleadoSeleccionado != null) {
                boolean disponible = citaController.verificarDisponibilidad(
                        fechaSeleccionada,
                        horaSeleccionada,
                        empleadoSeleccionado.getId()
                );

                if (disponible) {
                    String horaFormateada = citaController.formatearHoraParaMostrar(horaSeleccionada);
                    etHora.setText(horaFormateada);
                    dialog.dismiss();
                } else {
                    Toast.makeText(this,
                            "Este horario no está disponible. Por favor seleccione otro.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        dialog.show();
    }

    private void seleccionarFechaRapida(String tipo) {
        switch (tipo) {
            case "hoy":
                fechaSeleccionada = citaController.obtenerFechaActual();
                break;
            case "manana":
                fechaSeleccionada = citaController.obtenerFechaManana();
                break;
            case "pasado_manana":
                fechaSeleccionada = citaController.obtenerFechaPasadoManana();
                break;
        }

        String fechaFormateada = citaController.formatearFechaParaMostrar(fechaSeleccionada);
        etFecha.setText(fechaFormateada);

        // Limpiar hora si cambia la fecha
        etHora.setText("");
        horaSeleccionada = "";
    }

    private void agendarCita() {
        // Validaciones
        if (servicioSeleccionado == null) {
            Toast.makeText(this, "Error: Servicio no seleccionado", Toast.LENGTH_SHORT).show();
            return;
        }

        if (empleadoSeleccionado == null) {
            Toast.makeText(this, "Por favor seleccione un empleado", Toast.LENGTH_SHORT).show();
            return;
        }

        if (fechaSeleccionada.isEmpty()) {
            Toast.makeText(this, "Por favor seleccione una fecha", Toast.LENGTH_SHORT).show();
            return;
        }

        if (horaSeleccionada.isEmpty()) {
            Toast.makeText(this, "Por favor seleccione una hora", Toast.LENGTH_SHORT).show();
            return;
        }

        if (usuarioActual == null) {
            Toast.makeText(this, "Error: Usuario no encontrado", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear cita
        Cita cita = new Cita();
        cita.setIdCliente(usuarioActual.getId());
        cita.setIdServicio(servicioSeleccionado.getId());
        cita.setIdEmpleado(empleadoSeleccionado.getId());
        cita.setFecha(fechaSeleccionada);
        cita.setHora(horaSeleccionada);
        cita.setEstado("confirmada");
        cita.setPagada(false);

        // Guardar cita
        boolean exito = citaController.agendarCita(cita);

        if (exito) {
            // Ir a pantalla de confirmación
            Intent intent = new Intent(this, ConfirmacionCitaActivity.class);
            intent.putExtra("servicio_nombre", servicioSeleccionado.getNombre());
            intent.putExtra("empleado_nombre", empleadoSeleccionado.getNombre());
            intent.putExtra("fecha", fechaSeleccionada);
            intent.putExtra("hora", horaSeleccionada);
            intent.putExtra("duracion", servicioSeleccionado.getDuracion());
            intent.putExtra("precio", servicioSeleccionado.getPrecio());
            intent.putExtra("cliente_nombre", usuarioActual.getNombre());
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this,
                    "Error al agendar la cita. El horario puede no estar disponible.",
                    Toast.LENGTH_LONG).show();
        }
    }
}
package com.encanto_belleza_app.controller;

import android.content.Context;

import com.encanto_belleza_app.dao.CitaDAO;
import com.encanto_belleza_app.model.Cita;

import java.util.List;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CitaController {
    private CitaDAO citaDAO;
    private Context context;

    public CitaController(Context context) {
        this.context = context;
        citaDAO = new CitaDAO(context);
    }

    public boolean agendarCita(Cita cita) {
        citaDAO.open();

        // Verificar disponibilidad
        if (citaDAO.existeCitaEnHorario(cita.getFecha(), cita.getHora(), cita.getIdEmpleado())) {
            citaDAO.close();
            return false;
        }

        long result = citaDAO.insertCita(cita);
        citaDAO.close();
        return result != -1;
    }

    public List<Cita> obtenerCitasPorCliente(int idCliente) {
        citaDAO.open();
        List<Cita> citas = citaDAO.getCitasByCliente(idCliente);
        citaDAO.close();
        return citas;
    }

    // MÉTODO ACTUALIZADO: Obtener citas por empleado
    public List<Cita> obtenerCitasPorEmpleado(int idEmpleado) {
        citaDAO.open();
        List<Cita> citas = citaDAO.getCitasByEmpleado(idEmpleado);
        citaDAO.close();
        return citas;
    }

    public List<Cita> obtenerCitasPorFecha(String fecha) {
        citaDAO.open();
        List<Cita> citas = citaDAO.getCitasByFecha(fecha);
        citaDAO.close();
        return citas;
    }

    public boolean actualizarCita(Cita cita) {
        citaDAO.open();
        int result = citaDAO.updateCita(cita);
        citaDAO.close();
        return result > 0;
    }

    public boolean cancelarCita(int idCita) {
        citaDAO.open();
        Cita cita = citaDAO.getCitaById(idCita);
        if (cita != null) {
            cita.setEstado("cancelada");
            int result = citaDAO.updateCita(cita);
            citaDAO.close();
            return result > 0;
        }
        citaDAO.close();
        return false;
    }

    public Cita obtenerCitaPorId(int id) {
        citaDAO.open();
        Cita cita = citaDAO.getCitaById(id);
        citaDAO.close();
        return cita;
    }

    public boolean verificarDisponibilidad(String fecha, String hora, int idEmpleado) {
        citaDAO.open();
        boolean disponible = !citaDAO.existeCitaEnHorario(fecha, hora, idEmpleado);
        citaDAO.close();
        return disponible;
    }

    // Nuevos métodos para manejo de fechas y horas

    public List<String> generarHorasDisponibles(String fecha, int idEmpleado) {
        List<String> horasDisponibles = new ArrayList<>();
        String[] horasBase = {"09:00", "09:30", "10:00", "10:30", "11:00", "11:30",
                "14:00", "14:30", "15:00", "15:30", "16:00", "16:30"};

        citaDAO.open();

        for (String hora : horasBase) {
            if (!citaDAO.existeCitaEnHorario(fecha, hora, idEmpleado)) {
                horasDisponibles.add(hora);
            }
        }

        citaDAO.close();
        return horasDisponibles;
    }

    public String obtenerFechaActual() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }

    public String obtenerFechaManana() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(calendar.getTime());
    }

    public String obtenerFechaPasadoManana() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 2);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(calendar.getTime());
    }

    public String formatearFechaParaMostrar(String fecha) {
        try {
            SimpleDateFormat sdfEntrada = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat sdfSalida = new SimpleDateFormat("dd MMM yyyy", new Locale("es", "ES"));
            Date date = sdfEntrada.parse(fecha);
            return sdfSalida.format(date);
        } catch (Exception e) {
            return fecha;
        }
    }

    public String formatearHoraParaMostrar(String hora) {
        try {
            SimpleDateFormat sdfEntrada = new SimpleDateFormat("HH:mm", Locale.getDefault());
            SimpleDateFormat sdfSalida = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            Date date = sdfEntrada.parse(hora);
            return sdfSalida.format(date);
        } catch (Exception e) {
            return hora;
        }
    }
}
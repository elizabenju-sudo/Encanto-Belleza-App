package com.encanto_belleza_app.controller;

import android.content.Context;

import com.encanto_belleza_app.dao.CitaDAO;
import com.encanto_belleza_app.model.Cita;

import java.util.List;

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
}

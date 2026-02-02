package com.encanto_belleza_app.controller;

import android.content.Context;

import com.encanto_belleza_app.dao.ServicioDAO;
import com.encanto_belleza_app.model.Servicio;

import java.util.List;

public class ServicioController {
    private ServicioDAO servicioDAO;
    private Context context;
    
    public ServicioController(Context context) {
        this.context = context;
        servicioDAO = new ServicioDAO(context);
    }
    
    public List<Servicio> obtenerTodosServicios() {
        servicioDAO.open();
        List<Servicio> servicios = servicioDAO.getAllServicios();
        servicioDAO.close();
        return servicios;
    }
    
    public Servicio obtenerServicioPorId(int id) {
        servicioDAO.open();
        Servicio servicio = servicioDAO.getServicioById(id);
        servicioDAO.close();
        return servicio;
    }
    
    public boolean agregarServicio(Servicio servicio) {
        servicioDAO.open();
        long result = servicioDAO.insertServicio(servicio);
        servicioDAO.close();
        return result != -1;
    }
    
    public boolean actualizarServicio(Servicio servicio) {
        servicioDAO.open();
        int result = servicioDAO.updateServicio(servicio);
        servicioDAO.close();
        return result > 0;
    }
    
    public boolean eliminarServicio(int id) {
        servicioDAO.open();
        int result = servicioDAO.deleteServicio(id);
        servicioDAO.close();
        return result > 0;
    }
    
    public List<Servicio> buscarServicios(String query) {
        servicioDAO.open();
        List<Servicio> servicios = servicioDAO.searchServicios(query);
        servicioDAO.close();
        return servicios;
    }
}

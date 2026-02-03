package com.encanto_belleza_app.controller;

import android.content.Context;
import com.encanto_belleza_app.dao.EmpleadoDAO;
import com.encanto_belleza_app.model.Empleado;
import java.util.List;

public class EmpleadoController {
    private EmpleadoDAO empleadoDAO;
    private Context context;

    public EmpleadoController(Context context) {
        this.context = context;
        empleadoDAO = new EmpleadoDAO(context);
    }

    public List<Empleado> obtenerTodosEmpleados() {
        empleadoDAO.open();
        List<Empleado> empleados = empleadoDAO.getAllEmpleados();
        empleadoDAO.close();
        return empleados;
    }

    public Empleado obtenerEmpleadoPorId(int id) {
        empleadoDAO.open();
        Empleado empleado = empleadoDAO.getEmpleadoById(id);
        empleadoDAO.close();
        return empleado;
    }

    public List<Empleado> obtenerEmpleadosDisponibles(String fecha, String hora) {
        empleadoDAO.open();
        List<Empleado> empleados = empleadoDAO.getEmpleadosDisponibles(fecha, hora);
        empleadoDAO.close();
        return empleados;
    }

    public boolean verificarDisponibilidadEmpleado(int idEmpleado, String fecha, String hora) {
        empleadoDAO.open();
        List<Empleado> disponibles = empleadoDAO.getEmpleadosDisponibles(fecha, hora);
        empleadoDAO.close();

        for (Empleado empleado : disponibles) {
            if (empleado.getId() == idEmpleado) {
                return true;
            }
        }
        return false;
    }
}

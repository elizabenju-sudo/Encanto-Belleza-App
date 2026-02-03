package com.encanto_belleza_app.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.encanto_belleza_app.database.DatabaseHelper;
import com.encanto_belleza_app.model.Empleado;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAO {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public EmpleadoDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public List<Empleado> getAllEmpleados() {
        List<Empleado> empleados = new ArrayList<>();
        Cursor cursor = database.rawQuery(
                "SELECT * FROM usuarios WHERE tipo = 'empleado' ORDER BY nombre",
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Empleado empleado = new Empleado();
                empleado.setId(cursor.getInt(0));
                empleado.setNombre(cursor.getString(3));
                empleado.setEmail(cursor.getString(1));
                empleado.setEspecialidad("Estilista Profesional");
                empleado.setDisponible(true);
                empleados.add(empleado);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return empleados;
    }

    public Empleado getEmpleadoById(int id) {
        Cursor cursor = database.rawQuery(
                "SELECT * FROM usuarios WHERE id = ? AND tipo = 'empleado'",
                new String[]{String.valueOf(id)}
        );

        if (cursor != null && cursor.moveToFirst()) {
            Empleado empleado = new Empleado();
            empleado.setId(cursor.getInt(0));
            empleado.setNombre(cursor.getString(3));
            empleado.setEmail(cursor.getString(1));
            empleado.setEspecialidad("Estilista Profesional");
            empleado.setDisponible(true);
            cursor.close();
            return empleado;
        }
        return null;
    }

    public List<Empleado> getEmpleadosDisponibles(String fecha, String hora) {
        List<Empleado> empleados = getAllEmpleados();
        List<Empleado> disponibles = new ArrayList<>();

        for (Empleado empleado : empleados) {
            if (isEmpleadoDisponible(empleado.getId(), fecha, hora)) {
                disponibles.add(empleado);
            }
        }

        return disponibles;
    }

    private boolean isEmpleadoDisponible(int idEmpleado, String fecha, String hora) {
        Cursor cursor = database.rawQuery(
                "SELECT * FROM citas WHERE id_empleado = ? AND fecha = ? AND hora = ? AND estado != 'cancelada'",
                new String[]{String.valueOf(idEmpleado), fecha, hora}
        );

        boolean ocupado = cursor.getCount() > 0;
        cursor.close();
        return !ocupado;
    }
}

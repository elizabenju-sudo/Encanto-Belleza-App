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
        Cursor cursor = database.query(
                "empleados",
                null,
                null,
                null,
                null, null, "nombre ASC"
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Empleado empleado = new Empleado();
                empleado.setId(cursor.getInt(0));
                empleado.setNombre(cursor.getString(1));
                empleado.setEspecialidad(cursor.getString(2));
                empleado.setExperiencia(cursor.getString(3));
                empleado.setEmail(cursor.getString(4));
                empleado.setTelefono(cursor.getString(5));
                empleado.setDisponible(cursor.getInt(6) == 1);
                empleado.setHorario(cursor.getString(7));
                empleados.add(empleado);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return empleados;
    }

    public Empleado getEmpleadoById(int id) {
        Cursor cursor = database.query(
                "empleados",
                null,
                "id = ?",
                new String[]{String.valueOf(id)},
                null, null, null
        );

        if (cursor != null && cursor.moveToFirst()) {
            Empleado empleado = new Empleado();
            empleado.setId(cursor.getInt(0));
            empleado.setNombre(cursor.getString(1));
            empleado.setEspecialidad(cursor.getString(2));
            empleado.setExperiencia(cursor.getString(3));
            empleado.setEmail(cursor.getString(4));
            empleado.setTelefono(cursor.getString(5));
            empleado.setDisponible(cursor.getInt(6) == 1);
            empleado.setHorario(cursor.getString(7));
            cursor.close();
            return empleado;
        }
        return null;
    }

    public List<Empleado> getEmpleadosDisponibles(String fecha, String hora) {
        List<Empleado> todosEmpleados = getAllEmpleados();
        List<Empleado> disponibles = new ArrayList<>();

        for (Empleado empleado : todosEmpleados) {
            if (empleado.isDisponible() && isEmpleadoDisponible(empleado.getId(), fecha, hora)) {
                disponibles.add(empleado);
            }
        }

        return disponibles;
    }

    public List<Empleado> getEmpleadosPorEspecialidad(String especialidad) {
        List<Empleado> empleados = new ArrayList<>();
        Cursor cursor = database.query(
                "empleados",
                null,
                "especialidad LIKE ? AND disponible = 1",
                new String[]{"%" + especialidad + "%"},
                null, null, "nombre ASC"
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Empleado empleado = new Empleado();
                empleado.setId(cursor.getInt(0));
                empleado.setNombre(cursor.getString(1));
                empleado.setEspecialidad(cursor.getString(2));
                empleado.setExperiencia(cursor.getString(3));
                empleado.setEmail(cursor.getString(4));
                empleado.setTelefono(cursor.getString(5));
                empleado.setDisponible(cursor.getInt(6) == 1);
                empleado.setHorario(cursor.getString(7));
                empleados.add(empleado);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return empleados;
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

    public long insertEmpleado(Empleado empleado) {
        ContentValues values = new ContentValues();
        values.put("nombre", empleado.getNombre());
        values.put("especialidad", empleado.getEspecialidad());
        values.put("experiencia", empleado.getExperiencia());
        values.put("email", empleado.getEmail());
        values.put("telefono", empleado.getTelefono());
        values.put("disponible", empleado.isDisponible() ? 1 : 0);
        values.put("horario", empleado.getHorario());

        return database.insert("empleados", null, values);
    }

    public int updateEmpleado(Empleado empleado) {
        ContentValues values = new ContentValues();
        values.put("nombre", empleado.getNombre());
        values.put("especialidad", empleado.getEspecialidad());
        values.put("experiencia", empleado.getExperiencia());
        values.put("email", empleado.getEmail());
        values.put("telefono", empleado.getTelefono());
        values.put("disponible", empleado.isDisponible() ? 1 : 0);
        values.put("horario", empleado.getHorario());

        return database.update("empleados", values,
                "id = ?", new String[]{String.valueOf(empleado.getId())});
    }
}
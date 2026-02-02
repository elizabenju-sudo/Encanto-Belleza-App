package com.encanto_belleza_app.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.encanto_belleza_app.database.DatabaseHelper;
import com.encanto_belleza_app.model.Cita;

import java.util.ArrayList;
import java.util.List;

public class CitaDAO {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;
    
    public CitaDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }
    
    public void open() {
        database = dbHelper.getWritableDatabase();
    }
    
    public void close() {
        dbHelper.close();
    }
    
    public long insertCita(Cita cita) {
        ContentValues values = new ContentValues();
        values.put("id_cliente", cita.getIdCliente());
        values.put("id_servicio", cita.getIdServicio());
        values.put("id_empleado", cita.getIdEmpleado());
        values.put("fecha", cita.getFecha());
        values.put("hora", cita.getHora());
        values.put("estado", cita.getEstado());
        values.put("pagada", cita.isPagada() ? 1 : 0);
        
        return database.insert("citas", null, values);
    }
    
    public List<Cita> getCitasByCliente(int idCliente) {
        List<Cita> citas = new ArrayList<>();
        Cursor cursor = database.query(
            "citas",
            null,
            "id_cliente = ?",
            new String[]{String.valueOf(idCliente)},
            null, null, "fecha DESC, hora DESC"
        );
        
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Cita cita = new Cita();
                cita.setId(cursor.getInt(0));
                cita.setIdCliente(cursor.getInt(1));
                cita.setIdServicio(cursor.getInt(2));
                cita.setIdEmpleado(cursor.getInt(3));
                cita.setFecha(cursor.getString(4));
                cita.setHora(cursor.getString(5));
                cita.setEstado(cursor.getString(6));
                cita.setPagada(cursor.getInt(7) == 1);
                citas.add(cita);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return citas;
    }
    
    public List<Cita> getCitasByFecha(String fecha) {
        List<Cita> citas = new ArrayList<>();
        Cursor cursor = database.query(
            "citas",
            null,
            "fecha = ?",
            new String[]{fecha},
            null, null, "hora ASC"
        );
        
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Cita cita = new Cita();
                cita.setId(cursor.getInt(0));
                cita.setIdCliente(cursor.getInt(1));
                cita.setIdServicio(cursor.getInt(2));
                cita.setIdEmpleado(cursor.getInt(3));
                cita.setFecha(cursor.getString(4));
                cita.setHora(cursor.getString(5));
                cita.setEstado(cursor.getString(6));
                cita.setPagada(cursor.getInt(7) == 1);
                citas.add(cita);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return citas;
    }
    
    public int updateCita(Cita cita) {
        ContentValues values = new ContentValues();
        values.put("id_cliente", cita.getIdCliente());
        values.put("id_servicio", cita.getIdServicio());
        values.put("id_empleado", cita.getIdEmpleado());
        values.put("fecha", cita.getFecha());
        values.put("hora", cita.getHora());
        values.put("estado", cita.getEstado());
        values.put("pagada", cita.isPagada() ? 1 : 0);
        
        return database.update("citas", values, 
                             "id = ?", new String[]{String.valueOf(cita.getId())});
    }
    
    public int deleteCita(int id) {
        return database.delete("citas", "id = ?", new String[]{String.valueOf(id)});
    }
    
    public boolean existeCitaEnHorario(String fecha, String hora, int idEmpleado) {
        Cursor cursor = database.rawQuery(
            "SELECT * FROM citas WHERE fecha = ? AND hora = ? AND id_empleado = ? AND estado != 'cancelada'",
            new String[]{fecha, hora, String.valueOf(idEmpleado)}
        );
        
        boolean existe = cursor.getCount() > 0;
        cursor.close();
        return existe;
    }
    
    public Cita getCitaById(int id) {
        Cursor cursor = database.query(
            "citas",
            null,
            "id = ?",
            new String[]{String.valueOf(id)},
            null, null, null
        );
        
        if (cursor != null && cursor.moveToFirst()) {
            Cita cita = new Cita();
            cita.setId(cursor.getInt(0));
            cita.setIdCliente(cursor.getInt(1));
            cita.setIdServicio(cursor.getInt(2));
            cita.setIdEmpleado(cursor.getInt(3));
            cita.setFecha(cursor.getString(4));
            cita.setHora(cursor.getString(5));
            cita.setEstado(cursor.getString(6));
            cita.setPagada(cursor.getInt(7) == 1);
            cursor.close();
            return cita;
        }
        return null;
    }
}

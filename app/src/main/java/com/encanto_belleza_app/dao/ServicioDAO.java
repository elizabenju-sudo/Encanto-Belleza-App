package com.encanto_belleza_app.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.encanto_belleza_app.database.DatabaseHelper;
import com.encanto_belleza_app.model.Servicio;

import java.util.ArrayList;
import java.util.List;

public class ServicioDAO {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;
    
    public ServicioDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }
    
    public void open() {
        database = dbHelper.getWritableDatabase();
    }
    
    public void close() {
        dbHelper.close();
    }
    
    public long insertServicio(Servicio servicio) {
        ContentValues values = new ContentValues();
        values.put("nombre", servicio.getNombre());
        values.put("descripcion", servicio.getDescripcion());
        values.put("duracion", servicio.getDuracion());
        values.put("precio", servicio.getPrecio());
        values.put("imagen", servicio.getImagen());
        
        return database.insert("servicios", null, values);
    }
    
    public List<Servicio> getAllServicios() {
        List<Servicio> servicios = new ArrayList<>();
        Cursor cursor = database.query(
            "servicios",
            null,
            null,
            null,
            null, null, "nombre ASC"
        );
        
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Servicio servicio = new Servicio();
                servicio.setId(cursor.getInt(0));
                servicio.setNombre(cursor.getString(1));
                servicio.setDescripcion(cursor.getString(2));
                servicio.setDuracion(cursor.getInt(3));
                servicio.setPrecio(cursor.getDouble(4));
                servicio.setImagen(cursor.getString(5));
                servicios.add(servicio);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return servicios;
    }
    
    public Servicio getServicioById(int id) {
        Cursor cursor = database.query(
            "servicios",
            null,
            "id = ?",
            new String[]{String.valueOf(id)},
            null, null, null
        );
        
        if (cursor != null && cursor.moveToFirst()) {
            Servicio servicio = new Servicio();
            servicio.setId(cursor.getInt(0));
            servicio.setNombre(cursor.getString(1));
            servicio.setDescripcion(cursor.getString(2));
            servicio.setDuracion(cursor.getInt(3));
            servicio.setPrecio(cursor.getDouble(4));
            servicio.setImagen(cursor.getString(5));
            cursor.close();
            return servicio;
        }
        return null;
    }
    
    public int updateServicio(Servicio servicio) {
        ContentValues values = new ContentValues();
        values.put("nombre", servicio.getNombre());
        values.put("descripcion", servicio.getDescripcion());
        values.put("duracion", servicio.getDuracion());
        values.put("precio", servicio.getPrecio());
        values.put("imagen", servicio.getImagen());
        
        return database.update("servicios", values, 
                             "id = ?", new String[]{String.valueOf(servicio.getId())});
    }
    
    public int deleteServicio(int id) {
        return database.delete("servicios", "id = ?", new String[]{String.valueOf(id)});
    }
    
    public List<Servicio> searchServicios(String query) {
        List<Servicio> servicios = new ArrayList<>();
        Cursor cursor = database.rawQuery(
            "SELECT * FROM servicios WHERE nombre LIKE ? OR descripcion LIKE ?",
            new String[]{"%" + query + "%", "%" + query + "%"}
        );
        
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Servicio servicio = new Servicio();
                servicio.setId(cursor.getInt(0));
                servicio.setNombre(cursor.getString(1));
                servicio.setDescripcion(cursor.getString(2));
                servicio.setDuracion(cursor.getInt(3));
                servicio.setPrecio(cursor.getDouble(4));
                servicio.setImagen(cursor.getString(5));
                servicios.add(servicio);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return servicios;
    }
}

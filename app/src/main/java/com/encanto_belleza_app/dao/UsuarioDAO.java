package com.encanto_belleza_app.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.encanto_belleza_app.database.DatabaseHelper;
import com.encanto_belleza_app.model.Usuario;

public class UsuarioDAO {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;
    
    public UsuarioDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }
    
    public void open() {
        database = dbHelper.getWritableDatabase();
    }
    
    public void close() {
        dbHelper.close();
    }
    
    public long insertUsuario(Usuario usuario) {
        ContentValues values = new ContentValues();
        values.put("email", usuario.getEmail());
        values.put("password", usuario.getPassword());
        values.put("nombre", usuario.getNombre());
        values.put("tipo", usuario.getTipo());
        
        return database.insert("usuarios", null, values);
    }
    
    public Usuario getUsuarioByEmail(String email) {
        Cursor cursor = database.query(
            "usuarios",
            null,
            "email = ?",
            new String[]{email},
            null, null, null
        );
        
        if (cursor != null && cursor.moveToFirst()) {
            Usuario usuario = new Usuario();
            usuario.setId(cursor.getInt(0));
            usuario.setEmail(cursor.getString(1));
            usuario.setPassword(cursor.getString(2));
            usuario.setNombre(cursor.getString(3));
            usuario.setTipo(cursor.getString(4));
            cursor.close();
            return usuario;
        }
        return null;
    }
    
    public boolean validarLogin(String email, String password) {
        Cursor cursor = database.rawQuery(
            "SELECT * FROM usuarios WHERE email = ? AND password = ?",
            new String[]{email, password}
        );
        
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
    
    public Usuario autenticarUsuario(String email, String password) {
        Cursor cursor = database.rawQuery(
            "SELECT * FROM usuarios WHERE email = ? AND password = ?",
            new String[]{email, password}
        );
        
        if (cursor != null && cursor.moveToFirst()) {
            Usuario usuario = new Usuario();
            usuario.setId(cursor.getInt(0));
            usuario.setEmail(cursor.getString(1));
            usuario.setPassword(cursor.getString(2));
            usuario.setNombre(cursor.getString(3));
            usuario.setTipo(cursor.getString(4));
            cursor.close();
            return usuario;
        }
        return null;
    }
    
    public boolean existeUsuario(String email) {
        Cursor cursor = database.rawQuery(
            "SELECT * FROM usuarios WHERE email = ?",
            new String[]{email}
        );
        
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
}

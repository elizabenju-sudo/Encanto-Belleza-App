package com.encanto_belleza_app.controller;

import android.content.Context;

import com.encanto_belleza_app.dao.UsuarioDAO;
import com.encanto_belleza_app.model.Usuario;

public class UsuarioController {
    private UsuarioDAO usuarioDAO;
    private Context context;
    
    public UsuarioController(Context context) {
        this.context = context;
        usuarioDAO = new UsuarioDAO(context);
    }
    
    public boolean registrarUsuario(String email, String password, String nombre) {
        usuarioDAO.open();
        
        // Verificar si el usuario ya existe
        if (usuarioDAO.existeUsuario(email)) {
            usuarioDAO.close();
            return false;
        }
        
        Usuario usuario = new Usuario(email, password, nombre, "cliente");
        long result = usuarioDAO.insertUsuario(usuario);
        usuarioDAO.close();
        return result != -1;
    }
    
    public Usuario iniciarSesion(String email, String password) {
        usuarioDAO.open();
        Usuario usuario = usuarioDAO.autenticarUsuario(email, password);
        usuarioDAO.close();
        return usuario;
    }
    
    public Usuario obtenerUsuario(String email) {
        usuarioDAO.open();
        Usuario usuario = usuarioDAO.getUsuarioByEmail(email);
        usuarioDAO.close();
        return usuario;
    }
    
    public boolean validarCredenciales(String email, String password) {
        usuarioDAO.open();
        boolean isValid = usuarioDAO.validarLogin(email, password);
        usuarioDAO.close();
        return isValid;
    }
}

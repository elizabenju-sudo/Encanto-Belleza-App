package com.encanto_belleza_app.model;

public class Usuario {
    private int id;
    private String email;
    private String password;
    private String nombre;
    private String tipo; // cliente, empleado, admin
    
    public Usuario() {}
    
    public Usuario(String email, String password, String nombre, String tipo) {
        this.email = email;
        this.password = password;
        this.nombre = nombre;
        this.tipo = tipo;
    }
    
    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    
    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", nombre='" + nombre + '\'' +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}

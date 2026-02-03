package com.encanto_belleza_app.model;

public class Empleado {
    private int id;
    private String nombre;
    private String email;
    private String especialidad;
    private boolean disponible;

    public Empleado() {}

    public Empleado(String nombre, String email, String especialidad) {
        this.nombre = nombre;
        this.email = email;
        this.especialidad = especialidad;
        this.disponible = true;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    @Override
    public String toString() {
        return nombre + " - " + especialidad;
    }
}
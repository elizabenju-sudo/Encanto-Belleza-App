package com.encanto_belleza_app.model;

public class Empleado {
    private int id;
    private String nombre;
    private String especialidad;
    private String experiencia;
    private String email;
    private String telefono;
    private boolean disponible;
    private String horario;

    public Empleado() {}

    public Empleado(String nombre, String especialidad, String email) {
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.email = email;
        this.disponible = true;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public String getExperiencia() { return experiencia; }
    public void setExperiencia(String experiencia) { this.experiencia = experiencia; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    public String getHorario() { return horario; }
    public void setHorario(String horario) { this.horario = horario; }

    @Override
    public String toString() {
        return nombre + " - " + especialidad;
    }
}
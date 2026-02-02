package com.encanto_belleza_app.model;

public class Cita {
    private int id;
    private int idCliente;
    private int idServicio;
    private int idEmpleado;
    private String fecha;
    private String hora;
    private String estado; // pendiente, confirmada, cancelada, atendida
    private boolean pagada;
    
    public Cita() {}
    
    public Cita(int idCliente, int idServicio, String fecha, String hora) {
        this.idCliente = idCliente;
        this.idServicio = idServicio;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = "pendiente";
        this.pagada = false;
    }
    
    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
    
    public int getIdServicio() { return idServicio; }
    public void setIdServicio(int idServicio) { this.idServicio = idServicio; }
    
    public int getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(int idEmpleado) { this.idEmpleado = idEmpleado; }
    
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    
    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public boolean isPagada() { return pagada; }
    public void setPagada(boolean pagada) { this.pagada = pagada; }
    
    @Override
    public String toString() {
        return "Cita{" +
                "id=" + id +
                ", idCliente=" + idCliente +
                ", idServicio=" + idServicio +
                ", fecha='" + fecha + '\'' +
                ", hora='" + hora + '\'' +
                ", estado='" + estado + '\'' +
                ", pagada=" + pagada +
                '}';
    }
}

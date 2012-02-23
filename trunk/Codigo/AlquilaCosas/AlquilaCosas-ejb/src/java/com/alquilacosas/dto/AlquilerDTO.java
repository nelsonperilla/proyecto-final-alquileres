/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.dto;

import com.alquilacosas.ejb.entity.EstadoAlquiler.NombreEstadoAlquiler;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author ignaciogiagante
 */
public class AlquilerDTO implements Serializable {

    private int idPublicacion;
    private int idUsuario;
    private int idAlquiler;
    private int idPedidoCambio = -1;
    private String titulo, usuario;
    private Date fechaInicio;
    private Date fechaFin;
    private String fechaDesde;
    private String fechaHasta;
    private Integer imagenId = -1;
    private int cantidad;
    private double monto;
    private boolean calificado;
    private NombreEstadoAlquiler estadoAlquiler;
    private SimpleDateFormat formatoDia = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat formatoCompleto = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private boolean tomado;

    public AlquilerDTO() {
    }

    public AlquilerDTO(int idPublicacion, String titulo, int idUsuario, int idAlquiler,
            Date fechaInicioAlquiler, Date fechaFinAlquiler, int cantidad,
            NombreEstadoAlquiler estadoAlquiler, Integer imagenId) {

        this.idPublicacion = idPublicacion;
        this.idUsuario = idUsuario;
        this.idAlquiler = idAlquiler;
        this.fechaInicio = fechaInicioAlquiler;
        this.fechaFin = fechaFinAlquiler;
        this.cantidad = cantidad;
        this.estadoAlquiler = estadoAlquiler;
        this.imagenId = imagenId;
        this.titulo = titulo;

        fechaDesde = formatoDia.format(fechaInicio);
        fechaHasta = formatoDia.format(fechaFin);
    }

    public AlquilerDTO(int idPublicacion, int idUsuario, int idAlquiler, int imagenId,
            Date fechaInicioAlquiler,
            Date fechaFinAlquiler, NombreEstadoAlquiler estado, String titulo, 
            String usuario, int cantidad, double monto, boolean calificado) {
        this.idPublicacion = idPublicacion;
        this.idUsuario = idUsuario;
        this.idAlquiler = idAlquiler;
        this.fechaInicio = fechaInicioAlquiler;
        this.fechaFin = fechaFinAlquiler;
        this.usuario = usuario;
        this.titulo = titulo;
        this.cantidad = cantidad;
        this.monto = monto;
        this.calificado = calificado;
        this.estadoAlquiler = estado;
        this.imagenId = imagenId;
        fechaDesde = formatoDia.format(fechaInicio);
        fechaHasta = formatoDia.format(fechaFin);
    }
    
    public AlquilerDTO(int idPublicacion, int idUsuario, int idAlquiler, Date fechaInicioAlquiler,
            Date fechaFinAlquiler, double monto, boolean calificado) {
        this.idPublicacion = idPublicacion;
        this.idUsuario = idUsuario;
        this.idAlquiler = idAlquiler;
        this.fechaInicio = fechaInicioAlquiler;
        this.fechaFin = fechaFinAlquiler;
        this.monto = monto;
        this.calificado = calificado;

        fechaDesde = formatoDia.format(fechaInicio);
        fechaHasta = formatoDia.format(fechaFin);
    }
    
    

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public NombreEstadoAlquiler getEstadoAlquiler() {
        return estadoAlquiler;
    }

    public void setEstadoAlquiler(NombreEstadoAlquiler estadoAlquiler) {
        this.estadoAlquiler = estadoAlquiler;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFinAlquiler) {
        this.fechaFin = fechaFinAlquiler;
        fechaHasta = formatoDia.format(fechaFin);
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicioAlquiler) {
        this.fechaInicio = fechaInicioAlquiler;
        fechaDesde = formatoDia.format(fechaInicio);
    }

    public int getIdAlquiler() {
        return idAlquiler;
    }

    public void setIdAlquiler(int idAlquiler) {
        this.idAlquiler = idAlquiler;
    }

    public int getIdPublicacion() {
        return idPublicacion;
    }

    public void setIdPublicacion(int idPublicacion) {
        this.idPublicacion = idPublicacion;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public boolean isCalificado() {
        return calificado;
    }

    public void setCalificado(boolean calificado) {
        this.calificado = calificado;
    }

    public String getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(String fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public String getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(String fechaHasta) {
        this.fechaHasta = fechaHasta;
    }
    
    public String getFechaInicioConHora() {
        return formatoCompleto.format(fechaInicio);
    }
    
    public String getFechaFinConHora() {
        return formatoCompleto.format(fechaFin);
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getImagenId() {
        return imagenId;
    }

    public void setImagenId(Integer imagenId) {
        this.imagenId = imagenId;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public int getIdPedidoCambio() {
        return idPedidoCambio;
    }

    public void setIdPedidoCambio(int idPedidoCambio) {
        this.idPedidoCambio = idPedidoCambio;
    }
    
    public boolean isCancelable() {
        if(estadoAlquiler == NombreEstadoAlquiler.CONFIRMADO ||
                estadoAlquiler == NombreEstadoAlquiler.ACTIVO) {
            return true;
        }
        else
            return false;
    }
    
    public boolean isCalificable() {
        if(estadoAlquiler == NombreEstadoAlquiler.FINALIZADO ||
                estadoAlquiler == NombreEstadoAlquiler.CANCELADO ||
                estadoAlquiler == NombreEstadoAlquiler.CANCELADO_ALQUILADOR) {
            return true;
        }
        else
            return false;
    }
    
    public boolean isAlquilerPorHora() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fechaInicio);
        int n1 = cal.get(Calendar.HOUR_OF_DAY) + cal.get(Calendar.MINUTE);
        cal.setTime(fechaFin);
        int n2 = cal.get(Calendar.HOUR_OF_DAY) + cal.get(Calendar.MINUTE);
        if(n1 + n2 == 0) 
            return false;
        else
            return true;
    }

    /**
     * Indica si en el alquiler, el usuario fue alquilador (true) o dueño (false)
     * @return 
     */
    public boolean isTomado() {
        return tomado;
    }

    public void setTomado(boolean tomado) {
        this.tomado = tomado;
    }
}

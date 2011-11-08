/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.dto;

import com.alquilacosas.ejb.entity.TipoPublicidad.DuracionPublicidad;
import com.alquilacosas.ejb.entity.TipoPublicidad.UbicacionPublicidad;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author damiancardozo
 */
public class PublicidadDTO implements Serializable {
    
    public enum EstadoPublicidad { ACTIVA, PENDIENTE, INACTIVA };
    
    private int id;
    private String titulo, caption, url;
    private byte[] imagen;
    private Date fechaDesde;
    private Date fechaHasta;
    private double costo;
    private EstadoPublicidad estado;
    private DuracionPublicidad duracion;
    private UbicacionPublicidad ubicacion;
    

    public PublicidadDTO(int id, String titulo, String caption, String url) {
        this.id = id;
        this.titulo = titulo;
        this.caption = caption;
        this.url = url;
    }
    
    public PublicidadDTO( Integer id, String titulo, String url, String caption, Date fechaDesde,
            Date fechaHasta, double costo, EstadoPublicidad estado, byte [] imagen,
            DuracionPublicidad duracion, UbicacionPublicidad ubicacion){
        this.id = id;
        this.titulo = titulo;
        this.url = url;
        this.caption = caption;
        this.fechaDesde = fechaDesde;
        this.fechaHasta = fechaHasta;
        this.costo = costo;
        this.estado = estado;
        this.imagen = imagen;
        this.duracion = duracion;
        this.ubicacion = ubicacion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public EstadoPublicidad getEstado() {
        return estado;
    }
    
    public String getEstadoPublicidad() {
        return estado.name();
    }

    public void setEstado(EstadoPublicidad estado) {
        this.estado = estado;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public DuracionPublicidad getDuracion() {
        return duracion;
    }

    public void setDuracion(DuracionPublicidad duracion) {
        this.duracion = duracion;
    }

    public UbicacionPublicidad getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(UbicacionPublicidad ubicacion) {
        this.ubicacion = ubicacion;
    }

    
}

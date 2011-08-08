/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.common;

import java.util.Date;
import java.util.List;


/**
 *
 * @author jorge
 */
public class PublicacionFacade {
    
    
    public PublicacionFacade(int id,String titulo,String descripcion, Date fecha_desde,
            Date fecha_hasta, boolean destacada,int cantidad)//, Categoria categoria,Usuario usuario) 
    {
        this.id=id;
        this.titulo=titulo;
        this.descripcion=descripcion;
        this.fecha_desde=fecha_desde;
        this.fecha_hasta=fecha_hasta;
        this.destacada=destacada;
        this.cantidad=cantidad;
        //this.categoria=categoria;
        //this.usuario=usuario;
    }
    
    private int id;
    private String titulo;
    private String descripcion;
    private Date fecha_desde;
    private Date fecha_hasta;
    private boolean destacada;
    private int cantidad;
    private int imagenId = -1;
    private List<Integer> imagenIds;
    //private Categoria categoria;
    //private Usuario usuario;
    //private int pagina;
    
    /**
     * @return the titulo
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * @param titulo the titulo to set
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * @return the fecha_desde
     */
    public Date getFecha_desde() {
        return fecha_desde;
    }

    /**
     * @param fecha_desde the fecha_desde to set
     */
    public void setFecha_desde(Date fecha_desde) {
        this.fecha_desde = fecha_desde;
    }

    /**
     * @return the fecha_hasta
     */
   public Date getFecha_hasta() {
        return fecha_hasta;
    }

    /**
     * @param fecha_hasta the fecha_hasta to set
     */
    public void setFecha_hasta(Date fecha_hasta) {
        this.fecha_hasta = fecha_hasta;
    }

    /**
     * @return the destacada
     */
    public boolean getDestacada() {
        return destacada;
    }

    /**
     * @param destacada the destacada to set
     */
    public void setDestacada(boolean destacada) {
        this.destacada = destacada;
    }

    /**
     * @return the cantidad
     */
    public int getCantidad() {
        
        return cantidad;
    }

    /**
     * @param cantidad the cantidad to set
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }


    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    public int getImagenId() {
        return imagenId;
    }

    public void setImagenId(int imagenId) {
        this.imagenId = imagenId;
    }

    public List<Integer> getImagenIds() {
        return imagenIds;
    }

    public void setImagenIds(List<Integer> imagenIds) {
        this.imagenIds = imagenIds;
        if(imagenIds != null && !imagenIds.isEmpty())
            imagenId = imagenIds.get(0);
    }


    
}

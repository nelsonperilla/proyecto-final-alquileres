/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.common;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author damiancardozo
 */
public class CategoriaFacade {
    
    private int id, padreId;
    private String nombre, descripcion;
    private List<CategoriaFacade> subcategorias;

    public CategoriaFacade(int id, int padreId, String nombre, String descripcion) {
        this.id = id;
        this.padreId = padreId;
        this.nombre = nombre;
        this.descripcion = descripcion;
        subcategorias = new ArrayList<CategoriaFacade>();
    }
    
    public CategoriaFacade(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        subcategorias = new ArrayList<CategoriaFacade>();
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPadreId() {
        return padreId;
    }

    public void setPadreId(int padreId) {
        this.padreId = padreId;
    }

    public List<CategoriaFacade> getSubcategorias() {
        return subcategorias;
    }

    public void setSubcategorias(List<CategoriaFacade> subcategorias) {
        this.subcategorias = subcategorias;
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.dto;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author damiancardozo
 */
public class CategoriaDTO {
    
    private int id, padreId;
    private String nombre, descripcion;
    private List<CategoriaDTO> subcategorias;

    public CategoriaDTO(int id, int padreId, String nombre, String descripcion) {
        this.id = id;
        this.padreId = padreId;
        this.nombre = nombre;
        this.descripcion = descripcion;
        subcategorias = new ArrayList<CategoriaDTO>();
    }
    
    public CategoriaDTO(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        subcategorias = new ArrayList<CategoriaDTO>();
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

    public List<CategoriaDTO> getSubcategorias() {
        return subcategorias;
    }

    public void setSubcategorias(List<CategoriaDTO> subcategorias) {
        this.subcategorias = subcategorias;
    }
    
}

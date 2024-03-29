/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author damiancardozo
 */
public class CategoriaDTO implements Serializable  {
    
    private int id, nivel;
    private Integer padreId;
    private String nombre, descripcion;
    private List<CategoriaDTO> subcategorias;
    private CategoriaDTO padre;

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

    public Integer getPadreId() {
        return padreId;
    }

    public void setPadreId(Integer padreId) {
        this.padreId = padreId;
    }

    public List<CategoriaDTO> getSubcategorias() {
        return subcategorias;
    }

    public void setSubcategorias(List<CategoriaDTO> subcategorias) {
        this.subcategorias = subcategorias;
    }

    public CategoriaDTO getPadre() {
        return padre;
    }

    public void setPadre(CategoriaDTO padre) {
        this.padre = padre;
        padre.getSubcategorias().add(this);
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }
    
}

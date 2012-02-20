/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.dto;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author wilson
 */
public class ProvinciaDTO implements Serializable {

    private int id, paisId;
    private String nombre, paisNombre;
    private List<String> ciudades;

    public String getPaisNombre() {
        return paisNombre;
    }

    public void setPaisNombre(String paisNombre) {
        this.paisNombre = paisNombre;
    }

    public ProvinciaDTO(int Id, String Nombre, int PaisId, String PaisNombre) {
        id = Id;
        nombre = Nombre;
        paisId = PaisId;
        paisNombre = PaisNombre;
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

    public int getPaisId() {
        return paisId;
    }

    public void setPaisId(int paisId) {
        this.paisId = paisId;
    }

    public List<String> getCiudades() {
        return ciudades;
    }

    public void setCiudades(List<String> ciudades) {
        this.ciudades = ciudades;
    }
}

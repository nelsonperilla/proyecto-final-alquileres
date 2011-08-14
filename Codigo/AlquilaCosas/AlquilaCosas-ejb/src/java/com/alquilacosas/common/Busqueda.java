/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.common;

import java.util.List;

/**
 *
 * @author damiancardozo
 */
public class Busqueda {
    
    private List<PublicacionFacade> publicaciones;
    private List<CategoriaFacade> categorias;
    private int totalRegistros;

    public Busqueda(List<PublicacionFacade> publicaciones, List<CategoriaFacade> categorias) {
        this.publicaciones = publicaciones;
        this.categorias = categorias;
    }

    public List<CategoriaFacade> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<CategoriaFacade> categorias) {
        this.categorias = categorias;
    }

    public List<PublicacionFacade> getPublicaciones() {
        return publicaciones;
    }

    public void setPublicaciones(List<PublicacionFacade> publicaciones) {
        this.publicaciones = publicaciones;
    }

    public int getTotalRegistros() {
        return totalRegistros;
    }

    public void setTotalRegistros(int totalRegistros) {
        this.totalRegistros = totalRegistros;
    }
    
}

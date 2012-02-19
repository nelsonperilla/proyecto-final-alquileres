/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.common;

import com.alquilacosas.dto.CategoriaDTO;
import com.alquilacosas.dto.PublicacionDTO;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author damiancardozo
 */
public class Busqueda implements Serializable {
    
    private List<PublicacionDTO> publicaciones;
    private List<CategoriaDTO> categorias;
    private double precioMinimo, precioMaximo;
    private int totalRegistros;

    public Busqueda(List<PublicacionDTO> publicaciones, List<CategoriaDTO> categorias,
            double precioMin, double precioMax) {
        this.publicaciones = publicaciones;
        this.categorias = categorias;
        this.precioMinimo = precioMin;
        this.precioMaximo = precioMax;
    }

    public List<CategoriaDTO> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<CategoriaDTO> categorias) {
        this.categorias = categorias;
    }

    public List<PublicacionDTO> getPublicaciones() {
        return publicaciones;
    }

    public void setPublicaciones(List<PublicacionDTO> publicaciones) {
        this.publicaciones = publicaciones;
    }

    public int getTotalRegistros() {
        return totalRegistros;
    }

    public void setTotalRegistros(int totalRegistros) {
        this.totalRegistros = totalRegistros;
    }

    public double getPrecioMaximo() {
        return precioMaximo;
    }

    public void setPrecioMaximo(double precioMaximo) {
        this.precioMaximo = precioMaximo;
    }

    public double getPrecioMinimo() {
        return precioMinimo;
    }

    public void setPrecioMinimo(double precioMinimo) {
        this.precioMinimo = precioMinimo;
    }
    
}

    /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.dto;

import com.alquilacosas.ejb.entity.Categoria;
import com.alquilacosas.ejb.entity.EstadoPublicacion;
import com.alquilacosas.ejb.entity.ImagenPublicacion;
import com.alquilacosas.ejb.entity.Periodo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author jorge
 */
public class PublicacionDTO implements Serializable {

    private int id;
    private String titulo;
    private String descripcion;
    private Date fechaDesde;
    private Date fechaHasta;
    private boolean destacada;
    private int cantidad;
    private int imagenId = -1;
    private List<Integer> imagenIds;
    private Categoria categoria;
    private CategoriaDTO categoriaF;
    private List<PrecioDTO> precios;
    private EstadoPublicacion estado;
    private List<ImagenPublicacion> imagenes;
    private Integer periodoMinimoValor;
    private Integer periodoMaximoValor;
    private Periodo periodoMinimo;
    private Periodo periodoMaximo;
    private String provincia, ciudad, barrio;
    private UsuarioDTO propietario;
    private double latitud;
    private double longitud;

    public PublicacionDTO(int id, String titulo, String descripcion,
            Date fecha_desde, Date fecha_hasta, boolean destacada,
            int cantidad, Categoria categoria,
            List<ImagenPublicacion> imagenes, EstadoPublicacion estado,
            Integer periodoMinimoValor, Periodo periodoMinimo,
            Integer periodoMaximoValor, Periodo periodoMaximo) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaDesde = fecha_desde;
        this.fechaHasta = fecha_hasta;
        this.destacada = destacada;
        this.cantidad = cantidad;
        this.categoria = categoria;
        this.imagenes = imagenes;
        precios = new ArrayList<PrecioDTO>();
        this.estado = estado;
        this.periodoMinimoValor = periodoMinimoValor;
        this.periodoMinimo = periodoMinimo;
        this.periodoMaximoValor = periodoMaximoValor;
        this.periodoMaximo = periodoMaximo;
    }

    public PublicacionDTO(int id, String titulo, String descripcion,
            Date fecha_desde, Date fecha_hasta, boolean destacada,
            int cantidad) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaDesde = fecha_desde;
        this.fechaHasta = fecha_hasta;
        this.destacada = destacada;
        this.cantidad = cantidad;
    }

    public List<ImagenPublicacion> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<ImagenPublicacion> imagenes) {
        this.imagenes = imagenes;
    }

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
    public Date getFechaDesde() {
        return fechaDesde;
    }

    /**
     * @param fecha_desde the fecha_desde to set
     */
    public void setFechaDesde(Date fecha_desde) {
        this.fechaDesde = fecha_desde;
    }

    /**
     * @return the fecha_hasta
     */
    public Date getFechaHasta() {
        return fechaHasta;
    }

    /**
     * @param fecha_hasta the fecha_hasta to set
     */
    public void setFechaHasta(Date fecha_hasta) {
        this.fechaHasta = fecha_hasta;
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
        if (imagenIds != null && !imagenIds.isEmpty()) {
            imagenId = imagenIds.get(0);
        }
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    /**
     * @return the categoriaF
     */
    public CategoriaDTO getCategoriaF() {
        return categoriaF;
    }

    /**
     * @param categoriaF the categoriaF to set
     */
    public void setCategoriaF(CategoriaDTO categoriaF) {
        this.categoriaF = categoriaF;
    }

    public Integer getPeriodoMaximoValor() {
        return periodoMaximoValor;
    }

    public void setPeriodoMaximoValor(Integer periodoMaximo) {
        this.periodoMaximoValor = periodoMaximo;
    }

    public Integer getPeriodoMinimoValor() {
        return periodoMinimoValor;
    }

    public void setPeriodoMinimoValor(Integer periodoMinimo) {
        this.periodoMinimoValor = periodoMinimo;
    }

    public Periodo getPeriodoMinimo() {
        return periodoMinimo;
    }

    public void setPeriodoMinimo(Periodo periodoMinimo) {
        this.periodoMinimo = periodoMinimo;
    }

    public Periodo getPeriodoMaximo() {
        return periodoMaximo;
    }

    public void setPeriodoMaximo(Periodo periodoMaximo) {
        this.periodoMaximo = periodoMaximo;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public EstadoPublicacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoPublicacion estado) {
        this.estado = estado;
    }

    public List<PrecioDTO> getPrecios() {
        return precios;
    }

    public void setPrecios(List<PrecioDTO> precios) {
        this.precios = precios;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    /**
     * @return the propietario
     */
    public UsuarioDTO getPropietario() {
        return propietario;
    }

    /**
     * @param propietario the propietario to set
     */
    public void setPropietario(UsuarioDTO propietario) {
        this.propietario = propietario;
    }

    /**
     * @return the latitud
     */
    public double getLatitud() {
        return latitud;
    }

    /**
     * @param latitud the latitud to set
     */
    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    /**
     * @return the longitud
     */
    public double getLongitud() {
        return longitud;
    }

    /**
     * @param longitud the longitud to set
     */
    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }
}

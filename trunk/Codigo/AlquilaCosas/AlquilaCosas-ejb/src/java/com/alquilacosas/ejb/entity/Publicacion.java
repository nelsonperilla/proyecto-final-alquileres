/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author damiancardozo
 */
@Entity
@Table(name = "PUBLICACION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Publicacion.findAll", query = "SELECT p FROM Publicacion p"),
    @NamedQuery(name = "Publicacion.findByPublicacionId", query = "SELECT p FROM Publicacion p WHERE p.publicacionId = :publicacionId"),
    @NamedQuery(name = "Publicacion.findByTitulo", query = "SELECT p FROM Publicacion p WHERE p.titulo = :titulo"),
    @NamedQuery(name = "Publicacion.findByDescripcion", query = "SELECT p FROM Publicacion p WHERE p.descripcion = :descripcion"),
    @NamedQuery(name = "Publicacion.findByFechaDesde", query = "SELECT p FROM Publicacion p WHERE p.fechaDesde = :fechaDesde"),
    @NamedQuery(name = "Publicacion.findByFechaHasta", query = "SELECT p FROM Publicacion p WHERE p.fechaHasta = :fechaHasta"),
    @NamedQuery(name = "Publicacion.findByDestacada", query = "SELECT p FROM Publicacion p WHERE p.destacada = :destacada"),
    @NamedQuery(name = "Publicacion.findByCantidad", query = "SELECT p FROM Publicacion p WHERE p.cantidad = :cantidad")})
public class Publicacion implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PUBLICACION_ID")
    private Integer publicacionId;
    
    @Column(name = "TITULO")
    private String titulo;
    
    @Column(name = "DESCRIPCION")
    private String descripcion;
    
    @Column(name = "DESTACADA")
    private boolean destacada;
    
    @Column(name = "CANTIDAD")
    private int cantidad;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "publicacionFk")
    private List<Alquiler> alquilerList;
    
    @JoinColumn(name = "MAX_PERIODO_ALQUILER_FK", referencedColumnName = "PERIODO_ID")
    @ManyToOne
    private Periodo maxPeriodoAlquilerFk;
    
    @Column(name = "MAX_VALOR")
    private Integer maxValor;
    
    @JoinColumn(name = "MIN_PERIODO_ALQUILER_FK", referencedColumnName = "PERIODO_ID")
    @ManyToOne
    private Periodo minPeriodoAlquilerFk;
    
    @Column(name = "MIN_VALOR")
    private Integer minValor;
    
    @JoinColumn(name = "USUARIO_FK", referencedColumnName = "USUARIO_ID")
    @ManyToOne(optional = false)
    private Usuario usuarioFk;
    
    @JoinColumn(name = "CATEGORIA_FK", referencedColumnName = "CATEGORIA_ID")
    @ManyToOne(optional = false)
    private Categoria categoriaFk;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "publicacionFk")
    private List<ImagenPublicacion> imagenPublicacionList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "publicacion")
    private List<PublicacionXEstado> publicacionXEstadoList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "publicacionFk")
    private List<Comentario> comentarioList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "publicacionFk")
    private List<Precio> precioList;
    
    @Column(name = "FECHA_DESDE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaDesde;
    
    @Column(name = "FECHA_HASTA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHasta;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "publicacionFk")
    private List<Destacacion> servicioDestacacionList;

    public Publicacion() {
        imagenPublicacionList = new ArrayList<ImagenPublicacion>();
        comentarioList = new ArrayList<Comentario>();
        precioList = new ArrayList<Precio>();
        publicacionXEstadoList = new ArrayList<PublicacionXEstado>();
    }

    public Publicacion(Integer publicacionId) {
        this();
        this.publicacionId = publicacionId;
    }

    public Publicacion(Integer publicacionId, String titulo, Date fechaDesde, Date fechaHasta, boolean destacada, int cantidad) {
        this();
        this.publicacionId = publicacionId;
        this.titulo = titulo;
        this.fechaDesde = fechaDesde;
        this.fechaHasta = fechaHasta;
        this.destacada = destacada;
        this.cantidad = cantidad;
    }
    
    public void agregarImagen(ImagenPublicacion imagen) {
        imagenPublicacionList.add(imagen);
        imagen.setPublicacionFk(this);
    }
    
    public ImagenPublicacion removerImagen(ImagenPublicacion imagen) {
        imagenPublicacionList.remove(imagen);
        imagen.setPublicacionFk(null);
        return imagen;
    }
    
    public void agregarPrecio(Precio precio) {
        precioList.add(precio);
        precio.setPublicacionFk(this);
    }
    
    public void actualizarPrecio(Integer precioId, double valor, Periodo periodo) {
        for(Precio p: precioList) {
            if(p.getPrecioId().equals(precioId) && p.getPrecio() != valor) {
                p.setFechaHasta(new Date());
                Precio precio = new Precio();
                precio.setPrecio(valor);
                precio.setFechaDesde(new Date());
                precio.setPeriodoFk(periodo);
                this.agregarPrecio(precio);
                return; 
            }
        }
    }
    
    public Precio removerPrecio(Precio precio) {
        precioList.remove(precio);
        return precio;
    }
    
    public void agregarPublicacionXEstado(PublicacionXEstado pxe) {
        publicacionXEstadoList.add(pxe);
        pxe.setPublicacion(this);
    }

    public Integer getPublicacionId() {
        return publicacionId;
    }

    public void setPublicacionId(Integer publicacionId) {
        this.publicacionId = publicacionId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public boolean getDestacada() {
        return destacada;
    }

    public void setDestacada(boolean destacada) {
        this.destacada = destacada;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Usuario getUsuarioFk() {
        return usuarioFk;
    }

    public void setUsuarioFk(Usuario usuarioFk) {
        this.usuarioFk = usuarioFk;
    }

    public Categoria getCategoriaFk() {
        return categoriaFk;
    }

    public void setCategoriaFk(Categoria categoriaFk) {
        this.categoriaFk = categoriaFk;
    }

    @XmlTransient
    public List<ImagenPublicacion> getImagenPublicacionList() {
        return imagenPublicacionList;
    }

    public void setImagenPublicacionList(List<ImagenPublicacion> imagenPublicacionList) {
        this.imagenPublicacionList = imagenPublicacionList;
    }

    @XmlTransient
    public List<PublicacionXEstado> getPublicacionXEstadoList() {
        return publicacionXEstadoList;
    }

    public void setPublicacionXEstadoList(List<PublicacionXEstado> publicacionXEstadoList) {
        this.publicacionXEstadoList = publicacionXEstadoList;
    }

    @XmlTransient
    public List<Comentario> getComentarioList() {
        return comentarioList;
    }

    public void setComentarioList(List<Comentario> comentarioList) {
        this.comentarioList = comentarioList;
    }

    @XmlTransient
    public List<Precio> getPrecioList() {
        return precioList;
    }

    public void setPrecioList(List<Precio> precioList) {
        this.precioList = precioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (publicacionId != null ? publicacionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Publicacion)) {
            return false;
        }
        Publicacion other = (Publicacion) object;
        if ((this.publicacionId == null && other.publicacionId != null) || (this.publicacionId != null && !this.publicacionId.equals(other.publicacionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alquilacosas.ejb.entity.Publicacion[ publicacionId=" + publicacionId + " ]";
    }

    public Integer getMinValor() {
        return minValor;
    }

    public void setMinValor(Integer minValor) {
        this.minValor = minValor;
    }

    public Integer getMaxValor() {
        return maxValor;
    }

    public void setMaxValor(Integer maxValor) {
        this.maxValor = maxValor;
    }

    @XmlTransient
    public List<Alquiler> getAlquilerList() {
        return alquilerList;
    }

    public void setAlquilerList(List<Alquiler> alquilerList) {
        this.alquilerList = alquilerList;
    }

    public Periodo getMaxPeriodoAlquilerFk() {
        return maxPeriodoAlquilerFk;
    }

    public void setMaxPeriodoAlquilerFk(Periodo maxPeriodoAlquilerFk) {
        this.maxPeriodoAlquilerFk = maxPeriodoAlquilerFk;
    }

    public Periodo getMinPeriodoAlquilerFk() {
        return minPeriodoAlquilerFk;
    }

    public void setMinPeriodoAlquilerFk(Periodo minPeriodoAlquilerFk) {
        this.minPeriodoAlquilerFk = minPeriodoAlquilerFk;
    }

    @XmlTransient
    public List<Destacacion> getServicioDestacacionList() {
        return servicioDestacacionList;
    }

    public void setServicioDestacacionList(List<Destacacion> servicioDestacacionList) {
        this.servicioDestacacionList = servicioDestacacionList;
    }

}

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
@Table(name = "ALQUILER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Alquiler.findAll", query = "SELECT a FROM Alquiler a"),
    @NamedQuery(name = "Alquiler.findByAlquilerId", query = "SELECT a FROM Alquiler a WHERE a.alquilerId = :alquilerId"),
    @NamedQuery(name = "Alquiler.findByFechaInicio", query = "SELECT a FROM Alquiler a WHERE a.fechaInicio = :fechaInicio"),
    @NamedQuery(name = "Alquiler.findByFechaFin", query = "SELECT a FROM Alquiler a WHERE a.fechaFin = :fechaFin"),
    @NamedQuery(name = "Alquiler.findByCantidad", query = "SELECT a FROM Alquiler a WHERE a.cantidad = :cantidad")})

public class Alquiler implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ALQUILER_ID")
    private Integer alquilerId;
    
    @Column(name = "CANTIDAD")
    private int cantidad;
    
    @Column(name = "MONTO")
    private double monto;
    
    @Column(name = "FECHA_INICIO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInicio;
    
    @Column(name = "FECHA_FIN")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaFin;
    
    @JoinColumn(name = "PUBLICACION_FK", referencedColumnName = "PUBLICACION_ID")
    @ManyToOne(optional = false)
    private Publicacion publicacionFk;
    
    @JoinColumn(name = "USUARIO_FK", referencedColumnName = "USUARIO_ID")
    @ManyToOne(optional = false)
    private Usuario usuarioFk;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "alquilerFk")
    private List<AlquilerXEstado> alquilerXEstadoList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "alquilerFk")
    private List<Calificacion> calificacionList;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "alquilerFk")
    private List<PedidoCambio> pedidoCambioList;

    public Alquiler() {
        alquilerXEstadoList = new ArrayList<AlquilerXEstado>();
        calificacionList = new ArrayList<Calificacion>();
        pedidoCambioList = new ArrayList<PedidoCambio>();
    }

    public Alquiler(Integer alquilerId) {
        this();
        this.alquilerId = alquilerId;
    }

    public Alquiler(Integer alquilerId, Date fechaInicio, Date fechaFin, int cantidad) {
        this();
        this.alquilerId = alquilerId;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.cantidad = cantidad;
    }
    
    public void agregarAlquilerXEstado( AlquilerXEstado axe ){
        this.alquilerXEstadoList.add(axe);
        axe.setAlquilerFk(this);
    }
    
    public void removerAlquilerXEstado( AlquilerXEstado axe ){
        this.alquilerXEstadoList.remove(axe);
        axe.setAlquilerFk(null);
    }
    
    public void agregarCalificacion(Calificacion calificacion) {
        calificacionList.add(calificacion);
        calificacion.setAlquilerFk(this);
    }
    
    public void agregarPedidoCambio(PedidoCambio pedido) {
        pedidoCambioList.add(pedido);
        pedido.setAlquilerFk(this);
    }

    public Integer getAlquilerId() {
        return alquilerId;
    }

    public void setAlquilerId(Integer alquilerId) {
        this.alquilerId = alquilerId;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public Publicacion getPublicacionFk() {
        return publicacionFk;
    }

    public void setPublicacionFk(Publicacion publicacionFk) {
        this.publicacionFk = publicacionFk;
    }

    public Usuario getUsuarioFk() {
        return usuarioFk;
    }

    public void setUsuarioFk(Usuario usuarioFk) {
        this.usuarioFk = usuarioFk;
    }

    @XmlTransient
    public List<PedidoCambio> getPedidoCambioList() {
        return pedidoCambioList;
    }

    public void setPedidoCambioList(List<PedidoCambio> pedidoCambioList) {
        this.pedidoCambioList = pedidoCambioList;
    }

    @XmlTransient
    public List<AlquilerXEstado> getAlquilerXEstadoList() {
        return alquilerXEstadoList;
    }

    public void setAlquilerXEstadoList(List<AlquilerXEstado> alquilerXEstadoList) {
        this.alquilerXEstadoList = alquilerXEstadoList;
    }

    @XmlTransient
    public List<Calificacion> getCalificacionList() {
        return calificacionList;
    }

    public void setCalificacionList(List<Calificacion> calificacionList) {
        this.calificacionList = calificacionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (alquilerId != null ? alquilerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Alquiler)) {
            return false;
        }
        Alquiler other = (Alquiler) object;
        if ((this.alquilerId == null && other.alquilerId != null) || (this.alquilerId != null && !this.alquilerId.equals(other.alquilerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alquilacosas.ejb.entity.Alquiler[ alquilerId=" + alquilerId + " ]";
    }
    
}

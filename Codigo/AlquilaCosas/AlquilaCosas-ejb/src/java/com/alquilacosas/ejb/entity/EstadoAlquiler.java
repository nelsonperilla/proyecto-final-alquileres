/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author damiancardozo
 */
@Entity
@Table(name = "ESTADO_ALQUILER")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstadoAlquiler.findAll", query = "SELECT e FROM EstadoAlquiler e"),
    @NamedQuery(name = "EstadoAlquiler.findByEstadoAlquilerId", query = "SELECT e FROM EstadoAlquiler e WHERE e.estadoAlquilerId = :estadoAlquilerId"),
    @NamedQuery(name = "EstadoAlquiler.findByNombre", query = "SELECT e FROM EstadoAlquiler e WHERE e.nombre = :nombre"),
    @NamedQuery(name = "EstadoAlquiler.findByDescripcion", query = "SELECT e FROM EstadoAlquiler e WHERE e.descripcion = :descripcion")})
public class EstadoAlquiler implements Serializable {
    
    public enum NombreEstadoAlquiler {PEDIDO, CONFIRMADO, ACTIVO, FINALIZADO, 
    PEDIDO_CANCELADO, PEDIDO_RECHAZADO, CANCELADO_ALQUILADOR, CANCELADO}
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ESTADO_ALQUILER_ID")
    private Integer estadoAlquilerId;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "NOMBRE")
    @Enumerated(EnumType.STRING)
    private NombreEstadoAlquiler nombre;
    
    @Size(max = 150)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estadoAlquilerFk")
    private List<AlquilerXEstado> alquilerXEstadoList;

    public EstadoAlquiler() {
    }

    public EstadoAlquiler(Integer estadoAlquilerId) {
        this.estadoAlquilerId = estadoAlquilerId;
    }

    public EstadoAlquiler(Integer estadoAlquilerId, NombreEstadoAlquiler nombre) {
        this.estadoAlquilerId = estadoAlquilerId;
        this.nombre = nombre;
    }

    public Integer getEstadoAlquilerId() {
        return estadoAlquilerId;
    }

    public void setEstadoAlquilerId(Integer estadoAlquilerId) {
        this.estadoAlquilerId = estadoAlquilerId;
    }

    public NombreEstadoAlquiler getNombre() {
        return nombre;
    }

    public void setNombre(NombreEstadoAlquiler nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<AlquilerXEstado> getAlquilerXEstadoList() {
        return alquilerXEstadoList;
    }

    public void setAlquilerXEstadoList(List<AlquilerXEstado> alquilerXEstadoList) {
        this.alquilerXEstadoList = alquilerXEstadoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (estadoAlquilerId != null ? estadoAlquilerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EstadoAlquiler)) {
            return false;
        }
        EstadoAlquiler other = (EstadoAlquiler) object;
        if ((this.estadoAlquilerId == null && other.estadoAlquilerId != null) || (this.estadoAlquilerId != null && !this.estadoAlquilerId.equals(other.estadoAlquilerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alquilacosas.ejb.entity.EstadoAlquiler[ estadoAlquilerId=" + estadoAlquilerId + " ]";
    }
    
}

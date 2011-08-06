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
@Table(name = "ESTADO_PUBLICACION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstadoPublicacion.findAll", query = "SELECT e FROM EstadoPublicacion e"),
    @NamedQuery(name = "EstadoPublicacion.findByEstadoPublicacionId", query = "SELECT e FROM EstadoPublicacion e WHERE e.estadoPublicacionId = :estadoPublicacionId"),
    @NamedQuery(name = "EstadoPublicacion.findByNombre", query = "SELECT e FROM EstadoPublicacion e WHERE e.nombre = :nombre"),
    @NamedQuery(name = "EstadoPublicacion.findByDescripcion", query = "SELECT e FROM EstadoPublicacion e WHERE e.descripcion = :descripcion")})
public class EstadoPublicacion implements Serializable {
    
    
    public enum PublicacionEstado {ACTIVA, INACTIVA, SUSPENDIDA};
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ESTADO_PUBLICACION_ID")
    private Integer estadoPublicacionId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "NOMBRE")
    @Enumerated(EnumType.STRING)
    private PublicacionEstado nombre;
    @Size(max = 45)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estadoPublicacion")
    private List<PublicacionXEstado> publicacionXEstadoList;

    public EstadoPublicacion() {
    }

    public EstadoPublicacion(Integer estadoPublicacionId) {
        this.estadoPublicacionId = estadoPublicacionId;
    }

    public EstadoPublicacion(Integer estadoPublicacionId, PublicacionEstado nombre) {
        this.estadoPublicacionId = estadoPublicacionId;
        this.nombre = nombre;
    }

    public Integer getEstadoPublicacionId() {
        return estadoPublicacionId;
    }

    public void setEstadoPublicacionId(Integer estadoPublicacionId) {
        this.estadoPublicacionId = estadoPublicacionId;
    }

    public PublicacionEstado getNombre() {
        return nombre;
    }

    public void setNombre(PublicacionEstado nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<PublicacionXEstado> getPublicacionXEstadoList() {
        return publicacionXEstadoList;
    }

    public void setPublicacionXEstadoList(List<PublicacionXEstado> publicacionXEstadoList) {
        this.publicacionXEstadoList = publicacionXEstadoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (estadoPublicacionId != null ? estadoPublicacionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EstadoPublicacion)) {
            return false;
        }
        EstadoPublicacion other = (EstadoPublicacion) object;
        if ((this.estadoPublicacionId == null && other.estadoPublicacionId != null) || (this.estadoPublicacionId != null && !this.estadoPublicacionId.equals(other.estadoPublicacionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alquilacosas.ejb.entity.EstadoPublicacion[ estadoPublicacionId=" + estadoPublicacionId + " ]";
    }
    
}

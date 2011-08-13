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
@Table(name = "ESTADO_USUARIO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstadoUsuario.findAll", query = "SELECT e FROM EstadoUsuario e"),
    @NamedQuery(name = "EstadoUsuario.findByEstadoUsuarioId", query = "SELECT e FROM EstadoUsuario e WHERE e.estadoUsuarioId = :estadoUsuarioId"),
    @NamedQuery(name = "EstadoUsuario.findByNombre", query = "SELECT e FROM EstadoUsuario e WHERE e.nombre = :nombre"),
    @NamedQuery(name = "EstadoUsuario.findByDescripcion", query = "SELECT e FROM EstadoUsuario e WHERE e.descripcion = :descripcion")})
public class EstadoUsuario implements Serializable {
    
    public enum NombreEstado {REGISTRADO, ACTIVO, SUSPENDIDO}; 
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ESTADO_USUARIO_ID")
    private Integer estadoUsuarioId;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "NOMBRE")
    @Enumerated(EnumType.STRING)
    private NombreEstado nombre;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "estadoUsuario")
    private List<UsuarioXEstado> usuarioXEstadoList;

    public EstadoUsuario() {
    }

    public EstadoUsuario(Integer estadoUsuarioId) {
        this.estadoUsuarioId = estadoUsuarioId;
    }

    public EstadoUsuario(Integer estadoUsuarioId, NombreEstado nombre, String descripcion) {
        this.estadoUsuarioId = estadoUsuarioId;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Integer getEstadoUsuarioId() {
        return estadoUsuarioId;
    }

    public void setEstadoUsuarioId(Integer estadoUsuarioId) {
        this.estadoUsuarioId = estadoUsuarioId;
    }

    public void setNombre(NombreEstado nombre) {
        this.nombre = nombre;
    }
    
    public NombreEstado getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @XmlTransient
    public List<UsuarioXEstado> getUsuarioXEstadoList() {
        return usuarioXEstadoList;
    }

    public void setUsuarioXEstadoList(List<UsuarioXEstado> usuarioXEstadoList) {
        this.usuarioXEstadoList = usuarioXEstadoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (estadoUsuarioId != null ? estadoUsuarioId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EstadoUsuario)) {
            return false;
        }
        EstadoUsuario other = (EstadoUsuario) object;
        if ((this.estadoUsuarioId == null && other.estadoUsuarioId != null) || (this.estadoUsuarioId != null && !this.estadoUsuarioId.equals(other.estadoUsuarioId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alquilacosas.ejb.entity.EstadoUsuario[ estadoUsuarioId=" + estadoUsuarioId + " ]";
    }
    
}

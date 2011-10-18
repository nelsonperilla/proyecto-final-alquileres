/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author damiancardozo
 */
@Entity
@Table(name = "ROL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Rol.findAll", query = "SELECT r FROM Rol r"),
    @NamedQuery(name = "Rol.findByRolId", query = "SELECT r FROM Rol r WHERE r.rolId = :rolId"),
    @NamedQuery(name = "Rol.findByNombre", query = "SELECT r FROM Rol r WHERE r.nombre = :nombre"),
    @NamedQuery(name = "Rol.findByDescripcion", query = "SELECT r FROM Rol r WHERE r.descripcion = :descripcion")})
public class Rol implements Serializable {
    
    public enum NombreRol {USUARIO, ADMIN, PUBLICITANTE};
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROL_ID")
    private Integer rolId;
    
    @Basic(optional = false)
    @Enumerated(EnumType.STRING)
    @Column(name = "NOMBRE")
    private NombreRol nombre;
    
    @Column(name = "DESCRIPCION")
    private String descripcion;
    
    //@ManyToMany(mappedBy = "rolList")
    //private List<Login> loginList;

    public Rol() {
    }

    public Rol(Integer rolId) {
        this.rolId = rolId;
    }

    public Rol(Integer rolId, NombreRol nombre) {
        this.rolId = rolId;
        this.nombre = nombre;
    }

    public Integer getRolId() {
        return rolId;
    }

    public void setRolId(Integer rolId) {
        this.rolId = rolId;
    }

    public NombreRol getNombre() {
        return nombre;
    }

    public void setNombre(NombreRol nombre) {
        this.nombre = nombre;
    }
    
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rolId != null ? rolId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Rol)) {
            return false;
        }
        Rol other = (Rol) object;
        if ((this.rolId == null && other.rolId != null) || (this.rolId != null && !this.rolId.equals(other.rolId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alquilacosas.ejb.entity.Rol[ rolId=" + rolId + " ]";
    }
    
}

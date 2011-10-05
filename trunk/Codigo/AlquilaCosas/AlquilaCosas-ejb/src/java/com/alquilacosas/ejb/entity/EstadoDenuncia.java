/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author damiancardozo
 */
@Entity
@Table(name = "ESTADO_DENUNCIA")
@XmlRootElement
public class EstadoDenuncia implements Serializable {
 
    public enum NombreEstadoDenuncia {PENDIENTE, ACEPTADA, RECHAZADA}; 
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ESTADO_DENUNCIA_ID")
    private Integer estadoDenunciaId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "NOMBRE")
    private NombreEstadoDenuncia nombre;
    
    @Column(name = "DESCRIPCION")
    private String descripcion;

    public EstadoDenuncia() {
    }

    public EstadoDenuncia(Integer estadoDenunciaId) {
        this.estadoDenunciaId = estadoDenunciaId;
    }

    public EstadoDenuncia(Integer estadoDenunciaId, NombreEstadoDenuncia nombre, String descripcion) {
        this.estadoDenunciaId = estadoDenunciaId;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Integer getEstadoDenunciaId() {
        return estadoDenunciaId;
    }

    public void setEstadoDenunciaId(Integer estadoDenunciaId) {
        this.estadoDenunciaId = estadoDenunciaId;
    }

    public NombreEstadoDenuncia getNombre() {
        return nombre;
    }

    public void setNombre(NombreEstadoDenuncia nombre) {
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
        hash += (estadoDenunciaId != null ? estadoDenunciaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EstadoDenuncia)) {
            return false;
        }
        EstadoDenuncia other = (EstadoDenuncia) object;
        if ((this.estadoDenunciaId == null && other.estadoDenunciaId != null) || 
                (this.estadoDenunciaId != null && !this.estadoDenunciaId.equals(other.estadoDenunciaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alquilacosas.ejb.entity.EstadoDenuncia[ estadoDenunciaId=" + estadoDenunciaId + " ]";
    }
    
}

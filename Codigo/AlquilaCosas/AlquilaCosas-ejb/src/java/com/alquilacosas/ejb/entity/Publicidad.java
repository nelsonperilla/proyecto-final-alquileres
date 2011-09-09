/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.entity;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author damiancardozo
 */
@Entity
@Table(name = "PUBLICIDAD")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Publicidad.findAll", query = "SELECT p FROM Publicidad p"),
    @NamedQuery(name = "Publicidad.findByPublicidadId", query = "SELECT p FROM Publicidad p WHERE p.publicidadId = :publicidadId"),
    @NamedQuery(name = "Publicidad.findByTitulo", query = "SELECT p FROM Publicidad p WHERE p.titulo = :titulo")})
public class Publicidad implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PUBLICIDAD_ID")
    private Integer publicidadId;
    
    @Column(name = "TITULO")
    private String titulo;
    
    @JoinColumn(name = "USUARIO_FK", referencedColumnName = "USUARIO_ID")
    @ManyToOne(optional = false)
    private Usuario usuarioFk;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "publicidadFk")
    private List<ServicioPublicidad> servicioPublicidadList;

    public Publicidad() {
    }

    public Publicidad(Integer publicidadId) {
        this.publicidadId = publicidadId;
    }

    public Integer getPublicidadId() {
        return publicidadId;
    }

    public void setPublicidadId(Integer publicidadId) {
        this.publicidadId = publicidadId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Usuario getUsuarioFk() {
        return usuarioFk;
    }

    public void setUsuarioFk(Usuario usuarioFk) {
        this.usuarioFk = usuarioFk;
    }

    @XmlTransient
    public List<ServicioPublicidad> getServicioPublicidadList() {
        return servicioPublicidadList;
    }

    public void setServicioPublicidadList(List<ServicioPublicidad> servicioPublicidadList) {
        this.servicioPublicidadList = servicioPublicidadList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (publicidadId != null ? publicidadId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Publicidad)) {
            return false;
        }
        Publicidad other = (Publicidad) object;
        if ((this.publicidadId == null && other.publicidadId != null) || (this.publicidadId != null && !this.publicidadId.equals(other.publicidadId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alquilacosas.ejb.entity.Publicidad[ publicidadId=" + publicidadId + " ]";
    }
    
}

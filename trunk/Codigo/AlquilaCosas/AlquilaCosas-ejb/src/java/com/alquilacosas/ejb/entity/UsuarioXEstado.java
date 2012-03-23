/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author damiancardozo
 */
@Entity
@Table(name = "USUARIO_X_ESTADO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UsuarioXEstado.findAll", query = "SELECT u FROM UsuarioXEstado u"),
    @NamedQuery(name = "UsuarioXEstado.findByUsuarioFk", query = "SELECT u FROM UsuarioXEstado u WHERE u.usuario = :usuario"),
    @NamedQuery(name = "UsuarioXEstado.findByEstadoFk", query = "SELECT u FROM UsuarioXEstado u WHERE u.estadoUsuario = :estadoUsuario"),
    @NamedQuery(name = "UsuarioXEstado.findByFechaDesde", query = "SELECT u FROM UsuarioXEstado u WHERE u.fechaDesde = :fechaDesde"),
    @NamedQuery(name = "UsuarioXEstado.findByFechaHasta", query = "SELECT u FROM UsuarioXEstado u WHERE u.fechaHasta = :fechaHasta"),
    @NamedQuery(name = "UsuarioXEstado.findCurrentByUsuarioFk", query = "SELECT u FROM UsuarioXEstado u WHERE u.usuario = :usuario AND u.fechaHasta IS NULL")})

public class UsuarioXEstado implements Serializable {
    
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USUARIO_X_ESTADO_ID")
    private Integer usuarioXEstadoId;
    
    @Column(name = "FECHA_DESDE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaDesde;
    
    @Column(name =     "FECHA_HASTA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHasta;
    
    @JoinColumn(name = "ESTADO_FK", referencedColumnName = "ESTADO_USUARIO_ID")
    @ManyToOne(optional = false)
    private EstadoUsuario estadoUsuario;
    
    @JoinColumn(name = "USUARIO_FK", referencedColumnName = "USUARIO_ID")
    @ManyToOne(optional = false)
    private Usuario usuario;

    public UsuarioXEstado() {
    }
    
    public UsuarioXEstado(Integer usuarioXEstadoId) {
        this.usuarioXEstadoId = usuarioXEstadoId;
    }

    public UsuarioXEstado(Usuario usuario, EstadoUsuario estadoUsuario) {
        this.usuario = usuario;
        this.estadoUsuario = estadoUsuario;
        fechaDesde = new Date();
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

    public EstadoUsuario getEstadoUsuario() {
        return estadoUsuario;
    }

    public void setEstadoUsuario(EstadoUsuario estadoUsuario) {
        this.estadoUsuario = estadoUsuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Integer getUsuarioXEstadoId() {
        return usuarioXEstadoId;
    }

    public void setUsuarioXEstadoId(Integer usuarioXEstadoId) {
        this.usuarioXEstadoId = usuarioXEstadoId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usuarioXEstadoId != null ? usuarioXEstadoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof UsuarioXEstado)) {
            return false;
        }
        UsuarioXEstado other = (UsuarioXEstado) object;
        if ((this.usuarioXEstadoId == null && other.usuarioXEstadoId != null) || (this.usuarioXEstadoId != null && !this.usuarioXEstadoId.equals(other.usuarioXEstadoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alquilacosas.ejb.entity.UsuarioXEstado[ usuarioXEstadoId=" + usuarioXEstadoId + " ]";
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
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
    @NamedQuery(name = "UsuarioXEstado.findByUsuarioFk", query = "SELECT u FROM UsuarioXEstado u WHERE u.usuarioXEstadoPK.usuarioFk = :usuarioFk"),
    @NamedQuery(name = "UsuarioXEstado.findByEstadoFk", query = "SELECT u FROM UsuarioXEstado u WHERE u.usuarioXEstadoPK.estadoFk = :estadoFk"),
    @NamedQuery(name = "UsuarioXEstado.findByFechaDesde", query = "SELECT u FROM UsuarioXEstado u WHERE u.fechaDesde = :fechaDesde"),
    @NamedQuery(name = "UsuarioXEstado.findByFechaHasta", query = "SELECT u FROM UsuarioXEstado u WHERE u.fechaHasta = :fechaHasta")})
public class UsuarioXEstado implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA_DESDE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaDesde;
    @Column(name = "FECHA_HASTA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHasta;
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected UsuarioXEstadoPK usuarioXEstadoPK;
    @JoinColumn(name = "ESTADO_FK", referencedColumnName = "ESTADO_USUARIO_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private EstadoUsuario estadoUsuario;
    @JoinColumn(name = "USUARIO_FK", referencedColumnName = "USUARIO_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Usuario usuario;

    public UsuarioXEstado() {
    }

    public UsuarioXEstado(UsuarioXEstadoPK usuarioXEstadoPK) {
        this.usuarioXEstadoPK = usuarioXEstadoPK;
    }

    public UsuarioXEstado(UsuarioXEstadoPK usuarioXEstadoPK, Date fechaDesde) {
        this.usuarioXEstadoPK = usuarioXEstadoPK;
        this.fechaDesde = fechaDesde;
    }

    public UsuarioXEstado(int usuarioFk, int estadoFk) {
        this.usuarioXEstadoPK = new UsuarioXEstadoPK(usuarioFk, estadoFk);
    }

    public UsuarioXEstadoPK getUsuarioXEstadoPK() {
        return usuarioXEstadoPK;
    }

    public void setUsuarioXEstadoPK(UsuarioXEstadoPK usuarioXEstadoPK) {
        this.usuarioXEstadoPK = usuarioXEstadoPK;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usuarioXEstadoPK != null ? usuarioXEstadoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsuarioXEstado)) {
            return false;
        }
        UsuarioXEstado other = (UsuarioXEstado) object;
        if ((this.usuarioXEstadoPK == null && other.usuarioXEstadoPK != null) || (this.usuarioXEstadoPK != null && !this.usuarioXEstadoPK.equals(other.usuarioXEstadoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alquilacosas.ejb.entity.UsuarioXEstado[ usuarioXEstadoPK=" + usuarioXEstadoPK + " ]";
    }
    
}

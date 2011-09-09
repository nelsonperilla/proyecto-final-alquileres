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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author damiancardozo
 */
@Entity
@Table(name = "CALIFICACION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Calificacion.findAll", query = "SELECT c FROM Calificacion c"),
    @NamedQuery(name = "Calificacion.findByCalificacionId", query = "SELECT c FROM Calificacion c WHERE c.calificacionId = :calificacionId"),
    @NamedQuery(name = "Calificacion.findByFechaCalificacion", query = "SELECT c FROM Calificacion c WHERE c.fechaCalificacion = :fechaCalificacion"),
    @NamedQuery(name = "Calificacion.findByComentarioCalificador", query = "SELECT c FROM Calificacion c WHERE c.comentarioCalificador = :comentarioCalificador"),
    @NamedQuery(name = "Calificacion.findByFechaReplica", query = "SELECT c FROM Calificacion c WHERE c.fechaReplica = :fechaReplica"),
    @NamedQuery(name = "Calificacion.findByComentarioReplica", query = "SELECT c FROM Calificacion c WHERE c.comentarioReplica = :comentarioReplica")})
public class Calificacion implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CALIFICACION_ID")
    private Integer calificacionId;
    
    @Size(max = 255)
    @Column(name = "COMENTARIO_CALIFICADOR")
    private String comentarioCalificador;
    
    @Size(max = 255)
    @Column(name = "COMENTARIO_REPLICA")
    private String comentarioReplica;
    
    @Column(name = "FECHA_CALIFICACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCalificacion;
    
    @Column(name = "FECHA_REPLICA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaReplica;
    
    @JoinColumn(name = "PUNTUACION_FK", referencedColumnName = "PUNTUACION_ID")
    @ManyToOne(optional = false)
    private Puntuacion puntuacionFk;
    
    @JoinColumn(name = "ALQUILER_FK", referencedColumnName = "ALQUILER_ID")
    @ManyToOne(optional = false)
    private Alquiler alquilerFk;
    
    @JoinColumn(name = "USUARIO_REPLICADOR_FK", referencedColumnName = "USUARIO_ID")
    @ManyToOne
    private Usuario usuarioReplicadorFk;
    
    @JoinColumn(name = "USUARIO_CALIFICADOR_FK", referencedColumnName = "USUARIO_ID")
    @ManyToOne(optional = false)
    private Usuario usuarioCalificadorFk;

    public Calificacion() {
    }

    public Calificacion(Integer calificacionId) {
        this.calificacionId = calificacionId;
    }

    public Calificacion(Integer calificacionId, Date fechaCalificacion) {
        this.calificacionId = calificacionId;
        this.fechaCalificacion = fechaCalificacion;
    }

    public Integer getCalificacionId() {
        return calificacionId;
    }

    public void setCalificacionId(Integer calificacionId) {
        this.calificacionId = calificacionId;
    }

    public Date getFechaCalificacion() {
        return fechaCalificacion;
    }

    public void setFechaCalificacion(Date fechaCalificacion) {
        this.fechaCalificacion = fechaCalificacion;
    }

    public String getComentarioCalificador() {
        return comentarioCalificador;
    }

    public void setComentarioCalificador(String comentarioCalificador) {
        this.comentarioCalificador = comentarioCalificador;
    }

    public Date getFechaReplica() {
        return fechaReplica;
    }

    public void setFechaReplica(Date fechaReplica) {
        this.fechaReplica = fechaReplica;
    }

    public String getComentarioReplica() {
        return comentarioReplica;
    }

    public void setComentarioReplica(String comentarioReplica) {
        this.comentarioReplica = comentarioReplica;
    }

    public Puntuacion getPuntuacionFk() {
        return puntuacionFk;
    }

    public void setPuntuacionFk(Puntuacion puntuacionFk) {
        this.puntuacionFk = puntuacionFk;
    }

    public Alquiler getAlquilerFk() {
        return alquilerFk;
    }

    public void setAlquilerFk(Alquiler alquilerFk) {
        this.alquilerFk = alquilerFk;
    }

    public Usuario getUsuarioReplicadorFk() {
        return usuarioReplicadorFk;
    }

    public void setUsuarioReplicadorFk(Usuario usuarioReplicadorFk) {
        this.usuarioReplicadorFk = usuarioReplicadorFk;
    }

    public Usuario getUsuarioCalificadorFk() {
        return usuarioCalificadorFk;
    }

    public void setUsuarioCalificadorFk(Usuario usuarioCalificadorFk) {
        this.usuarioCalificadorFk = usuarioCalificadorFk;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (calificacionId != null ? calificacionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Calificacion)) {
            return false;
        }
        Calificacion other = (Calificacion) object;
        if ((this.calificacionId == null && other.calificacionId != null) || (this.calificacionId != null && !this.calificacionId.equals(other.calificacionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alquilacosas.ejb.entity.Calificacion[ calificacionId=" + calificacionId + " ]";
    }
    
}

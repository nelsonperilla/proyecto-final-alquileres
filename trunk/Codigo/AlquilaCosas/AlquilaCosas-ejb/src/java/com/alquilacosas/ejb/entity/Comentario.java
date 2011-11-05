/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author damiancardozo
 */
@Entity
@Table(name = "COMENTARIO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Comentario.findAll", query = "SELECT c FROM Comentario c"),
    @NamedQuery(name = "Comentario.findByComentarioId", query = "SELECT c FROM Comentario c WHERE c.comentarioId = :comentarioId"),
    @NamedQuery(name = "Comentario.findByComentario", query = "SELECT c FROM Comentario c WHERE c.comentario = :comentario"),
    @NamedQuery(name = "Comentario.findByFecha", query = "SELECT c FROM Comentario c WHERE c.fecha = :fecha")})
public class Comentario implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMENTARIO_ID")
    private Integer comentarioId;
    
    @Basic(optional = false)
    @Column(name = "COMENTARIO")
    private String comentario;
    
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    
    @Column(name = "PREGUNTA")
    private Boolean pregunta;
    
    @Column(name = "BANEADO")
    private Boolean baneado;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "respuesta")
    private List<Comentario> comentarioList;
    
    @JoinColumn(name = "RESPUESTA", referencedColumnName = "COMENTARIO_ID")
    @ManyToOne(optional = false)
    private Comentario respuesta;
    
    @JoinColumn(name = "PUBLICACION_FK", referencedColumnName = "PUBLICACION_ID")
    @ManyToOne(optional = false)
    private Publicacion publicacionFk;
    
    @JoinColumn(name = "USUARIO_FK", referencedColumnName = "USUARIO_ID")
    @ManyToOne(optional = false)
    private Usuario usuarioFk;

    public Comentario() {
    }

    public Comentario(Integer comentarioId) {
        this.comentarioId = comentarioId;
    }

    public Comentario(Integer comentarioId, String comentario, Date fecha) {
        this.comentarioId = comentarioId;
        this.comentario = comentario;
        this.fecha = fecha;
    }

    public Integer getComentarioId() {
        return comentarioId;
    }

    public void setComentarioId(Integer comentarioId) {
        this.comentarioId = comentarioId;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    @XmlTransient
    public List<Comentario> getComentarioList() {
        return comentarioList;
    }

    public void setComentarioList(List<Comentario> comentarioList) {
        this.comentarioList = comentarioList;
    }

    public Comentario getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(Comentario respuesta) {
        this.respuesta = respuesta;
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
    
    public Boolean getPregunta() {
        return pregunta;
    }

    public void setPregunta(Boolean pregunta) {
        this.pregunta = pregunta;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (comentarioId != null ? comentarioId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Comentario)) {
            return false;
        }
        Comentario other = (Comentario) object;
        if ((this.comentarioId == null && other.comentarioId != null) || (this.comentarioId != null && !this.comentarioId.equals(other.comentarioId))) {
            return false;
        }
        return true;
    }

     public Boolean getBaneado() {
          return baneado;
     }

     public void setBaneado(Boolean baneado) {
          this.baneado = baneado;
     }

    @Override
    public String toString() {
        return "com.alquilacosas.ejb.entity.Comentario[ comentarioId=" + comentarioId + " ]";
    }
    
}

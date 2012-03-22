/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.entity;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ignaciogiagante
 */
@Entity
@Table(name = "imagen_usuario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ImagenUsuario.findAll", query = "SELECT i FROM ImagenUsuario i"),
    @NamedQuery(name = "ImagenUsuario.findByImagenUsuarioId", query = "SELECT i FROM ImagenUsuario i WHERE i.imagenUsuarioId = :imagenUsuarioId"),
    @NamedQuery(name = "ImagenUsuario.findByUsar", query = "SELECT i FROM ImagenUsuario i WHERE i.usar = :usar")})
public class ImagenUsuario implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "IMAGEN_USUARIO_ID")
    private Integer imagenUsuarioId;
    
    @Lob
    @Column(name = "IMAGEN")
    private byte[] imagen;
    
    @Column(name = "USAR")
    private Boolean usar;
    
    @JoinColumn(name = "USUARIO_FK", referencedColumnName = "USUARIO_ID")
    @ManyToOne
    private Usuario usuarioFk;

    public ImagenUsuario() {
    }

    public ImagenUsuario(Integer imagenUsuarioId) {
        this.imagenUsuarioId = imagenUsuarioId;
    }

    public Integer getImagenUsuarioId() {
        return imagenUsuarioId;
    }

    public void setImagenUsuarioId(Integer imagenUsuarioId) {
        this.imagenUsuarioId = imagenUsuarioId;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public Boolean getUsar() {
        return usar;
    }

    public void setUsar(Boolean usar) {
        this.usar = usar;
    }

    public Usuario getUsuarioFk() {
        return usuarioFk;
    }

    public void setUsuarioFk(Usuario usuarioFk) {
        this.usuarioFk = usuarioFk;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (imagenUsuarioId != null ? imagenUsuarioId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ImagenUsuario)) {
            return false;
        }
        ImagenUsuario other = (ImagenUsuario) object;
        if ((this.imagenUsuarioId == null && other.imagenUsuarioId != null) || (this.imagenUsuarioId != null && !this.imagenUsuarioId.equals(other.imagenUsuarioId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alquilacosas.ejb.entity.ImagenUsuario[ imagenUsuarioId=" + imagenUsuarioId + " ]";
    }
    
}

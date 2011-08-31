/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author damiancardozo
 */
@Entity
@Table(name = "IMAGEN_PUBLICACION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ImagenPublicacion.findAll", query = "SELECT i FROM ImagenPublicacion i"),
    @NamedQuery(name = "ImagenPublicacion.findByPublicacionId", query = "SELECT i FROM ImagenPublicacion i WHERE i.publicacionFk = :publicacion"),
    @NamedQuery(name = "ImagenPublicacion.findByImagenPublicacionId", query = "SELECT i FROM ImagenPublicacion i WHERE i.imagenPublicacionId = :imagenPublicacionId")})
public class ImagenPublicacion implements Serializable {
    @Lob
    @Column(name = "IMAGEN")
    private byte[] imagen;
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "IMAGEN_PUBLICACION_ID")
    private Integer imagenPublicacionId;
    
    @JoinColumn(name = "PUBLICACION_FK", referencedColumnName = "PUBLICACION_ID")
    @ManyToOne()
    private Publicacion publicacionFk;

    public ImagenPublicacion() {
    }

    public ImagenPublicacion(Integer imagenPublicacionId) {
        this.imagenPublicacionId = imagenPublicacionId;
    }

    public Integer getImagenPublicacionId() {
        return imagenPublicacionId;
    }

    public void setImagenPublicacionId(Integer imagenPublicacionId) {
        this.imagenPublicacionId = imagenPublicacionId;
    }

    public Publicacion getPublicacionFk() {
        return publicacionFk;
    }

    public void setPublicacionFk(Publicacion publicacionFk) {
        this.publicacionFk = publicacionFk;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (imagenPublicacionId != null ? imagenPublicacionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ImagenPublicacion)) {
            return false;
        }
        ImagenPublicacion other = (ImagenPublicacion) object;
        if ((this.imagenPublicacionId == null && other.imagenPublicacionId != null) || (this.imagenPublicacionId != null && !this.imagenPublicacionId.equals(other.imagenPublicacionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.alquilacosas.ejb.entity.ImagenPublicacion[ imagenPublicacionId=" + imagenPublicacionId + " ]";
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }
    
}

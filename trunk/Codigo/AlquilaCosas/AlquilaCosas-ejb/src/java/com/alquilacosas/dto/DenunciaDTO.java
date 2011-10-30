/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.dto;

import com.alquilacosas.ejb.entity.MotivoDenuncia;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author jorge
 */
public class DenunciaDTO implements Serializable {
    private int id;
    private Date fecha;
    private String explicacion;
    private int usuarioId;
    private int comentarioId;
    private int publicacionId;
    private MotivoDenuncia motivo;
    
    public DenunciaDTO()
    {}
    
    public DenunciaDTO(int id, Date fecha, String explicacion,int usuarioId,int comentarioId, int publicacionId)
    {
        this.comentarioId = id;
        this.fecha = fecha;
        this.explicacion = explicacion;
        this.usuarioId = usuarioId;
        this.comentarioId = comentarioId;
        this.publicacionId = publicacionId;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the fecha
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * @return the explicacion
     */
    public String getExplicacion() {
        return explicacion;
    }

    /**
     * @param explicacion the explicacion to set
     */
    public void setExplicacion(String explicacion) {
        this.explicacion = explicacion;
    }

    /**
     * @return the usuarioId
     */
    public int getUsuarioId() {
        return usuarioId;
    }

    /**
     * @param usuarioId the usuarioId to set
     */
    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    /**
     * @return the comentarioId
     */
    public int getComentarioId() {
        return comentarioId;
    }

    /**
     * @param comentarioId the comentarioId to set
     */
    public void setComentarioId(int comentarioId) {
        this.comentarioId = comentarioId;
    }

    /**
     * @return the publicacionId
     */
    public int getPublicacionId() {
        return publicacionId;
    }

    /**
     * @param publicacionId the publicacionId to set
     */
    public void setPublicacionId(int publicacionId) {
        this.publicacionId = publicacionId;
    }

    /**
     * @return the motivo
     */
    public MotivoDenuncia getMotivo() {
        return motivo;
    }

    /**
     * @param motivo the motivo to set
     */
    public void setMotivo(MotivoDenuncia motivo) {
        this.motivo = motivo;
    }
    
    
    
    
}

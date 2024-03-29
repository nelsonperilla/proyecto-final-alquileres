/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.dto;

import com.alquilacosas.ejb.entity.ImagenPublicacion;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;

/**
 *
 * @author jorge
 */
public class ComentarioDTO implements Serializable  {
    
    private int id;
    private String comentario;
    private Date fecha;
    private int usuarioId;
    private String usuario;
    private ComentarioDTO respuesta;
    private int publicacionId;
    private String publicationTitle;
    private int imageId;
    private boolean baneado;
    private String fechaComentario;
    public ComentarioDTO(){ }
    
    public ComentarioDTO(int id, String comentario, Date fecha, int usuarioId,
            String usuario,ComentarioDTO respuesta){
        this.id=id;
        this.comentario=comentario;
        this.fecha=fecha;
        this.fechaComentario = DateFormat.getDateInstance(DateFormat.MEDIUM).format(this.fecha);
        this.usuarioId=usuarioId;
        this.usuario=usuario;
        if(respuesta!=null)
            this.respuesta=respuesta;
        else
            respuesta=new ComentarioDTO();
    }
    
    public ComentarioDTO(int id, String comentario, Date fecha, int usuarioId,
            String usuario,int publicacionId, ComentarioDTO respuesta){
        this.id=id;
        this.comentario=comentario;
        this.fecha=fecha;
        this.fechaComentario = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.SHORT).format(this.fecha);
        this.usuarioId=usuarioId;
        this.usuario=usuario;
        this.publicacionId = publicacionId;
        if(respuesta!=null)
            this.respuesta=respuesta;
        else{
            respuesta=new ComentarioDTO();
            respuesta.setId(-1);
        }
    }    
    
    public boolean equals(ComentarioDTO other){
        return this.id == other.id;
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
     * @return the comentario
     */
    public String getComentario() {
        return comentario;
    }

    /**
     * @param comentario the comentario to set
     */
    public void setComentario(String comentario) {
        this.comentario = comentario;
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
     * @return the respuestaId
     */
    public ComentarioDTO getRespuesta() {
        return respuesta;
    }

    /**
     * @param respuestaId the respuestaId to set
     */
    public void setRespuesta(ComentarioDTO respuesta) {
        this.respuesta = respuesta;
    }

    /**
     * @return the usuario
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
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
     * @return the baneado
     */
    public boolean isBaneado() {
        return baneado;
    }

    /**
     * @param baneado the baneado to set
     */
    public void setBaneado(boolean baneado) {
        this.baneado = baneado;
    }

    /**
     * @return the publicationTitle
     */
    public String getPublicationTitle() {
        return publicationTitle;
    }

    /**
     * @param publicationTitle the publicationTitle to set
     */
    public void setPublicationTitle(String publicationTitle) {
        this.publicationTitle = publicationTitle;
    }

    /**
     * @return the image
     */
    public int getImageId() {
        return imageId;
    }

    /**
     * @param image the image to set
     */
    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    /**
     * @return the fechaComentario
     */
    public String getFechaComentario() {
        return fechaComentario;
    }

    /**
     * @param fechaComentario the fechaComentario to set
     */
    public void setFechaComentario(String fechaComentario) {
        this.fechaComentario = fechaComentario;
    }
    
    
    
}

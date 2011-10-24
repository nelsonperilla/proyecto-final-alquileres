/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.dto;

import java.io.Serializable;

/**
 *
 * @author damiancardozo
 */
public class PublicidadDTO implements Serializable {
    
    private int id;
    private String titulo, caption, url;
    private byte[] imagen;

    public PublicidadDTO(int id, String titulo, String caption, String url) {
        this.id = id;
        this.titulo = titulo;
        this.caption = caption;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.dto;

import com.alquilacosas.ejb.entity.MotivoDenuncia;
import java.util.Date;

/**
 *
 * @author wilson
 */
public class DenunciaDTO {
     
     private Integer denunciaId;
     private Date fecha;
     private Integer usuarioId;
     private String usuarioUsername;
     private Integer publicacionId;
     private String textoPublicacion;
     private Integer comentarioId;
     private String textoComentario;
     private MotivoDenuncia motivo;
     private String nombreMotivo;
     private String explicacion;

     public DenunciaDTO(Integer denunciaId, Date fecha, Integer usuarioId, String usuarioUsername, Integer publicacionId, Integer comentarioId, String nombremotivo) {
          this.denunciaId = denunciaId;
          this.fecha = fecha;
          this.usuarioId = usuarioId;
          this.usuarioUsername = usuarioUsername;
          this.publicacionId = publicacionId;
          this.comentarioId = comentarioId;
          this.nombreMotivo = nombremotivo;
     }

     public DenunciaDTO() {
     }
     
     public Boolean getIsPublicacion() {
          return comentarioId == null;
     }

     public Integer getComentarioId() {
          return comentarioId;
     }

     public String getTextoComentario() {
          return textoComentario;
     }

     public void setTextoComentario(String textoComentario) {
          this.textoComentario = textoComentario;
     }

     public String getTextoPublicacion() {
          return textoPublicacion;
     }

     public void setTextoPublicacion(String textoPublicacion) {
          this.textoPublicacion = textoPublicacion;
     }

     public void setComentarioId(Integer comentarioId) {
          this.comentarioId = comentarioId;
     }

     public Integer getDenunciaId() {
          return denunciaId;
     }

     public void setDenunciaId(Integer denunciaId) {
          this.denunciaId = denunciaId;
     }

     public Date getFecha() {
          return fecha;
     }

     public void setFecha(Date fecha) {
          this.fecha = fecha;
     }

     public MotivoDenuncia getMotivo() {
          return motivo;
     }

          public Integer getPublicacionId() {
          return publicacionId;
     }

     public void setPublicacionId(Integer publicacionId) {
          this.publicacionId = publicacionId;
     }

     public Integer getUsuarioId() {
          return usuarioId;
     }

     public void setUsuarioId(Integer usuarioId) {
          this.usuarioId = usuarioId;
     }

     public String getUsuarioUsername() {
          return usuarioUsername;
     }

     public void setUsuarioUsername(String usuarioUsername) {
          this.usuarioUsername = usuarioUsername;
     }

    /**
     * @param motivo the motivo to set
     */
    public void setMotivo(MotivoDenuncia motivo) {
        this.motivo = motivo;
    }
    
    public String getExplicacion() {
          return explicacion;
     }

     public void setExplicacion(String explicacion) {
          this.explicacion = explicacion;
     }

     public String getNombreMotivo() {
          return nombreMotivo;
     }

     public void setNombreMotivo(String nombreMotivo) {
          this.nombreMotivo = nombreMotivo;
     }
}

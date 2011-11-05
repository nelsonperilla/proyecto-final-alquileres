/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.dto.DenunciaDTO;
import com.alquilacosas.ejb.session.AdministrarDenunciasBeanLocal;
import javax.faces.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

/**
 *
 * @author wilson
 */
@ManagedBean(name = "admDenuncias")
@ViewScoped
public class AdministrarDenunciasMBean {

     @EJB
     private AdministrarDenunciasBeanLocal AdministrarDenunciasBean;
     private List<SelectItem> filtros;
     private int filtroSeleccionado;
     private List<DenunciaDTO> denuncias;
     private Integer usuarioDenunciadoId;
     private Integer publicacionId;
     private Integer denunciaId;
     

     /** Creates a new instance of AdministrarDenunciasMBean */
     public AdministrarDenunciasMBean() {
     }

     @PostConstruct
     public void init() {
          filtros = new ArrayList<SelectItem>();
          filtros.add(new SelectItem(0, "Todos"));
          filtros.add(new SelectItem(1, "Publicaciones"));
          filtros.add(new SelectItem(2, "Comentarios"));
          filtroSeleccionado = 1;
          actualizarDenuncias();
     }

     public void actualizarDenuncias() {
          switch (filtroSeleccionado) {
               /* Carga Todas las Denuncias */
               case 0: {
                    denuncias = AdministrarDenunciasBean.getAllDenuncias();
                    break;
               }
               /* Carga Denuncias de Publicaciones */
               case 1: {
                    denuncias = AdministrarDenunciasBean.getDenunciasPublicacion();
                    break;
               }
               /* Carga Denuncias de Comentarios */
               case 2: {
                    denuncias = AdministrarDenunciasBean.getDenunciasComentario();
                    break;
               }
          }
     }
     
     public void prepararAceptarDenuncia (ActionEvent event) {
          denunciaId = (Integer) event.getComponent().getAttributes().get("denId");
     }

     public void aceptarDenuncia() {          
          AdministrarDenunciasBean.aceptarDenuncia(denunciaId);
          actualizarDenuncias();
     }
     
     public void prepararRechazarDenuncia(ActionEvent event) {
          denunciaId = (Integer) event.getComponent().getAttributes().get("denId");     
     }

     public void rechazarDenuncia() {
          AdministrarDenunciasBean.rechazarDenuncia(denunciaId);
          actualizarDenuncias();
     }

     public String verPublicacion() {
          return "mostrarPublicacion";
     }

     public String verUsuario() {
          return "verReputacionUsuario";
     }

     public Integer getPublicacionId() {
          return publicacionId;
     }

     public void setPublicacionId(Integer publicacionId) {
          this.publicacionId = publicacionId;
     }

     public List<DenunciaDTO> getDenuncias() {
          return denuncias;
     }

     public void setDenuncias(List<DenunciaDTO> denuncias) {
          this.denuncias = denuncias;
     }

     public int getFiltroSeleccionado() {
          return filtroSeleccionado;
     }

     public void setFiltroSeleccionado(int filtroSeleccionado) {
          this.filtroSeleccionado = filtroSeleccionado;
     }

     public List<SelectItem> getFiltros() {
          return filtros;
     }

     public void setFiltros(List<SelectItem> filtros) {
          this.filtros = filtros;
     }

     public Integer getUsuarioDenunciadoId() {
          return usuarioDenunciadoId;
     }

     public void setUsuarioDenunciadoId(Integer usuarioDenunciadoId) {
          this.usuarioDenunciadoId = usuarioDenunciadoId;
     }
}

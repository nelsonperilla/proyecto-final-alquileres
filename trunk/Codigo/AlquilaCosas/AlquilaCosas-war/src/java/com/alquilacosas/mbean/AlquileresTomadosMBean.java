/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.dto.AlquilerDTO;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

/**
 *
 * @author wilson
 */
@ManagedBean(name = "alquileresTomados")
@ViewScoped
public class AlquileresTomadosMBean {

     private List<SelectItem> filtros;
     private int filtroSeleccionado;
     private List<AlquilerDTO> alquileres;
     private int usuarioId;
     private int publicacionId;
     private Integer alquilerId;
     
     /** Creates a new instance of AlquileresTomadosMBean */
     public AlquileresTomadosMBean() {
     }
     
     @PostConstruct
     public void init(){     
          filtros = new ArrayList<SelectItem>();
          filtros.add(new SelectItem(0, "ACTIVO"));
          filtros.add(new SelectItem(1, "Finalizado SIN Calificación"));
          filtros.add(new SelectItem(2, "Finalizado CON Calificación"));
          filtroSeleccionado = 1;
          alquileres = new ArrayList<AlquilerDTO>();
          alquileres.add(new AlquilerDTO(0,1,1,new Date(),new Date(), 250, true));
          alquileres.add(new AlquilerDTO(2,3,6,new Date(),new Date(), 240, false));          
     }
     
     public void actualizarAlquileres() {
          switch(filtroSeleccionado){
               case 0:
                    
               case 1:
                    
               case 2:
                    
          }
     }   
     
     public String verPublicacion() {
          return "";
     }
     
     public String verUsuario() {
          return "";
     }
     
     public void tomarAlquilerACalificar(ActionEvent event) {
          alquilerId = (Integer) event.getComponent().getAttributes().get("alq");
     }
     
     public void verCalificacion(ActionEvent event) {
          alquilerId = (Integer) event.getComponent().getAttributes().get("alq");
          // llama al bea y obtiene las calificaciones para este alquiler
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
     
     public List<AlquilerDTO> getAlquileres() {
          return alquileres;
     }

     public void setAlquileres(List<AlquilerDTO> alquileres) {
          this.alquileres = alquileres;
     }

     public int getPublicacionId() {
          return publicacionId;
     }

     public void setPublicacionId(int publicacionId) {
          this.publicacionId = publicacionId;
     }

     public int getUsuarioId() {
          return usuarioId;
     }

     public void setUsuarioId(int usuarioId) {
          this.usuarioId = usuarioId;
     }
}

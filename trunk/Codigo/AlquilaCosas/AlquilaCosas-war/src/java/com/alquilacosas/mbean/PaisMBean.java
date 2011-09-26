/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.ejb.entity.Pais;
import com.alquilacosas.ejb.session.PaisBeanLocal;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author wilson
 */
@ManagedBean(name="pais")
@ViewScoped
public class PaisMBean implements Serializable {
     
     @EJB
     private PaisBeanLocal paisBean;
     private List<Pais> paises;
     private String nombre;
     private Pais paisSeleccionado;

     public Pais getPaisSeleccionado() {
          return paisSeleccionado;
     }

     public void setPaisSeleccionado(Pais paisSeleccionado) {
          this.paisSeleccionado = paisSeleccionado;
     }
     
     /** Creates a new instance of PaisMBean */
     public PaisMBean() {
     }
     
     @PostConstruct
     public void init(){
          paises = paisBean.getPaises();
     }
     
     public void borrarPais() {
          try{
               paisBean.borrarPais(paisSeleccionado);          
          }
          catch(AlquilaCosasException e){
               FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());  
               FacesContext.getCurrentInstance().addMessage(null, message);  
          }
     }
     
     public void modificarPais(RowEditEvent ev) {          
          paisSeleccionado = (Pais)ev.getObject();
          paisBean.modificarPais(paisSeleccionado);
     }
     
     public void registrarPais() {
          Pais nuevo = new Pais();
          nuevo.setNombre(nombre);
          try{
               paisBean.registrarPais(nuevo);
          }
          catch(AlquilaCosasException e){
               FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
               FacesContext.getCurrentInstance().addMessage(null, message); 
          }  
     }

     public List<Pais> getPaises() {
          return paises;
     }

     public void setPaises(List<Pais> paises) {
          this.paises = paises;
     }
     
     public String getNombre() {
          return nombre;
     }

     public void setNombre(String nombre) {
          this.nombre = nombre;
     }

}

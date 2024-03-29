/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.PeriodoDTO;
import com.alquilacosas.ejb.entity.Periodo;
import com.alquilacosas.ejb.session.PeriodoAlquilerBeanLocal;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.log4j.Logger;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author wilson
 */
@ManagedBean(name = "periodo")
@ViewScoped
public class PeriodoAlquilerMBean implements Serializable {

     /** Creates a new instance of PeriodoAlquilerMBean */
     public PeriodoAlquilerMBean() {
     }
     @EJB
     private PeriodoAlquilerBeanLocal periodoAlquilerBean;
     private List<PeriodoDTO> periodosAlquiler;
     private Periodo periodoSeleccionado;
     private String nombre;
     private String descripcion;
     private int horas;
     
     @PostConstruct
     public void init() {
         Logger.getLogger(PeriodoAlquilerMBean.class).debug("PeriodoAlquilerMBean: postconstruct.");
          periodosAlquiler = periodoAlquilerBean.getPeriodos();
     }
     
     public void borrarPeriodo() {
          try{
               periodoAlquilerBean.borrarPeriodo(periodoSeleccionado);          
          }
          catch(AlquilaCosasException e){
               FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());  
               FacesContext.getCurrentInstance().addMessage(null, message);  
          }
     }
     
     public void modificarPeriodo(RowEditEvent ev) {          
          periodoSeleccionado = (Periodo) ev.getObject();
          periodoAlquilerBean.modificarPeriodo(periodoSeleccionado);
     }
     
     public void registrarPeriodo() {
          try{
               periodoAlquilerBean.registrarPeriodo(nombre, descripcion, horas);
          }
          catch(AlquilaCosasException e){
               FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                       e.getMessage(), e.getMessage());
               FacesContext.getCurrentInstance().addMessage(null, message); 
          }  
     }
     
     public List<PeriodoDTO> getPeriodosAlquiler() {
          return periodosAlquiler;
     }
     
     public void setPeriodosAlquiler(List<PeriodoDTO> periodosAlquiler) {
          this.periodosAlquiler = periodosAlquiler;
     }
     
     public Periodo getPeriodoSeleccionado() {
          return periodoSeleccionado;
     }
     
     public void setPeriodoSeleccionado(Periodo periodoSeleccionado) {
          this.periodoSeleccionado = periodoSeleccionado;
     }

     public String getDescripcion() {
          return descripcion;
     }

     public void setDescripcion(String descripcion) {
          this.descripcion = descripcion;
     }

     public int getHoras() {
          return horas;
     }

     public void setHoras(int horas) {
          this.horas = horas;
     }

     public String getNombre() {
          return nombre;
     }

     public void setNombre(String nombre) {
          this.nombre = nombre;
     }
}

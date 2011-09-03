/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.ejb.entity.Pais;
import com.alquilacosas.ejb.entity.Provincia;
import com.alquilacosas.ejb.session.PaisBeanLocal;
import com.alquilacosas.ejb.session.ProvinciaBeanLocal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author wilson
 */
@ManagedBean(name = "provincia")
@ViewScoped
public class ProvinciaMBean {

     @EJB
     private PaisBeanLocal paisBean;
     @EJB
     private ProvinciaBeanLocal provinciaBean;
     private List<SelectItem> paises;
     private List<Provincia> provincias;
     private int paisSeleccionado;
     private Provincia provinciaSeleccionada;
     private String nombre;

     public String getNombre() {
          return nombre;
     }

     public void setNombre(String nombre) {
          this.nombre = nombre;
     }

     /** Creates a new instance of ProvinciaMBean */
     public ProvinciaMBean() {
     }

     @PostConstruct
     public void init() {
          paises = new ArrayList<SelectItem>();
          List<Pais> paisList = paisBean.getPaises();
          for (Pais p : paisList) {
               paises.add(new SelectItem(p.getPaisId(), p.getNombre()));
               if (p.getNombre().equals("ARGENTINA")) {
                    paisSeleccionado = p.getPaisId();
                    actualizarProvincias();
               }
          }
     }
     
     public void actualizarProvincias() {
          provincias = provinciaBean.getProvinciaByPais(paisSeleccionado);
     }
     
     public void modificarProvincia(RowEditEvent ev) {
          provinciaSeleccionada = (Provincia)ev.getObject();
          provinciaBean.modificarProvincia(provinciaSeleccionada);
     }
     
     public void borrarProvincia() throws AlquilaCosasException {
          try{
               provinciaBean.borrarProvincia(provinciaSeleccionada);
          }
          catch(AlquilaCosasException e){
               FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al Eliminar la Provincia " + e.getMessage(), "");  
               FacesContext.getCurrentInstance().addMessage(null, message); 
          }
     }
     
     public void registrarProvincia() throws AlquilaCosasException {
          Pais paisSel = paisBean.getPaisById(paisSeleccionado);
          try{
               provinciaBean.registrarProvincia(nombre, paisSel);
          }
          catch(AlquilaCosasException e){
               FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al Registrar la Provincia " + e.getMessage(), "");  
               FacesContext.getCurrentInstance().addMessage(null, message); 
          }
     }

     public int getPaisSeleccionado() {
          return paisSeleccionado;
     }

     public void setPaisSeleccionado(int paisSeleccionado) {
          this.paisSeleccionado = paisSeleccionado;
     }
     
     public Provincia getProvinciaSeleccionada() {
          return provinciaSeleccionada;
     }

     public void setProvinciaSeleccionada(Provincia provinciaSeleccionada) {
          this.provinciaSeleccionada = provinciaSeleccionada;
     }
     
     public List<Provincia> getProvincias() {
          return provincias;
     }

     public void setProvincias(List<Provincia> provincias) {
          this.provincias = provincias;
     }

     public List<SelectItem> getPaises() {
          return paises;
     }

     public void setPaises(List<SelectItem> paises) {
          this.paises = paises;
     }
     
     
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.common.ProvinciaFacade;
import com.alquilacosas.ejb.entity.Pais;
import com.alquilacosas.ejb.entity.Provincia;
import com.alquilacosas.ejb.session.ProvinciaBeanLocal;
import java.util.ArrayList;
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
@ManagedBean(name = "provincia")
@ViewScoped
public class ProvinciaMBean {

     @EJB
     private ProvinciaBeanLocal provinciaBean;
     private List<Provincia> provincias;
     private List<ProvinciaFacade> provinciasF;
     private List<Pais> paises;
     private String nombre;
     private Pais pais;
     private Provincia provinciaSeleccionada;
     private ProvinciaFacade provinciaSeleccionadaF;

     /** Creates a new instance of ProvinciaMBean */
     public ProvinciaMBean() {
     }
     
     @PostConstruct
     public void init(){
          provincias = provinciaBean.getProvincias();
          provinciasF = new ArrayList<ProvinciaFacade>();
          for (Provincia p : provincias)
                  {
                       provinciasF.add(new ProvinciaFacade(p.getProvinciaId(),p.getNombre(),p.getPaisFk().getPaisId(),p.getPaisFk().getNombre()));
                  }
          paises = provinciaBean.getPaises();
     }
     
     public void borrarProvincia() {
          try{
               provinciaBean.borrarProvincia(provinciaSeleccionada);          
          }
          catch(AlquilaCosasException e){
               FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());  
               FacesContext.getCurrentInstance().addMessage(null, message);  
          }
     }
     
     public void modificarProvincia(RowEditEvent ev) {          
          provinciaSeleccionadaF = (ProvinciaFacade)ev.getObject();
          provinciaBean.modificarProvincia(provinciaSeleccionada);
     }
     
     public void registrarProvincia() {
          Provincia nuevo = new Provincia();
          nuevo.setNombre(nombre);
          nuevo.setPaisFk(pais);
          try{
               provinciaBean.registrarProvincia(nuevo);
          }
          catch(AlquilaCosasException e){
               FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage());
               FacesContext.getCurrentInstance().addMessage(null, message); 
          }  
     }

     public List<Provincia> getProvincias() {
          return provincias;
     }

     public void setProvincias(List<Provincia> Provincias) {
          this.provincias = Provincias;
     }
     
     public String getNombre() {
          return nombre;
     }

     public void setNombre(String nombre) {
          this.nombre = nombre;
     }
     
     public List<Pais> getPaises() {
          return paises;
     }

     public void setPaises(List<Pais> paises) {
          this.paises = paises;
     }

     public Provincia getProvinciaSeleccionada() {
          return provinciaSeleccionada;
     }

     public void setProvinciaSeleccionada(Provincia provinciaSeleccionada) {
          this.provinciaSeleccionada = provinciaSeleccionada;
     }

     public Provincia getProvinciaSeleccionado() {
          return provinciaSeleccionada;
     }

     public void setProvinciaSeleccionado(Provincia ProvinciaSeleccionado) {
          this.provinciaSeleccionada = ProvinciaSeleccionado;
     }

     public Pais getPais() {
          return pais;
     }

     public void setPais(Pais pais) {
          this.pais = pais;
     }
     
}

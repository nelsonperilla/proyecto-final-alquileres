/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.dto.DomicilioDTO;
import com.alquilacosas.ejb.session.ProvinciaBeanLocal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import org.apache.log4j.Logger;

/**
 *
 * @author damiancardozo
 */
@ManagedBean(name = "buscarBean")
@SessionScoped
public class BuscarMBean {

    @EJB
    private ProvinciaBeanLocal provinciaBean;
    private String criterio, ciudad;
    private List<SelectItem> ciudades;
    
    @PostConstruct
    public void init() {
        Logger.getLogger(BuscarMBean.class).debug("BuscarMBean: postconstruct.");
        
        ciudades = new ArrayList<SelectItem>();
        for(String s: provinciaBean.getCiudades()) {
            ciudades.add(new SelectItem(s, s));
        }
        ciudad = (String) ciudades.get(0).getValue();
    }

    public String buscar() {
        //navegacion definida en faces-config
        return "";
    }
    
    public void seleccionarCiudad(ActionEvent event) {
        ciudad = (String) event.getComponent().getAttributes().get("ciudad");
        System.out.println("cambiado ciudad: " + ciudad);
    }
    
    public String getCriterio() {
        return criterio;
    }

    public void setCriterio(String criterio) {
        this.criterio = criterio;
    }

    public List<SelectItem> getCiudades() {
        return ciudades;
    }

    public void setCiudades(List<SelectItem> ciudades) {
        this.ciudades = ciudades;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

}

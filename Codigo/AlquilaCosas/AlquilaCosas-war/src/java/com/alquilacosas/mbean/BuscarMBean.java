/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import org.apache.log4j.Logger;

/**
 *
 * @author damiancardozo
 */
@ManagedBean(name = "buscarBean")
@SessionScoped
public class BuscarMBean {

    private String criterio, ciudad;
    private List<String> ciudades;
    
    @PostConstruct
    public void init() {
        Logger.getLogger(BuscarMBean.class).debug("BuscarMBean: postconstruct.");
        ciudades = new ArrayList<String>();
        ciudades.add("Cordoba");
        ciudades.add("Rosario");
        ciudades.add("Buenos Aires");
        ciudades.add("Mendoza");
        ciudades.add("Salta");
        ciudades.add("Neuquen");
        ciudades.add("Rio Cuarto");
        ciudades.add("Formosa");
        
        ciudad = "Cordoba";
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

    public List<String> getCiudades() {
        return ciudades;
    }

    public void setCiudades(List<String> ciudades) {
        this.ciudades = ciudades;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.dto.PublicacionDTO;
import com.alquilacosas.ejb.session.MostrarPublicacionesBeanLocal;
import java.util.List;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import org.apache.log4j.Logger;

/**
 *
 * @author jorge
 */
@ManagedBean(name="inicioBean")
@ViewScoped
public class PublicacionesInicioMBean implements Serializable {
    @EJB
    private MostrarPublicacionesBeanLocal publicationBean;
    private List<PublicacionDTO> publicacionesRandom;
    private int selectedPublicationId;

    public PublicacionesInicioMBean() {
    }

    @PostConstruct
    public void init() {
        Logger.getLogger(PublicacionesInicioMBean.class).debug("PublicationsGroup: postconstruct.");
        publicacionesRandom = publicationBean.getPublicacionesRandom(12);
    }
    
    public String seleccionarPublicacion() {

        return "mostrarPublicacion";
    }
    
    public int getSelectedPublicationId() {
        return selectedPublicationId;
    }

 
    
    public void setSelectedPublicationId(int id) {
        selectedPublicationId = id;
    }
    
    public List<PublicacionDTO> getPublicacionesRandom()
    {
        return publicacionesRandom;
    }


}

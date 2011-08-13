/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.common.PublicacionFacade;
import com.alquilacosas.ejb.session.MostrarPublicacionesBeanLocal;
import java.util.List;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author jorge
 */
@ManagedBean
@ViewScoped
public class PublicationsGroup implements Serializable
{
    @EJB
    private MostrarPublicacionesBeanLocal publicationBean;
    private List<PublicacionFacade> publicacionesRandom;
    private int selectedPublicationId;

    public PublicationsGroup() 
    {

    }

    @PostConstruct
    public void init()
    {
        publicacionesRandom = publicationBean.getPublicacionesRandom(0);
    }
    
    public String seleccionarPublicacion() {
        //return "mostrarPublicacion?id=" +  selectedPublicationId;
        return "test";
    }
    
    public int getSelectedPublicationId() {
        return selectedPublicationId;
    }
    
    public void setSelectedPublicationId(int id) {
        selectedPublicationId = id;
    }
    
    public List<PublicacionFacade> getPublicacionesRandom()
    {
        return publicacionesRandom;
    }
}

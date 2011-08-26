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
    private List<PublicacionDTO> publicacionesRandom;
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

        return "mostrarPublicacion";//"mostrarPublicacion.xhtml?id=" +  getSelectedPublicationId();

        //return "mostrarPublicacion?id=" +  selectedPublicationId;

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

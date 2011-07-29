/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.ejb.entity.Publicacion;
import com.alquilacosas.ejb.session.ShowPublicationsBeanLocal;
import java.util.List;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
/**
 *
 * @author jorge
 */
@ManagedBean
public class PublicationsGroup implements Serializable
{
    @EJB
    private ShowPublicationsBeanLocal publicationBean;
    private List<PublicacionMBean> publicacionesRandom;

    public PublicationsGroup() 
    {

    }

    @PostConstruct
    public void init() 
    {
            publicacionesRandom= new ArrayList<PublicacionMBean>();
        List<Publicacion> listaPublicaciones = publicationBean.getPublicacionesRandom(0);
        for(Publicacion publicacion : listaPublicaciones)
        {
            
            publicacionesRandom.add(new PublicacionMBean(publicacion.getTitulo(),
                    publicacion.getDescripcion(),publicacion.getFechaDesde(), 
                    publicacion.getFechaHasta(), publicacion.getDestacada()
                    ,publicacion.getCantidad()));
                    //publicacion.getCategoriaFk(),publicacion.getUsuarioFk()));
        }
    }
    
    public List<PublicacionMBean> getPublicacionesRandom()
    {
        return publicacionesRandom;
    }
   
}
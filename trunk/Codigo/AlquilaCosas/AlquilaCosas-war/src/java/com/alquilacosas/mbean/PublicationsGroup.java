/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.common.PublicacionFacade;
import com.alquilacosas.ejb.entity.Publicacion;
import com.alquilacosas.ejb.session.MostrarPublicacionesBeanLocal;
import java.io.ByteArrayInputStream;
import java.util.List;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
/**
 *
 * @author jorge
 */
@ManagedBean

public class PublicationsGroup implements Serializable
{
    @EJB
    private MostrarPublicacionesBeanLocal publicationBean;
    private List<PublicacionFacade> publicacionesRandom;
    private StreamedContent imagenPrincipal; 
    private String selectedPublicationId;

    public PublicationsGroup() 
    {

    }

    @PostConstruct
    public void init() 
    {
        publicacionesRandom= new ArrayList<PublicacionFacade>();
        List<Publicacion> listaPublicaciones = publicationBean.getPublicacionesRandom(0);
        for(Publicacion publicacion : listaPublicaciones)
        {
            PublicacionFacade temp=new PublicacionFacade(publicacion.getPublicacionId(),
                    publicacion.getTitulo(),publicacion.getDescripcion(),
                    publicacion.getFechaDesde(),publicacion.getFechaHasta(),
                    publicacion.getDestacada(), publicacion.getCantidad());
            String nombre=publicacion.getTitulo();
            temp.setImagenes(publicationBean.getImage(publicacion.getPublicacionId()));
            publicacionesRandom.add(temp);
                    //publicacion.getCategoriaFk(),publicacion.getUsuarioFk()));
        }
        byte[] img=publicacionesRandom.get(0).getImagenPrincipal();
        ByteArrayInputStream i = new ByteArrayInputStream(img);
        imagenPrincipal =new DefaultStreamedContent(i);        
        
        
    }
    
    public List<PublicacionFacade> getPublicacionesRandom()
    {
        return publicacionesRandom;
    }

    /**
     * @return the imagenPrincipal
     */
    public StreamedContent getImagenPrincipal() {
        
        boolean h=FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().containsKey("h");
        String id=FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("photo_id");
        if(id!=null){
            int photo_id  = Integer.parseInt(id);
            System.out.println(photo_id);
            byte[] img=publicacionesRandom.get(photo_id-1).getImagenPrincipal();
            ByteArrayInputStream i = new ByteArrayInputStream(img);
            imagenPrincipal=new DefaultStreamedContent(i);        
        }
        else 
            System.out.println("noooooooooooo");
        return imagenPrincipal;
    }

    /**
     * @param imagenPrincipal the imagenPrincipal to set
     */
    public void setImagenPrincipal(StreamedContent imagenPrincipal) {
        this.imagenPrincipal = imagenPrincipal;
    }

    /**
     * @return the selectedPublicationId
     */
    public String getselectedPublicationId() {
        return selectedPublicationId;
    }

    /**
     * @param selectedPublicationId the selectedPublicationId to set
     */
    public void setselectedPublicationId(String selectedPublicationId) {
         
        this.selectedPublicationId = selectedPublicationId;
        publicationBean.setSelectedPublication(Integer.parseInt(selectedPublicationId));
    }
   
}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.common.PublicacionFacade;
import com.alquilacosas.ejb.entity.Publicacion;
import com.alquilacosas.ejb.session.MostrarPublicacionesBeanLocal;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
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
    private byte[] imageNotfound;
    public PublicationsGroup() 
    {

    }

    @PostConstruct
    public void init()
    {
        try {
            File fileImageNotFound=new File(((ServletContext)
                 FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath
                 ("/resources/images/noImage.png"));
            FileInputStream streamImageNotFound=new FileInputStream(fileImageNotFound);
            imageNotfound=new byte[(int)fileImageNotFound.length()];
            streamImageNotFound.read(imageNotfound);
        } catch (FileNotFoundException ex) {
            
            Logger.getLogger(PublicationsGroup.class.getName()).log(Level.SEVERE, null, ex);
            System.out.print("El archivo sigue mal");
        } catch (IOException ex1) {
            Logger.getLogger(PublicationsGroup.class.getName()).log(Level.SEVERE, null, ex1);
        }
    
        publicacionesRandom= new ArrayList<PublicacionFacade>();
        List<Publicacion> listaPublicaciones = publicationBean.getPublicacionesRandom(0);
        for(Publicacion publicacion : listaPublicaciones)
        {
            PublicacionFacade temp=new PublicacionFacade(publicacion.getPublicacionId(),
                    publicacion.getTitulo(),publicacion.getDescripcion(),
                    publicacion.getFechaDesde(),publicacion.getFechaHasta(),
                    publicacion.getDestacada(), publicacion.getCantidad());
            List<byte[]> imagenes=publicationBean.getImage(publicacion.getPublicacionId());
            if(imagenes.isEmpty())
            {
                imagenes.add(imageNotfound);
            }
            temp.setImagenes(imagenes);
            publicacionesRandom.add(temp);
                    //publicacion.getCategoriaFk(),publicacion.getUsuarioFk()));
        }
        //byte[] img=publicacionesRandom.get(0).getImagenPrincipal();
        //ByteArrayInputStream i = new ByteArrayInputStream(img);
        //imagenPrincipal =new DefaultStreamedContent(i);        
        
        
    }
    
    public List<PublicacionFacade> getPublicacionesRandom()
    {
        return publicacionesRandom;
    }

    /**
     * @return the imagenPrincipal
     */
    public StreamedContent getImagenPrincipal() {
        String id=FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("photo_id");
        if(id!=null){
            int photo_id  = Integer.parseInt(id);
            //System.out.println(photo_id);
            byte[] img=publicacionesRandom.get(photo_id-1).getImagenPrincipal();
            ByteArrayInputStream i = new ByteArrayInputStream(img);
            imagenPrincipal=new DefaultStreamedContent(i);        
        }
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
    public String seleccionarPublicacion() {
        return "mostrarPublicacion?id=" +  selectedPublicationId;
    }
    
    public String getselectedPublicationId() {
        return selectedPublicationId;
    }

    /**
     * @param selectedPublicationId the selectedPublicationId to set
     */
    public void setselectedPublicationId(String selectedPublicationId) {
         ///vistas/mostrarPublicacion.xhtm?id=#{publicacionMBean.id}
        this.selectedPublicationId = selectedPublicationId;
        //publicationBean.setSelectedPublication(Integer.parseInt(selectedPublicationId));
        //System.out.println(selectedPublicationId);
    }
   
}

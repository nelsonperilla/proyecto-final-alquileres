/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.common.PublicacionFacade;
import com.alquilacosas.ejb.session.BuscarPublicacionBeanLocal;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author damiancardozo
 */
@ManagedBean(name = "resultadosBean")
@ViewScoped
public class ResultadosMBean implements Serializable {

    @EJB
    private BuscarPublicacionBeanLocal buscarBean;
    private LazyDataModel model;
    private List<PublicacionFacade> publicaciones;
    private PublicacionFacade publicacionActual;
    private String busqueda;
    private int categoria;

    /** Creates a new instance of BuscarPublicacionMBean */
    public ResultadosMBean() {
    }

    @PostConstruct
    public void init() {
        publicaciones = new ArrayList<PublicacionFacade>();
        busqueda = FacesContext.getCurrentInstance().getExternalContext()
                .getRequestParameterMap().get("art");
        String cat = FacesContext.getCurrentInstance().getExternalContext()
                .getRequestParameterMap().get("cat");
        if(cat != null && !cat.equals(""))
            categoria = Integer.valueOf(cat);
        
        model = new LazyDataModel<PublicacionFacade>() {

            @Override
            public List<PublicacionFacade> load(int first, int pageSize, String sortFielf, 
                            boolean sort, Map<String, String> filters) {
                if((busqueda == null || busqueda.equals("")) && categoria <= 0)
                    return new ArrayList<PublicacionFacade>();
                else if(busqueda != null && !busqueda.equals("") && categoria <= 0)
                    publicaciones = buscarBean.buscarPublicaciones(busqueda, pageSize, first);
                else if((busqueda == null || !busqueda.equals("")) && categoria > 0)
                    publicaciones = buscarBean.buscarPublicacionesPorCategoria(categoria, pageSize, first);
                else if(busqueda != null && !busqueda.equals("") && categoria > 0) {
                    publicaciones = buscarBean.buscarPublicaciones(busqueda, categoria, pageSize, first);
                }
                return publicaciones;
            }
            
        };
        model.setRowCount(10);
    }

    public String buscar() {
        try {
            publicaciones = buscarBean.buscarPublicaciones(busqueda, 10, 0);
            System.out.println("publicaciones traidas: " + publicaciones.size());
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Error al buscar publicacion", e.getMessage()));
        }
        return null;
    }

    public StreamedContent getImagenPrincipal() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("photo_id");
        Integer photoId = Integer.parseInt(id);
        if(photoId != null) {
            for (PublicacionFacade pub : publicaciones) {
                if (pub.getId() == photoId) {
                    return new DefaultStreamedContent(
                            new ByteArrayInputStream(pub.getImagenPrincipal()));
                }
            }
        }
        return new DefaultStreamedContent();
    }

    public String getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(String busqueda) {
        this.busqueda = busqueda;
    }

    public List<PublicacionFacade> getPublicaciones() {
        return publicaciones;
    }

    public void setPublicaciones(List<PublicacionFacade> publicaciones) {
        this.publicaciones = publicaciones;
    }

    public PublicacionFacade getPublicacionActual() {
        return publicacionActual;
    }

    public void setPublicacionActual(PublicacionFacade publicacionActual) {
        this.publicacionActual = publicacionActual;
    }

    public LazyDataModel getModel() {
        return model;
    }

    public void setModel(LazyDataModel model) {
        this.model = model;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.common.Busqueda;
import com.alquilacosas.common.CategoriaFacade;
import com.alquilacosas.common.PublicacionFacade;
import com.alquilacosas.ejb.session.BuscarPublicacionBeanLocal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.model.LazyDataModel;

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
    private List<CategoriaFacade> categorias;
    private PublicacionFacade publicacionActual;
    private String busqueda;
    private Integer categoria;
    private int publicacionSeleccionada;
    private boolean primeraVez = true;

    /** Creates a new instance of BuscarPublicacionMBean */
    public ResultadosMBean() {
    }

    @PostConstruct
    public void init() {
        publicaciones = new ArrayList<PublicacionFacade>();
        busqueda = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("art");
        String cat = FacesContext.getCurrentInstance().getExternalContext()
                .getRequestParameterMap().get("cat");
        if(cat != null && !cat.equals(""))
            categoria = Integer.valueOf(cat);

        buscar(0, 10);
        model = new LazyDataModel<PublicacionFacade>() {

            @Override
            public List<PublicacionFacade> load(int first, int pageSize, String sortFielf,
                    boolean sort, Map<String, String> filters) {
                if(!primeraVez)
                    buscar(first, pageSize);
                return publicaciones;
            }
        };
        model.setRowCount(10);
    }

    public void filtrarCategoria(ActionEvent event) {
        categoria = (Integer) event.getComponent().getAttributes().get("cat");
        buscar(0, 10);
    }
    
    public void noFiltrarCategoria() {
        categoria = null;
        buscar(0, 10);
    }
    
    public String getNombreCategoria() {
        if(categoria != null)
        for(CategoriaFacade c: categorias) {
            if(c.getId() == categoria)
                return c.getNombre();
        }
        return "";
    }

    public void buscar(int first, int pageSize) {
        if ((busqueda == null || busqueda.equals("")) && categoria == null) {
            publicaciones = new ArrayList<PublicacionFacade>();
        } else if (busqueda != null && !busqueda.equals("") && categoria == null) {
            Busqueda b = buscarBean.buscarPublicaciones(busqueda, pageSize, first);
            publicaciones = b.getPublicaciones();
            categorias = b.getCategorias();
        } else if ((busqueda == null || !busqueda.equals("")) && categoria != null) {
            Busqueda b = buscarBean.buscarPublicacionesPorCategoria(categoria, pageSize, first);
            publicaciones = b.getPublicaciones();
            categorias = b.getCategorias();
        } else if (busqueda != null && !busqueda.equals("") && categoria != null) {
            Busqueda b = buscarBean.buscarPublicaciones(busqueda, categoria, pageSize, first);
            publicaciones = b.getPublicaciones();
            categorias = b.getCategorias();
        }
        
        if(primeraVez)
            primeraVez = false;
    }

    public String verPublicacion() {
        return "";
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

    public int getPublicacionSeleccionada() {
        return publicacionSeleccionada;
    }

    public void setPublicacionSeleccionada(int publicacionSeleccionada) {
        this.publicacionSeleccionada = publicacionSeleccionada;
    }

    public List<CategoriaFacade> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<CategoriaFacade> categorias) {
        this.categorias = categorias;
    }

    public Integer getCategoria() {
        return categoria;
    }

    public void setCategoria(Integer categoria) {
        this.categoria = categoria;
    }
}

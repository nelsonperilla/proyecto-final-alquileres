/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.common.Busqueda;
import com.alquilacosas.dto.CategoriaDTO;
import com.alquilacosas.dto.PublicacionDTO;
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
    private List<PublicacionDTO> publicaciones;
    private List<CategoriaDTO> categorias;
    private PublicacionDTO publicacionActual;
    private String busqueda, ubicacion;
    private Integer categoria;
    private int publicacionSeleccionada;
    private int totalRegistros;
    private boolean filtrarUbicacion, filtrarPrecio;
    private boolean noBuscarEnModel = true;

    /** Creates a new instance of BuscarPublicacionMBean */
    public ResultadosMBean() {
    }

    @PostConstruct
    public void init() {
        publicaciones = new ArrayList<PublicacionDTO>();
        busqueda = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("art");
//        String cat = FacesContext.getCurrentInstance().getExternalContext()
//                .getRequestParameterMap().get("cat");
//        if(cat != null && !cat.equals(""))
//            categoria = Integer.valueOf(cat);
        
        buscar(0, 10);
        model = new LazyDataModel<PublicacionDTO>() {

            @Override
            public List<PublicacionDTO> load(int first, int pageSize, String sortFielf,
                    boolean sort, Map<String, String> filters) {
                if(noBuscarEnModel) {
                    noBuscarEnModel = false;
                }
                else
                    buscar(first, pageSize);
                return publicaciones;
            }
        };
        model.setRowCount(totalRegistros);
    }

    public void filtrarCategoria(ActionEvent event) {
        categoria = (Integer) event.getComponent().getAttributes().get("cat");
        buscar(0, 10);
    }
    
    public void noFiltrarCategoria() {
        categoria = null;
        buscar(0, 10);
        noBuscarEnModel = true;
    }
    
    public void filtrarUbicacion() {
        filtrarUbicacion = true;
        buscar(0, 10);
        noBuscarEnModel = true;
    }
    
    public void noFiltrarUbicacion() {
        filtrarUbicacion = false;
    }
    
    public void filtrarPrecio() {
        filtrarPrecio = true;
        buscar(0, 10);
        noBuscarEnModel = true;
    }
    
    public void noFiltrarPrecio() {
        filtrarPrecio = false;
    }
    
    public String getNombreCategoria() {
        if(categoria != null)
        for(CategoriaDTO c: categorias) {
            if(c.getId() == categoria)
                return c.getNombre();
        }
        return "";
    }

    public void buscar(int first, int pageSize) {
        Busqueda b = buscarBean.buscar(busqueda, categoria, ubicacion, null, null, null, pageSize, first);
        publicaciones = b.getPublicaciones();
        categorias = b.getCategorias();
        if(first == 0) {
            totalRegistros = b.getTotalRegistros();
            if(model != null)
                model.setRowCount(totalRegistros);
        }
    }

    public String verPublicacion() {
        return "mostrarPublicacion";
    }

    public String getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(String busqueda) {
        this.busqueda = busqueda;
    }

    public List<PublicacionDTO> getPublicaciones() {
        return publicaciones;
    }

    public void setPublicaciones(List<PublicacionDTO> publicaciones) {
        this.publicaciones = publicaciones;
    }

    public PublicacionDTO getPublicacionActual() {
        return publicacionActual;
    }

    public void setPublicacionActual(PublicacionDTO publicacionActual) {
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

    public List<CategoriaDTO> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<CategoriaDTO> categorias) {
        this.categorias = categorias;
    }

    public Integer getCategoria() {
        return categoria;
    }

    public void setCategoria(Integer categoria) {
        this.categoria = categoria;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public boolean isFiltrarUbicacion() {
        return filtrarUbicacion;
    }

    public void setFiltrarUbicacion(boolean filtrarUbicacion) {
        this.filtrarUbicacion = filtrarUbicacion;
    }
        
}

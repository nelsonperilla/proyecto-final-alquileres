/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.common.Busqueda;
import com.alquilacosas.dto.CategoriaDTO;
import com.alquilacosas.dto.PublicacionDTO;
import com.alquilacosas.ejb.session.BuscarPublicacionBeanLocal;
import com.alquilacosas.ejb.session.CategoriaBeanLocal;
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
import org.apache.log4j.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author damiancardozo
 */
@ManagedBean(name = "verCategoriaBean")
@ViewScoped
public class VerCategoriaMBean implements Serializable {

    @EJB
    private CategoriaBeanLocal categoriaBean;
    @EJB
    private BuscarPublicacionBeanLocal buscarBean;
    private Integer id, publicacionSeleccionada;
    private String nombreCategoria;
    private LazyDataModel model;
    private List<PublicacionDTO> publicaciones;
    private List<CategoriaDTO> subcategorias;
    private Integer subcategoriaSeleccionada;
    private int totalRegistros;
    private boolean noBuscarEnModel = true;
    
    /** Creates a new instance of VerCategoriaMBean */
    public VerCategoriaMBean() {
    }
    
    @PostConstruct
    public void init() {
        Logger.getLogger(VerCategoriaMBean.class).debug("VerCategoriaMBean: postconstruct.");
        String param = FacesContext.getCurrentInstance().getExternalContext()
                .getRequestParameterMap().get("id");
        if(param != null) {
            try {
                id = Integer.valueOf(param);
            } catch (NumberFormatException e) {
                Logger.getLogger(VerCategoriaMBean.class).error("NumberFormatExcepcion al convertir parametro de url en int");
            }
        } else {
            return;
        }
        if(id != null) {
            buscar(0, 10);
            subcategorias = categoriaBean.getSubCategorias(id);
        } else {
            return;
        }
        
        model = new LazyDataModel<PublicacionDTO>() {

            @Override
            public List<PublicacionDTO> load(int first, int pageSize, String sortFielf,
                    SortOrder sortOrder, Map<String, String> filters) {
                if(noBuscarEnModel) {
                    noBuscarEnModel = false;
                } else {
                    buscar(first, pageSize);
                }
                return publicaciones;
            }
        };
        model.setRowCount(totalRegistros);
    }
    
    public void buscar(int first, int pageSize) {
        if(id == null)
            return;
        Busqueda b = null;
        try {
            b = buscarBean.buscarPublicacionesPorCategoria(id, pageSize, first);
        } catch(AlquilaCosasException e) {
            publicaciones = new ArrayList<PublicacionDTO>();
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Error al buscar publicaciones", e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return;
        } 
        publicaciones = b.getPublicaciones();        
        if(b.getCategorias() != null && !b.getCategorias().isEmpty())
            nombreCategoria = b.getCategorias().get(0).getNombre();
        
        if(first == 0) {
            totalRegistros = b.getTotalRegistros();
            if(model != null)
                model.setRowCount(totalRegistros);
        }
    }
    
    public String verPublicacion() {
        return "mostrarPublicacionDeCat";
    }
    
    public String verSubcategoria() {
        return "verSubCategoria";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LazyDataModel getModel() {
        return model;
    }

    public void setModel(LazyDataModel model) {
        this.model = model;
    }

    public List<PublicacionDTO> getPublicaciones() {
        return publicaciones;
    }

    public void setPublicaciones(List<PublicacionDTO> publicaciones) {
        this.publicaciones = publicaciones;
    }

    public List<CategoriaDTO> getSubcategorias() {
        return subcategorias;
    }

    public void setSubcategorias(List<CategoriaDTO> subcategorias) {
        this.subcategorias = subcategorias;
    }

    public Integer getPublicacionSeleccionada() {
        return publicacionSeleccionada;
    }

    public void setPublicacionSeleccionada(Integer publicacionSeleccionada) {
        this.publicacionSeleccionada = publicacionSeleccionada;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public Integer getSubcategoriaSeleccionada() {
        return subcategoriaSeleccionada;
    }

    public void setSubcategoriaSeleccionada(Integer subcategoriaSeleccionada) {
        this.subcategoriaSeleccionada = subcategoriaSeleccionada;
    }
    
}

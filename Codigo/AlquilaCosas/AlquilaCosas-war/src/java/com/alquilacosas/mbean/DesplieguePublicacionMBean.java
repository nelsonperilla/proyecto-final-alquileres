/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.common.PrecioFacade;
import com.alquilacosas.ejb.entity.Categoria;
import com.alquilacosas.ejb.session.PublicacionBean;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author jorge
 */
@ManagedBean
@ViewScoped
public class DesplieguePublicacionMBean {

    @EJB
    private PublicacionBean publicacionBean;
    
//    
//    @EJB
//    private CategoriaBean categoriaBean;
    
    //Datos de la publicacion
    private String titulo;
    private String descripcion;
    private Date fechaDesde;
    private Date fechaHasta;
    private boolean destacada;
    private int cantidad;
    
    //Select Items
    private List<Categoria> categorias;
    private int selectedCategoria;
    
//    private List<SelectItem> periodos;
//    private String selectedPeriodo;
   
    
    //Object Precio
    private double precio;
    private List<PrecioFacade> precios;
    private PrecioFacade precioFacade;
    
    private Date today;
    private List<Integer> idImagenes;

    
    private PrecioFacade precioSeleccionado;
    
    
    /** Creates a new instance of DesplieguePublicacionMBean */
    public DesplieguePublicacionMBean() { }
    
    
    @PostConstruct
    public void init(){
        
    }
}

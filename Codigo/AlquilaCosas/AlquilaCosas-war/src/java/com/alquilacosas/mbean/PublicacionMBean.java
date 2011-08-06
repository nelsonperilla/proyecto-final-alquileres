package com.alquilacosas.mbean;

/**
 *
 * @author jorge
 */

import com.alquilacosas.common.PrecioFacade;
import com.alquilacosas.ejb.entity.Categoria;
import com.alquilacosas.ejb.entity.ImagenPublicacion;
import com.alquilacosas.ejb.entity.Periodo;
import com.alquilacosas.ejb.session.CategoriaBeanLocal;
import com.alquilacosas.ejb.session.PublicacionBeanLocal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.primefaces.event.FileUploadEvent;



@ManagedBean(name = "publicacion")
@ViewScoped
public class PublicacionMBean implements Serializable{
    
    public PublicacionMBean() 
    {
    }   
    
    @EJB
    private PublicacionBeanLocal publicacionBean;
    
    
    @EJB
    private CategoriaBeanLocal categoriaBean;
    
    //Datos de la publicacion
    private String titulo;
    private String descripcion;
    private Date fechaDesde;
    private Date fechaHasta;
    private boolean destacada;
    private int cantidad;
    
    //Select Items
    private List<SelectItem> categorias;
    private int selectedCategoria;
    
    private List<SelectItem> periodos;
    private String selectedPeriodo;
   
    
    //Object Precio
    private double precio;
    private List<PrecioFacade> precios;
    private PrecioFacade precioFacade;
    
    private Date today;
    private List<ImagenPublicacion> imagenes;

    
    private PrecioFacade precioSeleccionado;
        
    
    @PostConstruct
    public void init() {
     
        imagenes = new ArrayList<ImagenPublicacion>();
        precios  = new ArrayList<PrecioFacade>();
        today = new Date();
        categorias = new ArrayList<SelectItem>();
        periodos = new ArrayList<SelectItem>();

        imagenes = new ArrayList<ImagenPublicacion>();
        
        List<Categoria> listaCategoria = categoriaBean.getCategorias();
        for(Categoria categoria : listaCategoria) {
           categorias.add(new SelectItem(categoria.getCategoriaId(), categoria.getNombre()));
       }
        
       List<Periodo> listaPeriodos = publicacionBean.getPeriodos();
       for(Periodo p : listaPeriodos){
           periodos.add( new SelectItem(p.getNombre()) );    
       }

    }
    
        public void crearPublicacion(){
        
        try {
            
            publicacionBean.registrarPublicacion(titulo, descripcion, 
                    new Date(), new Date(), destacada, cantidad, 
                    1 , selectedCategoria, precios, imagenes);
            
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage("Publicacion Creada"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Todo mal", e.getMessage()));
        }
    }
    
    public void cargarPrecio(){
        
        if( !existPrecio() ){

            precioFacade = new PrecioFacade();
            precioFacade.setPeriodoNombre(selectedPeriodo);
            precioFacade.setPrecio(precio);

            precios.add(precioFacade); 
        }else{
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                "No se puede agregar el precio", "Ya existe otro precio con el mismo periodo"));
        }
        
        
    }
    
    public void handleFileUpload(FileUploadEvent event){
       
        ImagenPublicacion ip = new ImagenPublicacion();

        ip.setImagen(event.getFile().getContents());
        imagenes.add(ip);
        System.out.println("llamaaaa");
        
        try {
            
            FacesMessage msg = new FacesMessage("Successful", 
                    event.getFile().getFileName() + "is uploaded");
        
            FacesContext.getCurrentInstance().addMessage(null, msg);
            
        } catch (Exception e) {
            
            FacesMessage error = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "The files were not uploaded!", " ");
            FacesContext.getCurrentInstance().addMessage(null, error);
        }      
       
    }
    
    public void borrarPrecio(){
        System.out.println("ejecutando borrarPrecio");
        precios.remove(precioSeleccionado);
        System.out.println("jajja");
    }
    
    public boolean existPrecio(){
        
        for( PrecioFacade p : precios ){
            if( p.getPeriodoNombre().equals(selectedPeriodo) )
                return true;
        }
        return false;
    }
            

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public CategoriaBeanLocal getCategoriaBean() {
        return categoriaBean;
    }

    public void setCategoriaBean(CategoriaBeanLocal categoriaBean) {
        this.categoriaBean = categoriaBean;
    }

    public List<SelectItem> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<SelectItem> categorias) {
        this.categorias = categorias;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isDestacada() {
        return destacada;
    }

    public void setDestacada(boolean destacada) {
        this.destacada = destacada;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public List<ImagenPublicacion> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<ImagenPublicacion> imagenes) {
        this.imagenes = imagenes;
    }

    public List<SelectItem> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<SelectItem> periodos) {
        this.periodos = periodos;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public PrecioFacade getPrecioFacade() {
        return precioFacade;
    }

    public void setPrecioFacade(PrecioFacade precioFacade) {
        this.precioFacade = precioFacade;
    }

    public List<PrecioFacade> getPrecios() {
        return precios;
    }

    public void setPrecios(List<PrecioFacade> precios) {
        this.precios = precios;
    }

    public PublicacionBeanLocal getPublicacionBean() {
        return publicacionBean;
    }

    public void setPublicacionBean(PublicacionBeanLocal publicacionBean) {
        this.publicacionBean = publicacionBean;
    }

    public int getSelectedCategoria() {
        return selectedCategoria;
    }

    public void setSelectedCategoria(int selectedCategoria) {
        this.selectedCategoria = selectedCategoria;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Date getToday() {
        return today;
    }

    public void setToday(Date today) {
        this.today = today;
    }
    
    public PrecioFacade getPrecioSeleccionado() {
        return precioSeleccionado;
    }

    public void setPrecioSeleccionado(PrecioFacade precioSeleccionado) {
        this.precioSeleccionado = precioSeleccionado;
        System.out.println("target: " + precioSeleccionado.getPrecio());
    }

    public String getSelectedPeriodo() {
        return selectedPeriodo;
    }

    public void setSelectedPeriodo(String selectedPeriodo) {
        this.selectedPeriodo = selectedPeriodo;
    }
    
    
}

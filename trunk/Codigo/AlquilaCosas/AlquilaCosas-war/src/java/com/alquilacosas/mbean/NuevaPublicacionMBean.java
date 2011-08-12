package com.alquilacosas.mbean;

/**
 *
 * @author jorge
 */

import com.alquilacosas.common.PrecioFacade;
import com.alquilacosas.ejb.entity.Categoria;
import com.alquilacosas.ejb.entity.Domicilio;
import com.alquilacosas.ejb.entity.ImagenPublicacion;
import com.alquilacosas.ejb.entity.Usuario;
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
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.primefaces.event.FileUploadEvent;



@ManagedBean(name = "publicacion")
@ViewScoped
public class NuevaPublicacionMBean implements Serializable{
    
    public NuevaPublicacionMBean() 
    {
    }   
    
    @EJB 
    private PublicacionBeanLocal publicacionBean;
    @EJB 
    private CategoriaBeanLocal categoriaBean;
    @ManagedProperty (value = "#{login}")
    private ManejadorUsuarioMBean login;
    
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
    
    //Object Precio
    private List<PrecioFacade> precios;
    private PrecioFacade precioFacade;
    
    private Date today;
    private List<ImagenPublicacion> imagenes;
    private Usuario usuario;
    private Domicilio domicilio;

        
    
    @PostConstruct
    public void init() {
     
        imagenes = new ArrayList<ImagenPublicacion>();
        precios  = new ArrayList<PrecioFacade>();
        precios.add( new PrecioFacade( 0, 1, 0.0, "hora" ) );
        precios.add( new PrecioFacade( 0, 2, 0.0, "dia" ) );
        precios.add( new PrecioFacade( 0, 3, 0.0, "semana" ) );
        precios.add( new PrecioFacade( 0, 4, 0.0, "mes") );
        today = new Date();
        categorias = new ArrayList<SelectItem>();
        
        List<Categoria> listaCategoria = categoriaBean.getCategorias();
        for(Categoria categoria : listaCategoria) {
           categorias.add(new SelectItem(categoria.getCategoriaId(), categoria.getNombre()));
        }

    }
    
        public void crearPublicacion(){
        
        try {
            
            publicacionBean.registrarPublicacion(titulo, descripcion, 
                    new Date(), new Date(), destacada, cantidad, 
                    login.getUsuarioId(), selectedCategoria, precios, imagenes);
            
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage("Publicacion Creada"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Publicacion Fallida", e.getMessage()));
        }
    }

    
    public void handleFileUpload(FileUploadEvent event){
       
        ImagenPublicacion ip = new ImagenPublicacion();

        ip.setImagen(event.getFile().getContents());
        System.out.println("llamaaaa");
        
        try {
            
            FacesMessage msg = new FacesMessage("Excelente", 
                    event.getFile().getFileName() + "fue cargado correctamente");
        
            FacesContext.getCurrentInstance().addMessage(null, msg);
            
        } catch (Exception e) {
            
            FacesMessage error = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "El archivo no pudo ser cargado!", " ");
            FacesContext.getCurrentInstance().addMessage(null, error);
        }      
       
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


    public ManejadorUsuarioMBean getLogin() {
        return login;
    }

    public void setLogin(ManejadorUsuarioMBean login) {
        this.login = login;
    }

}

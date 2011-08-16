package com.alquilacosas.mbean;

/**
 *
 * @author jorge
 */

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.common.CategoriaFacade;
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
    private List<SelectItem> subCategorias;
    private int selectedSubCategoria;
    private List<SelectItem> subCategorias1;
    private int selectedSubCategoria1; 
    private boolean subCategoriaRender;
    private boolean subCategoria1Render;
    
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
        precios.add( new PrecioFacade( 0, 1, 0.0, "Hora" ) );
        precios.add( new PrecioFacade( 0, 2, 0.0, "Dia" ) );
        precios.add( new PrecioFacade( 0, 3, 0.0, "Semana" ) );
        precios.add( new PrecioFacade( 0, 4, 0.0, "Mes") );
        today = new Date();
        categorias = new ArrayList<SelectItem>();
        subCategorias = new ArrayList<SelectItem>();
        subCategorias1 = new ArrayList<SelectItem>();
        subCategoriaRender = false;
        subCategoria1Render = false;
        List<Categoria> listaCategoria = categoriaBean.getCategoriasPrincipal();
        for(Categoria categoria : listaCategoria) {
           categorias.add(new SelectItem(categoria.getCategoriaId(), categoria.getNombre()));
        }

    }
    
     public String crearPublicacion(){
        
        try {
            
            if( precios.get(1).getPrecio() == 0 )
                throw new AlquilaCosasException("El precio por dia es obligatorio!");
            
            publicacionBean.registrarPublicacion(titulo, descripcion, 
                    new Date(), new Date(), destacada, cantidad, 
                    login.getUsuarioId(), selectedCategoria, precios, imagenes);
            
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage("Publicacion Creada"));
            
            return "misPublicaciones";
            
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Publicacion Fallida", e.getMessage()));
            return null;
        }
        
    }

    
    public void handleFileUpload(FileUploadEvent event){
       try {
       
          if( imagenes.size() <  6 ){
                ImagenPublicacion ip = new ImagenPublicacion();
                ip.setImagen(event.getFile().getContents());
                imagenes.add(ip);
          }else{
               throw new AlquilaCosasException("No se puede cargar más de 5 fotos");
          }
         
         FacesMessage msg = new FacesMessage("Excelente", 
                    event.getFile().getFileName() + "fue cargado correctamente");
        
         FacesContext.getCurrentInstance().addMessage(null, msg);
            
        } catch (Exception e) {
            
            FacesMessage error = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "El archivo no pudo ser cargado!", " ");
            FacesContext.getCurrentInstance().addMessage(null, error);
        }      
       
    }
    
    public void actualizarCategorias(){
        subCategorias.clear();
        List<CategoriaFacade> categoriaList = categoriaBean.getSubCategorias(selectedCategoria);
        if(!categoriaList.isEmpty()) {
            subCategoriaRender = true;
            for(CategoriaFacade c : categoriaList) {
                subCategorias.add(new SelectItem( c.getId(), c.getNombre() ));
            }
        }else{
            subCategoriaRender = false;
            subCategoria1Render = false;
        }
            
    }
    
    public void actualizarSubCategorias(){
        subCategorias1.clear();
        List<CategoriaFacade> categoriaList = categoriaBean.getSubCategorias(selectedSubCategoria);
        if(!categoriaList.isEmpty()) {
            subCategoria1Render = true;
            for(CategoriaFacade c : categoriaList) {
                subCategorias1.add(new SelectItem( c.getId(), c.getNombre() ));
            }
        }else{
            subCategoria1Render = false;
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

    public int getSelectedSubcategoria() {
        return selectedSubCategoria;
    }

    public void setSelectedSubcategoria(int selectedSubcategoria) {
        this.selectedSubCategoria = selectedSubcategoria;
    }

    public List<SelectItem> getSubCategorias() {
        return subCategorias;
    }

    public void setSubCategorias(List<SelectItem> subCategorias) {
        this.subCategorias = subCategorias;
    }

    public int getSelectedSubCategoria() {
        return selectedSubCategoria;
    }

    public void setSelectedSubCategoria(int selectedSubCategoria) {
        this.selectedSubCategoria = selectedSubCategoria;
    }

    public int getSelectedSubCategoria1() {
        return selectedSubCategoria1;
    }

    public void setSelectedSubCategoria1(int selectedSubCategoria1) {
        this.selectedSubCategoria1 = selectedSubCategoria1;
    }

    public List<SelectItem> getSubCategorias1() {
        return subCategorias1;
    }

    public void setSubCategorias1(List<SelectItem> subCategorias1) {
        this.subCategorias1 = subCategorias1;
    }

    public boolean isSubCategoria1Render() {
        return subCategoria1Render;
    }

    public void setSubCategoria1Render(boolean subCategoria1Render) {
        this.subCategoria1Render = subCategoria1Render;
    }

    public boolean isSubCategoriaRender() {
        return subCategoriaRender;
    }

    public void setSubCategoriaRender(boolean subCategoriaRender) {
        this.subCategoriaRender = subCategoriaRender;
    }
    
    
    
}

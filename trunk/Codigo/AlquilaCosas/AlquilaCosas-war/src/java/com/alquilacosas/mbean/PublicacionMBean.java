package com.alquilacosas.mbean;

/**
 *
 * @author jorge
 */

import com.alquilacosas.ejb.session.ShowPublicationsBeanLocal;
import java.io.Serializable;
import java.util.Date;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;



@ManagedBean
public class PublicacionMBean implements Serializable{
    
    public PublicacionMBean() 
    {
        titulo="prod";
        descripcion="call";
    }
    
    
    public PublicacionMBean(String titulo,String descripcion, Date fecha_desde,
            Date fecha_hasta, boolean destacada,int cantidad)//, Categoria categoria,Usuario usuario) 
    {
        this.titulo=titulo;
        this.descripcion=descripcion;
        this.fecha_desde=fecha_desde;
        this.fecha_hasta=fecha_hasta;
        this.destacada=destacada;
        this.cantidad=cantidad;
        //this.categoria=categoria;
        //this.usuario=usuario;
    }
    
    @EJB
    private ShowPublicationsBeanLocal publicationBean;
    private String titulo;
    private String descripcion;
    private Date fecha_desde;
    private Date fecha_hasta;
    private boolean destacada;
    private int cantidad;
    //private Categoria categoria;
    //private Usuario usuario;
    //private int pagina;

    
    
    /**
     * @return the titulo
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * @param titulo the titulo to set
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * @return the fecha_desde
     */
    public Date getFecha_desde() {
        return fecha_desde;
    }

    /**
     * @param fecha_desde the fecha_desde to set
     */
    public void setFecha_desde(Date fecha_desde) {
        this.fecha_desde = fecha_desde;
    }

    /**
     * @return the fecha_hasta
     */
   public Date getFecha_hasta() {
        return fecha_hasta;
    }

    /**
     * @param fecha_hasta the fecha_hasta to set
     */
    public void setFecha_hasta(Date fecha_hasta) {
        this.fecha_hasta = fecha_hasta;
    }

    /**
     * @return the destacada
     */
    public boolean getDestacada() {
        return destacada;
    }

    /**
     * @param destacada the destacada to set
     */
    public void setDestacada(boolean destacada) {
        this.destacada = destacada;
    }

    /**
     * @return the cantidad
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * @param cantidad the cantidad to set
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * @return the categoria
     */
/*    public Categoria getCategoria() {
        return categoria;
    }

    /**
     * @param categoria the categoria to set
     */
/*    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    /**
     * @return the usuario
     */
  /*  public Usuario getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    /*public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
*/
    
    
}
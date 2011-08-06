/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.common.DomicilioFacade;
import com.alquilacosas.common.UsuarioFacade;
import com.alquilacosas.ejb.entity.Pais;
import com.alquilacosas.ejb.session.UsuarioBeanLocal;
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

/**
 *
 * @author damiancardozo
 */
@ManagedBean(name="modUsuario")
@ViewScoped
public class ModificarUsuarioMBean {

    @EJB
    private UsuarioBeanLocal usuarioBean;
    @ManagedProperty(value="#{login}")
    private ManejadorUsuarioMBean usuarioMBean;
    
    
    private UsuarioFacade usuario;
    private String nombre, apellido, dni, telefono, email;
    private Date fechaNacimiento;
    private String calle, depto, barrio, ciudad;
    private Integer numero, piso;
    private Date today;
    private List<SelectItem> provincias;
    private int provinciaSeleccionada;
    private List<SelectItem> paises;
    private int paisSeleccionado;
    private List<DomicilioFacade> domicilios;
    private DomicilioFacade domicilioSeleccionado;
    
    /** Creates a new instance of ModificarUsuarioMBean */
    public ModificarUsuarioMBean() {
    }
    
    @PostConstruct
    public void init() {
        
        Integer id = usuarioMBean.getUsuarioId();
        usuario = usuarioBean.getDatosUsuario(id);
        
        nombre = usuario.getNombre();
        apellido = usuario.getApellido();
        dni = usuario.getDni();
        email = usuario.getEmail();
        telefono = usuario.getTelefono();
        fechaNacimiento = usuario.getFechaNacimiento();
        domicilios = usuario.getDomicilios();
        
        paises = new ArrayList<SelectItem>();
        provincias = new ArrayList<SelectItem>();
        
        List<Pais> listaPais = usuarioBean.getPaises();
        if(!listaPais.isEmpty()) {
            for(Pais p: listaPais) {
                paises.add(new SelectItem(p.getPaisId(), p.getNombre()));
            }
        }
        today = new Date();
    }
    
    public void agregarDomicilio() {
        if(domicilios.size() >= 3) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Error al agregar domicilio", 
                    "Se permiten hasta 3 domicilios"));
            return;
        }
        DomicilioFacade dom = new DomicilioFacade();
        dom.setCalle(calle);
        dom.setNumero(numero);
        if(piso != null)
            dom.setPiso(piso);
        if(!depto.equals(""))
            dom.setDepto(depto);
        dom.setBarrio(barrio);
        dom.setCiudad(ciudad);
        domicilios.add(dom);
        calle = "";
        numero = null;
        piso = null;
        depto = "";
        barrio = "";
        ciudad = "";
    }
    
     public String crearUsuario() {   
        if(domicilios.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Error al crear usuario", "No se cargo ningun domicilio"));
            return null;
        }
        return null;
    }    

    
    /*
     * Getters & Setters
     */
    
    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getDepto() {
        return depto;
    }

    public void setDepto(String depto) {
        this.depto = depto;
    }

    public DomicilioFacade getDomicilioSeleccionado() {
        return domicilioSeleccionado;
    }

    public void setDomicilioSeleccionado(DomicilioFacade domicilioSeleccionado) {
        this.domicilioSeleccionado = domicilioSeleccionado;
    }

    public List<DomicilioFacade> getDomicilios() {
        return domicilios;
    }

    public void setDomicilios(List<DomicilioFacade> domicilios) {
        this.domicilios = domicilios;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public int getPaisSeleccionado() {
        return paisSeleccionado;
    }

    public void setPaisSeleccionado(int paisSeleccionado) {
        this.paisSeleccionado = paisSeleccionado;
    }

    public List<SelectItem> getPaises() {
        return paises;
    }

    public void setPaises(List<SelectItem> paises) {
        this.paises = paises;
    }

    public Integer getPiso() {
        return piso;
    }

    public void setPiso(Integer piso) {
        this.piso = piso;
    }

    public int getProvinciaSeleccionada() {
        return provinciaSeleccionada;
    }

    public void setProvinciaSeleccionada(int provinciaSeleccionada) {
        this.provinciaSeleccionada = provinciaSeleccionada;
    }

    public List<SelectItem> getProvincias() {
        return provincias;
    }

    public void setProvincias(List<SelectItem> provincias) {
        this.provincias = provincias;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Date getToday() {
        return today;
    }

    public void setToday(Date today) {
        this.today = today;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ManejadorUsuarioMBean getUsuarioMBean() {
        return usuarioMBean;
    }

    public void setUsuarioMBean(ManejadorUsuarioMBean usuarioMBean) {
        this.usuarioMBean = usuarioMBean;
    }

}

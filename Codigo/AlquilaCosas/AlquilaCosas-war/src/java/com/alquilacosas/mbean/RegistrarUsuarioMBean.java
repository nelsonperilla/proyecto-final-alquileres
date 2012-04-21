/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.DomicilioDTO;
import com.alquilacosas.ejb.entity.Pais;
import com.alquilacosas.ejb.entity.Provincia;
import com.alquilacosas.ejb.session.UsuarioBeanLocal;
import java.awt.event.ActionEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import org.apache.log4j.Logger;
import org.primefaces.event.map.MarkerDragEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

/**
 *
 * @author damiancardozo
 */
@ManagedBean(name = "regUsuario")
@ViewScoped
public class RegistrarUsuarioMBean implements Serializable {
    
    @EJB
    private UsuarioBeanLocal usuarioBean;
    private String username;
    private String password;
    private String password2;
    private String nombre;
    private String apellido;
    private String dni;
    private String telefono;
    private String email;
    private String calle;
    private String depto;
    private String barrio;
    private String ciudad;
    private Integer numero;
    private Integer piso;
    private Date fechaNacimiento;
    private Date today;
    private List<SelectItem> provincias;
    private int provinciaSeleccionada;
    private List<SelectItem> paises;
    private int paisSeleccionado;
    private DomicilioDTO domicilio;
    private boolean creado;
    private MapModel gMap;
    private double lat;
    private double lng;
    
    public RegistrarUsuarioMBean() {
        
    }
    
    @PostConstruct
    public void init() {
        Logger.getLogger(RegistrarUsuarioMBean.class).debug("RegistrarUsuarioMBean: postconstruct.");
        paises = new ArrayList<SelectItem>();
        provincias = new ArrayList<SelectItem>();
        List<Pais> listaPais = usuarioBean.getPaises();
        if(!listaPais.isEmpty()) {
            for(Pais p: listaPais) {
                paises.add(new SelectItem(p.getPaisId(), p.getNombre()));
            }
        }
        today = new Date();
        gMap = new DefaultMapModel();
    }

    public void addMarker(ActionEvent actionEvent) {
        Marker marker = new Marker(new LatLng(getLat(), getLng()), "alquilaCosas");
        getgMap().addOverlay(marker);
    }
    
    public void updateCoordinates(MarkerDragEvent event)
    {
        lat = event.getMarker().getLatlng().getLat();
        lng = event.getMarker().getLatlng().getLng();
    }
    
   
    public void crearDomicilio() {
        domicilio = new DomicilioDTO();
        domicilio.setCalle(calle);
        domicilio.setNumero(numero);
        if(piso != null)
            domicilio.setPiso(piso);
        if(depto != null && !depto.equals(""))
            domicilio.setDepto(depto);
        domicilio.setBarrio(barrio);
        domicilio.setCiudad(ciudad);
        domicilio.setLatitud(lat);
        domicilio.setLongitud(lng);
    }
    
    public String crearUsuario() {   
        crearDomicilio();
        try {
            usuarioBean.registrarUsuario(username, password, nombre, apellido, domicilio,
                provinciaSeleccionada, fechaNacimiento, dni, telefono, email);
            return "pusuarioCreado";
        } catch (AlquilaCosasException e) {
                FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Error al crear usuario", e.getMessage()));
            return null;
        }
    }
    
    public void actualizarProvincias() {
        provincias.clear();
        List<Provincia> provList = usuarioBean.getProvincias(paisSeleccionado);
        if(!provList.isEmpty()) {
            for(Provincia p: provList) {
                provincias.add(new SelectItem(p.getProvinciaId(), p.getNombre()));
            }
        }
    }
    
    public void prepararDomicilio() {
        
    }
    
    public void validarUsername(FacesContext context, UIComponent toValidate, Object value) {
        String user = (String) value;
        if(user.equals(""))
            return;
        else if(user.length() < 6) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "El usuario debe tener al menos 6 caracteres", 
                    "El usuario debe tener al menos 6 caracteres");
            throw new ValidatorException(message);
        }
        boolean existe = usuarioBean.usernameExistente(user);
        if(existe) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "El usuario ya existe", 
                    "El usuario ya existe");
            throw new ValidatorException(message);
        }
    }
    
    public void validarPassword(FacesContext context, UIComponent toValidate, Object value) {
        String confirmarPassword = (String)value;
        if(!password.equals(confirmarPassword)) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Las contraseñas no coinciden", "Las contraseñas no coinciden");
            throw new ValidatorException(message);
        }
    }
    
    
    /*
     * Getters & Setters
     */
    
    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

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

    public String getDepto() {
        return depto;
    }

    public void setDepto(String depto) {
        this.depto = depto;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public Integer getPiso() {
        return piso;
    }

    public void setPiso(Integer piso) {
        this.piso = piso;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<SelectItem> getPaises() {
        return paises;
    }

    public void setPaises(List<SelectItem> paises) {
        this.paises = paises;
    }

    public List<SelectItem> getProvincias() {
        return provincias;
    }

    public void setProvincias(List<SelectItem> provincias) {
        this.provincias = provincias;
    }

    public int getPaisSeleccionado() {
        return paisSeleccionado;
    }

    public void setPaisSeleccionado(int selectedPais) {
        this.paisSeleccionado = selectedPais;
    }

    public int getProvinciaSeleccionada() {
        return provinciaSeleccionada;
    }

    public void setProvinciaSeleccionada(int selectedProvincia) {
        this.provinciaSeleccionada = selectedProvincia;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public Date getToday() {
        return today;
    }

    public void setToday(Date today) {
        this.today = today;
    }

    public boolean isCreado() {
        return creado;
    }

    public void setCreado(boolean creado) {
        this.creado = creado;
    }

    /**
     * @return the gMap
     */
    public MapModel getgMap() {
        return gMap;
    }

    /**
     * @param gMap the gMap to set
     */
    public void setgMap(MapModel gMap) {
        this.gMap = gMap;
    }

    /**
     * @return the lat
     */
    public double getLat() {
        return lat;
    }

    /**
     * @param lat the lat to set
     */
    public void setLat(double lat) {
        this.lat = lat;
    }

    /**
     * @return the lng
     */
    public double getLng() {
        return lng;
    }

    /**
     * @param lng the lng to set
     */
    public void setLng(double lng) {
        this.lng = lng;
    }
}

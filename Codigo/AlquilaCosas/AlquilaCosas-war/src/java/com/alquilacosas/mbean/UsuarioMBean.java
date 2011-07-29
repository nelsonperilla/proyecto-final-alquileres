/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.ejb.entity.Domicilio;
import com.alquilacosas.ejb.entity.Pais;
import com.alquilacosas.ejb.entity.Provincia;
import com.alquilacosas.ejb.session.RegistrarUsuarioBeanLocal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author damiancardozo
 */
@ManagedBean(name = "usuario")
@ViewScoped
public class UsuarioMBean implements Serializable {

    /** Creates a new instance of UsuarioMBean */
    public UsuarioMBean() {
    }
    
    @EJB
    private RegistrarUsuarioBeanLocal usuarioBean;
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
    private int numero;
    private int piso;
    private Date fechaNacimiento;
    private Date today;
    private List<SelectItem> provincias;
    private int selectedProvincia;
    private List<SelectItem> paises;
    private int selectedPais;
    
    @PostConstruct
    public void init() {
        paises = new ArrayList<SelectItem>();
        provincias = new ArrayList<SelectItem>();
        List<Pais> listaPais = usuarioBean.getPaises();
        for(Pais p: listaPais) {
            paises.add(new SelectItem(p.getPaisId(), p.getNombre()));
        }
        today = new Date();
    }
    
    public void crearUsuario() {
        Domicilio dom = new Domicilio();
        dom.setCalle(calle);
        dom.setNumero(numero);
        dom.setPiso(piso);
        dom.setDepto(depto);
        dom.setBarrio(barrio);
        List<Domicilio> domicilios = new ArrayList<Domicilio>();
        List<Provincia> provincias;
        domicilios.add(dom);
        
        
        try {
            usuarioBean.registrarUsuario(username, password, nombre, apellido, domicilios,
                selectedProvincia, fechaNacimiento, dni, telefono, email);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Usuario creado"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Error al crear usuario", e.getMessage()));
        }
        
        
    }    
    
    public void actualizarProvincias() {
        //selectedPais = ((SelectItem)e.getNewValue()).getLabel();
        List<Provincia> prov = usuarioBean.getProvincias(selectedPais);
        for(Provincia p: prov) {
            provincias.add(new SelectItem(p.getProvinciaId(), p.getNombre()));
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
     * 
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

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
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

    public int getPiso() {
        return piso;
    }

    public void setPiso(int piso) {
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

    public int getSelectedPais() {
        return selectedPais;
    }

    public void setSelectedPais(int selectedPais) {
        this.selectedPais = selectedPais;
    }

    public int getSelectedProvincia() {
        return selectedProvincia;
    }

    public void setSelectedProvincia(int selectedProvincia) {
        this.selectedProvincia = selectedProvincia;
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
    
}

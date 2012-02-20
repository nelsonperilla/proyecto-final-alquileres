/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.DomicilioDTO;
import com.alquilacosas.dto.UsuarioDTO;
import com.alquilacosas.ejb.entity.Pais;
import com.alquilacosas.ejb.entity.Provincia;
import com.alquilacosas.ejb.session.ModificarUsuarioBeanLocal;
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
import org.apache.log4j.Logger;

/**
 *
 * @author damiancardozo
 */
@ManagedBean(name = "modUsuario")
@ViewScoped
public class ModificarUsuarioMBean implements Serializable {

    @EJB
    private ModificarUsuarioBeanLocal usuarioBean;
    @ManagedProperty(value = "#{login}")
    private ManejadorUsuarioMBean usuarioMBean;
    private UsuarioDTO usuario;
    private String telefono;
    private Date fechaNacimiento;
    private String calle, depto, barrio, ciudad;
    private Integer numero, piso;
    private Date today;
    private List<SelectItem> provincias;
    private int provinciaSeleccionada;
    private List<SelectItem> paises;
    private int paisSeleccionado;
    private DomicilioDTO domicilio;
    private boolean editando;

    /** Creates a new instance of ModificarUsuarioMBean */
    public ModificarUsuarioMBean() {
    }

    @PostConstruct
    public void init() {
        Logger.getLogger(ModificarUsuarioMBean.class).debug("ModificarUsuarioMBean: postconstruct.");
        Integer id = usuarioMBean.getUsuarioId();
        usuario = usuarioBean.getDatosUsuario(id);

        telefono = usuario.getTelefono();
        fechaNacimiento = usuario.getFechaNacimiento();
        domicilio = usuario.getDomicilio();
        
        if(domicilio != null) {
            paisSeleccionado = domicilio.getPaisId();
            provinciaSeleccionada = domicilio.getProvinciaId();
        }
        
        paises = new ArrayList<SelectItem>();
        provincias = new ArrayList<SelectItem>();
        if(paisSeleccionado != 0) {
            actualizarProvincias();
        }

        List<Pais> listaPais = usuarioBean.getPaises();
        if (!listaPais.isEmpty()) {
            for (Pais p : listaPais) {
                paises.add(new SelectItem(p.getPaisId(), p.getNombre()));
            }
        }
        today = new Date();
    }

    public void crearDomicilio() {
        domicilio = new DomicilioDTO();
        domicilio.setCalle(calle);
        domicilio.setNumero(numero);
        if (piso != null) {
            domicilio.setPiso(piso);
        }
        if (!depto.equals("")) {
            domicilio.setDepto(depto);
        }
        domicilio.setBarrio(barrio);
        domicilio.setCiudad(ciudad);
    }

    public void actualizarUsuario() {
        try {
            usuario = usuarioBean.actualizarUsuario(usuario.getId(), telefono, 
                    fechaNacimiento, domicilio);
            editando = false;
        } catch(AlquilaCosasException e) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                    "Error al actualizar usuario", e.getMessage()));
        }
    }

    public void actualizarProvincias() {
        provincias.clear();
        List<Provincia> provList = usuarioBean.getProvincias(paisSeleccionado);
        if (!provList.isEmpty()) {
            for (Provincia p : provList) {
                provincias.add(new SelectItem(p.getProvinciaId(), p.getNombre()));
            }
        }
        for(SelectItem si: paises) {
            if(si.getValue().equals(new Integer(paisSeleccionado))) {
                domicilio.setPais(si.getLabel());
                break;
            }
        }
        for(SelectItem si: provincias) {
            if(si.getValue().equals(new Integer(provinciaSeleccionada))) {
                domicilio.setProvincia(si.getLabel());
                break;
            }
        }
    }
    
    public void editar() {
        editando = true;
    }
    
    public void cancelarEdicion() {
        editando= false;
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

    public DomicilioDTO getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(DomicilioDTO domicilio) {
        this.domicilio = domicilio;
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

    public ManejadorUsuarioMBean getUsuarioMBean() {
        return usuarioMBean;
    }

    public void setUsuarioMBean(ManejadorUsuarioMBean usuarioMBean) {
        this.usuarioMBean = usuarioMBean;
    }

    public UsuarioDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDTO usuario) {
        this.usuario = usuario;
    }

    public boolean isEditando() {
        return editando;
    }

    public void setEditando(boolean editando) {
        this.editando = editando;
    }
}

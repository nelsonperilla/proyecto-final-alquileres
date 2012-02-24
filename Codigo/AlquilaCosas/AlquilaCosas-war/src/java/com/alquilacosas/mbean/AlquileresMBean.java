/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.AlquilerDTO;
import com.alquilacosas.ejb.session.AlquileresBeanLocal;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author damiancardozo
 */
@ManagedBean(name="alquileresBean")
@ViewScoped
public class AlquileresMBean {

    @EJB
    private AlquileresBeanLocal alquileresBean;
    @ManagedProperty(value = "#{login}")
    private ManejadorUsuarioMBean loginBean;
    private Integer usuarioLogueado;
    private List<AlquilerDTO> alquileres;
    private int alquilerId;
    
    /** Creates a new instance of AlquileresMBean */
    public AlquileresMBean() {
    }
    
    @PostConstruct
    public void init() {
        usuarioLogueado = loginBean.getUsuarioId();
        if (usuarioLogueado == null) {
            return;
        }
        alquileres = alquileresBean.getAlquileres(usuarioLogueado);
    }
    
    public void cancelarAlquiler() {
        boolean borrado = false;
//        try {
//            borrado = alquileresBean.cancelarAlquiler(alquilerId);
//        } catch (AlquilaCosasException e) {
//            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
//                    "Error al cancelar alquiler", e.getMessage());
//            FacesContext.getCurrentInstance().addMessage(null, msg);
//        }
        if (borrado) {
            for (int i = 0; i < alquileres.size(); i++) {
                AlquilerDTO alq = alquileres.get(i);
                if (alq.getIdAlquiler() == alquilerId) {
                    alquileres.remove(alq);
                    return;
                }
            }
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Alquiler cancelado.", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public List<AlquilerDTO> getAlquileres() {
        return alquileres;
    }

    public void setAlquileres(List<AlquilerDTO> alquileres) {
        this.alquileres = alquileres;
    }

    public ManejadorUsuarioMBean getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(ManejadorUsuarioMBean loginBean) {
        this.loginBean = loginBean;
    }

    public int getAlquilerId() {
        return alquilerId;
    }

    public void setAlquilerId(int alquilerId) {
        this.alquilerId = alquilerId;
    }
}

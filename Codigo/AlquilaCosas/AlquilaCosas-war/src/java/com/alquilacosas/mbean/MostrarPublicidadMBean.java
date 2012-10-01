/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.dto.PublicidadDTO;
import com.alquilacosas.ejb.entity.TipoPublicidad.UbicacionPublicidad;
import com.alquilacosas.ejb.session.MostrarPublicidadBeanLocal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author damiancardozo
 */
@ManagedBean(name="mostrarPublicidad")
@ViewScoped
public class MostrarPublicidadMBean implements Serializable {

    @EJB
    private MostrarPublicidadBeanLocal publicidadBean;
    private List<PublicidadDTO> publicidadesCarrusel;
    private List<PublicidadDTO> publicidadesIzq;
    private List<PublicidadDTO> publicidadesDer;
    private PublicidadDTO pubCarrusel, pubIzq, pubDer;
    
    /** Creates a new instance of MostrarPublicidadMBean */
    public MostrarPublicidadMBean() {
    }
    
    @PostConstruct
    public void cargarPublicidades() {
        publicidadesCarrusel = publicidadBean.getPublicidades(UbicacionPublicidad.CARRUSEL, 5);
        publicidadesIzq = publicidadBean.getPublicidades(UbicacionPublicidad.LATERAL_IZQUIERDA, 1);
        publicidadesDer = new ArrayList<PublicidadDTO>();
    }
    
    public List<PublicidadDTO> getPublicidadesCarrusel() {
        return publicidadesCarrusel;
    }

    public List<PublicidadDTO> getPublicidadesDer() {
        return publicidadesDer;
    }

    public List<PublicidadDTO> getPublicidadesIzq() {
        return publicidadesIzq;
    }
    
}

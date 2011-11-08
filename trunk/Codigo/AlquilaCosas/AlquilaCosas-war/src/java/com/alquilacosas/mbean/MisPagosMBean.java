/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.dto.PagoDTO;
import com.alquilacosas.ejb.session.MisPagosBeanLocal;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.apache.log4j.Logger;

/**
 *
 * @author damiancardozo
 */
@ManagedBean(name = "misPagosBean")
@ViewScoped
public class MisPagosMBean implements Serializable {

    @EJB
    private MisPagosBeanLocal pagosBean;
    @ManagedProperty(value="#{login}")
    private ManejadorUsuarioMBean loginBean;
    private List<PagoDTO> pagos;
    private Integer usuarioId;
    
    /** Creates a new instance of MisPagosMBean */
    public MisPagosMBean() {
    }
    
    @PostConstruct
    public void init() {
        Logger.getLogger(MisPagosMBean.class).debug("MisPagosMBean: postconstruct."); 
        usuarioId = loginBean.getUsuarioId();
        if(usuarioId != null) {
            pagos = pagosBean.getMisPagos(usuarioId);
        }
    }

    public List<PagoDTO> getPagos() {
        return pagos;
    }

    public void setPagos(List<PagoDTO> pagos) {
        this.pagos = pagos;
    }

    public ManejadorUsuarioMBean getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(ManejadorUsuarioMBean loginBean) {
        this.loginBean = loginBean;
    }
    
}

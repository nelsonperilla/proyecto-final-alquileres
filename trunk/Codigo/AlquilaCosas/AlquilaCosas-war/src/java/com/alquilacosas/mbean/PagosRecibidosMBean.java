/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.dto.PagoDTO;
import com.alquilacosas.ejb.entity.TipoPago.NombreTipoPago;
import com.alquilacosas.ejb.session.PagosRecibidosBeanLocal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.apache.log4j.Logger;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author damiancardozo
 */
@ManagedBean(name="pagos")
@ViewScoped
public class PagosRecibidosMBean implements Serializable {

    @EJB
    private PagosRecibidosBeanLocal pagosBean;
    private LazyDataModel model;
    private List<PagoDTO> pagos;
    private boolean todos, paypal, dm, noBuscarEnModel;
    private int totalRegistros, pagoElegido, desde;
    private NombreTipoPago tipoPago;
    
    /** Creates a new instance of PagosRecibidosMBean */
    public PagosRecibidosMBean() {
    }
    
    @PostConstruct
    public void init() {
        Logger.getLogger(PagosRecibidosMBean.class).debug("PagosRecibidosMBean: postconstruct");
        todos = true;
        pagos = new ArrayList<PagoDTO>();
        getPagosRecibidos(20, 0);
        
        model = new LazyDataModel<PagoDTO>() {

            @Override
            public List<PagoDTO> load(int first, int pageSize, String sortFielf,
                    boolean sort, Map<String, String> filters) {
                if (noBuscarEnModel) {
                    noBuscarEnModel = false;
                } else {
                    getPagosRecibidos(pageSize, first);
                }
                return pagos;
            }
        };
        model.setRowCount(totalRegistros);
        
    }
    
    public void getPagosRecibidos(int registros, int desde) {
        pagos = pagosBean.getPagosRecibidos(tipoPago, null, null, registros, desde);
        Long n = pagosBean.getCantidadPagos(null, null, todos);
        if(n != null)
            totalRegistros = n.intValue();
        else
            totalRegistros = 0;
        this.desde = desde;
    }
    
    public void mostrarTodos() {
        todos = true;
        paypal = false;
        dm = false;
        tipoPago = null;
        getPagosRecibidos(20, 0);
    }
    
    public void mostrarPagosPaypal() {
        todos = false;
        paypal = true;
        dm = false;
        tipoPago = NombreTipoPago.PAYPAL;
        getPagosRecibidos(20, 0);
    }
    
    public void mostrarPagosDineroMail() {
        todos = false;
        paypal = false;
        dm = true;
        tipoPago = NombreTipoPago.DINEROMAIL;
        getPagosRecibidos(20, 0);
    }
    
    public void confirmarPago() {
        pagosBean.confirmarPago(pagoElegido);
        getPagosRecibidos(20, desde);
    }
    
    /*
     * Getters & Setters
     */

    public List<PagoDTO> getPagos() {
        return pagos;
    }

    public void setPagos(List<PagoDTO> pagos) {
        this.pagos = pagos;
    }

    public boolean isTodos() {
        return todos;
    }

    public void setTodos(boolean todos) {
        this.todos = todos;
    }

    public boolean isDm() {
        return dm;
    }

    public void setDm(boolean dm) {
        this.dm = dm;
    }

    public boolean isPaypal() {
        return paypal;
    }

    public void setPaypal(boolean paypal) {
        this.paypal = paypal;
    }

    public LazyDataModel getModel() {
        return model;
    }

    public void setModel(LazyDataModel model) {
        this.model = model;
    }

    public int getPagoElegido() {
        return pagoElegido;
    }

    public void setPagoElegido(int pagoElegido) {
        this.pagoElegido = pagoElegido;
    }
    
    
}

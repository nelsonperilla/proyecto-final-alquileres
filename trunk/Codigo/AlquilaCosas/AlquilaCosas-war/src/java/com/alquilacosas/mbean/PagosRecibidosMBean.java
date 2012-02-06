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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import org.apache.log4j.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

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
    private List<SelectItem> periodos;
    private int periodoSeleccionado;
    private Date fechaDesde;
    private boolean todos, paypal, dm, todosEstados, confirmados, pendientes, noBuscarEnModel;
    private int totalRegistros, pagoElegido, desde;
    private NombreTipoPago tipoPago;
    private Boolean confirmado;
    
    /** Creates a new instance of PagosRecibidosMBean */
    public PagosRecibidosMBean() {
    }
    
    @PostConstruct
    public void init() {
        Logger.getLogger(PagosRecibidosMBean.class).debug("PagosRecibidosMBean: postconstruct");
        
        periodos = new ArrayList<SelectItem>();
        periodos.add(new SelectItem(0, "Todos"));
        periodos.add(new SelectItem(1, "Última semana"));
        periodos.add(new SelectItem(2, "Últimos 30 días"));
        periodos.add(new SelectItem(3, "Últimos 60 días"));
        
        periodoSeleccionado = 0;
        todos = true;
        todosEstados = true;
        pagos = new ArrayList<PagoDTO>();
        getPagosRecibidos(15, 0);
        
        model = new LazyDataModel<PagoDTO>() {

            @Override
            public List<PagoDTO> load(int first, int pageSize, String sortFielf,
                    SortOrder sortOrder, Map<String, String> filters) {
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
        pagos = pagosBean.getPagosRecibidos(tipoPago, fechaDesde, confirmado, registros, desde);
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
        getPagosRecibidos(15, 0);
    }
    
    public void mostrarPagosPaypal() {
        todos = false;
        paypal = true;
        dm = false;
        tipoPago = NombreTipoPago.PAYPAL;
        getPagosRecibidos(15, 0);
    }
    
    public void mostrarPagosDineroMail() {
        todos = false;
        paypal = false;
        dm = true;
        tipoPago = NombreTipoPago.DINEROMAIL;
        getPagosRecibidos(15, 0);
    }
    
    public void mostrarConfirmados() {
        confirmado = true;
        confirmados = true;
        pendientes = false;
        todosEstados = false;
        getPagosRecibidos(15, 0);
    }
    
    public void mostrarPendientes() {
        confirmado = false;
        confirmados = false;
        pendientes = true;
        todosEstados = false;
        getPagosRecibidos(15, 0);
    }
    
    public void mostrarTodosEstados() {
        confirmado = null;
        confirmados = false;
        pendientes = false;
        todosEstados = true;
        getPagosRecibidos(15, 0);
    }
    
    public void elegirPeriodo() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        switch(periodoSeleccionado) {
            case 0: {
                fechaDesde = null;
                break;
            } case 1: {
                cal.add(Calendar.DATE, -7);
                fechaDesde = cal.getTime();
                break;
            } case 2: {
                cal.add(Calendar.MONTH, -1);
                fechaDesde = cal.getTime();
                break;
            } case 3: {
                cal.add(Calendar.MONTH, -2);
                fechaDesde = cal.getTime();
                break;
            } default: {
                fechaDesde = null;
            }
        }
        getPagosRecibidos(15, 0);
    }
    
    public void confirmarPago() {
        pagosBean.confirmarPago(pagoElegido);
        getPagosRecibidos(15, desde);
    }
    
    public void eliminarPago() {
        pagosBean.eliminarPago(pagoElegido);
        getPagosRecibidos(15, desde);
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

    public boolean isConfirmados() {
        return confirmados;
    }

    public void setConfirmados(boolean confirmados) {
        this.confirmados = confirmados;
    }

    public boolean isPendientes() {
        return pendientes;
    }

    public void setPendientes(boolean pendientes) {
        this.pendientes = pendientes;
    }

    public boolean isTodosEstados() {
        return todosEstados;
    }

    public void setTodosEstados(boolean todosEstados) {
        this.todosEstados = todosEstados;
    }

    public List<SelectItem> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<SelectItem> periodos) {
        this.periodos = periodos;
    }

    public int getPeriodoSeleccionado() {
        return periodoSeleccionado;
    }

    public void setPeriodoSeleccionado(int periodoSeleccionado) {
        this.periodoSeleccionado = periodoSeleccionado;
    }
    
    
}

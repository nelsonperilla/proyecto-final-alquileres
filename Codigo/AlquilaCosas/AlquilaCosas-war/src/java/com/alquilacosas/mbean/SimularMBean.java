/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.mbean;

import com.alquilacosas.ejb.session.AlquileresBeanLocal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author damiancardozo
 */
@ManagedBean(name = "simular")
@RequestScoped
public class SimularMBean {

    @EJB AlquileresBeanLocal alquilerEJB;
    private int dias = 5;
    
    
    public void simular() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, dias);
        Date fecha = cal.getTime();
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        alquilerEJB.simularPasoTiempoAlquiler(fecha);
        FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO, 
                "Paso de tiempo simulado.", "La nueva fecha es: " + sdf.format(fecha)));
    }

    public int getDias() {
        return dias;
    }

    public void setDias(int dias) {
        this.dias = dias;
    }
    
}

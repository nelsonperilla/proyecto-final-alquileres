/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.validator.webutil;

import com.alquilacosas.mbean.DesplieguePublicacionMBean;
import javax.faces.context.FacesContext;
import org.apache.log4j.Logger;

/**
 *
 * @author damiancardozo
 */
public class Navigation {
    
    public static void redirect(String url) {
        /*try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(url);
        } catch (Exception e) {
            Logger.getLogger(DesplieguePublicacionMBean.class).error("Excepcion al ejecutar redirect().");
        }*/
    }
    
}

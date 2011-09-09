/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.common;

import com.alquilacosas.ejb.entity.EstadoAlquiler;

/**
 *
 * @author ignaciogiagante
 */
public class Util {
    
    public static double roundToDecimals(double d, int c) {
        int temp = (int)( (d*Math.pow(10,c) ));
        return ( ( (double)temp ) / Math.pow(10,c) );
    }
    
    public static String convertNombreEstadoAlquilerToString(EstadoAlquiler.NombreEstadoAlquiler estado){
        String state = null;
        if( estado.compareTo(EstadoAlquiler.NombreEstadoAlquiler.ACTIVO) == 0 ){
            state = "activo";
        }else if( estado.compareTo(EstadoAlquiler.NombreEstadoAlquiler.CANCELADO) == 0 ){
            state = "cancelado";
        }else if( estado.compareTo(EstadoAlquiler.NombreEstadoAlquiler.CANCELADO_ALQUILADOR) == 0 ){
            state = "cancelado por el alquilador";
        }else if( estado.compareTo(EstadoAlquiler.NombreEstadoAlquiler.CONFIRMADO) == 0 ){
            state = "confirmado";
        }else if( estado.compareTo(EstadoAlquiler.NombreEstadoAlquiler.FINALIZADO) == 0 ){
            state = "finalizado";
        }else if( estado.compareTo(EstadoAlquiler.NombreEstadoAlquiler.PEDIDO) == 0 ){
            state = "pedido";
        }else if( estado.compareTo(EstadoAlquiler.NombreEstadoAlquiler.PEDIDO_CANCELADO) == 0 ){
            state = "cancelado";
        }else if( estado.compareTo(EstadoAlquiler.NombreEstadoAlquiler.PEDIDO_RECHAZADO) == 0 ){
            state = "rechazado";
        }
        return state;
    }
    
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.timer;

import javax.ejb.Timer;

/**
 *
 * @author ignaciogiagante
 */
public interface AlquilerTimerBeanLocal {
    
    public void fromConfirmadoToActivo(Timer timer);
    public void fromActivoToFinalizado(Timer timer);
    public void controlarPublicacionesVencidas(Timer timer);
    public void borrarPedidosRechazadosYCancelados(Timer timer);
    
}

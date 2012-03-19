/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.EstadisticaAdminAlquiler;
import com.alquilacosas.common.EstadisticaAdminPublicacion;
import com.alquilacosas.common.EstadisticaAdminUsuarios;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author wilson
 */
@Local
public interface EstadisticasAdministradorBeanLocal {

     List<EstadisticaAdminUsuarios> getEstadisticaAdminUsuarios();

     List<EstadisticaAdminPublicacion> getEstadisticaAdminPublicaciones();

     List<EstadisticaAdminAlquiler> getEstadisticaAdminAlquiler();
     
}

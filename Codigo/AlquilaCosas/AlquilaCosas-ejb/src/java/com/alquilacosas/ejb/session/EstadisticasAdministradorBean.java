/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.EstadisticaAdminAlquiler;
import com.alquilacosas.common.EstadisticaAdminPublicacion;
import com.alquilacosas.common.EstadisticaAdminUsuarios;
import com.alquilacosas.ejb.entity.Alquiler;
import com.alquilacosas.ejb.entity.AlquilerXEstado;
import com.alquilacosas.ejb.entity.EstadoAlquiler;
import com.alquilacosas.ejb.entity.EstadoUsuario;
import com.alquilacosas.ejb.entity.Publicacion;
import com.alquilacosas.ejb.entity.UsuarioXEstado;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author wilson
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class EstadisticasAdministradorBean implements EstadisticasAdministradorBeanLocal {

     @PersistenceContext(unitName = "AlquilaCosas-ejbPU")
     private EntityManager entityManager;
     
     // Add business logic below. (Right-click in editor and choose
     // "Insert Code > Add Business Method")

     @Override
     public List<EstadisticaAdminUsuarios> getEstadisticaAdminUsuarios() {
          List<EstadisticaAdminUsuarios> listEstadistica = new ArrayList<EstadisticaAdminUsuarios>();

          Query query = entityManager.createNamedQuery("UsuarioXEstado.findAll");
          List<UsuarioXEstado> estados = query.getResultList();

          Calendar fecha = Calendar.getInstance();
          fecha.add(Calendar.MONTH, -12);
          
          for (int i = 1; i < 13; i++) {
               Date Mes = getPrimerDiaDelMes(fecha);
               listEstadistica.add(new EstadisticaAdminUsuarios(Mes, EstadoUsuario.NombreEstadoUsuario.REGISTRADO, 0, 0));
               listEstadistica.add(new EstadisticaAdminUsuarios(Mes, EstadoUsuario.NombreEstadoUsuario.ACTIVO, 0, 0));
               listEstadistica.add(new EstadisticaAdminUsuarios(Mes, EstadoUsuario.NombreEstadoUsuario.SUSPENDIDO, 0, 0));
               fecha.add(Calendar.MONTH, 1);
          }
          
          fecha = Calendar.getInstance();
          fecha.add(Calendar.MONTH, -12);
          for (UsuarioXEstado e : estados) {
               if (e.getFechaDesde().after(fecha.getTime())) {
                    for (EstadisticaAdminUsuarios est : listEstadistica) {
                         Calendar mes = Calendar.getInstance();
                         mes.setTime(est.getMes());
                         if (est.getEstado() == e.getEstadoUsuario().getNombre() && e.getFechaDesde().after(est.getMes()) && e.getFechaDesde().before(getUltimoDiaDelMes(mes))) {
                              est.incrementarCantidad();
                         }
                         if (est.getEstado() == e.getEstadoUsuario().getNombre() && e.getFechaDesde().before(getUltimoDiaDelMes(mes))) {
                              est.incrementarAcumulado();
                         }
                    }
               }
          }
          return listEstadistica;
     }
     
     @Override
     public List<EstadisticaAdminPublicacion> getEstadisticaAdminPublicaciones(){
          List<EstadisticaAdminPublicacion> listEstadistica = new ArrayList<EstadisticaAdminPublicacion>();

          Query query = entityManager.createNamedQuery("Publicacion.findAll");
          List<Publicacion> publicaciones = query.getResultList();

          Calendar fecha = Calendar.getInstance();
          fecha.add(Calendar.MONTH, -12);
          
          for (int i = 1; i < 13; i++) {
               Date Mes = getPrimerDiaDelMes(fecha);
               listEstadistica.add(new EstadisticaAdminPublicacion(Mes, 0, 0));
               fecha.add(Calendar.MONTH, 1);
          }
          
          fecha = Calendar.getInstance();
          fecha.add(Calendar.MONTH, -12);
          for (Publicacion p : publicaciones) {
               if (p.getFechaDesde().after(fecha.getTime())) {
                    for (EstadisticaAdminPublicacion est : listEstadistica) {
                         Calendar mes = Calendar.getInstance();
                         mes.setTime(est.getMes());
                         if (p.getFechaDesde().after(est.getMes()) && p.getFechaDesde().before(getUltimoDiaDelMes(mes)))
                              est.incrementarCantidad();
                         if (p.getFechaDesde().before(getUltimoDiaDelMes(mes)))
                              est.incrementarAcumulado();
                    }
               }
          }
          return listEstadistica;
     }

     public static Date getPrimerDiaDelMes(Calendar dia) {
          dia.set(dia.get(Calendar.YEAR),
                  dia.get(Calendar.MONTH),
                  dia.getActualMinimum(Calendar.DAY_OF_MONTH),
                  dia.getMinimum(Calendar.HOUR_OF_DAY),
                  dia.getMinimum(Calendar.MINUTE),
                  dia.getMinimum(Calendar.SECOND));
          return dia.getTime();
     }

     public static Date getUltimoDiaDelMes(Calendar dia) {
          dia.set(dia.get(Calendar.YEAR),
                  dia.get(Calendar.MONTH),
                  dia.getActualMaximum(Calendar.DAY_OF_MONTH),
                  dia.getMaximum(Calendar.HOUR_OF_DAY),
                  dia.getMaximum(Calendar.MINUTE),
                  dia.getMaximum(Calendar.SECOND));
          return dia.getTime();
     }

     @Override
     public List<EstadisticaAdminAlquiler> getEstadisticaAdminAlquiler() {
          List<EstadisticaAdminAlquiler> listEstadistica = new ArrayList<EstadisticaAdminAlquiler>();

          Query query = entityManager.createNamedQuery("AlquilerXEstado.findAll");
          List<AlquilerXEstado> estados = query.getResultList();

          Calendar fecha = Calendar.getInstance();
          fecha.add(Calendar.MONTH, -12);
          
          for (int i = 1; i < 13; i++) {
               Date Mes = getPrimerDiaDelMes(fecha);
               listEstadistica.add(new EstadisticaAdminAlquiler(Mes, EstadoAlquiler.NombreEstadoAlquiler.PEDIDO, 0, 0));
               listEstadistica.add(new EstadisticaAdminAlquiler(Mes, EstadoAlquiler.NombreEstadoAlquiler.CONFIRMADO, 0, 0));
               listEstadistica.add(new EstadisticaAdminAlquiler(Mes, EstadoAlquiler.NombreEstadoAlquiler.ACTIVO, 0, 0));
               listEstadistica.add(new EstadisticaAdminAlquiler(Mes, EstadoAlquiler.NombreEstadoAlquiler.FINALIZADO, 0, 0));
               fecha.add(Calendar.MONTH, 1);
          }
          
          fecha = Calendar.getInstance();
          fecha.add(Calendar.MONTH, -12);
          for (AlquilerXEstado e : estados) {
               if (e.getFechaDesde().after(fecha.getTime())) {
                    for (EstadisticaAdminAlquiler est : listEstadistica) {
                         Calendar mes = Calendar.getInstance();
                         mes.setTime(est.getMes());
                         if (est.getEstado() == e.getEstadoAlquilerFk().getNombre() && e.getFechaDesde().after(est.getMes()) && e.getFechaDesde().before(getUltimoDiaDelMes(mes))) {
                              est.incrementarCantidad();
                         }
                         if (est.getEstado() == e.getEstadoAlquilerFk().getNombre() && e.getFechaDesde().before(getUltimoDiaDelMes(mes))) {
                              est.incrementarAcumulado();
                         }
                    }
               }
          }
          return listEstadistica;
     }
}

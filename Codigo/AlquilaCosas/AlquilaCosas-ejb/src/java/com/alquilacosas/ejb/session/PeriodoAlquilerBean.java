/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.ejb.entity.Periodo;
import com.alquilacosas.ejb.entity.Periodo.NombrePeriodo;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author wilson
 */
@Stateless
public class PeriodoAlquilerBean implements PeriodoAlquilerBeanLocal {

    @PersistenceContext(unitName = "AlquilaCosas-ejbPU")
    private EntityManager entityManager;

    @Override
    public List<Periodo> getPeriodos() {
        List<Periodo> periodos;
        Query query = entityManager.createNamedQuery("Periodo.findAll");
        periodos = query.getResultList();
        return periodos;
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public void modificarPeriodo(Periodo periodoNuevo) {
        Periodo modifPeriodo = entityManager.find(Periodo.class, periodoNuevo.getPeriodoId());
        modifPeriodo.setNombre(periodoNuevo.getNombre());
        modifPeriodo.setDescripcion(periodoNuevo.getDescripcion());
        modifPeriodo.setHoras(periodoNuevo.getHoras());
        entityManager.merge(modifPeriodo);
    }

    @Override
    public void borrarPeriodo(Periodo periodo) throws AlquilaCosasException {
        Periodo borrarPeriodo = entityManager.find(Periodo.class, periodo.getPeriodoId());
        if (borrarPeriodo.getPrecioList().isEmpty()) {
            entityManager.remove(borrarPeriodo);
        } else {
            throw new AlquilaCosasException("El Periodo tiene Precios Asociados");
        }
    }

    @Override
    public void registrarPeriodo(String nombre, String descripcion, int horas) throws AlquilaCosasException {
        try {
            Periodo periodo = new Periodo();
            periodo.setNombre(NombrePeriodo.valueOf(nombre));
            periodo.setDescripcion(descripcion);
            periodo.setHoras(horas);
            entityManager.persist(periodo);
        } catch (Exception e) {
            throw new AlquilaCosasException("Error al insertar el Periodo - " + e.getMessage());
        }
    }

    @Override
    public Periodo getPeriodo(NombrePeriodo nombrePeriodo) {
        Query query = entityManager.createNamedQuery("Periodo.findByNombre");
        query.setParameter("nombre", nombrePeriodo);
        Periodo periodo = (Periodo) query.getSingleResult();
        return periodo;
    }
}

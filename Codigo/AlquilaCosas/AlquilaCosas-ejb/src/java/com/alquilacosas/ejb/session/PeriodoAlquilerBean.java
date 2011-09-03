/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.dto.PeriodoDTO;
import com.alquilacosas.ejb.entity.Periodo;
import com.alquilacosas.ejb.entity.Periodo.NombrePeriodo;
import com.alquilacosas.facade.PeriodoFacade;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
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
    
    @EJB
    private PeriodoFacade periodoFacade;

    @Override
    public List<PeriodoDTO> getPeriodos() {
        List<PeriodoDTO> periodosDto = new ArrayList<PeriodoDTO>();
        List<Periodo> periodos = periodoFacade.getPeriodos();
        for( Periodo p : periodos ){
            PeriodoDTO periodoDto = new PeriodoDTO(p.getPeriodoId(), p.getNombre());
            periodosDto.add(periodoDto);
        }
        
        return periodosDto;
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

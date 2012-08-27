/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.facade;

import com.alquilacosas.ejb.entity.Alquiler;
import com.alquilacosas.ejb.entity.AlquilerXEstado;
import com.alquilacosas.ejb.entity.EstadoAlquiler;
import com.alquilacosas.ejb.entity.EstadoAlquiler.NombreEstadoAlquiler;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author damiancardozo
 */
@Stateless
public class AlquilerXEstadoFacade extends AbstractFacade<AlquilerXEstado> {

    @PersistenceContext(unitName = "AlquilaCosas-ejbPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public AlquilerXEstadoFacade() {
        super(AlquilerXEstado.class);
    }

    public AlquilerXEstado findByAlquiler(int alquilerId) {
        AlquilerXEstado axe = new AlquilerXEstado();
        Query query = em.createQuery("SELECT axe FROM AlquilerXEstado axe "
                + "WHERE axe.alquilerFk.alquilerId = :alquilerId "
                + "AND axe.fechaHasta IS NULL");
        query.setParameter("alquilerId", alquilerId);
        axe = (AlquilerXEstado) query.getSingleResult();
        return axe;
    }

    public void saveState(Alquiler alquiler, NombreEstadoAlquiler nombreEstadoAlquiler) {
        AlquilerXEstado estadoActual = new AlquilerXEstado();
        estadoActual.setAlquilerFk(alquiler);

        Query query = em.createNamedQuery("EstadoAlquiler.findByNombre");
        query.setParameter("nombre", nombreEstadoAlquiler);
        EstadoAlquiler estado = (EstadoAlquiler) query.getSingleResult();
        estadoActual.setEstadoAlquilerFk(estado);
        estadoActual.setFechaDesde(new Date());

        alquiler.agregarAlquilerXEstado(estadoActual);
        em.persist(estadoActual);
    }

    
    

        class RandomDateGenerator {

        private Date sebelum;
        private Date sesudah;
        private int repetition;

        public Date getSebelum() {
            return sebelum;
        }

        public void setSebelum(Date sebelum) {
            this.sebelum = sebelum;
        }

        public Date getSesudah() {
            return sesudah;
        }

        public void setSesudah(Date sesudah) {
            this.sesudah = sesudah;
        }

        public int getRepetition() {
            return repetition;
        }

        public void setRepetition(int repetition) {
            this.repetition = repetition;
        }

        public RandomDateGenerator() {
        }

        private Date getRandomDateBetween(Date from, Date to) {
            Calendar cal = Calendar.getInstance();

            cal.setTime(from);
            BigDecimal decFrom = new BigDecimal(cal.getTimeInMillis());

            cal.setTime(to);
            BigDecimal decTo = new BigDecimal(cal.getTimeInMillis());

            BigDecimal selisih = decTo.subtract(decFrom);
            BigDecimal factor = selisih.multiply(new BigDecimal(Math.random()));

            return new Date((factor.add(decFrom)).longValue());
        }
    }    
    

    
    private List<Date> createDates() {
        List<Date> dates = new ArrayList<Date>();
        try {
            RandomDateGenerator rdg = new RandomDateGenerator();
            rdg.setSebelum(new SimpleDateFormat("dd MM yyyy").parse("01 06 2011"));
            rdg.setSesudah(new SimpleDateFormat("dd MM yyyy").parse("01 05 2012"));
            rdg.setRepetition(100);

            for (int i = 0; i < 100; i++) {
                dates.add(rdg.getRandomDateBetween(rdg.sebelum, rdg.sesudah));
            }

        } catch (Exception e) {
        }
        return dates;
    }    
    

    
    public void saveStateFurioso(Alquiler alquiler, NombreEstadoAlquiler nombreEstadoAlquiler) {
        AlquilerXEstado estadoActual = new AlquilerXEstado();
        estadoActual.setAlquilerFk(alquiler);

        Query query = em.createNamedQuery("EstadoAlquiler.findByNombre");
        query.setParameter("nombre", nombreEstadoAlquiler);
        EstadoAlquiler estado = (EstadoAlquiler) query.getSingleResult();
        estadoActual.setEstadoAlquilerFk(estado);
        estadoActual.setFechaDesde(createDates().get(new Double(Math.random() * 100).intValue()));

        alquiler.agregarAlquilerXEstado(estadoActual);
        em.persist(estadoActual);
    }    

}

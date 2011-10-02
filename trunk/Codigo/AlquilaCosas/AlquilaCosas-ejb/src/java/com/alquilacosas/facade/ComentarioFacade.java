/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.facade;

import com.alquilacosas.ejb.entity.Comentario;
import com.alquilacosas.ejb.entity.Publicacion;
import com.alquilacosas.ejb.entity.Usuario;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author damiancardozo
 */
@Stateless
public class ComentarioFacade extends AbstractFacade<Comentario> {

    @PersistenceContext(unitName = "AlquilaCosas-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ComentarioFacade() {
        super(Comentario.class);
    }
    
    public List<Comentario> findPreguntasByPublicacion(Publicacion publicacion) {
        Query query = em.createQuery("SELECT c FROM Comentario c WHERE c.publicacionFk = :publicacion AND"
        + " c.pregunta = true ORDER BY c.fecha ASC");
        query.setParameter("publicacion", publicacion);
        return query.getResultList();
    }
    
    public List<Comentario> findPreguntasSinResponderByUsuario(Usuario usuario) {
        Query query = em.createQuery("SELECT c FROM Comentario c, Publicacion p WHERE c.publicacionFk = p "
        + "AND p.usuarioFk = :usuario AND c.pregunta = true AND "
        + "c.respuesta IS NULL ORDER BY c.fecha ASC");
        query.setParameter("usuario", usuario);
        return query.getResultList();
    }
}

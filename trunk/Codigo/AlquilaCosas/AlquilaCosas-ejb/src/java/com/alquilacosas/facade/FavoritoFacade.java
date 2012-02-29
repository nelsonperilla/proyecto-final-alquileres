/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.facade;

import com.alquilacosas.ejb.entity.Favorito;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author ignaciogiagante
 */
@Stateless
public class FavoritoFacade extends AbstractFacade<Favorito> {
    
    @PersistenceContext(unitName = "AlquilaCosas-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public FavoritoFacade(){
        super(Favorito.class);
    }
    
    public List<Favorito> getFavorito( Integer userId, Integer pId){
        
        Query query = em.createNativeQuery(
                    "SELECT * FROM FAVORITO F, PUBLICACION P, USUARIO U "
                  + "WHERE F.PUBLICACION_FK = " + pId + " "
                  + "AND F.USUARIO_FK = " + userId + " "
                  + "GROUP BY F.FAVORITO_ID", Favorito.class);
        
      List<Favorito> favoritoList = query.getResultList();
      return favoritoList;
    }
    
}

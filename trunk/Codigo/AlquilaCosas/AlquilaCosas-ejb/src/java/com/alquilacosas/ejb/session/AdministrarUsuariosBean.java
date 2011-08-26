/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.dto.UsuarioDTO;
import com.alquilacosas.ejb.entity.Login;
import com.alquilacosas.ejb.entity.Rol;
import com.alquilacosas.ejb.entity.Usuario;
import java.util.ArrayList;
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
 * @author damiancardozo
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class AdministrarUsuariosBean implements AdministrarUsuariosBeanLocal {

    @PersistenceContext(unitName = "AlquilaCosas-ejbPU")
    private EntityManager entityManager;

    @Override
    public List<UsuarioDTO> getUsuariosList() {
        
        List<UsuarioDTO> usuarios = new ArrayList<UsuarioDTO>();
        
        Query query = entityManager.createNativeQuery("select u.usuario_id, l.username, "
                + "u.email, u.nombre, u.apellido, uxe.fecha_desde, COUNT(r.rol_id) as NumRoles, r.nombre "
       + " from rol r, login_x_rol lxr, login l, usuario u, usuario_x_estado uxe, estado_usuario eu "
       + " where lxr.rol_fk = r.rol_id "
       + " and lxr.login_fk = l.login_id " 
       + " and l.usuario_fk = u.usuario_id "
       + " and u.usuario_id = uxe.usuario_fk "
       + " and uxe.estado_fk = eu.estado_usuario_id "
       + " and fecha_hasta IS NULL "
       + " group by u.usuario_id");
        
        List<Object[]> list = query.getResultList();
        
        for( Object[] object : list){
            
            Integer id = (Integer) object[0];
            String username = (String) object[1];
            String email = (String) object[2];
            String nombre = (String) object[3];
            String apellido = (String) object[4];
            Date fechaDeRegistro = (Date) object[5];
            Long numRoles = (Long) object[6];
            String tipoUsuario = (String) object[7];

            UsuarioDTO uf = new UsuarioDTO( id, username, email, nombre, 
                    apellido, fechaDeRegistro, numRoles, tipoUsuario); 
            
           usuarios.add(uf);     
        }
        
        return usuarios;
    }

    @Override
    public void setRoles(UsuarioDTO usuarioFacade, List<Integer> rolesVista) {
       
       Usuario usuario = entityManager.find(Usuario.class, usuarioFacade.getId());
        
       Query loginQuery = entityManager.createNamedQuery("Login.findByUsuarioFk");
       loginQuery.setParameter("usuarioFk", usuario);
       Login login = (Login) loginQuery.getSingleResult();
       List<Rol> rols = login.getRolList();
       List<Integer> rolIds = new ArrayList<Integer>();
       for(Rol r: rols) {
           rolIds.add(r.getRolId());
       }
       
       for(Integer i: rolesVista) {
           if(!rolIds.contains(i)) {
               Rol r = entityManager.find(Rol.class, i);
               rols.add(r);               
           }
       }
       for(Integer i: rolIds) {
           if(!rolesVista.contains(i)) {
               for(int j = 0; j < rols.size(); j++) {
                   Rol r = rols.get(j);
                   if(r.getRolId() == i) {
                       rols.remove(r);
                       break;
                   }
               }
           }
       }
       login.setRolList(rols);
       entityManager.merge(login); 
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.dto.UsuarioDTO;
import com.alquilacosas.ejb.entity.Login;
import com.alquilacosas.ejb.entity.Rol;
import com.alquilacosas.ejb.entity.Usuario;
import com.alquilacosas.facade.LoginFacade;
import com.alquilacosas.facade.RolFacade;
import com.alquilacosas.facade.UsuarioFacade;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
    
    @EJB
    private UsuarioFacade usuarioFacade;
    @EJB
    private LoginFacade loginFacade;
    @EJB
    private RolFacade rolFacade;

    @Override
    public List<UsuarioDTO> getUsuariosList() {
        
        List<UsuarioDTO> usuarios = new ArrayList<UsuarioDTO>();
        
        List<Usuario> lista = usuarioFacade.findAll();
        for( Usuario u : lista ){
            List<Rol> roles = u.getLoginList().get(0).getRolList();
            UsuarioDTO usuarioDTO = new UsuarioDTO(u.getUsuarioId(), u.getLoginList().get(0).getUsername(),
                    u.getEmail(), u.getNombre(), u.getApellido(), u.getLoginList().get(0).getFechaCreacion(), roles);
            usuarios.add(usuarioDTO);
        }
        
        return usuarios;
    }

    @Override
    public void setRoles(UsuarioDTO usuarioDTO, List<Integer> rolesVista) {
       
       Usuario usuario = usuarioFacade.find(usuarioDTO.getId());
       
       Login login = loginFacade.findByUsuario(usuario);
       List<Rol> rols = login.getRolList();
       List<Integer> rolIds = new ArrayList<Integer>();
       
       for(Rol r: rols) {
           rolIds.add(r.getRolId());
       }
       
       for(Integer i: rolesVista) {
           if(!rolIds.contains(i)) {
               Rol r = rolFacade.find(i);
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
       loginFacade.edit(login); 
    }
}

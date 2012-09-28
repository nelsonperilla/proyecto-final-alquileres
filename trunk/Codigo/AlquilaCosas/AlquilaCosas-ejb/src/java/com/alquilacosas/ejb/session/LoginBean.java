/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.common.SeguridadException;
import com.alquilacosas.common.UsuarioLogueado;
import com.alquilacosas.ejb.entity.*;
import com.alquilacosas.ejb.entity.EstadoUsuario.NombreEstadoUsuario;
import com.alquilacosas.facade.*;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author damiancardozo
 */
@Stateless
public class LoginBean implements LoginBeanLocal {

    @EJB
    private LoginFacade loginFacade;
    @EJB
    private UsuarioFacade usuarioFacade;
    @EJB
    private UsuarioXEstadoFacade uxeFacade;
    @EJB
    private EstadoUsuarioFacade estadoUsuarioFacade;
    @EJB
    private ImagenUsuarioFacade imagenFacade;

    @Override
    public boolean activarCuenta(String username, String codigo) throws AlquilaCosasException {

        Login login = loginFacade.findByUsername(username);

        if (login == null) {
            throw new AlquilaCosasException("Nombre de usuario inexistente.");
        }

        String codigoDB = login.getCodigoActivacion();

        if (codigo.equals(codigoDB)) {

            Usuario usuario = login.getUsuarioFk();

            UsuarioXEstado ultimoUxe = uxeFacade.findCurrent(usuario.getUsuarioId());
            if (ultimoUxe == null || ultimoUxe.getEstadoUsuario().getNombre()
                    != NombreEstadoUsuario.REGISTRADO) {
                throw new AlquilaCosasException("El estado del usuario no requiere activación.");
            }

            EstadoUsuario estado = estadoUsuarioFacade.findByNombre(NombreEstadoUsuario.ACTIVO);
            if (estado == null) {
                throw new AlquilaCosasException("No se encontro el estado "
                        + NombreEstadoUsuario.ACTIVO.toString() + " en la base de datos.");
            }

            ultimoUxe.setFechaHasta(new Date());

            UsuarioXEstado nuevoUxE = new UsuarioXEstado(usuario, estado);
            nuevoUxE.setFechaDesde(new Date());
            usuario.agregarUsuarioXEstado(nuevoUxE);
            usuarioFacade.edit(usuario);
            return true;

        } else {
            return false;
        }
    }

    @Override
    public UsuarioLogueado login(String username, String password) throws SeguridadException {
        Login login = loginFacade.login(username, password);
        if (login == null) {
            throw new SeguridadException("Credenciales incorrectas.");
        } else {
            Usuario usuario = login.getUsuarioFk();
            NombreEstadoUsuario nombreEstado = usuario.getEstadoVigente().getEstadoUsuario().getNombre();
            if(nombreEstado == NombreEstadoUsuario.REGISTRADO) {
                throw new SeguridadException("La cuenta no está activa");
            } else if(nombreEstado == NombreEstadoUsuario.SUSPENDIDO) {
                throw new SeguridadException("La cuenta se encuentra suspendida");
            }
            String ciudad = null;
            boolean dir = false;
            if (!usuario.getDomicilioList().isEmpty()) {
                ciudad = usuario.getDomicilioList().get(0).getCiudad();
                dir = true;
            }

            List<Rol.NombreRol> roles = new ArrayList<Rol.NombreRol>();
            for (Rol r : login.getRolList()) {
                roles.add(r.getNombre());
            }

            ImagenUsuario iu = null;
            if (!usuario.getImagenUsuarioList().isEmpty()) {
                iu = usuario.getImagenUsuarioList().get(0);
            }

            UsuarioLogueado user = new UsuarioLogueado(usuario.getUsuarioId(), usuario.getNombre(),
                    usuario.getApellido(), ciudad, iu, roles, usuario.getFacebookId());
            user.setDireccionRegistrada(dir);

            return user;
        }
    }

    @Override
    public UsuarioLogueado facebookLogin(String email, String facebookId) throws SeguridadException {
        Login login = loginFacade.findByEmail(email);
        if (login == null) {
            throw new SeguridadException("Usuario no registrado.");
        } else {
            Usuario usuario = login.getUsuarioFk();
            NombreEstadoUsuario nombreEstado = usuario.getEstadoVigente().getEstadoUsuario().getNombre();
            if(nombreEstado == NombreEstadoUsuario.REGISTRADO) {
                throw new SeguridadException("La cuenta no está activa");
            } else if(nombreEstado == NombreEstadoUsuario.SUSPENDIDO) {
                throw new SeguridadException("La cuenta se encuentra suspendida");
            }
            if (usuario.getFacebookId() == null || usuario.getFacebookId().equals("")) {
                usuario.setFacebookId(facebookId);
                usuarioFacade.edit(usuario);
            }
            String ciudad = null;
            boolean dir = false;
            if (!usuario.getDomicilioList().isEmpty()) {
                ciudad = usuario.getDomicilioList().get(0).getCiudad();
                dir = true;
            }
            List<Rol.NombreRol> roles = new ArrayList<Rol.NombreRol>();
            for (Rol r : login.getRolList()) {
                roles.add(r.getNombre());
            }
            ImagenUsuario iu = null;
            if (!usuario.getImagenUsuarioList().isEmpty()) {
                iu = usuario.getImagenUsuarioList().get(0);
            }
            UsuarioLogueado user = new UsuarioLogueado(usuario.getUsuarioId(),
                    usuario.getNombre(), usuario.getApellido(), ciudad, iu, roles,
                    usuario.getFacebookId());
            user.setDireccionRegistrada(dir);
            return user;
        }
    }

    @Override
    public Integer loginUsuario(String username) throws AlquilaCosasException {

        Login login = loginFacade.findByUsername(username);
        if (login == null) {
            throw new AlquilaCosasException("Nombre de usuario inexistente");
        }
        login.setFechaUltimoIngreso(new Date());
        loginFacade.edit(login);
        Usuario usuario = login.getUsuarioFk();
        if (usuario == null) {
            throw new AlquilaCosasException("Usuario no asociado a la cuenta.");
        }

        return usuario.getUsuarioId();
    }

    @Override
    public void cambiarPassword(int usuarioId, String password, String passwordNuevo) throws AlquilaCosasException {
        Login login = loginFacade.findByUsuarioId(usuarioId);
        if (!login.getPassword().equals(password)) {
            throw new AlquilaCosasException("La contraseña no coincide.");
        }

        login.setPassword(passwordNuevo);
        loginFacade.edit(login);
    }

    @Override
    public byte[] leerImagen(int id) {
        ImagenUsuario imagen = imagenFacade.find(id);
        byte[] img = null;
        if (imagen != null) {
            img = imagen.getImagen();
        }
        return img;
    }

    @Override
    public boolean usarImagenLocal(Integer usuarioId) {
        Usuario usuario = usuarioFacade.find(usuarioId);
        boolean usar = false;
        if (!usuario.getImagenUsuarioList().isEmpty()) {
            ImagenUsuario iu = usuario.getImagenUsuarioList().get(0);
            if (iu != null) {
                usar = iu.getUsar();
            }
        }
        return usar;
    }
}

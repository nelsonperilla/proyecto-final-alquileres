/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.*;
import com.alquilacosas.ejb.entity.*;
import com.alquilacosas.facade.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
    @EJB
    private RolFacade rolFacade;
    @EJB
    private EstadoUsuarioFacade estadoUsuarioFacade;
    @EJB
    private UsuarioFacade usuarioFacade;
    @EJB
    private CategoriaFacade categoriaFacade;
    @EJB
    private EstadoPublicacionFacade estadoPublicacionFacade;
    @EJB
    private PeriodoFacade periodoFacade;
    @EJB
    private PublicacionFacade publicacionFacade;
    @EJB
    private AlquilerXEstadoFacade estadoAlquiler;
    @EJB
    private AlquilerFacade alquilerFacade;
    @EJB
    private CategoriaBeanLocal categoriaEjb;
    @EJB 
    private DomicilioFacade domicilioFacade;

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
    public List<EstadisticaAdminPublicacion> getEstadisticaAdminPublicaciones() {
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
                    if (p.getFechaDesde().after(est.getMes()) && p.getFechaDesde().before(getUltimoDiaDelMes(mes))) {
                        est.incrementarCantidad();
                    }
                    if (p.getFechaDesde().before(getUltimoDiaDelMes(mes))) {
                        est.incrementarAcumulado();
                    }
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
                    if (est.getEstado() == e.getEstadoAlquilerFk().getNombre() 
                            && e.getFechaDesde().after(est.getMes()) 
                            && e.getFechaDesde().before(getUltimoDiaDelMes(mes))) {
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

    @Override
    public List<EstadisticaAdminCategoria> getEstadisticaAdminCategoria(String anioMes) {
        List<EstadisticaAdminCategoria> listEstadistica = new ArrayList<EstadisticaAdminCategoria>();

        int anio = Integer.parseInt(anioMes.substring(0, 4));
        int mes = Integer.parseInt(anioMes.substring(5)) - 1;
        Calendar calDesde = Calendar.getInstance();
        calDesde.clear();
        calDesde.set(anio, mes, 1, 0, 0, 0);
        Date fechaDesde = calDesde.getTime();
        Calendar calHasta = Calendar.getInstance();
        calHasta.clear();
        calHasta.set(anio, mes, 1, 0, 0, 0);
        calHasta.add(Calendar.MONTH, 1);
        Date fechaHasta = calHasta.getTime();

        Query query = entityManager.createNamedQuery("Publicacion.findAll");
        List<Publicacion> publicaciones = query.getResultList();

        for (Publicacion p : publicaciones) {
            if (p.getFechaDesde().before(fechaHasta) && p.getFechaDesde().after(fechaDesde)) {
                boolean inserta = true;
                for (EstadisticaAdminCategoria e : listEstadistica) {
                    if (e.getNombre().equals(p.getCategoriaFk().getNombre())) {
                        e.aumentarCantidad();
                        inserta = false;
                    }
                }
                if (inserta) {
                    listEstadistica.add(new EstadisticaAdminCategoria(p.getCategoriaFk().getNombre(), 1));
                }
            }
        }

        return listEstadistica;
    }

    /**
     * El siguiente método es un script para crear usuarios con estado ACTIVO,
     * REGISTRADO y SUSPENDIDO
     */
    @Override
    public void crearUsuarios() {

        List<Date> dates = createDates();

        // se crearan 5 usuarios ACTIVOS
        for (int i = 0; i < 100; i++) {
            //Se crea un usuario
            Usuario usuario = new Usuario();
            usuario.setNombre("Juan" + i);
            usuario.setApellido("Ortega" + i);
            usuario.setEmail("juanortega" + i + "@gmail.com");

            Date fecha = dates.get(new Double(Math.random() * 100).intValue());

            //Se crea un estado ACTIVO y un USUARIOXESTADO
            EstadoUsuario estado = estadoUsuarioFacade.findByNombre(EstadoUsuario.NombreEstadoUsuario.ACTIVO);
            UsuarioXEstado uxe = new UsuarioXEstado(usuario, estado);
            uxe.setFechaDesde(fecha);
            usuario.agregarUsuarioXEstado(uxe);

            //Se crea un Login para el usuario creado con su respectivo estado
            Login login = new Login();
            login.setUsername("juancito");
            login.setPassword("juancito");
            login.setUsuarioFk(usuario);
            login.setCodigoActivacion("sarasa");
            login.setFechaCreacion(fecha);
            login.setFechaUltimoIngreso(fecha);

            Rol rol = rolFacade.findByNombre(Rol.NombreRol.USUARIO);

            login.agregarRol(rol);
            usuario.agregarLogin(login);

            entityManager.persist(usuario);
        }

        // se crearan 3 usuarios REGISTRADOS
        for (int i = 0; i < 30; i++) {
            //Se crea un usuario
            Usuario usuario = new Usuario();
            usuario.setNombre("Miguel" + i);
            usuario.setApellido("Jose" + i);
            usuario.setEmail("migueljose" + i + "@gmail.com");

            Date fecha = dates.get(new Double(Math.random() * 100).intValue());

            //Se crea un estado ACTIVO y un USUARIOXESTADO
            EstadoUsuario estado = estadoUsuarioFacade.findByNombre(EstadoUsuario.NombreEstadoUsuario.REGISTRADO);
            UsuarioXEstado uxe = new UsuarioXEstado(usuario, estado);
            uxe.setFechaDesde(fecha);
            usuario.agregarUsuarioXEstado(uxe);

            //Se crea un Login para el usuario creado con su respectivo estado
            Login login = new Login();
            login.setUsername("miguel");
            login.setPassword("miguel");
            login.setUsuarioFk(usuario);
            login.setCodigoActivacion("sarasa");
            login.setFechaCreacion(fecha);
            login.setFechaUltimoIngreso(fecha);

            Rol rol = rolFacade.findByNombre(Rol.NombreRol.USUARIO);

            login.agregarRol(rol);
            usuario.agregarLogin(login);

            entityManager.persist(usuario);
        }

        // se crearan 2 usuarios SUSPENDIDOS
        for (int i = 0; i < 10; i++) {
            //Se crea un usuario
            Usuario usuario = new Usuario();
            usuario.setNombre("omar" + i);
            usuario.setApellido("santamaria" + i);
            usuario.setEmail("omarsantamaria" + i + "@gmail.com");

            Date fecha = dates.get(new Double(Math.random() * 100).intValue());

            //Se crea un estado ACTIVO y un USUARIOXESTADO
            EstadoUsuario estado = estadoUsuarioFacade.findByNombre(EstadoUsuario.NombreEstadoUsuario.SUSPENDIDO);
            UsuarioXEstado uxe = new UsuarioXEstado(usuario, estado);
            uxe.setFechaDesde(fecha);
            usuario.agregarUsuarioXEstado(uxe);

            //Se crea un Login para el usuario creado con su respectivo estado
            Login login = new Login();
            login.setUsername("omar");
            login.setPassword("santamaria");
            login.setUsuarioFk(usuario);
            login.setCodigoActivacion("sarasa");
            login.setFechaCreacion(fecha);
            login.setFechaUltimoIngreso(fecha);

            Rol rol = rolFacade.findByNombre(Rol.NombreRol.USUARIO);

            login.agregarRol(rol);
            usuario.agregarLogin(login);

            entityManager.persist(usuario);
        }

    }

    @Override
    public void crearPublicaciones() throws AlquilaCosasException {

        List<Date> dates = createDates();

        //Se crearan 200 publicaciones entre el periodo 01/06/2011 y 01/05/2012
        for (int i = 0; i < 100; i++) {

            Date fecha = dates.get(new Double(Math.random() * 100).intValue());

            Publicacion publicacion = new Publicacion();
            publicacion.setTitulo("publicacion" + i);
            publicacion.setCantidad(new Double(Math.random() * 10).intValue());
            publicacion.setCategoriaFk(null);
            publicacion.setDescripcion("Una vez venia pepito volando en bicicleta...");
            publicacion.setFechaDesde(fecha);
            publicacion.setDestacada(false);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(fecha);
            calendar.add(Calendar.MONTH, 2);
            publicacion.setFechaHasta(calendar.getTime());

            Integer categoriaId = new Double(Math.random() * 29).intValue();
            //Se trae la categoria random a la cual pertenecera la publicacion
            try {
                Categoria c = categoriaFacade.find(categoriaId);
                publicacion.setCategoriaFk(c);
            } catch (NoResultException e) {
                throw new AlquilaCosasException("No se encontro la Categoria en la "
                        + "base de datos.");
            }

            //Se trae un usuario random de la base de datos
            Usuario usuario = null;
            Integer usuarioId = new Double(Math.random() * 10).intValue(); // se debe cambiar por un rango mayor
            try {
                usuario = usuarioFacade.find(usuarioId);
            } catch (NoResultException e) {
                throw new AlquilaCosasException("No se encontro el Usuario en la "
                        + "base de datos.");
            }

            // Fijar estado de publicacion
            EstadoPublicacion estadoPublicacion = null;
            try {
                estadoPublicacion = estadoPublicacionFacade.findByNombre(EstadoPublicacion.NombreEstadoPublicacion.ACTIVA);
            } catch (NoResultException e) {
                throw new AlquilaCosasException("No se encontro el estado de la publicacion"
                        + " en la base de datos.");
            }
            PublicacionXEstado pxe = new PublicacionXEstado(publicacion, estadoPublicacion);
            publicacion.agregarPublicacionXEstado(pxe);

            //Crear periodos de alquiler minimo de un DIA y máximo igual A NULL
            Periodo periodoMin = periodoFacade.findByNombre(Periodo.NombrePeriodo.DIA);
            publicacion.setMinPeriodoAlquilerFk(periodoMin);

            //Se crea un precio para el período mínimo de un DIA
            Precio precio = new Precio(33.3, periodoMin);
            publicacion.agregarPrecio(precio);

            //Se agrega la publicacion creada al usuario
            usuario.agregarPublicacion(publicacion);
            publicacionFacade.create(publicacion);
        }
    }

    @Override
    public void crearAlquileres() {
        
        //Se crearan 200 alquileres en estado PEDIDO entre el periodo 01/06/2011 y 01/05/2012
        for (int i = 0; i < 50; i++) {
            crearAlquileresPorPeriodo(EstadoAlquiler.NombreEstadoAlquiler.PEDIDO);
        }
        //Se crearan 200 alquileres en estado PEDIDO entre el periodo 01/06/2011 y 01/05/2012
        for (int i = 0; i < 100; i++) {
            crearAlquileresPorPeriodo(EstadoAlquiler.NombreEstadoAlquiler.CONFIRMADO);
        }
        //Se crearan 200 alquileres en estado PEDIDO entre el periodo 01/06/2011 y 01/05/2012
        for (int i = 0; i < 80; i++) {
            crearAlquileresPorPeriodo(EstadoAlquiler.NombreEstadoAlquiler.ACTIVO);
        }
        //Se crearan 200 alquileres en estado PEDIDO entre el periodo 01/06/2011 y 01/05/2012
        for (int i = 0; i < 150; i++) {
            crearAlquileresPorPeriodo(EstadoAlquiler.NombreEstadoAlquiler.FINALIZADO);
        }
        
    
    }
    
    /**
     * Crea alquileres con un estado específico
     * @param estado 
     */
    private void crearAlquileresPorPeriodo(EstadoAlquiler.NombreEstadoAlquiler estado) {

        List<Date> dates = createDates();

        Date fecha = dates.get(new Double(Math.random() * 100).intValue());

        Alquiler alquiler = new Alquiler();
        Publicacion publicacion = publicacionFacade.find(new Double(Math.random() * 10).intValue() + 30);
        alquiler.setPublicacionFk(publicacion);
        alquiler.setUsuarioFk(usuarioFacade.find(new Double(Math.random() * 10).intValue() + 1));
        alquiler.setFechaInicio(fecha);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        calendar.add(Calendar.DAY_OF_MONTH, new Double(Math.random() * 10).intValue());

        alquiler.setFechaFin(calendar.getTime());
        alquiler.setMonto(100.0);
        alquiler.setCantidad(new Double(Math.random() * 6).intValue());

        alquilerFacade.create(alquiler);

        estadoAlquiler.saveStateFurioso(alquiler, estado);

    }
    
    @Override
    public void crearDomicilios(){
        Provincia prov = entityManager.find(Provincia.class, 28);
        for(int i = 9; i < 150; i++){
            Usuario usuario = entityManager.find(Usuario.class, i);
            Domicilio domicilio = new Domicilio("felix" + i, 33, 1, "A", "Jardín", "Córdoba", prov);
            domicilio.setUsuarioFk(usuario);
            domicilioFacade.create(domicilio);
        }
    }

    /**
     * Devuelve un array de fechas entre el periodo 01/06/2011 y 01/05/2012
     * @return 
     */
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
}

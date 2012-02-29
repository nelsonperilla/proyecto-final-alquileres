/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.common.Busqueda;
import com.alquilacosas.dto.CategoriaDTO;
import com.alquilacosas.dto.PeriodoDTO;
import com.alquilacosas.dto.PrecioDTO;
import com.alquilacosas.dto.PublicacionDTO;
import com.alquilacosas.ejb.entity.Categoria;
import com.alquilacosas.ejb.entity.Domicilio;
import com.alquilacosas.ejb.entity.ImagenPublicacion;
import com.alquilacosas.ejb.entity.Periodo;
import com.alquilacosas.ejb.entity.Periodo.NombrePeriodo;
import com.alquilacosas.ejb.entity.Precio;
import com.alquilacosas.ejb.entity.Publicacion;
import com.alquilacosas.facade.CategoriaFacade;
import com.alquilacosas.facade.ImagenPublicacionFacade;
import com.alquilacosas.facade.PeriodoFacade;
import com.alquilacosas.facade.PublicacionFacade;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author damiancardozo
 */
@Stateless
public class BuscarPublicacionBean implements BuscarPublicacionBeanLocal {

    @EJB
    private PrecioBeanLocal precioBean;
    @EJB
    private PublicacionFacade publicacionFacade;
    @EJB
    private CategoriaFacade categoriaFacade;
    @EJB
    private ImagenPublicacionFacade imagenFacade;
    @EJB
    private PeriodoFacade periodoFacade;

    @Override
    @PermitAll
    public Busqueda buscar(String palabra, List<Integer> categoriaIds,
            String ubicacion, NombrePeriodo nombrePeriodo, Double precioDesde, Double precioHasta,
            int registros, int desde) {
        List<Publicacion> publicaciones = publicacionFacade.findPublicaciones(palabra,
                categoriaIds, ubicacion, nombrePeriodo, precioDesde, precioHasta, registros, desde);
        List<PublicacionDTO> pubFacadeList = new ArrayList<PublicacionDTO>();
        List<Categoria> categorias = new ArrayList<Categoria>();
        double precioMin = 1000000;
        double precioMax = 0;
        for (Publicacion p : publicaciones) {

            PublicacionDTO facade = crearPublicacionFacade(p);
            pubFacadeList.add(facade);

            // obtener precio diario
            double pre = 0;
            for (Precio precio : p.getPrecioList()) {
                if (precio.getPeriodoFk().getNombre() == Periodo.NombrePeriodo.DIA) {
                    pre = precio.getPrecio();
                    break;
                }
            }
            if (pre > precioMax) {
                precioMax = pre;
            }
            if (pre < precioMin) {
                precioMin = pre;
            }
            // agregar categoria a la lista si no esta
            Categoria c = p.getCategoriaFk();
            if (!categorias.contains(c)) {
                categorias.add(c);
            }
        }

        TreeMap<Integer, CategoriaDTO> categoriaTree = new TreeMap<Integer, CategoriaDTO>();
        for (Categoria categoria : categorias) {
            if (categoriaTree.containsKey(categoria.getCategoriaId())) {
                continue;
            } else {
                int nivel = categoriaFacade.getNivel(categoria);
                if (categoria.getCategoriaFk() != null && categoriaTree.containsKey(categoria.getCategoriaFk().getCategoriaId())) {
                    CategoriaDTO cat = new CategoriaDTO(categoria.getCategoriaId(), categoria.getNombre());
                    cat.setNivel(nivel);
                    CategoriaDTO padre = categoriaTree.get(categoria.getCategoriaFk().getCategoriaId());
                    padre.getSubcategorias().add(cat);
                } else {
                    CategoriaDTO dto = new CategoriaDTO(categoria.getCategoriaId(), categoria.getNombre());
                    dto.setNivel(nivel);
                    CategoriaDTO aux1 = dto;
                    Categoria c = categoria;
                    int nivelAux = nivel;
                    while (c.getCategoriaFk() != null) {
                        nivelAux--;
                        CategoriaDTO aux2 = new CategoriaDTO(c.getCategoriaFk().getCategoriaId(), c.getCategoriaFk().getNombre());
                        aux2.setNivel(nivelAux);
                        aux1.setPadre(aux2);
                        c = c.getCategoriaFk();
                        aux1 = aux2;
                        categoriaTree.put(aux1.getId(), aux1);
                    }
                    categoriaTree.put(dto.getId(), dto);
                }
            }
        }
        // armar lista de categorias padre (nivel 1)
        List<CategoriaDTO> catFacade = new ArrayList<CategoriaDTO>();
        for(CategoriaDTO cat: categoriaTree.values()) {
            if(cat.getNivel() == 1) {
                catFacade.add(cat);
            }
        }
        
        Busqueda busqueda = new Busqueda(pubFacadeList, catFacade, precioMin, precioMax);
        if (desde == 0) {
            Long totalRegistros = publicacionFacade.countBusquedaPublicaciones(palabra,
                    categoriaIds, ubicacion, nombrePeriodo, precioDesde, precioHasta);
            if (totalRegistros != null) {
                busqueda.setTotalRegistros(totalRegistros.intValue());
            }
        }
        return busqueda;
    }

    /*
     * Busca publicaciones por categoria
     */
    @Override
    public Busqueda buscarPublicacionesPorCategoria(int categoriaId, int registros, int desde)
            throws AlquilaCosasException {

        List<Integer> listaCategorias = new ArrayList<Integer>();
        Categoria cat = categoriaFacade.find(categoriaId);
        if (cat == null) {
            throw new AlquilaCosasException("Categoria inexistente");
        }
        listaCategorias.add(cat.getCategoriaId());
        List<Categoria> sublista = cat.getCategoriaList();
        List<Categoria> sublista2 = new ArrayList<Categoria>();
        for (Categoria c : sublista) {
            sublista2.add(c);
            listaCategorias.add(c.getCategoriaId());
        }
        List<Categoria> sublista3 = new ArrayList<Categoria>();
        for (Categoria c : sublista2) {
            sublista.add(c);
            listaCategorias.add(c.getCategoriaId());
        }
        for (Categoria c : sublista3) {
            listaCategorias.add(c.getCategoriaId());
        }


        List<Publicacion> publicaciones = publicacionFacade.findByCategoria(listaCategorias, registros, desde);
        List<PublicacionDTO> pubFacadeList = new ArrayList<PublicacionDTO>();
        double precioMin = 1000000;
        double precioMax = 0;
        for (Publicacion p : publicaciones) {
            PublicacionDTO facade = crearPublicacionFacade(p);
            pubFacadeList.add(facade);
            // obtener precio diario
            double pre = 0;
            for (Precio precio : p.getPrecioList()) {
                if (precio.getPeriodoFk().getNombre() == Periodo.NombrePeriodo.DIA) {
                    pre = precio.getPrecio();
                    break;
                }
            }
            if (pre > precioMax) {
                precioMax = pre;
            }
            if (pre < precioMin) {
                precioMin = pre;
            }
        }

        List<CategoriaDTO> categorias = new ArrayList<CategoriaDTO>();
        Categoria categoria = categoriaFacade.find(categoriaId);
        categorias.add(new CategoriaDTO(categoriaId, categoria.getNombre()));

        Busqueda busqueda = new Busqueda(pubFacadeList, categorias, precioMin, precioMax);

        if (desde == 0) {
            Long totalRegistros = publicacionFacade.countByCategoria(listaCategorias);
            if (totalRegistros != null) {
                busqueda.setTotalRegistros(totalRegistros.intValue());
            }
        }

        return busqueda;
    }

    @Override
    public byte[] leerImagen(int id) {
        ImagenPublicacion imagen = imagenFacade.find(id);
        byte[] img = null;
        if (imagen != null) {
            img = imagen.getImagen();
        }
        return img;
    }

    private PublicacionDTO crearPublicacionFacade(Publicacion publicacion) {
        PublicacionDTO facade = new PublicacionDTO(publicacion.getPublicacionId(),
                publicacion.getTitulo(), publicacion.getDescripcion(), publicacion.getFechaDesde(),
                publicacion.getFechaHasta(), publicacion.getDestacada(),
                publicacion.getCantidad());
        List<Integer> imagenes = new ArrayList<Integer>();
        for (ImagenPublicacion ip : publicacion.getImagenPublicacionList()) {
            imagenes.add(ip.getImagenPublicacionId());
        }
        facade.setImagenIds(imagenes);
        Domicilio d = publicacion.getUsuarioFk().getDomicilioList().get(0);
        facade.setProvincia(d.getProvinciaFk().getNombre());
        facade.setCiudad(d.getCiudad());
        List<PrecioDTO> precios = precioBean.getPrecios(publicacion);
        facade.setPrecios(precios);

        return facade;
    }

    @Override
    public List<PeriodoDTO> getPeriodos() {
        List<Periodo> periodos = periodoFacade.findAll();
        List<PeriodoDTO> periodosDto = new ArrayList<PeriodoDTO>();
        for (Periodo p : periodos) {
            periodosDto.add(new PeriodoDTO(p.getPeriodoId(), p.getNombre()));
        }
        return periodosDto;
    }
}

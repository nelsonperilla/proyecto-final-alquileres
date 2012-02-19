/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alquilacosas.ejb.session;

import com.alquilacosas.common.AlquilaCosasException;
import com.alquilacosas.common.Busqueda;
import com.alquilacosas.dto.PeriodoDTO;
import com.alquilacosas.ejb.entity.Periodo.NombrePeriodo;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author damiancardozo
 */
@Local
public interface BuscarPublicacionBeanLocal {

    Busqueda buscar(String palabra, Integer categoriaId,
            String ubicacion, NombrePeriodo nombrePeriodo, Double precioDesde, Double precioHasta,
            int registros, int desde);
    
    Busqueda buscarPublicacionesPorCategoria(int categoriaId, int registros, int desde) 
            throws AlquilaCosasException;
    
    byte[] leerImagen(int id);
    
    List<PeriodoDTO> getPeriodos();

}

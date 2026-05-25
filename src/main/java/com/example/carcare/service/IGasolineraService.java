package com.example.carcare.service;

import com.example.carcare.dto.GasolineraDto;
import com.example.carcare.dto.LocalidadDto;

import java.util.List;

public interface IGasolineraService {

    /** Gasolineras de un municipio concreto (código INE 5 dígitos, ej: "03065") */
    List<GasolineraDto> findByMunicipio(String codigoMunicipio);

    /** Gasolineras de una provincia completa (código 2 dígitos, ej: "03") */
    List<GasolineraDto> findByProvincia(String codigoProvincia);

    /**
     * Gasolineras de una localidad por nombre.
     * Si se proporciona codigoProvincia se reutiliza la caché de provincia (rápido);
     * si no, se busca sobre todas las estaciones (lento pero funcional).
     */
    List<GasolineraDto> findByLocalidad(String nombreLocalidad, String codigoProvincia);

    /**
     * Lista de municipios con código INE de una provincia.
     * Útil para poblar un Spinner en Android antes de llamar a findByMunicipio.
     */
    List<LocalidadDto> findLocalidades(String codigoProvincia);

    /** Todas las gasolineras de España (~12 000 registros, usar con precaución) */
    List<GasolineraDto> findAll();
}

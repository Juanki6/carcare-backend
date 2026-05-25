package com.example.carcare.service;

import com.example.carcare.dto.GasolineraApiResponse;
import com.example.carcare.dto.GasolineraDto;
import com.example.carcare.dto.LocalidadDto;
import com.example.carcare.dto.MunicipioApiItem;
import com.example.carcare.exception.ErrorException;
import com.example.carcare.mapper.GasolineraMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class GasolineraService implements IGasolineraService {

    private static final String BASE_ESTACIONES =
            "https://sedeaplicaciones.minetur.gob.es/ServiciosRESTCarburantes/PreciosCarburantes/EstacionesTerrestres/";

    // Endpoint oficial de municipios por provincia (ruta actualizada: Listados en lugar de Filtros).
    // Si devuelve error, findLocalidades recae automáticamente en el fallback
    // que extrae los municipios de los datos de estaciones (ya cacheados).
    private static final String BASE_MUNICIPIOS =
            "https://sedeaplicaciones.minetur.gob.es/ServiciosRESTCarburantes/PreciosCarburantes/Listados/MunicipiosPorProvincia/";

    private final RestTemplate restTemplate;
    private final GasolineraMapper gasolineraMapper;

    @Autowired
    public GasolineraService(RestTemplate restTemplate, GasolineraMapper gasolineraMapper) {
        this.restTemplate = restTemplate;
        this.gasolineraMapper = gasolineraMapper;
    }

    @Override
    @Cacheable(value = "gasolinerasMunicipio", key = "#codigoMunicipio")
    public List<GasolineraDto> findByMunicipio(String codigoMunicipio) {
        return llamarApiEstaciones(
                BASE_ESTACIONES + "FiltroMunicipio/" + codigoMunicipio,
                "municipio " + codigoMunicipio);
    }

    @Override
    @Cacheable(value = "gasolinerasProvincia", key = "#codigoProvincia")
    public List<GasolineraDto> findByProvincia(String codigoProvincia) {
        return llamarApiEstaciones(
                BASE_ESTACIONES + "FiltroProvincia/" + codigoProvincia,
                "provincia " + codigoProvincia);
    }

    @Override
    public List<GasolineraDto> findByLocalidad(String nombreLocalidad, String codigoProvincia) {
        List<GasolineraDto> fuente = (codigoProvincia != null && !codigoProvincia.isBlank())
                ? findByProvincia(codigoProvincia)
                : findAll();

        String nombre = nombreLocalidad.trim();
        return fuente.stream()
                .filter(g -> g.getLocalidad() != null && g.getLocalidad().equalsIgnoreCase(nombre))
                .toList();
    }

    // Devuelve los municipios de una provincia. Primero llama al endpoint oficial de MINETUR;
    // si falla (404, SSL, timeout) usa un fallback que extrae los nombres de las estaciones.
    @Override
    @Cacheable(value = "localidades", key = "#codigoProvincia")
    public List<LocalidadDto> findLocalidades(String codigoProvincia) {
        String url = BASE_MUNICIPIOS + codigoProvincia;
        log.info("Llamando a API de municipios para provincia: {}", codigoProvincia);
        log.info("URL: {}", url);

        try {
            ResponseEntity<List<MunicipioApiItem>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<MunicipioApiItem>>() {}
            );

            log.info("Respuesta HTTP: {}", response.getStatusCode());

            List<MunicipioApiItem> items = response.getBody();
            log.info("Municipios recibidos del endpoint oficial: {}",
                    items != null ? items.size() : 0);

            if (items != null && !items.isEmpty()) {
                return items.stream()
                        .map(m -> new LocalidadDto(m.getIdMunicipio(), m.getNombre(), m.getProvincia()))
                        .sorted(Comparator.comparing(LocalidadDto::getNombre))
                        .toList();
            }

        } catch (Exception ex) {
            // El endpoint oficial falló (404, PKIX, timeout…); usamos el fallback.
            log.warn("Endpoint oficial de municipios no disponible para provincia {} ({}). "
                   + "Usando fallback con datos de estaciones.", codigoProvincia, ex.getMessage());
        }

        return fallbackMunicipiosDesdeEstaciones(codigoProvincia);
    }

    @Override
    @Cacheable("gasolinerasTodas")
    public List<GasolineraDto> findAll() {
        return llamarApiEstaciones(BASE_ESTACIONES, "todas las estaciones");
    }

    // Extrae los nombres de municipio únicos directamente de los datos de estaciones.
    // Se usa cuando el endpoint oficial de MINETUR no está disponible.
    // El codigoMunicipio queda a null porque las estaciones no incluyen el código INE.
    private List<LocalidadDto> fallbackMunicipiosDesdeEstaciones(String codigoProvincia) {
        log.info("Fallback: extrayendo municipios de las estaciones de la provincia {}", codigoProvincia);

        List<GasolineraDto> estaciones = findByProvincia(codigoProvincia);

        if (estaciones.isEmpty()) {
            log.warn("Sin estaciones para la provincia {}", codigoProvincia);
            return Collections.emptyList();
        }

        String nombreProvincia = estaciones.stream()
                .map(GasolineraDto::getProvincia)
                .filter(p -> p != null && !p.isBlank())
                .findFirst()
                .orElse(null);

        List<LocalidadDto> localidades = estaciones.stream()
                .filter(g -> g.getLocalidad() != null && !g.getLocalidad().isBlank())
                .map(g -> g.getLocalidad().trim())
                .distinct()
                .sorted()
                .map(nombre -> new LocalidadDto(null, nombre, nombreProvincia))
                .toList();

        log.info("Fallback: {} municipios extraídos de estaciones para provincia {}",
                localidades.size(), codigoProvincia);
        return localidades;
    }

    private List<GasolineraDto> llamarApiEstaciones(String url, String contexto) {
        log.info("Llamando a API de estaciones [{}]: {}", contexto, url);
        try {
            ResponseEntity<GasolineraApiResponse> response =
                    restTemplate.exchange(url, HttpMethod.GET, null, GasolineraApiResponse.class);

            log.info("Respuesta HTTP [{}]: {}", contexto, response.getStatusCode());

            GasolineraApiResponse body = response.getBody();
            if (body == null || body.getListaEstaciones() == null) {
                log.warn("Respuesta vacía para {}", contexto);
                return Collections.emptyList();
            }

            log.info("ResultadoConsulta [{}]: {} — {} estaciones",
                    contexto, body.getResultadoConsulta(), body.getListaEstaciones().size());

            if (!"OK".equalsIgnoreCase(body.getResultadoConsulta())) {
                return Collections.emptyList();
            }
            return gasolineraMapper.toDTOList(body.getListaEstaciones());

        } catch (ResourceAccessException ex) {
            log.error("Timeout o error de red [{}]: {}", contexto, ex.getMessage());
            throw new ErrorException(
                    "No se pudo conectar con la API de gasolineras (" + contexto + "): " + ex.getMessage(), ex);
        } catch (HttpStatusCodeException ex) {
            log.error("Error HTTP {} [{}]: {}", ex.getStatusCode(), contexto, ex.getResponseBodyAsString());
            throw new ErrorException(
                    "La API de gasolineras devolvió el estado " + ex.getStatusCode() + " para " + contexto, ex);
        } catch (Exception ex) {
            log.error("Error inesperado [{}]: {}", contexto, ex.getMessage(), ex);
            throw new ErrorException(
                    "Error inesperado al obtener gasolineras (" + contexto + "): " + ex.getMessage(), ex);
        }
    }
}

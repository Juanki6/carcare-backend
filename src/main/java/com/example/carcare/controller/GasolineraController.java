package com.example.carcare.controller;

import com.example.carcare.dto.GasolineraDto;
import com.example.carcare.dto.LocalidadDto;
import com.example.carcare.service.IGasolineraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Gasolineras", description = "Consulta de gasolineras por municipio, provincia o localidad (datos del MINCOTUR)")
@Slf4j
@RestController
@RequestMapping("/api/gasolineras")
public class GasolineraController {

    private final IGasolineraService gasolineraService;

    @Autowired
    public GasolineraController(IGasolineraService gasolineraService) {
        this.gasolineraService = gasolineraService;
    }

    @Operation(summary = "Gasolineras por municipio", description = "Filtra por código INE del municipio. Opcional: ?tipoCombustible=GASOLINA95|GASOLINA98|GASOLEO_A|GASOLEO_B")
    @ApiResponse(responseCode = "200", description = "Lista de gasolineras del municipio")
    @GetMapping("/municipio/{codigoMunicipio}")
    public ResponseEntity<List<GasolineraDto>> getByMunicipio(
            @Parameter(description = "Código INE del municipio (ej: 03065)") @PathVariable String codigoMunicipio,
            @Parameter(description = "Tipo de combustible a filtrar") @RequestParam(required = false) String tipoCombustible) {

        List<GasolineraDto> gasolineras = gasolineraService.findByMunicipio(codigoMunicipio);
        return ResponseEntity.ok(filtrarPorCombustible(gasolineras, tipoCombustible));
    }

    @Operation(summary = "Gasolineras por provincia", description = "Devuelve todas las estaciones de una provincia. Opcional: ?tipoCombustible=...")
    @ApiResponse(responseCode = "200", description = "Lista de gasolineras de la provincia")
    @GetMapping("/provincia/{codigoProvincia}")
    public ResponseEntity<List<GasolineraDto>> getByProvincia(
            @Parameter(description = "Código de provincia de 2 dígitos (ej: 03)") @PathVariable String codigoProvincia,
            @Parameter(description = "Tipo de combustible a filtrar") @RequestParam(required = false) String tipoCombustible) {

        List<GasolineraDto> gasolineras = gasolineraService.findByProvincia(codigoProvincia);
        return ResponseEntity.ok(filtrarPorCombustible(gasolineras, tipoCombustible));
    }

    @Operation(summary = "Gasolineras por localidad", description = "Busca por nombre de localidad. Se recomienda pasar codigoProvincia para reutilizar caché y evitar descargar las ~12 000 estaciones nacionales.")
    @ApiResponse(responseCode = "200", description = "Lista de gasolineras de la localidad")
    @GetMapping("/localidad")
    public ResponseEntity<List<GasolineraDto>> getByLocalidad(
            @Parameter(description = "Nombre de la localidad (ej: Elche)") @RequestParam String nombre,
            @Parameter(description = "Código de provincia opcional para filtrar más rápido") @RequestParam(required = false) String codigoProvincia,
            @Parameter(description = "Tipo de combustible a filtrar") @RequestParam(required = false) String tipoCombustible) {

        List<GasolineraDto> gasolineras = gasolineraService.findByLocalidad(nombre, codigoProvincia);
        return ResponseEntity.ok(filtrarPorCombustible(gasolineras, tipoCombustible));
    }

    @Operation(summary = "Municipios de una provincia", description = "Devuelve la lista de municipios con su código INE para usar en un selector antes de llamar a /municipio/{codigo}")
    @ApiResponse(responseCode = "200", description = "Lista de municipios de la provincia")
    @GetMapping("/localidades/{codigoProvincia}")
    public ResponseEntity<List<LocalidadDto>> getLocalidades(
            @Parameter(description = "Código de provincia de 2 dígitos (ej: 03)") @PathVariable String codigoProvincia) {

        log.info("GET /api/gasolineras/localidades/{}", codigoProvincia);
        List<LocalidadDto> localidades = gasolineraService.findLocalidades(codigoProvincia);
        log.info("Devolviendo {} municipios para provincia {}", localidades.size(), codigoProvincia);
        return ResponseEntity.ok(localidades);
    }

    @Operation(summary = "Todas las gasolineras de España", description = "Devuelve ~12 000 registros. La primera petición puede ser lenta por la descarga externa.")
    @ApiResponse(responseCode = "200", description = "Lista completa de gasolineras")
    @GetMapping
    public ResponseEntity<List<GasolineraDto>> getAll() {
        return ResponseEntity.ok(gasolineraService.findAll());
    }

    private List<GasolineraDto> filtrarPorCombustible(List<GasolineraDto> lista, String tipo) {
        if (tipo == null || tipo.isBlank()) return lista;
        return switch (tipo.toUpperCase()) {
            case "GASOLINA95" -> lista.stream().filter(g -> g.getPrecioGasolina95() != null).toList();
            case "GASOLINA98" -> lista.stream().filter(g -> g.getPrecioGasolina98() != null).toList();
            case "GASOLEO_A"  -> lista.stream().filter(g -> g.getPrecioGasoleoA()   != null).toList();
            case "GASOLEO_B"  -> lista.stream().filter(g -> g.getPrecioGasoleoB()   != null).toList();
            default           -> lista;
        };
    }
}

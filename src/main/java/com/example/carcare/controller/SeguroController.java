package com.example.carcare.controller;

import com.example.carcare.dto.SeguroCreateDTO;
import com.example.carcare.dto.SeguroDto;
import com.example.carcare.entity.Seguro;
import com.example.carcare.entity.Vehiculo;
import com.example.carcare.mapper.SeguroMapper;
import com.example.carcare.repository.SeguroRepository;
import com.example.carcare.repository.VehiculoRepository;
import com.example.carcare.service.ISeguroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Tag(name = "Seguros", description = "Gestión de seguros asociados a vehículos")
@RestController
@RequestMapping("/api/seguros")
public class SeguroController {

    private final ISeguroService seguroService;
    private final VehiculoRepository vehiculoRepository;
    private final SeguroRepository seguroRepository;
    private final SeguroMapper seguroMapper;

    @Autowired
    public SeguroController(ISeguroService seguroService,
                            VehiculoRepository vehiculoRepository,
                            SeguroRepository seguroRepository,
                            SeguroMapper seguroMapper) {
        this.seguroService = seguroService;
        this.vehiculoRepository = vehiculoRepository;
        this.seguroRepository = seguroRepository;
        this.seguroMapper = seguroMapper;
    }

    @Operation(summary = "Obtener todos los seguros")
    @ApiResponse(responseCode = "200", description = "Lista de seguros obtenida correctamente")
    @GetMapping
    public ResponseEntity<List<SeguroDto>> getAllSeguros() {
        List<SeguroDto> seguros = seguroService.findAll();
        return ResponseEntity.ok(seguros);
    }

    @Operation(summary = "Obtener seguro por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Seguro encontrado"),
        @ApiResponse(responseCode = "500", description = "Seguro no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SeguroDto> getSeguroById(
            @Parameter(description = "ID del seguro") @PathVariable Long id) {
        SeguroDto seguro = seguroService.findById(id);
        return ResponseEntity.ok(seguro);
    }

    @Operation(summary = "Obtener seguro por vehículo")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Seguro encontrado"),
        @ApiResponse(responseCode = "404", description = "El vehículo no tiene seguro registrado")
    })
    @GetMapping("/vehiculo/{vehiculoId}")
    public ResponseEntity<SeguroDto> getSeguroByVehiculoId(
            @Parameter(description = "ID del vehículo") @PathVariable Long vehiculoId) {
        Optional<SeguroDto> seguro = seguroService.findByVehiculoId(vehiculoId);
        return seguro.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear nuevo seguro")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Seguro creado correctamente"),
        @ApiResponse(responseCode = "500", description = "Vehículo no encontrado")
    })
    @PostMapping
    public ResponseEntity<SeguroDto> createSeguro(@RequestBody SeguroCreateDTO createDTO) {
        Vehiculo vehiculo = vehiculoRepository.findById(createDTO.getIdVehiculo())
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado con id: " + createDTO.getIdVehiculo()));

        Seguro seguro = new Seguro();
        seguro.setVehiculo(vehiculo);
        seguro.setCompania(createDTO.getCompania());
        seguro.setNumeroPoliza(createDTO.getNumeroPoliza());
        seguro.setFechaInicio(createDTO.getFechaInicio());
        seguro.setFechaFin(createDTO.getFechaFin());
        seguro.setTelefonoAsistencia(createDTO.getTelefonoAsistencia());
        seguro.setCoberturas(createDTO.getCoberturas());
        seguro.setPrecioAnual(createDTO.getPrecioAnual());
        seguro.setActivo(createDTO.getActivo() != null ? createDTO.getActivo() : true);

        Seguro saved = seguroService.saveEntity(seguro);
        SeguroDto responseDto = seguroMapper.toDTO(saved);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @Operation(summary = "Actualizar seguro existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Seguro actualizado correctamente"),
        @ApiResponse(responseCode = "500", description = "Vehículo no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<SeguroDto> updateSeguro(
            @Parameter(description = "ID del seguro") @PathVariable Long id,
            @RequestBody SeguroCreateDTO createDTO) {
        Vehiculo vehiculo = vehiculoRepository.findById(createDTO.getIdVehiculo())
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado con id: " + createDTO.getIdVehiculo()));

        Seguro seguro = new Seguro();
        seguro.setIdSeguro(id);
        seguro.setVehiculo(vehiculo);
        seguro.setCompania(createDTO.getCompania());
        seguro.setNumeroPoliza(createDTO.getNumeroPoliza());
        seguro.setFechaInicio(createDTO.getFechaInicio());
        seguro.setFechaFin(createDTO.getFechaFin());
        seguro.setTelefonoAsistencia(createDTO.getTelefonoAsistencia());
        seguro.setCoberturas(createDTO.getCoberturas());
        seguro.setPrecioAnual(createDTO.getPrecioAnual());
        seguro.setActivo(createDTO.getActivo() != null ? createDTO.getActivo() : true);

        Seguro updated = seguroService.saveEntity(seguro);
        SeguroDto responseDto = seguroMapper.toDTO(updated);

        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "Eliminar seguro por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Seguro eliminado correctamente"),
        @ApiResponse(responseCode = "500", description = "Seguro no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeguro(
            @Parameter(description = "ID del seguro") @PathVariable Long id) {
        seguroService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener seguros de múltiples vehículos", description = "Devuelve un mapa idVehiculo → SeguroDto para los IDs recibidos en el body")
    @ApiResponse(responseCode = "200", description = "Mapa de seguros por vehículo")
    @PostMapping("/vehiculos")
    public ResponseEntity<Map<Long, SeguroDto>> getSegurosByVehiculoIds(@RequestBody List<Long> ids) {
        List<Seguro> seguros = seguroRepository.findByVehiculoIdVehiculoIn(ids);
        Map<Long, SeguroDto> mapa = new HashMap<>();
        for (Seguro s : seguros) {
            mapa.put(s.getVehiculo().getIdVehiculo(), seguroMapper.toDTO(s));
        }
        return ResponseEntity.ok(mapa);
    }
}

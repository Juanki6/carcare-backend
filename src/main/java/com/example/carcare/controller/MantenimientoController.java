package com.example.carcare.controller;

import com.example.carcare.dto.MantenimientoCreateDTO;
import com.example.carcare.dto.MantenimientoDto;
import com.example.carcare.dto.ProximoMantenimientoDTO;
import com.example.carcare.entity.Mantenimiento;
import com.example.carcare.entity.Vehiculo;
import com.example.carcare.mapper.MantenimientoMapper;
import com.example.carcare.repository.VehiculoRepository;
import com.example.carcare.service.IMantenimientoService;
import com.example.carcare.service.MantenimientoProximoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Mantenimientos", description = "Gestión de mantenimientos y alertas de próximos mantenimientos")
@RestController
@RequestMapping("/api/mantenimientos")
public class MantenimientoController {

    private final IMantenimientoService mantenimientoService;
    private final MantenimientoProximoService mantenimientoProximoService;
    private final VehiculoRepository vehiculoRepository;
    private final MantenimientoMapper mantenimientoMapper;

    @Autowired
    public MantenimientoController(IMantenimientoService mantenimientoService,
                                   MantenimientoProximoService mantenimientoProximoService,
                                   VehiculoRepository vehiculoRepository,
                                   MantenimientoMapper mantenimientoMapper) {
        this.mantenimientoService = mantenimientoService;
        this.mantenimientoProximoService = mantenimientoProximoService;
        this.vehiculoRepository = vehiculoRepository;
        this.mantenimientoMapper = mantenimientoMapper;
    }

    @Operation(summary = "Obtener todos los mantenimientos")
    @ApiResponse(responseCode = "200", description = "Lista de mantenimientos obtenida correctamente")
    @GetMapping
    public ResponseEntity<List<MantenimientoDto>> getAllMantenimientos() {
        List<MantenimientoDto> mantenimientos = mantenimientoService.findAll();
        return ResponseEntity.ok(mantenimientos);
    }

    @Operation(summary = "Obtener mantenimiento por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Mantenimiento encontrado"),
        @ApiResponse(responseCode = "500", description = "Mantenimiento no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MantenimientoDto> getMantenimientoById(
            @Parameter(description = "ID del mantenimiento") @PathVariable Long id) {
        MantenimientoDto mantenimiento = mantenimientoService.findById(id);
        return ResponseEntity.ok(mantenimiento);
    }

    @Operation(summary = "Obtener mantenimientos de un vehículo")
    @ApiResponse(responseCode = "200", description = "Lista de mantenimientos del vehículo")
    @GetMapping("/vehiculo/{vehiculoId}")
    public ResponseEntity<List<MantenimientoDto>> getMantenimientosByVehiculoId(
            @Parameter(description = "ID del vehículo") @PathVariable Long vehiculoId) {
        List<MantenimientoDto> mantenimientos = mantenimientoService.findByVehiculoId(vehiculoId);
        return ResponseEntity.ok(mantenimientos);
    }

    @Operation(summary = "Crear nuevo mantenimiento")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Mantenimiento creado correctamente"),
        @ApiResponse(responseCode = "500", description = "Vehículo no encontrado")
    })
    @PostMapping
    public ResponseEntity<MantenimientoDto> createMantenimiento(@RequestBody MantenimientoCreateDTO createDTO) {
        Vehiculo vehiculo = vehiculoRepository.findById(createDTO.getIdVehiculo())
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado con id: " + createDTO.getIdVehiculo()));

        Mantenimiento mantenimiento = new Mantenimiento();
        mantenimiento.setVehiculo(vehiculo);
        mantenimiento.setTipoEnum(Mantenimiento.TipoMantenimientoEnum.valueOf(createDTO.getTipoEnum()));
        mantenimiento.setFecha(createDTO.getFecha());
        mantenimiento.setKilometraje(createDTO.getKilometraje());
        mantenimiento.setNombreTaller(createDTO.getNombreTaller());
        mantenimiento.setNotas(createDTO.getNotas());

        Mantenimiento saved = mantenimientoService.saveEntity(mantenimiento);
        MantenimientoDto responseDto = mantenimientoMapper.toDTO(saved);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @Operation(summary = "Actualizar mantenimiento existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Mantenimiento actualizado correctamente"),
        @ApiResponse(responseCode = "500", description = "Mantenimiento no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MantenimientoDto> updateMantenimiento(
            @Parameter(description = "ID del mantenimiento") @PathVariable Long id,
            @RequestBody MantenimientoDto mantenimientoDto) {
        MantenimientoDto mantenimientoActualizado = mantenimientoService.update(id, mantenimientoDto);
        return ResponseEntity.ok(mantenimientoActualizado);
    }

    @Operation(summary = "Eliminar mantenimiento por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Mantenimiento eliminado correctamente"),
        @ApiResponse(responseCode = "500", description = "Mantenimiento no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMantenimiento(
            @Parameter(description = "ID del mantenimiento") @PathVariable Long id) {
        mantenimientoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Próximos mantenimientos de un vehículo", description = "Calcula qué mantenimientos se acercan según kilometraje y fecha del último registrado")
    @ApiResponse(responseCode = "200", description = "Lista de próximos mantenimientos")
    @GetMapping("/proximos/{idVehiculo}")
    public ResponseEntity<List<ProximoMantenimientoDTO>> getProximosMantenimientos(
            @Parameter(description = "ID del vehículo") @PathVariable Long idVehiculo) {
        List<ProximoMantenimientoDTO> proximos = mantenimientoProximoService.calcularProximosPorVehiculo(idVehiculo);
        return ResponseEntity.ok(proximos);
    }

    @Operation(summary = "Próximos mantenimientos de todos los vehículos de un usuario")
    @ApiResponse(responseCode = "200", description = "Lista de próximos mantenimientos agrupados por vehículo")
    @GetMapping("/proximos/usuario/{idUsuario}")
    public ResponseEntity<List<ProximoMantenimientoDTO>> getProximosMantenimientosPorUsuario(
            @Parameter(description = "ID del usuario") @PathVariable Long idUsuario) {
        List<ProximoMantenimientoDTO> proximos = mantenimientoProximoService.calcularProximosPorUsuario(idUsuario);
        return ResponseEntity.ok(proximos);
    }
}

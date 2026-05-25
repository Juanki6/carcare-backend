package com.example.carcare.controller;

import com.example.carcare.dto.CitaCreateDTO;
import com.example.carcare.dto.CitaDto;
import com.example.carcare.entity.Cita;
import com.example.carcare.entity.Vehiculo;
import com.example.carcare.mapper.CitaMapper;
import com.example.carcare.repository.TipoMantenimientoRepository;
import com.example.carcare.repository.VehiculoRepository;
import com.example.carcare.service.ICitaService;
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

@Tag(name = "Citas", description = "Gestión de citas de mantenimiento: pendientes, atrasadas y completar")
@RestController
@RequestMapping("/api/citas")
public class CitaController {

    private final ICitaService citaService;
    private final VehiculoRepository vehiculoRepository;
    private final TipoMantenimientoRepository tipoMantenimientoRepository;
    private final CitaMapper citaMapper;

    @Autowired
    public CitaController(ICitaService citaService,
                          VehiculoRepository vehiculoRepository,
                          TipoMantenimientoRepository tipoMantenimientoRepository,
                          CitaMapper citaMapper) {
        this.citaService = citaService;
        this.vehiculoRepository = vehiculoRepository;
        this.tipoMantenimientoRepository = tipoMantenimientoRepository;
        this.citaMapper = citaMapper;
    }

    @Operation(summary = "Obtener todas las citas")
    @ApiResponse(responseCode = "200", description = "Lista de citas obtenida correctamente")
    @GetMapping
    public ResponseEntity<List<CitaDto>> getAllCitas() {
        List<CitaDto> citas = citaService.findAll();
        return ResponseEntity.ok(citas);
    }

    @Operation(summary = "Obtener cita por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cita encontrada"),
        @ApiResponse(responseCode = "500", description = "Cita no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CitaDto> getCitaById(
            @Parameter(description = "ID de la cita") @PathVariable Long id) {
        CitaDto cita = citaService.findById(id);
        return ResponseEntity.ok(cita);
    }

    @Operation(summary = "Obtener citas de un vehículo")
    @ApiResponse(responseCode = "200", description = "Lista de citas del vehículo")
    @GetMapping("/vehiculo/{vehiculoId}")
    public ResponseEntity<List<CitaDto>> getCitasByVehiculoId(
            @Parameter(description = "ID del vehículo") @PathVariable Long vehiculoId) {
        List<CitaDto> citas = citaService.findByVehiculoId(vehiculoId);
        return ResponseEntity.ok(citas);
    }

    @Operation(summary = "Obtener citas pendientes", description = "Devuelve todas las citas no completadas de todos los vehículos")
    @ApiResponse(responseCode = "200", description = "Lista de citas pendientes")
    @GetMapping("/pendientes")
    public ResponseEntity<List<CitaDto>> getCitasPendientes() {
        List<CitaDto> citas = citaService.findCitasPendientes();
        return ResponseEntity.ok(citas);
    }

    @Operation(summary = "Obtener citas atrasadas", description = "Devuelve citas no completadas cuya fecha programada ya ha pasado")
    @ApiResponse(responseCode = "200", description = "Lista de citas atrasadas")
    @GetMapping("/atrasadas")
    public ResponseEntity<List<CitaDto>> getCitasAtrasadas() {
        List<CitaDto> citas = citaService.findCitasAtrasadas();
        return ResponseEntity.ok(citas);
    }

    @Operation(summary = "Crear nueva cita")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Cita creada correctamente"),
        @ApiResponse(responseCode = "500", description = "Vehículo o tipo de mantenimiento no encontrado")
    })
    @PostMapping
    public ResponseEntity<CitaDto> createCita(@RequestBody CitaCreateDTO createDTO) {
        Vehiculo vehiculo = vehiculoRepository.findById(createDTO.getIdVehiculo())
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado con id: " + createDTO.getIdVehiculo()));

        Cita cita = new Cita();
        cita.setVehiculo(vehiculo);
        cita.setTitulo(createDTO.getTitulo());
        cita.setFechaProgramada(createDTO.getFechaProgramada());
        cita.setKilometrajeProgramado(createDTO.getKilometrajeProgramado());
        cita.setNotas(createDTO.getNotas());
        cita.setCompletada(createDTO.getCompletada() != null ? createDTO.getCompletada() : false);
        cita.setRecordatorioEnviado(createDTO.getRecordatorioEnviado() != null ? createDTO.getRecordatorioEnviado() : false);

        if (createDTO.getIdTipoMantenimiento() != null) {
            cita.setTipoMantenimiento(tipoMantenimientoRepository.findById(createDTO.getIdTipoMantenimiento())
                    .orElseThrow(() -> new RuntimeException("TipoMantenimiento no encontrado con id: " + createDTO.getIdTipoMantenimiento())));
        }

        Cita saved = citaService.saveEntity(cita);
        CitaDto responseDto = citaMapper.toDTO(saved);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @Operation(summary = "Actualizar cita existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cita actualizada correctamente"),
        @ApiResponse(responseCode = "500", description = "Cita no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CitaDto> updateCita(
            @Parameter(description = "ID de la cita") @PathVariable Long id,
            @RequestBody CitaDto citaDto) {
        CitaDto citaActualizada = citaService.update(id, citaDto);
        return ResponseEntity.ok(citaActualizada);
    }

    @Operation(summary = "Marcar cita como completada")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cita marcada como completada"),
        @ApiResponse(responseCode = "500", description = "Cita no encontrada")
    })
    @PatchMapping("/{id}/completar")
    public ResponseEntity<CitaDto> marcarComoCompletada(
            @Parameter(description = "ID de la cita") @PathVariable Long id) {
        CitaDto citaCompletada = citaService.marcarComoCompletada(id);
        return ResponseEntity.ok(citaCompletada);
    }

    @Operation(summary = "Marcar recordatorio como enviado")
    @ApiResponse(responseCode = "200", description = "Recordatorio marcado como enviado")
    @PatchMapping("/{id}/recordatorio")
    public ResponseEntity<CitaDto> marcarRecordatorioEnviado(
            @Parameter(description = "ID de la cita") @PathVariable Long id) {
        CitaDto cita = citaService.marcarRecordatorioEnviado(id);
        return ResponseEntity.ok(cita);
    }

    @Operation(summary = "Eliminar cita por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Cita eliminada correctamente"),
        @ApiResponse(responseCode = "500", description = "Cita no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCita(
            @Parameter(description = "ID de la cita") @PathVariable Long id) {
        citaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

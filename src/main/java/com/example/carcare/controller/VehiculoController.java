package com.example.carcare.controller;

import com.example.carcare.dto.VehiculoDto;
import com.example.carcare.exception.CreateEntityException;
import com.example.carcare.exception.Response;
import com.example.carcare.service.IVehiculoService;
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

@Tag(name = "Vehículos", description = "Gestión de vehículos por usuario")
@RestController
@RequestMapping("/api/vehiculos")
public class VehiculoController {

    private final IVehiculoService vehiculoService;

    @Autowired
    public VehiculoController(IVehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }

    @Operation(summary = "Obtener todos los vehículos")
    @ApiResponse(responseCode = "200", description = "Lista de vehículos obtenida correctamente")
    @GetMapping
    public ResponseEntity<List<VehiculoDto>> getAllVehiculos() {
        List<VehiculoDto> vehiculos = vehiculoService.findAll();
        return ResponseEntity.ok(vehiculos);
    }

    @Operation(summary = "Obtener vehículo por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Vehículo encontrado"),
        @ApiResponse(responseCode = "500", description = "Vehículo no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<VehiculoDto> getVehiculoById(
            @Parameter(description = "ID del vehículo") @PathVariable Long id) {
        VehiculoDto vehiculo = vehiculoService.findById(id);
        return ResponseEntity.ok(vehiculo);
    }

    @Operation(summary = "Obtener vehículos de un usuario")
    @ApiResponse(responseCode = "200", description = "Lista de vehículos del usuario")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<VehiculoDto>> getVehiculosByUsuarioId(
            @Parameter(description = "ID del usuario propietario") @PathVariable Long usuarioId) {
        List<VehiculoDto> vehiculos = vehiculoService.findByUsuarioId(usuarioId);
        return ResponseEntity.ok(vehiculos);
    }

    @Operation(summary = "Crear nuevo vehículo")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Vehículo creado correctamente"),
        @ApiResponse(responseCode = "409", description = "Ya existe un vehículo con esa matrícula para este usuario")
    })
    @PostMapping
    public ResponseEntity<?> createVehiculo(@RequestBody VehiculoDto vehiculoDto) {
        try {
            VehiculoDto nuevoVehiculo = vehiculoService.save(vehiculoDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoVehiculo);
        } catch (CreateEntityException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Response.generalError(HttpStatus.CONFLICT.value(), ex.getMessage()));
        }
    }

    @Operation(summary = "Actualizar vehículo existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Vehículo actualizado correctamente"),
        @ApiResponse(responseCode = "409", description = "Matrícula duplicada para este usuario")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateVehiculo(
            @Parameter(description = "ID del vehículo") @PathVariable Long id,
            @RequestBody VehiculoDto vehiculoDto) {
        try {
            VehiculoDto vehiculoActualizado = vehiculoService.update(id, vehiculoDto);
            return ResponseEntity.ok(vehiculoActualizado);
        } catch (CreateEntityException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Response.generalError(HttpStatus.CONFLICT.value(), ex.getMessage()));
        }
    }

    @Operation(summary = "Eliminar vehículo por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Vehículo eliminado correctamente"),
        @ApiResponse(responseCode = "500", description = "Vehículo no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehiculo(
            @Parameter(description = "ID del vehículo") @PathVariable Long id) {
        vehiculoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

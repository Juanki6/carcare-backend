package com.example.carcare.controller;

import com.example.carcare.dto.RepostajeDto;
import com.example.carcare.dto.RepostajeSaveResponse;
import com.example.carcare.service.IRepostajeService;
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

@Tag(name = "Repostajes", description = "Registro y consulta de repostajes por vehículo")
@RestController
@RequestMapping("/api/repostajes")
public class RepostajeController {

    private final IRepostajeService repostajeService;

    @Autowired
    public RepostajeController(IRepostajeService repostajeService) {
        this.repostajeService = repostajeService;
    }

    @Operation(summary = "Obtener todos los repostajes")
    @ApiResponse(responseCode = "200", description = "Lista de repostajes obtenida correctamente")
    @GetMapping
    public ResponseEntity<List<RepostajeDto>> getAllRepostajes() {
        List<RepostajeDto> repostajes = repostajeService.findAll();
        return ResponseEntity.ok(repostajes);
    }

    @Operation(summary = "Obtener repostaje por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Repostaje encontrado"),
        @ApiResponse(responseCode = "500", description = "Repostaje no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<RepostajeDto> getRepostajeById(
            @Parameter(description = "ID del repostaje") @PathVariable Long id) {
        RepostajeDto repostaje = repostajeService.findById(id);
        return ResponseEntity.ok(repostaje);
    }

    @Operation(summary = "Obtener repostajes de un vehículo", description = "Devuelve los repostajes ordenados por fecha descendente")
    @ApiResponse(responseCode = "200", description = "Lista de repostajes del vehículo")
    @GetMapping("/vehiculo/{vehiculoId}")
    public ResponseEntity<List<RepostajeDto>> getRepostajesByVehiculoId(
            @Parameter(description = "ID del vehículo") @PathVariable Long vehiculoId) {
        List<RepostajeDto> repostajes = repostajeService.findByVehiculoId(vehiculoId);
        return ResponseEntity.ok(repostajes);
    }

    @Operation(summary = "Crear nuevo repostaje", description = "Si ya existe un repostaje en la misma fecha para ese vehículo, devuelve 208. Usar ?forzar=true para guardar igualmente.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Repostaje creado correctamente"),
        @ApiResponse(responseCode = "208", description = "Posible duplicado detectado")
    })
    @PostMapping
    public ResponseEntity<RepostajeSaveResponse> createRepostaje(
            @RequestBody RepostajeDto repostajeDto,
            @Parameter(description = "true para guardar aunque sea duplicado") @RequestParam(defaultValue = "false") boolean forzar) {
        RepostajeSaveResponse resultado = repostajeService.save(repostajeDto, forzar);
        if (resultado.isDuplicado()) {
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(resultado);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }

    @Operation(summary = "Actualizar repostaje existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Repostaje actualizado correctamente"),
        @ApiResponse(responseCode = "500", description = "Repostaje no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<RepostajeDto> updateRepostaje(
            @Parameter(description = "ID del repostaje") @PathVariable Long id,
            @RequestBody RepostajeDto repostajeDto) {
        RepostajeDto repostajeActualizado = repostajeService.update(id, repostajeDto);
        return ResponseEntity.ok(repostajeActualizado);
    }

    @Operation(summary = "Eliminar repostaje por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Repostaje eliminado correctamente"),
        @ApiResponse(responseCode = "500", description = "Repostaje no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRepostaje(
            @Parameter(description = "ID del repostaje") @PathVariable Long id) {
        repostajeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

package com.example.carcare.controller;

import com.example.carcare.dto.TallerDto;
import com.example.carcare.dto.TallerSaveResponse;
import com.example.carcare.service.ITallerService;
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

@Tag(name = "Talleres", description = "Gestión de talleres mecánicos y búsqueda por nombre")
@RestController
@RequestMapping("/api/talleres")
public class TallerController {

    private final ITallerService tallerService;

    @Autowired
    public TallerController(ITallerService tallerService) {
        this.tallerService = tallerService;
    }

    @Operation(summary = "Obtener todos los talleres")
    @ApiResponse(responseCode = "200", description = "Lista de talleres obtenida correctamente")
    @GetMapping
    public ResponseEntity<List<TallerDto>> getAllTalleres() {
        List<TallerDto> talleres = tallerService.findAll();
        return ResponseEntity.ok(talleres);
    }

    @Operation(summary = "Obtener taller por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Taller encontrado"),
        @ApiResponse(responseCode = "500", description = "Taller no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TallerDto> getTallerById(
            @Parameter(description = "ID del taller") @PathVariable Long id) {
        TallerDto taller = tallerService.findById(id);
        return ResponseEntity.ok(taller);
    }

    @Operation(summary = "Buscar talleres por nombre", description = "Búsqueda parcial: devuelve talleres cuyo nombre contiene el texto indicado")
    @ApiResponse(responseCode = "200", description = "Lista de talleres que coinciden")
    @GetMapping("/buscar")
    public ResponseEntity<List<TallerDto>> getTalleresByNombre(
            @Parameter(description = "Texto a buscar en el nombre del taller") @RequestParam String nombre) {
        List<TallerDto> talleres = tallerService.findByNombreContaining(nombre);
        return ResponseEntity.ok(talleres);
    }

    @Operation(summary = "Crear nuevo taller", description = "Si ya existe un taller con el mismo nombre, devuelve 208. Usar ?confirmarDuplicado=true para guardar igualmente.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Taller creado correctamente"),
        @ApiResponse(responseCode = "208", description = "Posible taller duplicado detectado")
    })
    @PostMapping
    public ResponseEntity<TallerSaveResponse> createTaller(
            @RequestBody TallerDto tallerDto,
            @Parameter(description = "true para guardar aunque sea duplicado") @RequestParam(defaultValue = "false") boolean confirmarDuplicado) {
        TallerSaveResponse resultado = tallerService.save(tallerDto, confirmarDuplicado);
        if (resultado.isDuplicado()) {
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(resultado);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }

    @Operation(summary = "Actualizar taller existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Taller actualizado correctamente"),
        @ApiResponse(responseCode = "500", description = "Taller no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TallerDto> updateTaller(
            @Parameter(description = "ID del taller") @PathVariable Long id,
            @RequestBody TallerDto tallerDto) {
        TallerDto tallerActualizado = tallerService.update(id, tallerDto);
        return ResponseEntity.ok(tallerActualizado);
    }

    @Operation(summary = "Eliminar taller por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Taller eliminado correctamente"),
        @ApiResponse(responseCode = "500", description = "Taller no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTaller(
            @Parameter(description = "ID del taller") @PathVariable Long id) {
        tallerService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

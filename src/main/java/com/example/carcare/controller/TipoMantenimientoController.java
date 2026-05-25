package com.example.carcare.controller;

import com.example.carcare.dto.TipoMantenimientoDto;
import com.example.carcare.service.ITipoMantenimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tipos-mantenimiento")
public class TipoMantenimientoController {

    private final ITipoMantenimientoService tipoMantenimientoService;

    @Autowired
    public TipoMantenimientoController(ITipoMantenimientoService tipoMantenimientoService) {
        this.tipoMantenimientoService = tipoMantenimientoService;
    }

    /**
     * Obtiene todos los tipos de mantenimiento
     * GET /api/tipos-mantenimiento
     */
    @GetMapping
    public ResponseEntity<List<TipoMantenimientoDto>> getAllTipos() {
        List<TipoMantenimientoDto> tipos = tipoMantenimientoService.findAll();
        return ResponseEntity.ok(tipos);
    }

    /**
     * Busca un tipo de mantenimiento por su ID
     * GET /api/tipos-mantenimiento/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<TipoMantenimientoDto> getTipoById(@PathVariable Long id) {
        TipoMantenimientoDto tipo = tipoMantenimientoService.findById(id);
        return ResponseEntity.ok(tipo);
    }

    /**
     * Busca un tipo de mantenimiento por su nombre
     * GET /api/tipos-mantenimiento/nombre?nombre=xxx
     */
    @GetMapping("/nombre")
    public ResponseEntity<TipoMantenimientoDto> getTipoByNombre(@RequestParam String nombre) {
        Optional<TipoMantenimientoDto> tipo = tipoMantenimientoService.findByNombreTipo(nombre);
        return tipo.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea un nuevo tipo de mantenimiento
     * POST /api/tipos-mantenimiento
     */
    @PostMapping
    public ResponseEntity<TipoMantenimientoDto> createTipo(@RequestBody TipoMantenimientoDto tipoMantenimientoDto) {
        TipoMantenimientoDto nuevoTipo = tipoMantenimientoService.save(tipoMantenimientoDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoTipo);
    }

    /**
     * Actualiza un tipo de mantenimiento existente
     * PUT /api/tipos-mantenimiento/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<TipoMantenimientoDto> updateTipo(@PathVariable Long id, @RequestBody TipoMantenimientoDto tipoMantenimientoDto) {
        TipoMantenimientoDto tipoActualizado = tipoMantenimientoService.update(id, tipoMantenimientoDto);
        return ResponseEntity.ok(tipoActualizado);
    }

    /**
     * Elimina un tipo de mantenimiento por su ID
     * DELETE /api/tipos-mantenimiento/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTipo(@PathVariable Long id) {
        tipoMantenimientoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
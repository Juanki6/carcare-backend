package com.example.carcare.controller;

import com.example.carcare.dto.FacturaDto;
import com.example.carcare.service.IFacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/facturas")
public class FacturaController {

    private final IFacturaService facturaService;

    @Autowired
    public FacturaController(IFacturaService facturaService) {
        this.facturaService = facturaService;
    }

    /**
     * Obtiene todas las facturas
     * GET /api/facturas
     */
    @GetMapping
    public ResponseEntity<List<FacturaDto>> getAllFacturas() {
        List<FacturaDto> facturas = facturaService.findAll();
        return ResponseEntity.ok(facturas);
    }

    /**
     * Busca una factura por su ID
     * GET /api/facturas/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<FacturaDto> getFacturaById(@PathVariable Long id) {
        FacturaDto factura = facturaService.findById(id);
        return ResponseEntity.ok(factura);
    }

    /**
     * Busca la factura asociada a un mantenimiento
     * GET /api/facturas/mantenimiento/{mantenimientoId}
     */
    @GetMapping("/mantenimiento/{mantenimientoId}")
    public ResponseEntity<FacturaDto> getFacturaByMantenimientoId(@PathVariable Long mantenimientoId) {
        Optional<FacturaDto> factura = facturaService.findByMantenimientoId(mantenimientoId);
        return factura.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea una nueva factura
     * POST /api/facturas
     */
    @PostMapping
    public ResponseEntity<FacturaDto> createFactura(@RequestBody FacturaDto facturaDto) {
        FacturaDto nuevaFactura = facturaService.save(facturaDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaFactura);
    }

    /**
     * Actualiza una factura existente
     * PUT /api/facturas/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<FacturaDto> updateFactura(@PathVariable Long id, @RequestBody FacturaDto facturaDto) {
        FacturaDto facturaActualizada = facturaService.update(id, facturaDto);
        return ResponseEntity.ok(facturaActualizada);
    }

    /**
     * Elimina una factura por su ID
     * DELETE /api/facturas/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFactura(@PathVariable Long id) {
        facturaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
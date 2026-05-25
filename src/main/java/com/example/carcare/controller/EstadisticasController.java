package com.example.carcare.controller;

import com.example.carcare.dto.ComparativaVehiculosDTO;
import com.example.carcare.dto.ConsumoMedioGlobalDTO;
import com.example.carcare.dto.DistribucionGastosDTO;
import com.example.carcare.dto.EstadisticasDTO;
import com.example.carcare.dto.EvolucionConsumoDTO;
import com.example.carcare.dto.EvolucionPreciosDTO;
import com.example.carcare.dto.GastosMensualesDTO;
import com.example.carcare.dto.VehiculoGastoDTO;
import com.example.carcare.service.EstadisticasService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Estadísticas", description = "Estadísticas de consumo, gastos, evolución de precios y comparativas entre vehículos")
@RestController
@RequestMapping("/api/estadisticas")
@AllArgsConstructor
public class EstadisticasController {

    private final EstadisticasService estadisticasService;

    @Operation(summary = "Resumen estadístico de un vehículo", description = "Consumo medio L/100km, precio medio por litro, gasto total, último repostaje y número de repostajes")
    @ApiResponse(responseCode = "200", description = "Estadísticas del vehículo")
    @GetMapping("/vehiculo/{idVehiculo}")
    public ResponseEntity<EstadisticasDTO> getEstadisticasVehiculo(
            @Parameter(description = "ID del vehículo") @PathVariable Long idVehiculo) {
        return ResponseEntity.ok(estadisticasService.getEstadisticasVehiculo(idVehiculo));
    }

    @Operation(summary = "Evolución del consumo de un vehículo", description = "Historial de consumo repostaje a repostaje, ordenado cronológicamente")
    @ApiResponse(responseCode = "200", description = "Lista de puntos de consumo a lo largo del tiempo")
    @GetMapping("/vehiculo/{idVehiculo}/evolucion")
    public ResponseEntity<List<EvolucionConsumoDTO>> getEvolucionConsumo(
            @Parameter(description = "ID del vehículo") @PathVariable Long idVehiculo) {
        return ResponseEntity.ok(estadisticasService.getEvolucionConsumo(idVehiculo));
    }

    @Operation(summary = "Comparativa de vehículos de un usuario", description = "Compara consumo medio y gasto total de todos los vehículos del usuario")
    @ApiResponse(responseCode = "200", description = "Lista comparativa de vehículos")
    @GetMapping("/usuario/{idUsuario}/comparativa")
    public ResponseEntity<List<ComparativaVehiculosDTO>> getComparativaVehiculos(
            @Parameter(description = "ID del usuario") @PathVariable Long idUsuario) {
        return ResponseEntity.ok(estadisticasService.getComparativaVehiculos(idUsuario));
    }

    @Operation(summary = "Gastos mensuales de un vehículo", description = "Desglose mensual de gastos en combustible y mantenimiento para los últimos 12 meses")
    @ApiResponse(responseCode = "200", description = "Lista de gastos por mes")
    @GetMapping("/vehiculo/{idVehiculo}/gastos-mensuales")
    public ResponseEntity<List<GastosMensualesDTO>> getGastosMensuales(
            @Parameter(description = "ID del vehículo") @PathVariable Long idVehiculo) {
        return ResponseEntity.ok(estadisticasService.getGastosMensuales(idVehiculo));
    }

    @Operation(summary = "Evolución del precio del combustible", description = "Precio medio por litro agrupado por mes, con el tipo de combustible del vehículo")
    @ApiResponse(responseCode = "200", description = "Lista de precios medios por mes")
    @GetMapping("/vehiculo/{idVehiculo}/evolucion-precios")
    public ResponseEntity<List<EvolucionPreciosDTO>> getEvolucionPrecios(
            @Parameter(description = "ID del vehículo") @PathVariable Long idVehiculo) {
        return ResponseEntity.ok(estadisticasService.getEvolucionPrecios(idVehiculo));
    }

    @Operation(summary = "Distribución de gastos de un vehículo", description = "Porcentaje y total de gasto dividido en Combustible, Mantenimiento y Seguro")
    @ApiResponse(responseCode = "200", description = "Lista de categorías de gasto con porcentaje")
    @GetMapping("/vehiculo/{idVehiculo}/distribucion-gastos")
    public ResponseEntity<List<DistribucionGastosDTO>> getDistribucionGastos(
            @Parameter(description = "ID del vehículo") @PathVariable Long idVehiculo) {
        return ResponseEntity.ok(estadisticasService.getDistribucionGastos(idVehiculo));
    }

    @Operation(summary = "Consumo medio global por marca y modelo", description = "Ranking de consumo medio L/100km calculado a partir de todos los repostajes registrados, agrupado por marca y modelo")
    @ApiResponse(responseCode = "200", description = "Lista de marcas/modelos con su consumo medio")
    @GetMapping("/consumo-medio-global")
    public ResponseEntity<List<ConsumoMedioGlobalDTO>> getConsumoMedioGlobal() {
        return ResponseEntity.ok(estadisticasService.getConsumoMedioGlobal());
    }

    @Operation(summary = "Vehículos con mayor gasto en combustible", description = "Top 10 de vehículos ordenados por gasto total en combustible (litros × precio/litro)")
    @ApiResponse(responseCode = "200", description = "Lista de los 10 vehículos con mayor gasto")
    @GetMapping("/vehiculos-mayor-gasto")
    public ResponseEntity<List<VehiculoGastoDTO>> getVehiculosMayorGasto() {
        return ResponseEntity.ok(estadisticasService.getVehiculosMayorGasto());
    }
}

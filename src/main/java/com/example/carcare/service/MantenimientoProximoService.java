package com.example.carcare.service;

import com.example.carcare.dto.ProximoMantenimientoDTO;
import com.example.carcare.entity.Mantenimiento;
import com.example.carcare.entity.Mantenimiento.TipoMantenimientoEnum;
import com.example.carcare.entity.Vehiculo;
import com.example.carcare.exception.NotFoundException;
import com.example.carcare.repository.MantenimientoRepository;
import com.example.carcare.repository.VehiculoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MantenimientoProximoService {

    private final VehiculoRepository vehiculoRepository;
    private final MantenimientoRepository mantenimientoRepository;

    private static final int URGENTE_KM = 2000;
    private static final int URGENTE_DIAS = 30;

    public List<ProximoMantenimientoDTO> calcularProximosPorVehiculo(Long idVehiculo) {
        Vehiculo vehiculo = vehiculoRepository.findById(idVehiculo)
                .orElseThrow(() -> new NotFoundException(Vehiculo.class.getSimpleName(), idVehiculo));
        return calcularParaVehiculo(vehiculo);
    }

    public List<ProximoMantenimientoDTO> calcularProximosPorUsuario(Long idUsuario) {
        List<Vehiculo> vehiculos = vehiculoRepository.findByUsuarioIdUsuario(idUsuario);
        List<ProximoMantenimientoDTO> resultado = new ArrayList<>();
        for (Vehiculo vehiculo : vehiculos) {
            resultado.addAll(calcularParaVehiculo(vehiculo));
        }
        return resultado;
    }

    private List<ProximoMantenimientoDTO> calcularParaVehiculo(Vehiculo vehiculo) {
        List<ProximoMantenimientoDTO> proximos = new ArrayList<>();
        LocalDate hoy = LocalDate.now();

        ProximoMantenimientoDTO itv = calcularProximaITV(vehiculo, hoy);
        if (itv != null) {
            proximos.add(itv);
        }

        for (TipoMantenimientoEnum tipo : TipoMantenimientoEnum.values()) {
            if (tipo == TipoMantenimientoEnum.ITV || tipo == TipoMantenimientoEnum.OTRO) {
                continue;
            }
            ProximoMantenimientoDTO proximo = calcularProximoMantenimiento(vehiculo, tipo, hoy);
            if (proximo != null) {
                proximos.add(proximo);
            }
        }

        return proximos;
    }

    // Calcula la próxima ITV según el calendario legal español:
    //   - Primera ITV a los 4 años de la matriculación
    //   - Entre 4 y 10 años: cada 2 años
    //   - A partir de 10 años: cada año
    // El bucle avanza el punto teórico hacia delante hasta superar la fecha actual.
    private ProximoMantenimientoDTO calcularProximaITV(Vehiculo vehiculo, LocalDate hoy) {
        LocalDate fechaMatriculacion = vehiculo.getFechaMatriculacion();
        if (fechaMatriculacion == null) {
            return null;
        }

        LocalDate proximaFecha = fechaMatriculacion.plusYears(4);

        while (!proximaFecha.isAfter(hoy)) {
            int edadEnFecha = Period.between(fechaMatriculacion, proximaFecha).getYears();
            int intervalo = edadEnFecha >= 10 ? 1 : 2;
            proximaFecha = proximaFecha.plusYears(intervalo);
        }

        long diasRestantes = ChronoUnit.DAYS.between(hoy, proximaFecha);

        return buildDTO(
                vehiculo.getIdVehiculo(),
                TipoMantenimientoEnum.ITV.name(),
                proximaFecha,
                null,
                null,
                (int) diasRestantes
        );
    }

    // Estima cuándo toca el próximo mantenimiento de un tipo concreto.
    // Si existe historial, se calcula desde el último registro (km y/o fecha).
    // Si no existe historial, se estima desde el estado actual del vehículo,
    // asumiendo que el intervalo completo está por delante.
    private ProximoMantenimientoDTO calcularProximoMantenimiento(Vehiculo vehiculo,
                                                                  TipoMantenimientoEnum tipo,
                                                                  LocalDate hoy) {
        Integer intervaloKm = getIntervaloKm(tipo);
        Integer intervaloMeses = getIntervaloMeses(tipo);

        if (intervaloKm == null && intervaloMeses == null) {
            return null;
        }

        Optional<Mantenimiento> ultimo = mantenimientoRepository
                .findTopByVehiculoIdVehiculoAndTipoEnumOrderByFechaDesc(
                        vehiculo.getIdVehiculo(), tipo);

        Integer kmActual = vehiculo.getKilometraje();
        Integer kmEstimado = null;
        Integer kmRestante = null;
        LocalDate fechaEstimada = null;
        Integer diasRestantes = null;

        if (ultimo.isPresent()) {
            Mantenimiento m = ultimo.get();

            if (intervaloKm != null) {
                kmEstimado = m.getKilometraje() + intervaloKm;
                kmRestante = kmEstimado - kmActual;
            }
            if (intervaloMeses != null) {
                fechaEstimada = m.getFecha().plusMonths(intervaloMeses);
                diasRestantes = (int) ChronoUnit.DAYS.between(hoy, fechaEstimada);
            }
        } else {
            // Sin historial: estimación desde km/fecha actuales del vehículo
            if (intervaloKm != null) {
                kmEstimado = kmActual + intervaloKm;
                kmRestante = intervaloKm;
            }
            if (intervaloMeses != null) {
                fechaEstimada = hoy.plusMonths(intervaloMeses);
                diasRestantes = (int) ChronoUnit.DAYS.between(hoy, fechaEstimada);
            }
        }

        return buildDTO(vehiculo.getIdVehiculo(), tipo.name(), fechaEstimada, kmEstimado, kmRestante, diasRestantes);
    }

    private ProximoMantenimientoDTO buildDTO(Long idVehiculo, String tipo, LocalDate fechaEstimada,
                                              Integer kmEstimado, Integer kmRestante, Integer diasRestantes) {
        boolean esUrgente = (kmRestante != null && kmRestante < URGENTE_KM)
                || (diasRestantes != null && diasRestantes < URGENTE_DIAS);

        ProximoMantenimientoDTO dto = new ProximoMantenimientoDTO();
        dto.setIdVehiculo(idVehiculo);
        dto.setTipo(tipo);
        dto.setFechaEstimada(fechaEstimada);
        dto.setKilometrajeEstimado(kmEstimado);
        dto.setKmRestante(kmRestante);
        dto.setDiasRestantes(diasRestantes);
        dto.setEsUrgente(esUrgente);
        return dto;
    }

    private Integer getIntervaloKm(TipoMantenimientoEnum tipo) {
        return switch (tipo) {
            case ACEITE -> 15000;
            case NEUMATICOS -> 40000;
            case CORREA_DISTRIBUCION -> 120000;
            case FRENOS -> 30000;
            default -> null;
        };
    }

    private Integer getIntervaloMeses(TipoMantenimientoEnum tipo) {
        return switch (tipo) {
            case ACEITE -> 12;
            case CORREA_DISTRIBUCION -> 60;
            case BATERIA -> 48;
            default -> null;
        };
    }
}

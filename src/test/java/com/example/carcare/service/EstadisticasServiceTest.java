package com.example.carcare.service;

import com.example.carcare.dto.EstadisticasDTO;
import com.example.carcare.entity.Repostaje;
import com.example.carcare.entity.Vehiculo;
import com.example.carcare.exception.NotFoundException;
import com.example.carcare.repository.MantenimientoRepository;
import com.example.carcare.repository.RepostajeRepository;
import com.example.carcare.repository.SeguroRepository;
import com.example.carcare.repository.VehiculoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EstadisticasServiceTest {

    @Mock VehiculoRepository vehiculoRepository;
    @Mock RepostajeRepository repostajeRepository;
    @Mock MantenimientoRepository mantenimientoRepository;
    @Mock SeguroRepository seguroRepository;

    @InjectMocks EstadisticasService estadisticasService;

    // ─── helpers ──────────────────────────────────────────────────────────────

    private Vehiculo vehiculo(int km) {
        Vehiculo v = new Vehiculo();
        v.setIdVehiculo(1L);
        v.setKilometraje(km);
        v.setCombustible(Vehiculo.Combustible.GASOLINA);
        return v;
    }

    private Repostaje repostaje(int km, double litros, double precio) {
        Repostaje r = new Repostaje();
        r.setKilometraje(km);
        r.setLitros(BigDecimal.valueOf(litros));
        r.setPrecioLitro(BigDecimal.valueOf(precio));
        r.setFecha(LocalDate.now());
        return r;
    }

    // ─── sin repostajes ───────────────────────────────────────────────────────

    @Test
    void getEstadisticasVehiculo_sinRepostajes_devuelveCerosYKilometrajeActual() {
        when(vehiculoRepository.findById(1L)).thenReturn(Optional.of(vehiculo(50000)));
        when(repostajeRepository.findByVehiculoIdVehiculoOrderByFechaDesc(1L)).thenReturn(List.of());

        EstadisticasDTO resultado = estadisticasService.getEstadisticasVehiculo(1L);

        assertThat(resultado.getConsumoMedioL100km()).isEqualTo(0.0);
        assertThat(resultado.getGastoTotal()).isEqualTo(0.0);
        assertThat(resultado.getKilometrajeTotal()).isEqualTo(50000);
        assertThat(resultado.getNumRepostajes()).isEqualTo(0);
    }

    // ─── un repostaje (sin par → consumo no calculable) ───────────────────────

    @Test
    void getEstadisticasVehiculo_unRepostaje_gastoCalculadoSinConsumo() {
        // 40 litros a 1.80 €/L → gasto = 72 €
        Repostaje r = repostaje(100000, 40.0, 1.80);

        when(vehiculoRepository.findById(1L)).thenReturn(Optional.of(vehiculo(100000)));
        when(repostajeRepository.findByVehiculoIdVehiculoOrderByFechaDesc(1L)).thenReturn(List.of(r));

        EstadisticasDTO resultado = estadisticasService.getEstadisticasVehiculo(1L);

        assertThat(resultado.getConsumoMedioL100km()).isEqualTo(0.0);
        assertThat(resultado.getGastoTotal()).isEqualTo(72.0);
        assertThat(resultado.getNumRepostajes()).isEqualTo(1);
    }

    // ─── dos repostajes → consumo calculable ──────────────────────────────────

    @Test
    void getEstadisticasVehiculo_dosRepostajes_calculaConsumoCorrectamente() {
        // Ordenados desc: más reciente primero
        // r1 (km 10500) - r2 (km 10000) = 500 km recorridos con 50 litros → 10 L/100km
        Repostaje r1 = repostaje(10500, 50.0, 1.50);
        Repostaje r2 = repostaje(10000, 45.0, 1.45);

        when(vehiculoRepository.findById(1L)).thenReturn(Optional.of(vehiculo(10500)));
        when(repostajeRepository.findByVehiculoIdVehiculoOrderByFechaDesc(1L)).thenReturn(List.of(r1, r2));

        EstadisticasDTO resultado = estadisticasService.getEstadisticasVehiculo(1L);

        assertThat(resultado.getConsumoMedioL100km()).isEqualTo(10.0);
        assertThat(resultado.getNumRepostajes()).isEqualTo(2);
    }

    // ─── vehículo no encontrado ───────────────────────────────────────────────

    @Test
    void getEstadisticasVehiculo_lanzaNotFoundException_siVehiculoNoExiste() {
        when(vehiculoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> estadisticasService.getEstadisticasVehiculo(99L))
                .isInstanceOf(NotFoundException.class);
    }
}

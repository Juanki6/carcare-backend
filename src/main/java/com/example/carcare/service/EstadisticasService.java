package com.example.carcare.service;

import com.example.carcare.dto.ComparativaVehiculosDTO;
import com.example.carcare.dto.ConsumoMedioGlobalDTO;
import com.example.carcare.dto.DistribucionGastosDTO;
import com.example.carcare.dto.EstadisticasDTO;
import com.example.carcare.dto.EvolucionConsumoDTO;
import com.example.carcare.dto.EvolucionPreciosDTO;
import com.example.carcare.dto.GastosMensualesDTO;
import com.example.carcare.dto.VehiculoGastoDTO;
import com.example.carcare.entity.Mantenimiento;
import com.example.carcare.entity.Repostaje;
import com.example.carcare.entity.Vehiculo;
import com.example.carcare.exception.ErrorException;
import com.example.carcare.exception.NotFoundException;
import com.example.carcare.repository.MantenimientoRepository;
import com.example.carcare.repository.RepostajeRepository;
import com.example.carcare.repository.SeguroRepository;
import com.example.carcare.repository.VehiculoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class EstadisticasService {

    private final RepostajeRepository repostajeRepository;
    private final VehiculoRepository vehiculoRepository;
    private final MantenimientoRepository mantenimientoRepository;
    private final SeguroRepository seguroRepository;

    private double toDouble(BigDecimal value) {
        return value != null ? value.doubleValue() : 0.0;
    }

    private double round2(double v) { return Math.round(v * 100.0) / 100.0; }
    private double round3(double v) { return Math.round(v * 1000.0) / 1000.0; }
    private double round1(double v) { return Math.round(v * 10.0) / 10.0; }

    // Resumen global del vehículo: consumo medio, precio medio por litro y gasto total.
    // Los repostajes llegan ordenados de más reciente a más antiguo; el consumo L/100km
    // se calcula entre el repostaje [i] y el anterior [i+1] usando la fórmula:
    //   consumo = litros[i] * 100 / (km[i] - km[i+1])
    // El último repostaje de la lista no tiene par, por lo que no aporta consumo.
    public EstadisticasDTO getEstadisticasVehiculo(Long idVehiculo) {
        Vehiculo vehiculo = vehiculoRepository.findById(idVehiculo)
                .orElseThrow(() -> new NotFoundException("Vehículo con id " + idVehiculo + " no encontrado"));

        List<Repostaje> repostajes = repostajeRepository.findByVehiculoIdVehiculoOrderByFechaDesc(idVehiculo);

        if (repostajes.isEmpty()) {
            return new EstadisticasDTO(0.0, 0.0, 0.0, null, vehiculo.getKilometraje(), 0);
        }

        double sumaConsumo = 0.0;
        double sumaPrecio = 0.0;
        double sumaGasto = 0.0;
        int numConsumos = 0;

        for (int i = 0; i < repostajes.size(); i++) {
            Repostaje r = repostajes.get(i);
            double precioLitro = toDouble(r.getPrecioLitro());
            double litros = toDouble(r.getLitros());

            sumaPrecio += precioLitro;
            sumaGasto += litros * precioLitro;

            if (i < repostajes.size() - 1) {
                Repostaje anterior = repostajes.get(i + 1);
                int diffKm = r.getKilometraje() - anterior.getKilometraje();
                if (diffKm > 0) {
                    double consumo = (litros * 100.0) / diffKm;
                    sumaConsumo += consumo;
                    numConsumos++;
                }
            }
        }

        double consumoMedio = numConsumos > 0 ? sumaConsumo / numConsumos : 0.0;
        double precioMedio = repostajes.size() > 0 ? sumaPrecio / repostajes.size() : 0.0;

        return new EstadisticasDTO(consumoMedio, precioMedio, sumaGasto,
                repostajes.get(0).getFecha(), vehiculo.getKilometraje(), repostajes.size());
    }

    // Historial de consumo repostaje a repostaje, ordenado cronológicamente al final.
    // Usa el mismo algoritmo de pares (i, i+1) que getEstadisticasVehiculo.
    public List<EvolucionConsumoDTO> getEvolucionConsumo(Long idVehiculo) {
        List<Repostaje> repostajes = repostajeRepository.findByVehiculoIdVehiculoOrderByFechaDesc(idVehiculo);
        List<EvolucionConsumoDTO> evolucion = new ArrayList<>();

        for (int i = 0; i < repostajes.size(); i++) {
            Repostaje r = repostajes.get(i);
            if (i < repostajes.size() - 1) {
                Repostaje anterior = repostajes.get(i + 1);
                int diffKm = r.getKilometraje() - anterior.getKilometraje();
                if (diffKm > 0) {
                    double litros = toDouble(r.getLitros());
                    double consumo = (litros * 100.0) / diffKm;
                    double precioLitro = toDouble(r.getPrecioLitro());
                    evolucion.add(new EvolucionConsumoDTO(r.getFecha(), consumo,
                            precioLitro, litros, r.getKilometraje()));
                }
            }
        }

        evolucion.sort((a, b) -> a.getFecha().compareTo(b.getFecha()));
        return evolucion;
    }

    public List<ComparativaVehiculosDTO> getComparativaVehiculos(Long idUsuario) {
        List<Vehiculo> vehiculos = vehiculoRepository.findByUsuarioIdUsuario(idUsuario);
        List<ComparativaVehiculosDTO> comparativa = new ArrayList<>();

        for (Vehiculo v : vehiculos) {
            List<Repostaje> repostajes = repostajeRepository.findByVehiculoIdVehiculoOrderByFechaDesc(v.getIdVehiculo());

            if (repostajes.isEmpty()) {
                comparativa.add(new ComparativaVehiculosDTO(v.getIdVehiculo(),
                        v.getMarca() + " " + v.getModelo(), 0.0, 0.0, 0));
                continue;
            }

            double sumaConsumo = 0.0;
            double sumaGasto = 0.0;
            int numConsumos = 0;

            for (int i = 0; i < repostajes.size(); i++) {
                Repostaje r = repostajes.get(i);
                double litros = toDouble(r.getLitros());
                double precioLitro = toDouble(r.getPrecioLitro());

                sumaGasto += litros * precioLitro;

                if (i < repostajes.size() - 1) {
                    Repostaje anterior = repostajes.get(i + 1);
                    int diffKm = r.getKilometraje() - anterior.getKilometraje();
                    if (diffKm > 0) {
                        double consumo = (litros * 100.0) / diffKm;
                        sumaConsumo += consumo;
                        numConsumos++;
                    }
                }
            }

            double consumoMedio = numConsumos > 0 ? sumaConsumo / numConsumos : 0.0;
            comparativa.add(new ComparativaVehiculosDTO(v.getIdVehiculo(),
                    v.getMarca() + " " + v.getModelo() + " (" + v.getMatricula() + ")",
                    consumoMedio, sumaGasto, repostajes.size()));
        }

        return comparativa;
    }

    // Gastos agrupados por mes para los últimos 12 meses, separados en combustible y mantenimiento.
    // Se inicializa un mapa con los 12 meses aunque no haya datos, para que el gráfico
    // muestre meses vacíos en lugar de omitirlos.
    public List<GastosMensualesDTO> getGastosMensuales(Long idVehiculo) {
        LocalDate hoy = LocalDate.now();
        List<Repostaje> repostajes = repostajeRepository.findByVehiculoIdVehiculoOrderByFechaDesc(idVehiculo);
        List<Mantenimiento> mantenimientos = mantenimientoRepository.findByVehiculoIdVehiculoWithFactura(idVehiculo);

        // Inicializar los 12 meses en orden ascendente con valores a cero
        Map<YearMonth, double[]> mapa = new LinkedHashMap<>();
        for (int i = 11; i >= 0; i--) {
            mapa.put(YearMonth.from(hoy.minusMonths(i)), new double[]{0.0, 0.0});
        }

        for (Repostaje r : repostajes) {
            YearMonth ym = YearMonth.from(r.getFecha());
            if (mapa.containsKey(ym)) {
                mapa.get(ym)[0] += toDouble(r.getLitros()) * toDouble(r.getPrecioLitro());
            }
        }

        for (Mantenimiento m : mantenimientos) {
            YearMonth ym = YearMonth.from(m.getFecha());
            if (mapa.containsKey(ym) && m.getFactura() != null && m.getFactura().getImporte() != null) {
                mapa.get(ym)[1] += m.getFactura().getImporte().doubleValue();
            }
        }

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM");
        List<GastosMensualesDTO> result = new ArrayList<>();
        for (Map.Entry<YearMonth, double[]> entry : mapa.entrySet()) {
            double combustible = round2(entry.getValue()[0]);
            double mantenimiento = round2(entry.getValue()[1]);
            result.add(new GastosMensualesDTO(
                    entry.getKey().format(fmt),
                    combustible,
                    mantenimiento,
                    round2(combustible + mantenimiento)
            ));
        }
        return result;
    }

    public List<EvolucionPreciosDTO> getEvolucionPrecios(Long idVehiculo) {
        Vehiculo vehiculo = vehiculoRepository.findById(idVehiculo)
                .orElseThrow(() -> new NotFoundException(Vehiculo.class.getSimpleName(), idVehiculo));

        List<Repostaje> repostajes = repostajeRepository.findByVehiculoIdVehiculoOrderByFechaDesc(idVehiculo);
        String tipo = vehiculo.getCombustible() != null ? vehiculo.getCombustible().name() : "OTRO";

        // Agrupar precios por mes
        Map<YearMonth, List<Double>> preciosPorMes = new LinkedHashMap<>();
        for (Repostaje r : repostajes) {
            if (r.getPrecioLitro() != null) {
                preciosPorMes
                        .computeIfAbsent(YearMonth.from(r.getFecha()), k -> new ArrayList<>())
                        .add(r.getPrecioLitro().doubleValue());
            }
        }

        List<EvolucionPreciosDTO> result = new ArrayList<>();
        for (Map.Entry<YearMonth, List<Double>> entry : preciosPorMes.entrySet()) {
            double media = entry.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            // Fecha: primer día del mes como representante del período
            result.add(new EvolucionPreciosDTO(entry.getKey().atDay(1), round3(media), tipo));
        }

        result.sort(Comparator.comparing(EvolucionPreciosDTO::getFecha));
        return result;
    }

    public List<ConsumoMedioGlobalDTO> getConsumoMedioGlobal() {
        List<Object[]> rows = repostajeRepository.findConsumoMedioGlobal();
        List<ConsumoMedioGlobalDTO> result = new ArrayList<>();
        for (Object[] row : rows) {
            String marca = (String) row[0];
            String modelo = (String) row[1];
            double consumoMedio = row[2] != null ? ((Number) row[2]).doubleValue() : 0.0;
            result.add(new ConsumoMedioGlobalDTO(marca, modelo, round2(consumoMedio)));
        }
        return result;
    }

    public List<VehiculoGastoDTO> getVehiculosMayorGasto() {
        List<Object[]> rows = repostajeRepository.findVehiculosMayorGasto();
        List<VehiculoGastoDTO> result = new ArrayList<>();
        for (Object[] row : rows) {
            String marca = (String) row[0];
            String modelo = (String) row[1];
            String matricula = (String) row[2];
            double gastoTotal = row[3] != null ? ((Number) row[3]).doubleValue() : 0.0;
            result.add(new VehiculoGastoDTO(marca, modelo, matricula, round2(gastoTotal)));
        }
        return result;
    }

    // Proporción de gasto total dividida en Combustible, Mantenimiento y Seguro.
    // Solo se incluye una categoría si tiene gasto real (>0), para no mostrar 0% en el gráfico.
    public List<DistribucionGastosDTO> getDistribucionGastos(Long idVehiculo) {
        List<Repostaje> repostajes = repostajeRepository.findByVehiculoIdVehiculoOrderByFechaDesc(idVehiculo);
        double totalCombustible = repostajes.stream()
                .mapToDouble(r -> toDouble(r.getLitros()) * toDouble(r.getPrecioLitro()))
                .sum();

        List<Mantenimiento> mantenimientos = mantenimientoRepository.findByVehiculoIdVehiculoWithFactura(idVehiculo);
        double totalMantenimiento = mantenimientos.stream()
                .filter(m -> m.getFactura() != null && m.getFactura().getImporte() != null)
                .mapToDouble(m -> m.getFactura().getImporte().doubleValue())
                .sum();

        // Seguro: precio anual registrado para el vehículo
        double totalSeguro = seguroRepository.findByVehiculoIdVehiculo(idVehiculo)
                .filter(s -> s.getPrecioAnual() != null)
                .map(s -> s.getPrecioAnual().doubleValue())
                .orElse(0.0);

        // Solo incluir categorías con gasto real
        Map<String, Double> categorias = new LinkedHashMap<>();
        if (totalCombustible > 0) categorias.put("Combustible", totalCombustible);
        if (totalMantenimiento > 0) categorias.put("Mantenimiento", totalMantenimiento);
        if (totalSeguro > 0) categorias.put("Seguro", totalSeguro);

        double totalGeneral = categorias.values().stream().mapToDouble(Double::doubleValue).sum();

        List<DistribucionGastosDTO> result = new ArrayList<>();
        for (Map.Entry<String, Double> entry : categorias.entrySet()) {
            double total = round2(entry.getValue());
            double porcentaje = totalGeneral > 0 ? round1(entry.getValue() / totalGeneral * 100.0) : 0.0;
            result.add(new DistribucionGastosDTO(entry.getKey(), total, porcentaje));
        }
        return result;
    }
}

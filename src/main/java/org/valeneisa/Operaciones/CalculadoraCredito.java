package org.valeneisa.Operaciones;

import org.valeneisa.Core.IOperacion;
import org.valeneisa.Dtos.CreditoSolicitud;
import org.valeneisa.Dtos.CreditoRespuesta; // <-- ESTO ES VITAL
import org.valeneisa.Dtos.FilaAmortizacion;
import org.valeneisa.Util.MatematicasUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Componente encargado de calcular un crédito con sistema de amortización.
 * Implementa la interfaz IOperacion para procesar solicitudes de tipo CreditoSolicitud
 * y retornar una respuesta de tipo CreditoRespuesta.
 */
@Component
public class CalculadoraCredito implements IOperacion<CreditoSolicitud, CreditoRespuesta> {

    /**
     * Retorna el código único de la operación.
     *
     * @return Código de la operación.
     */
    @Override
    public String obtenerCodigoOp() {
        return "OP-01";
    }

    /**
     * Retorna el costo base en tokens de esta operación.
     *
     * @return Costo base en tokens.
     */
    @Override
    public int obtenerCostoBase() {
        return 50;
    }

    /**
     * Ejecuta el cálculo del crédito con base en la solicitud recibida.
     * Genera la cuota mensual, el total pagado, los intereses y la tabla de amortización.
     *
     * @param solicitud Datos necesarios para calcular el crédito.
     * @return Respuesta con los resultados del cálculo.
     */
    @Override
    public CreditoRespuesta ejecutar(CreditoSolicitud solicitud) {
        double p = solicitud.getPrecio();
        int n = solicitud.getCuotas();
        double i = (solicitud.getTasaMensual() / 100.0);

        // Evitar división por cero si la tasa es 0
        double cuota = (i == 0) ? p / n : p * (i * Math.pow(1 + i, n)) / (Math.pow(1 + i, n) - 1);

        List<FilaAmortizacion> tabla = new ArrayList<>();
        double saldoRestante = p;

        for (int mes = 1; mes <= n; mes++) {
            double interesMes = saldoRestante * i;
            double capitalMes = cuota - interesMes;
            saldoRestante -= capitalMes;

            tabla.add(FilaAmortizacion.builder()
                    .periodo(mes)
                    .cuota(MatematicasUtil.redondear(cuota))
                    .interesPagado(MatematicasUtil.redondear(interesMes))
                    .capitalAmortizado(MatematicasUtil.redondear(capitalMes))
                    .saldoRestante(MatematicasUtil.redondear(Math.max(0, saldoRestante)))
                    .build());
        }

        return CreditoRespuesta.builder()
                .cuotaMensual(MatematicasUtil.redondear(cuota))
                .totalPagado(MatematicasUtil.redondear(cuota * n))
                .totalIntereses(MatematicasUtil.redondear((cuota * n) - p))
                .tablaAmortizacion(tabla)
                .build();
    }
}
package org.valeneisa.Operaciones;

import org.valeneisa.Core.IOperacion;
import org.valeneisa.Dtos.CreditoPedido;
import org.valeneisa.Dtos.CreditoRespuesta;
import org.valeneisa.Dtos.FilaAmortizacion;
import org.valeneisa.Util.MatematicasUtil;
import java.util.ArrayList;
import java.util.List;

public class CalculadoraCredito implements IOperacion<CreditoPedido, CreditoRespuesta> {

    @Override
    public String obtenerCodigoOp() { return "OP-01"; }

    @Override
    public int obtenerCostoBase() { return 50; }

    @Override
    public CreditoRespuesta ejecutar(CreditoPedido solicitud) {
        double p = solicitud.getPrecio();
        int n = solicitud.getCuotas();
        double i = (solicitud.getTasaMensual() / 100.0);

        double cuota = p * (i * Math.pow(1 + i, n)) / (Math.pow(1 + i, n) - 1);
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
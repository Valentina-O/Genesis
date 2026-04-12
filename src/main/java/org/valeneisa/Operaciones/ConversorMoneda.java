package org.valeneisa.Operaciones;

import org.valeneisa.Core.IOperacion;
import org.valeneisa.Dtos.ConversorRequest;
import org.valeneisa.Dtos.ConversorResponse;
import org.valeneisa.Util.MatematicasUtil;

public class ConversorMoneda implements IOperacion<ConversorRequest, ConversorResponse> {

    @Override
    public String obtenerCodigoOp() { return "OP-02"; }

    @Override
    public int obtenerCostoBase() { return 10; }

    @Override
    public ConversorResponse ejecutar(ConversorRequest solicitud) {
        double resultado;

        // Si la moneda de origen es COP, queremos pasar a USD (Dividimos)
        if (solicitud.getMonedaOrigen().equalsIgnoreCase("COP")) {
            resultado = solicitud.getMonto() / solicitud.getTasaSugerida();
        } else {
            // Si el origen es USD, pasamos a COP (Multiplicamos)
            resultado = solicitud.getMonto() * solicitud.getTasaSugerida();
        }

        return ConversorResponse.builder()
                .resultado(MatematicasUtil.redondear(resultado))
                .monedaDestino(solicitud.getMonedaDestino().toUpperCase())
                .tasaUtilizada(solicitud.getTasaSugerida())
                .build();
    }
}
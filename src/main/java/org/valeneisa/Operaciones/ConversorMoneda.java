package org.valeneisa.Operaciones;

import org.valeneisa.Core.IOperacion;
import org.valeneisa.Dtos.ConversorSolicitud;
import org.valeneisa.Dtos.ConversorRespuesta;
import org.valeneisa.Util.MatematicasUtil;
import org.springframework.stereotype.Component;

@Component
public class ConversorMoneda implements IOperacion<ConversorSolicitud, ConversorRespuesta> {

    @Override
    public String obtenerCodigoOp() {
        return "OP-02";
    }

    @Override
    public int obtenerCostoBase() {
        return 10;
    }

    @Override
    public ConversorRespuesta ejecutar(ConversorSolicitud solicitud) {
        double resultado;

        // Lógica de conversión
        if (solicitud.getMonedaOrigen().equalsIgnoreCase("COP")) {
            // De COP a USD (Dividimos)
            resultado = solicitud.getMonto() / solicitud.getTasaSugerida();
        } else {
            // De USD a COP (Multiplicamos)
            resultado = solicitud.getMonto() * solicitud.getTasaSugerida();
        }

        return ConversorRespuesta.builder()
                .resultado(MatematicasUtil.redondear(resultado))
                .monedaDestino(solicitud.getMonedaDestino().toUpperCase())
                .tasaUtilizada(solicitud.getTasaSugerida())
                .build();
    }
}
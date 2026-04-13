package org.valeneisa.Operaciones;

import org.valeneisa.Core.IOperacion;
import org.valeneisa.Dtos.ConversorSolicitud;
import org.valeneisa.Dtos.ConversorRespuesta;
import org.valeneisa.Util.MatematicasUtil;
import org.springframework.stereotype.Component;

/**
 * Componente encargado de realizar conversiones de moneda.
 * Implementa la interfaz IOperacion para procesar solicitudes de tipo ConversorSolicitud
 * y retornar una respuesta de tipo ConversorRespuesta.
 */
@Component
public class ConversorMoneda implements IOperacion<ConversorSolicitud, ConversorRespuesta> {

    /**
     * Retorna el código único de la operación.
     *
     * @return Código de la operación.
     */
    @Override
    public String obtenerCodigoOp() {
        return "OP-02";
    }

    /**
     * Retorna el costo base en tokens de esta operación.
     *
     * @return Costo base en tokens.
     */
    @Override
    public int obtenerCostoBase() {
        return 10;
    }

    /**
     * Ejecuta la conversión de moneda con base en la solicitud recibida.
     * Convierte entre COP y USD utilizando la tasa proporcionada.
     *
     * @param solicitud Datos necesarios para realizar la conversión.
     * @return Respuesta con el resultado de la conversión.
     */
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
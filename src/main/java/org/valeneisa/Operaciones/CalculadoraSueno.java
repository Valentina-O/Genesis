package org.valeneisa.Operaciones;

import org.valeneisa.Core.IOperacion;
import org.valeneisa.Dtos.SolicitudSueno;
import org.valeneisa.Dtos.RespuestaSueno;
import org.valeneisa.Dtos.OpcionSueno;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Componente encargado de calcular horarios óptimos de sueño basados en ciclos.
 * Implementa la interfaz IOperacion para procesar solicitudes de tipo SolicitudSueno
 * y retornar una respuesta de tipo RespuestaSueno.
 */
@Component
public class CalculadoraSueno implements IOperacion<SolicitudSueno, RespuestaSueno> {

    /**
     * Retorna el código único de la operación.
     *
     * @return Código de la operación.
     */
    @Override
    public String obtenerCodigoOp() {
        return "OP-04";
    }

    /**
     * Retorna el costo base en tokens de esta operación.
     *
     * @return Costo base en tokens.
     */
    @Override
    public int obtenerCostoBase() {
        return 20;
    }

    /**
     * Ejecuta el cálculo de ciclos de sueño con base en la solicitud recibida.
     * Genera distintas opciones dependiendo del número de ciclos (mínimo, recomendado e ideal).
     *
     * @param solicitud Datos necesarios para calcular los horarios de sueño.
     * @return Respuesta con las opciones calculadas.
     */
    @Override
    public RespuestaSueno ejecutar(SolicitudSueno solicitud) {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime horaRef = LocalTime.parse(solicitud.getHoraReferencia(), formato);

        List<OpcionSueno> opciones = new ArrayList<>();

        int[] ciclosArray = {4, 5, 6};
        String[] etiquetas = {"Mínimo", "Recomendado", "Ideal"};

        // Si es DESPERTAR restamos minutos (para saber a qué hora dormir)
        // Si es DORMIR sumamos minutos (para saber a qué hora despertar)
        int factor = solicitud.getModo().equalsIgnoreCase("DESPERTAR") ? -1 : 1;

        for (int i = 0; i < ciclosArray.length; i++) {
            int minutosEfectivos = (ciclosArray[i] * 90) + solicitud.getMinutosParaConciliar();
            LocalTime resultado = horaRef.plusMinutes((long) minutosEfectivos * factor);

            OpcionSueno opcion = new OpcionSueno();
            opcion.setCiclos(ciclosArray[i]);
            opcion.setHoraCalculada(resultado.format(formato));
            opcion.setCalidad(etiquetas[i]);

            opciones.add(opcion);
        }

        return RespuestaSueno.builder()
                .opciones(opciones)
                .build();
    }
}
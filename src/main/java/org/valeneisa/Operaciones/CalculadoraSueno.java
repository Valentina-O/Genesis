package org.valeneisa.Operaciones;

import org.valeneisa.Core.IOperacion;
import org.valeneisa.Dtos.RespuestaSueno;
import org.valeneisa.Dtos.SolicitudSueno;
import org.valeneisa.Dtos.OpcionSueno;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CalculadoraSueno implements IOperacion<RespuestaSueno, SolicitudSueno> {

    @Override
    public String obtenerCodigoOp() { return "OP-04"; }

    @Override
    public int obtenerCostoBase() { return 20; }

    @Override
    public SolicitudSueno ejecutar(RespuestaSueno solicitud) {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime horaRef = LocalTime.parse(solicitud.getHoraReferencia(), formato);
        List<OpcionSueno> opciones = new ArrayList<>();

        int[] ciclosArray = {4, 5, 6};
        String[] etiquetas = {"Mínimo", "Recomendado", "Ideal"};

        // Si es DESPERTAR restamos minutos (-1), si es DORMIR sumamos (+1)
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

        return SolicitudSueno.builder()
                .opciones(opciones)
                .build();
    }
}
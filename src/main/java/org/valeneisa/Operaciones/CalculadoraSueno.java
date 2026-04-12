package org.valeneisa.Operaciones;

import org.valeneisa.Core.IOperacion;

import org.valeneisa.Dtos.SuenoPedido;
import org.valeneisa.Dtos.SuenoRespuesta;
import org.valeneisa.Dtos.OpcionSueno;

import org.valeneisa.Dtos.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class CalculadoraSueno implements IOperacion<SuenoPedido, SuenoRespuesta> {

public class CalculadoraSueno implements IOperacion<SolicitudSueno, RespuestaSueno> {

    @Override
    public String obtenerCodigoOp() { return "OP-04"; }

    @Override
    public int obtenerCostoBase() { return 20; }

    @Override

    public SuenoRespuesta ejecutar(SuenoPedido solicitud) {

    public RespuestaSueno ejecutar(SolicitudSueno solicitud) {


        DateTimeFormatter formato = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime horaRef = LocalTime.parse(solicitud.getHoraReferencia(), formato);

        List<OpcionSueno> opciones = new ArrayList<>();

        int[] ciclosArray = {4, 5, 6};
        String[] etiquetas = {"Mínimo", "Recomendado", "Ideal"};

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


        return SuenoRespuesta.builder()

        return RespuestaSueno.builder()

                .opciones(opciones)
                .build();
    }
}
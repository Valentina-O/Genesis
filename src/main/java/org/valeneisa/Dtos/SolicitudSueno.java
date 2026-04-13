package org.valeneisa.Dtos;

import lombok.Data;

/**
 * DTO que representa la solicitud para el cálculo de horarios de sueño.
 * Contiene el modo de cálculo, una hora de referencia y el tiempo estimado
 * para conciliar el sueño.
 */
@Data
public class SolicitudSueno {

    /**
     * Modo de cálculo (por ejemplo: dormir o despertar).
     */
    private String modo;

    /**
     * Hora de referencia utilizada para el cálculo.
     */
    private String horaReferencia;

    /**
     * Minutos estimados que tarda la persona en conciliar el sueño.
     * Valor por defecto: 14 minutos.
     */
    private int minutosParaConciliar = 14;
}
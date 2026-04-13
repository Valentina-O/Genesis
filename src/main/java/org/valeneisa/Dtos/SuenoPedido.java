package org.valeneisa.Dtos;

import lombok.Data;

/**
 * DTO que representa la solicitud para el cálculo de ciclos de sueño.
 * Contiene el modo de operación, la hora de referencia y el tiempo estimado
 * para conciliar el sueño.
 */
@Data
public class SuenoPedido {

    /**
     * Modo de cálculo (por ejemplo: "DESPERTAR" o "DORMIR").
     */
    private String modo; // "DESPERTAR" o "DORMIR"

    /**
     * Hora de referencia en formato HH:mm.
     */
    private String horaReferencia; // HH:mm

    /**
     * Minutos estimados que tarda la persona en conciliar el sueño.
     * Valor por defecto: 14 minutos.
     */
    private int minutosParaConciliar = 14;
}
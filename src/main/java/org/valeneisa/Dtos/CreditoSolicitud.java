package org.valeneisa.Dtos;

import lombok.Data;

/**
 * DTO que representa la solicitud para el cálculo de un crédito.
 * Contiene el precio a financiar, el número de cuotas y la tasa de interés mensual.
 */
@Data
public class CreditoSolicitud {

    /**
     * Monto total que se desea financiar.
     */
    private double precio;

    /**
     * Número de cuotas mensuales en las que se dividirá el crédito.
     */
    private int cuotas;

    /**
     * Tasa de interés mensual expresada en porcentaje.
     */
    private double tasaMensual;
}
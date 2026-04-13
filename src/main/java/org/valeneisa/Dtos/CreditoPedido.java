package org.valeneisa.Dtos;

import lombok.Data;

/**
 * DTO que representa una solicitud de crédito.
 * Contiene el precio a financiar, el número de cuotas
 * y la tasa de interés mensual aplicada.
 */
@Data
public class CreditoPedido {

    /**
     * Monto total a financiar.
     */
    private double precio;        // monto a financiar [cite: 47]

    /**
     * Número de cuotas mensuales en las que se dividirá el pago.
     */
    private int cuotas;           // número de cuotas mensuales [cite: 49]

    /**
     * Tasa de interés mensual expresada en porcentaje.
     */
    private double tasaMensual; // tasa de interés mensual en porcentaje [cite: 50]
}
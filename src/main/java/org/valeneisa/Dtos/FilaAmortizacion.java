package org.valeneisa.Dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa una fila dentro de la tabla de amortización de un crédito.
 * Contiene la información correspondiente a cada periodo de pago.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FilaAmortizacion {

    /**
     * Número del periodo (cuota) actual.
     */
    private int periodo;

    /**
     * Valor de la cuota pagada en este periodo.
     */
    private double cuota;

    /**
     * Parte de la cuota destinada a amortizar el capital.
     */
    private double capitalAmortizado;

    /**
     * Parte de la cuota correspondiente a los intereses pagados.
     */
    private double interesPagado;

    /**
     * Saldo restante del crédito después del pago de este periodo.
     */
    private double saldoRestante;
}
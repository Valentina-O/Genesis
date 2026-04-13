package org.valeneisa.Dtos;

import lombok.Data;

/**
 * DTO que representa la solicitud de conversión de moneda enviada por el cliente.
 * <p>
 * Contiene el monto a convertir, las monedas de origen y destino, y una tasa
 * de cambio sugerida provista por el administrador de la plataforma.
 * </p>
 */
@Data
public class ConversorPedido {

    /**
     * Monto numérico a convertir.
     */
    private double monto;

    /**
     * Código de la moneda de origen (por ejemplo, {@code "COP"} o {@code "USD"}).
     */
    private String monedaOrigen;

    /**
     * Código de la moneda de destino (por ejemplo, {@code "COP"} o {@code "USD"}).
     */
    private String monedaDestino;

    /**
     * Tasa de cambio sugerida por el administrador para realizar la conversión
     * (por ejemplo, {@code 3950.0}).
     */
    private double tasaSugerida;
}
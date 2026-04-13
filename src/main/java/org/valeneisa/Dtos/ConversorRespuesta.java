package org.valeneisa.Dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa la respuesta de un conversor de moneda.
 * Contiene el resultado de la conversión, la moneda destino,
 * la tasa de cambio utilizada y la cantidad de tokens consumidos.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConversorRespuesta {

    /**
     * Resultado de la conversión realizada.
     */
    private double resultado;

    /**
     * Código o nombre de la moneda de destino.
     */
    private String monedaDestino;

    /**
     * Tasa de cambio utilizada para realizar la conversión.
     */
    private double tasaUtilizada;

    /**
     * Cantidad de tokens consumidos durante la operación.
     */
    private int tokensConsumidos;
}
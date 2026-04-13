/**
 * DTO que representa la solicitud para realizar una conversión de moneda.
 * Contiene el monto a convertir, la moneda de origen, la moneda de destino
 * y una tasa sugerida proporcionada por el administrador.
 */
package org.valeneisa.Dtos;

import lombok.Data;

@Data
public class ConversorSolicitud {

    /**
     * Monto que se desea convertir.
     */
    private double monto;

    /**
     * Código de la moneda de origen (por ejemplo: "COP" o "USD").
     */
    private String monedaOrigen; // "COP" o "USD"

    /**
     * Código de la moneda de destino (por ejemplo: "COP" o "USD").
     */
    private String monedaDestino; // "COP" o "USD"

    /**
     * Tasa de cambio sugerida enviada por el administrador (ejemplo: 3950.0).
     */
    private double tasaSugerida; // La envía el "Admin" (ej: 3950.0)
}
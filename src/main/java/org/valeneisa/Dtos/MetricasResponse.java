package org.valeneisa.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para representar las estadísticas globales del sistema.
 * Coincide con el esquema definido en openapi.yml.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetricasResponse {
    private int usuariosTotales;
    private String operacionMasUsada;
    private int totalTokensConsumidos;
}
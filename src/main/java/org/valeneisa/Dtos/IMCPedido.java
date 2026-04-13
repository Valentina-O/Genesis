package org.valeneisa.Dtos;

import lombok.Data;

/**
 * DTO que representa la solicitud para el cálculo del Índice de Masa Corporal (IMC).
 * Contiene el peso en kilogramos y la altura en centímetros.
 */
@Data
public class IMCPedido {

    /**
     * Peso de la persona en kilogramos.
     */
    private double pesoKg;    // peso en kilogramos

    /**
     * Altura de la persona en centímetros.
     */
    private double alturaCm;  // altura en centímetros [cite: 80]
}
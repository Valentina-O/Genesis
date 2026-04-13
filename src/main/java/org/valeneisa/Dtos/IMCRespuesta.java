package org.valeneisa.Dtos;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * DTO que representa la respuesta del cálculo del Índice de Masa Corporal (IMC).
 * Incluye el valor del IMC, la categoría correspondiente, el rango de peso saludable,
 * la diferencia de peso respecto al rango y los tokens consumidos en la operación.
 */
@Data
@Builder
@AllArgsConstructor // Genera el constructor público con todos los campos
@NoArgsConstructor  // Genera el constructor público vacío
public class IMCRespuesta {

    /**
     * Valor calculado del Índice de Masa Corporal.
     */
    private double imc;

    /**
     * Categoría del IMC (por ejemplo: bajo peso, normal, sobrepeso, obesidad).
     */
    private String categoria;

    /**
     * Peso mínimo considerado saludable para la altura dada.
     */
    private double pesoMinSaludable;

    /**
     * Peso máximo considerado saludable para la altura dada.
     */
    private double pesoMaxSaludable;

    /**
     * Diferencia entre el peso actual y el rango saludable.
     */
    private double diferenciaPeso;

    /**
     * Cantidad de tokens consumidos durante la operación.
     */
    private int tokensConsumidos;
}
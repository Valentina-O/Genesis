package org.valeneisa.Dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * DTO que representa la respuesta de un cálculo de ciclos de sueño.
 * Contiene las opciones generadas y la cantidad de tokens consumidos.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SuenoRespuesta {

    /**
     * Lista de opciones de sueño calculadas.
     */
    private List<OpcionSueno> opciones;

    /**
     * Cantidad de tokens consumidos durante la operación.
     */
    private int tokensConsumidos;
}